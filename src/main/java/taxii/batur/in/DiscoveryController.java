package taxii.batur.in;

import org.mitre.taxii.ContentBindings;
import org.mitre.taxii.Versions;
import org.mitre.taxii.messages.xml11.DiscoveryRequest;
import org.mitre.taxii.messages.xml11.DiscoveryResponse;
import org.mitre.taxii.messages.xml11.MessageHelper;
import org.mitre.taxii.messages.xml11.ServiceInstanceType;
import org.mitre.taxii.messages.xml11.ServiceTypeEnum;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxii")
public class DiscoveryController extends AbstractControllerClass
{
	@CrossOrigin
	@RequestMapping(value = "/discovery", method = RequestMethod.GET)
	public String discovery() 
	{
		return "only POST method accepted";
	}
	
	@CrossOrigin
	@RequestMapping(value = "/discovery", method = RequestMethod.POST, consumes = "application/xml;charset=UTF-8", produces = "application/xml;charset=UTF-8")
	public DiscoveryResponse discovery(@RequestBody DiscoveryRequest discoveryRequest) 
	{
		try 
		{
			ServiceInstanceType discoveryServiceInstanceType = objectFactory.createServiceInstanceType()
					.withServiceType(ServiceTypeEnum.DISCOVERY)
					.withAddress("/discovery")
					.withAvailable(true).withProtocolBinding(Versions.VID_TAXII_HTTP_10)
					.withServiceVersion(Versions.VID_TAXII_SERVICES_11).withMessageBindings(Versions.VID_TAXII_XML_11)
					.withMessage("This is your discovery service")
					.withContentBindings(objectFactory.createContentBindingIDType().withBindingId(ContentBindings.CB_STIX_XML_111));
			ServiceInstanceType poolServiceInstanceType = objectFactory.createServiceInstanceType()
					.withServiceType(ServiceTypeEnum.POLL)
					.withAddress("/poll")
					.withAvailable(true).withProtocolBinding(Versions.VID_TAXII_HTTP_10)
					.withServiceVersion(Versions.VID_TAXII_SERVICES_11).withMessageBindings(Versions.VID_TAXII_XML_11)
					.withMessage("This is your poll service")
					.withContentBindings(objectFactory.createContentBindingIDType().withBindingId(ContentBindings.CB_STIX_XML_111));
			ServiceInstanceType inboxServiceInstanceType = objectFactory.createServiceInstanceType()
					.withServiceType(ServiceTypeEnum.INBOX)
					.withAddress("/inbox")
					.withAvailable(true).withProtocolBinding(Versions.VID_TAXII_HTTP_10)
					.withServiceVersion(Versions.VID_TAXII_SERVICES_11).withMessageBindings(Versions.VID_TAXII_XML_11)
					.withMessage("This is your inbox service")
					.withContentBindings(objectFactory.createContentBindingIDType().withBindingId(ContentBindings.CB_STIX_XML_111));
			ServiceInstanceType collectionServiceInstanceType = objectFactory.createServiceInstanceType()
					.withServiceType(ServiceTypeEnum.COLLECTION_MANAGEMENT)
					.withAddress("/collection-management")
					.withAvailable(true).withProtocolBinding(Versions.VID_TAXII_HTTP_10)
					.withServiceVersion(Versions.VID_TAXII_SERVICES_11).withMessageBindings(Versions.VID_TAXII_XML_11)
					.withMessage("This is your collection-management service")
					.withContentBindings(objectFactory.createContentBindingIDType().withBindingId(ContentBindings.CB_STIX_XML_111));
			DiscoveryResponse discoveryResponse = objectFactory.createDiscoveryResponse()
					.withInResponseTo(discoveryRequest.getMessageId()).withMessageId(MessageHelper.generateMessageId())
					.withServiceInstances(poolServiceInstanceType, discoveryServiceInstanceType, inboxServiceInstanceType, collectionServiceInstanceType);

			System.out.println(taxiiXml.marshalToString(discoveryResponse, true));

			return discoveryResponse;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}
}
