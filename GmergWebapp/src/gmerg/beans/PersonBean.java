package gmerg.beans;

import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.entities.submission.Person;

import java.util.Map;

import javax.faces.context.FacesContext;

public class PersonBean {
	
	private String id;
    private Map requestParams;
	private Person person;
	private Person[] people;
	private ISHSubmissionAssembler ishSubmissionAssembler;

	public PersonBean () {
		FacesContext context = FacesContext.getCurrentInstance();
        this.requestParams = context.getExternalContext().getRequestParameterMap();
        this.id = (String)requestParams.get("id");
        this.ishSubmissionAssembler = new ISHSubmissionAssembler();
        this.people = null;
        this.person = null;
	}

	public Person getPerson() {
		person = new Person();
        id = (String)requestParams.get("id");
        ishSubmissionAssembler = new ISHSubmissionAssembler();
        if(id != null){
        	person = ishSubmissionAssembler.getPerson(id);
        }
        else {
            id = (String)requestParams.get("personId");
            if(id != null)
            	person = ishSubmissionAssembler.getPersonById(id);
        }
		return person;
	}
	
	
	/**
	 *  @author xingjun - 18/07/2011
	 *  <p>There might be multiple PIs linked to one certain submission </p>
	 */ 
	public Person[] getPeople() {
        id = (String)requestParams.get("id");
        ishSubmissionAssembler = new ISHSubmissionAssembler();
        if(id != null){
        	people = ishSubmissionAssembler.getPeopleBySubmissionId(id);
        }
		return people;
	}
}
