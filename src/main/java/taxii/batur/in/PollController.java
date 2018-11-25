package taxii.batur.in;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.mitre.taxii.messages.xml11.ContentBlock;
import org.mitre.taxii.messages.xml11.InboxMessage;
import org.mitre.taxii.messages.xml11.MessageHelper;
import org.mitre.taxii.messages.xml11.PollRequest;
import org.mitre.taxii.messages.xml11.PollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxii")
public class PollController extends AbstractControllerClass
{
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin
	@RequestMapping(value = "/poll", method = RequestMethod.GET)
	public String inbox() 
	{
		return "only POST method accepted";
	}
	
	@CrossOrigin
	@RequestMapping(value = "/poll", method = RequestMethod.POST, consumes = "application/xml;charset=UTF-8", produces = "application/xml;charset=UTF-8")
	public String poll(@RequestBody PollRequest pollRequest) 
	{
		try 
		{
			List<InboxMessage> inboxMessagesList = mongoTemplate.findAll(InboxMessage.class);
			PollResponse pollResponse = objectFactory.createPollResponse().withInResponseTo(pollRequest.getMessageId())
					.withMessageId(MessageHelper.generateMessageId())
					.withCollectionName(pollRequest.getCollectionName())
					.withRecordCount(objectFactory.createRecordCountType().withValue(BigInteger.valueOf(inboxMessagesList.size())).withPartialCount(false))
					.withExclusiveBeginTimestamp(pollRequest.getExclusiveBeginTimestamp())
					.withInclusiveEndTimestamp(pollRequest.getInclusiveEndTimestamp()).withMessage("Here's your data");
			List<ContentBlock> contentBlockList = new ArrayList<>();
			for (InboxMessage inboxMessage : inboxMessagesList) 
			{
				for (ContentBlock contentBlock : inboxMessage.getContentBlocks()) 
				{
					contentBlockList.add(contentBlock);
				}
			}
			pollResponse.withContentBlocks(contentBlockList);
			return StringEscapeUtils.unescapeXml(taxiiXml.marshalToString(pollResponse, true));
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}
}
