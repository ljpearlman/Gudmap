package gmerg.sample.lucene.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.SAXException;


/**
 * Parses the contents of address-book XML file.  The name of the file to
 * parse must be specified as the first command line argument.
 */
public class XMLDocParser
{
	List<Contact> contacts;
	List<REF_PROBE> probes;
	 
    /**
     * Configures Digester rules and actions, parses the XML file specified
     * as the first argument.
     *
     * @param args command line arguments
     */
	public XMLDocParser(String xmlPath)
    {
		contacts = new ArrayList<Contact>();
		probes = new ArrayList<REF_PROBE>();
		try {
	        // instantiate Digester and disable XML validation
	        Digester digester = new Digester();
	        digester.setValidating(false);
	        digester.setRules(new ExtendedBaseRules());
	        digester.push(this);
	
	        /*// instantiate Contact class
	        digester.addObjectCreate("address-book/contact", Contact.class );
	
	        digester.addBeanPropertySetter("address-book/contact/?");
	
	        
	
	        // call 'addContact' method when the next 'address-book/contact' pattern is seen
	        digester.addSetNext("address-book/contact",               "addContact" );
	        // now that rules and actions are configured, start the parsing process
	        XMLDocParser abp = (XMLDocParser) digester.parse(new File(xmlPath));
	        setContacts(abp.getContacts());*/
	        digester.addObjectCreate("GudmapGX_test/REF_PROBE", REF_PROBE.class );
	    	
	        digester.addBeanPropertySetter("GudmapGX_test/REF_PROBE/?");
	
	        
	
	        // call 'addContact' method when the next 'address-book/contact' pattern is seen
	        digester.addSetNext("GudmapGX_test/REF_PROBE",               "addREF_PROBE" );
//	      now that rules and actions are configured, start the parsing process
	        XMLDocParser abp = (XMLDocParser) digester.parse(new File(xmlPath));
	        setProbes(abp.getProbes());
	        
	        
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e2) {
			e2.printStackTrace();
		}
    }	
	
    /**
     * Prints the contact information to standard output.
     *
     * @param contact the <code>Contact</code> to print out
     */
    public void addContact(Contact contact)
    {
    	contacts.add(contact);
    	
    }

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public void addREF_PROBE(REF_PROBE probe)
    {
    	probes.add(probe);
    	
    }

	public List<REF_PROBE> getProbes() {
		return probes;
	}

	public void setProbes(List<REF_PROBE> probes) {
		this.probes = probes;
	}



    /**
     * JavaBean class that holds properties of each Contact entry.
     * It is important that this class be public and static, in order for
     * Digester to be able to instantiate it.
     */

}