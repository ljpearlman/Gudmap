package gmerg.db;

import java.util.ResourceBundle;

public class TrackingIPQuery {
	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	
	final static String name213 = "GET_IP_LOG";
	final static String query213 = "select LWS_IP, LWS_VIEWID, LWS_BROWSER, LWS_PLATFORM from LOG_WEBSITE_STATS where LWS_IP=? and LWS_VIEWID=? ";
	
	final static String name214 = "INSERT_LOG";
	final static String query214 = "insert into LOG_WEBSITE_STATS(LWS_IP, LWS_VIEWID, LWS_BROWSER, LWS_PLATFORM,LWS_LASTMODIED_TIME) values(?,?,?,?,(select now())) ";

	final static String name215 = "UPDATE_LOG";
	final static String query215 = "update LOG_WEBSITE_STATS set LWS_LASTMODIED_TIME=(select now()), LWS_BROWSER=?, LWS_PLATFORM=? where LWS_IP=? and LWS_VIEWID=? ";

	
	static ParamQuery pqList[] = {
	      new ParamQuery(name213, query213),
	      new ParamQuery(name214, query214),
	      new ParamQuery(name215, query215)};
	
//	 finds ParamQuery object by name and returns
	  public static ParamQuery getParamQuery(String name) {
		  for (int i = 0; i < pqList.length; i++) {
			  if (pqList[i].getQueryName().equals(name)) {
				  return pqList[i];
			  }
	      }
	      return null;
	  }	  
}
