package gmerg.model;

import gmerg.model.BooleanQueryParser.BooleanQueryParser;
import gmerg.model.BooleanQueryParser.ParseException;
import gmerg.model.BooleanQueryParser.TokenMgrError;

import java.io.StringReader;
import java.util.regex.*;

public class BooleanQueryParserDelegate {
	String code = null;
	
	public String parse(String input) {
		String errorMessage = null;
		String message = null;
		try {
			BooleanQueryParser parser = new BooleanQueryParser(new StringReader(input)) ;
			code = parser.parseQuery();
		}
		catch( TokenMgrError e ) {
			errorMessage = "Illegal symbol in the your query! ";
			message = e.getMessage();
//			System.out.println("message1 ====== "+message);		
		}
		catch (ParseException e) {
			errorMessage = "Syntax error in the your query! ";
			message = e.getMessage();
//			System.out.println("message2 ====== "+message);		
		}
		finally {
			if (message != null) {
				Pattern p = Pattern.compile("\\?scolumn\\s*(\\d+)\\s*\\.\\s*(.*)");	// ?s is to indicate . (dot) feeds new line aswell 
				Matcher m = p.matcher(message);
				if (m.find()) 
					errorMessage += "Column: " + m.group(1) + ",  " + m.group(2);
				else 
					errorMessage += message;
			}
			else
				errorMessage = null;
		}
		
		if (errorMessage != null)
			errorMessage = errorMessage.replaceAll("\\\\\\\"", "\"");
//		System.out.println("errorMessage ====== "+errorMessage);
		return errorMessage;
	}
	
	public String getCode() {
		return code;
	}
}
