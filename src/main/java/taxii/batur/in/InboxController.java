package taxii.batur.in;

import org.bson.Document;
import org.mitre.taxii.messages.xml11.InboxMessage;
import org.mitre.taxii.messages.xml11.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/taxii")
public class InboxController extends AbstractControllerClass
{
	@Autowired
	MongoTemplate mongoTemplate;

	@CrossOrigin
	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public String inbox() 
	{
		return "only POST method accepted";
	}
	
	@CrossOrigin
	@RequestMapping(value = "/inbox", method = RequestMethod.POST, consumes = "application/xml;charset=UTF-8", produces = "application/xml;charset=UTF-8")
	public StatusMessage inbox(@RequestBody InboxMessage inboxMessage) 
	{
		try 
		{
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(inboxMessage);
			System.out.println(json);
			Document document = mongoTemplate.insert(Document.parse(json), "inboxMessage");
			System.out.println(document);
			StatusMessage statusMessage = objectFactory.createStatusMessage()
					.withStatusType("SÜKSESFÜL")
					.withMessage("Saved!")
					.withMessageId(inboxMessage.getMessageId());
			return statusMessage;
		} 
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
			StatusMessage statusMessage = objectFactory.createStatusMessage()
					.withStatusType("FAILED")
					.withMessage(inboxMessage.getMessage())
					.withMessageId(inboxMessage.getMessageId());
			return statusMessage;
		}
	}
}
