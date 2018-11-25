package taxii.batur.in;

import java.util.ArrayList;
import java.util.List;

import org.mitre.taxii.ContentBindings;
import org.mitre.taxii.Versions;
import org.mitre.taxii.messages.xml11.CollectionInformationRequest;
import org.mitre.taxii.messages.xml11.CollectionInformationResponse;
import org.mitre.taxii.messages.xml11.CollectionRecordType;
import org.mitre.taxii.messages.xml11.CollectionTypeEnum;
import org.mitre.taxii.messages.xml11.MessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxii/collection-management")
public class CollectionInformationController extends AbstractControllerClass
{
	@CrossOrigin
	@RequestMapping(value = "/collection-management", method = RequestMethod.GET)
	public String poll() 
	{
		return "only POST method accepted";
	}
	
	@CrossOrigin
	@RequestMapping(value = "/collection-information", method = RequestMethod.POST, consumes = "application/xml;charset=UTF-8", produces = "application/xml;charset=UTF-8")
	public CollectionInformationResponse poll(@RequestBody CollectionInformationRequest collectionInformationRequest) 
	{
		try 
		{
			List<CollectionRecordType> collectionRecordTypeList = new ArrayList<CollectionRecordType>();
			collectionRecordTypeList.add(objectFactory.createCollectionRecordType()
            		.withAvailable(true)
                    .withCollectionType(CollectionTypeEnum.DATA_FEED)
                    .withCollectionName("default")
                    .withDescription("Default data set description")
                    .withPollingServices(objectFactory.createServiceContactInfoType().withAddress("/poll")
                    		.withMessageBindings(Versions.VID_TAXII_XML_11)
                            .withProtocolBinding(Versions.VID_TAXII_HTTP_10))
                    .withSubscriptionServices(objectFactory.createServiceContactInfoType()
                            .withAddress("/collection-management/collection-subscription")
                            .withMessageBindings(Versions.VID_TAXII_XML_11)
                            .withProtocolBinding(Versions.VID_TAXII_HTTP_10)
                    )
                    .withContentBindings(objectFactory.createContentBindingIDType().withBindingId(ContentBindings.CB_STIX_XML_111))
            );
            CollectionInformationResponse collectionInformationResponse = objectFactory.createCollectionInformationResponse()
                    .withInResponseTo(collectionInformationRequest.getMessageId())
                    .withMessageId(MessageHelper.generateMessageId())
                    .withCollections(collectionRecordTypeList);
            return collectionInformationResponse;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}
}
