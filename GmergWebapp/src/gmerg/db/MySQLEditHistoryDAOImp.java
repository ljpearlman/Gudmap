package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLEditHistoryDAOImp implements EditHistoryDAO{
    private Connection conn;
    
    // default constructor
    public MySQLEditHistoryDAOImp() {
    	
    }
    
    // constructor with connection initialisation
    public MySQLEditHistoryDAOImp(Connection conn) {
    	this.conn = conn;
    }

    public ArrayList getDates() {
    	ResultSet resSet = null;
        ArrayList result = new ArrayList();
        ParamQuery parQ = null;
        String queryString = AnnotationToolQuery.getParamQuery("GET_LOG_HISTORY_DATE").getQuerySQL();
        
        
        		
        PreparedStatement prepStmt = null;
        
        try {
            prepStmt = conn.prepareStatement(queryString);           
            // execute
            resSet = prepStmt.executeQuery();
            if(resSet.first()){
				resSet.beforeFirst();
				while(resSet.next()){
					result.add(resSet.getString(1));
				}
				
			}

            // release database resources
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }    	
        return result;
    }
    
    public ArrayList getUsersInDates(String sort) {
    	ResultSet resSet = null;
    	ArrayList users = new ArrayList<String[]>();
    	String queryString = null;
    	if(null != sort && sort.equals("person")) {
    		queryString = AnnotationToolQuery.getParamQuery("GET_HISTORY_BY_PERSON").getQuerySQL();
    	} else {
    		queryString = AnnotationToolQuery.getParamQuery("GET_HISTORY").getQuerySQL();
    	}
    	PreparedStatement prepStmt = null;
    	String[] oldvalues = null;

    	try {
    		prepStmt = conn.prepareStatement(queryString);
    		
            resSet = prepStmt.executeQuery();
	    	if(resSet.first()){
				resSet.beforeFirst();
				while(resSet.next()){
					users.add(new String[]{resSet.getString(1),resSet.getString(2),resSet.getString(3),resSet.getString(4),resSet.getString(5), resSet.getString(6), resSet.getString(7), resSet.getString(8)});
				}
	    	}
	    	DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);
    	} catch (SQLException se) {
            se.printStackTrace();
        } 
    	ArrayList results = new ArrayList<Object>();
    	String[] newvalues = null;
    	ArrayList<String[]> probeList = new ArrayList<String[]>();
    	ArrayList<Object> sectionItem = new ArrayList<Object>();
    	ArrayList<Object> section = new ArrayList<Object>();
    	ArrayList<Object> submission = new ArrayList<Object>();
    	ArrayList<Object> person = new ArrayList<Object>();
    	ArrayList<Object> modDate = new ArrayList<Object>();
    	ArrayList<Object> result = new ArrayList<Object>();
    	ArrayList<Object> persons = new ArrayList<Object>();
    	ArrayList<Object> submissions = new ArrayList<Object>();
    	ArrayList<Object> sections = new ArrayList<Object>();
    	ArrayList<Object> sectionItems = new ArrayList<Object>();
    	
    	for(int i = 0 ; i < users.size(); i++) {
    		
    		if(null == oldvalues) {
    			oldvalues = (String[])users.get(i);
    			probeList.add(new String[]{oldvalues[5], oldvalues[6], oldvalues[7]});
    		} else {
    			newvalues = (String[])users.get(i);
    			if(newvalues[4].equals(oldvalues[4]) &&
    					newvalues[3].equals(oldvalues[3]) && 
    					newvalues[2].equals(oldvalues[2]) && 
    					newvalues[1].equals(oldvalues[1]) && 
    					newvalues[0].equals(oldvalues[0])) {
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]});
    				//System.out.println("5:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			} else if(newvalues[3].equals(oldvalues[3]) &&
    					newvalues[2].equals(oldvalues[2]) && 
    					newvalues[1].equals(oldvalues[1]) && 
    					newvalues[0].equals(oldvalues[0])) {
    				sectionItem.add(oldvalues[4]);
    				sectionItem.add(probeList);
    				sectionItems.add(sectionItem);
    				probeList = new ArrayList<String[]>();
    				sectionItem = new ArrayList<Object>();
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]}); 
    				//System.out.println("4:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			} else if(newvalues[2].equals(oldvalues[2]) &&
    					newvalues[1].equals(oldvalues[1]) && 
    					newvalues[0].equals(oldvalues[0])) {
    				sectionItem.add(oldvalues[4]);
    				sectionItem.add(probeList);
    				sectionItems.add(sectionItem);
    				section.add(oldvalues[3]);
    				section.add(sectionItems);
    				sectionItems = new ArrayList<Object>();
    				sections.add(section);
    				probeList = new ArrayList<String[]>();
    				sectionItem = new ArrayList<Object>();
    				section = new ArrayList<Object>();
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]}); 
    				//System.out.println("3:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			} else if(newvalues[1].equals(oldvalues[1]) &&
    					newvalues[0].equals(oldvalues[0])) {
    				sectionItem.add(oldvalues[4]);
    				sectionItem.add(probeList);
    				sectionItems.add(sectionItem);
    				section.add(oldvalues[3]);
    				section.add(sectionItems);
    				sectionItems = new ArrayList<Object>();
    				sections.add(section);
    				submission.add(oldvalues[2]);
    				submission.add(sections);
    				sections = new ArrayList<Object>();
    				submissions.add(submission);
    				probeList = new ArrayList<String[]>();
    				sectionItem = new ArrayList<Object>();
    				section = new ArrayList<Object>();
    				submission = new ArrayList<Object>();
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]}); 
    				//System.out.println("2:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			} else if (newvalues[0].equals(oldvalues[0])) {
    				sectionItem.add(oldvalues[4]);
    				sectionItem.add(probeList);
    				sectionItems.add(sectionItem);
    				section.add(oldvalues[3]);
    				section.add(sectionItems);
    				sectionItems = new ArrayList<Object>();
    				sections.add(section);
    				submission.add(oldvalues[2]);
    				submission.add(sections);
    				sections = new ArrayList<Object>();
    				submissions.add(submission);
    				person.add(oldvalues[1]);
    				person.add(submissions);
    				submissions = new ArrayList<Object>();
    				persons.add(person);
    				probeList = new ArrayList<String[]>();
    				sectionItem = new ArrayList<Object>();
    				section = new ArrayList<Object>();
    				submission = new ArrayList<Object>();
    				person = new ArrayList<Object>();
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]}); 
    				//System.out.println("1:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			} else {
    				sectionItem.add(oldvalues[4]);
    				sectionItem.add(probeList);
    				sectionItems.add(sectionItem);
    				section.add(oldvalues[3]);
    				section.add(sectionItems);
    				sectionItems = new ArrayList<Object>();
    				sections.add(section);
    				submission.add(oldvalues[2]);
    				submission.add(sections);
    				sections = new ArrayList<Object>();
    				submissions.add(submission);
    				person.add(oldvalues[1]);
    				person.add(submissions);
    				submissions = new ArrayList<Object>();
    				persons.add(person);
    				modDate.add(oldvalues[0]);
    				modDate.add(persons);
    				persons = new ArrayList<Object>();
    				result.add(modDate);
    				probeList = new ArrayList<String[]>();
    				sectionItem = new ArrayList<Object>();
    				section = new ArrayList<Object>();
    				submission = new ArrayList<Object>();
    				person = new ArrayList<Object>();
    				modDate = new ArrayList<Object>();
    				probeList.add(new String[]{newvalues[5], newvalues[6], newvalues[7]}); 
    				//System.out.println("0:"+newvalues[5]+":"+newvalues[6]+":"+newvalues[7]);
    			}
    			oldvalues = newvalues;
    		}
    		if(i == users.size()-1) {
    			sectionItem.add(oldvalues[4]);
				sectionItem.add(probeList);
				sectionItems.add(sectionItem);
				section.add(oldvalues[3]);
				section.add(sectionItems);
				sections.add(section);
				submission.add(oldvalues[2]);
				submission.add(sections);
				submissions.add(submission);
				person.add(oldvalues[1]);
				person.add(submissions);
				persons.add(person);
				modDate.add(oldvalues[0]);
				modDate.add(persons);
				result.add(modDate);
    		}
    	}
    	
    	return result;
    	/*ResultSet resSet = null;
    	ResultSet resSet2 = null;
    	ResultSet resSet3 = null;
    	ResultSet resSet4 = null;
        ArrayList users = new ArrayList<Object>();
        ParamQuery parQ = null;
        String queryString = AnnotationToolQuery.getParamQuery("GET_USERS_IN_DATE").getQuerySQL();
        String subString = AnnotationToolQuery.getParamQuery("GET_SUBMISSIONS").getQuerySQL();
        String sectionString = AnnotationToolQuery.getParamQuery("GET_SECTIONS").getQuerySQL();
        String itemString = AnnotationToolQuery.getParamQuery("GET_ITEMS").getQuerySQL();
        ArrayList<Object> row2 = new ArrayList<Object>();
        ArrayList<Object> row = new ArrayList<Object>();
        ArrayList<Object> row3 = new ArrayList<Object>();
        ArrayList<Object> row4 = new ArrayList<Object>();
        ArrayList<Object> row5 = new ArrayList<Object>();
        		
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmt2 = null;
        PreparedStatement prepStmt3 = null;
        PreparedStatement prepStmt4 = null;
        if(null != dates) {
	        try {
	        	for(int i = 0; i < dates.size(); i++) {
	        		row = null;
	        		row = new ArrayList<Object>();
	        		prepStmt = null;
		            prepStmt = conn.prepareStatement(queryString);   
		            prepStmt.setString(1, (String)dates.get(i));
		            users = null;
		            users = new ArrayList<String>();
		            // execute
		            resSet = null;
		            resSet = prepStmt.executeQuery();
		            if(resSet.first()){
						resSet.beforeFirst();
						while(resSet.next()){
							users.add(resSet.getString(1));
							row3 = null;
							row3 = new ArrayList<Object>();
							prepStmt2 = null;
							prepStmt2 = conn.prepareStatement(subString);
							prepStmt2.setString(1, (String)dates.get(i));
							prepStmt2.setString(2, resSet.getString(1));//user
							resSet2 = null;
							resSet2 = prepStmt2.executeQuery();
							if(resSet2.first()){
								resSet2.beforeFirst();
								while(resSet2.next()){
									row3.add(resSet2.getString(1));//submission id
									prepStmt3 = null;
									prepStmt3 = conn.prepareStatement(sectionString);
									prepStmt3.setString(1, (String)dates.get(i));
									prepStmt3.setString(2, resSet.getString(1));
									prepStmt3.setString(3, resSet2.getString(1));
									resSet3 = null;
									resSet3 = prepStmt3.executeQuery();
									
									row4 = null;
									row4 = new ArrayList<Object>();
									if(resSet3.first()){
										resSet3.beforeFirst();
										while(resSet3.next()){
											row4.add(resSet3.getString(1));//section name
											prepStmt4 = null;
											prepStmt4 = conn.prepareStatement(itemString);
											prepStmt4.setString(1, (String)dates.get(i));
											prepStmt4.setString(2, resSet.getString(1));
											prepStmt4.setString(3, resSet2.getString(1));
											prepStmt4.setString(4, resSet3.getString(1));
											resSet4 = null;
											resSet4 = prepStmt4.executeQuery();
											row5 = null;
											row5 = new ArrayList<Object>();
											if(resSet4.first()){
												resSet4.beforeFirst();
												while(resSet4.next()){
													row5.add(new String[]{resSet4.getString(1), resSet4.getString(2), resSet4.getString(3)});
												}
											}
											row4.add(row5);

										}
									}
									row3.add(row4);

								}
							}
							users.add(row3);
							
						}						
					}
		            row.add((String)dates.get(i));
		            row.add(users);
		            row2.add(row);
		            // release database resources
		            
	        	}
				DBHelper.closeResultSet(resSet4);
	            DBHelper.closePreparedStatement(prepStmt4);
				DBHelper.closeResultSet(resSet3);
	            DBHelper.closePreparedStatement(prepStmt3);
	            DBHelper.closeResultSet(resSet2);
	            DBHelper.closePreparedStatement(prepStmt2);
	            DBHelper.closeResultSet(resSet);
	            DBHelper.closePreparedStatement(prepStmt);
	        } catch (SQLException se) {
	            se.printStackTrace();
	        } 
	           	
        }
        return row2;*/
    }
}
