package gmerg.beans;

import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.entities.submission.Person;

import java.util.Map;
import javax.faces.context.FacesContext;

public class PersonBean {
    private boolean debug = false;
    private Person[] people;
    
    public PersonBean () {
	if (debug)
	    System.out.println("PersonBean.constructor");

	FacesContext context = FacesContext.getCurrentInstance();
	Map requestParams = context.getExternalContext().getRequestParameterMap();
	ISHSubmissionAssembler ishSubmissionAssembler = new ISHSubmissionAssembler();
	String id = (String)requestParams.get("id");
	
	if (null != id)
	    people = ishSubmissionAssembler.getPeopleBySubmissionId(id);
	if (null == people || 0 == people.length) {
	    id = (String)requestParams.get("personId");
	    Person person = null;
	    if (null != id)
		person = ishSubmissionAssembler.getPersonById(id);
	    
	    if (null == person)
		people = null;
	    else {
		people = new Person[1];
		people[0] = person;
	    }
	}
    }
    
    public Person[] getPeople() {
	return people;
    }
}
