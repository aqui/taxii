package taxii.batur.in;

import org.mitre.taxii.messages.TaxiiXml;
import org.mitre.taxii.messages.xml11.ObjectFactory;
import org.mitre.taxii.messages.xml11.TaxiiXmlFactory;

public abstract class AbstractControllerClass 
{
	protected ObjectFactory objectFactory = new ObjectFactory();
	protected TaxiiXmlFactory taxiiXmlFactory = new TaxiiXmlFactory();
	protected TaxiiXml taxiiXml = taxiiXmlFactory.createTaxiiXml();
}
