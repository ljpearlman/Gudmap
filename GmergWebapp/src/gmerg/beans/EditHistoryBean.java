/**
 * 
 */
package gmerg.beans;

import gmerg.assemblers.EditHistoryAssembler;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class EditHistoryBean {
	
	private ArrayList modifiedSubmissions = null;
	private String groupBy = null;
//	private String groupBy = new String("person");
	private EditHistoryAssembler assembler;
	
	public EditHistoryBean() {
		groupBy = new String("date");
	}
	
	private ArrayList getHistory() {
		
		assembler = new EditHistoryAssembler();
		System.out.println("reload:"+groupBy);
		return assembler.getData(groupBy);
	}
	
	public ArrayList getModifiedSubmissions() {
		
		modifiedSubmissions = getHistory();
		return this.modifiedSubmissions;
	}
	
	public void setModifiedSubmissions(ArrayList modifiedSubmissions) {
		this.modifiedSubmissions = modifiedSubmissions;
	}
	
	public String getGroupBy() {
		return this.groupBy;
	}
	
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	public String reload() {
		return null;
	}

}
