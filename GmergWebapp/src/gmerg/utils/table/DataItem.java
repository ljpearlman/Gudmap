/*
 * Created on November-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gmerg.utils.table;

import java.io.*;
import java.net.URLEncoder;

/**
 * @author Mehran Sharghi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class DataItem implements Serializable {
	Object value = null;
    byte type;
	String title = null;
	String link = null;
	String[] params = null; // Params for links target window
	Object spareValue = null; // for any additional requirement will set separatley (not by constructor);

	public DataItem(Object value) {
		this(value, null, null, 0);
	}

	public DataItem(Object value, int type) {
		this(value, null, null, type);
	}

	public DataItem(Object value, String title, int type) {
		this(value, title, null, type);
	}
	
	public DataItem(Object value, String title, String link, int type) {
		this.value = value;
		this.title = title;
		this.link = link;
		this.type = (byte) type;
		params = null;
	}

	public DataItem(Object value, String title, String link, int type, int height, int width) {
		this(value, title, link, type);
		params = new String[2];
		params[0] = (height==0)? "" : String.valueOf(height);
		params[1] = (width==0)? "" : String.valueOf(width);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(value);
		out.writeByte(type);
		out.writeChars(title);
		out.writeChars(link);
		int n = (params==null)? 0 : params.length;
		out.writeInt(n);
		for (int i=0; i<n; i++)
			out.writeChars(params[i]);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		value = in.readObject();
		type = in.readByte();
		title = (String)in.readObject();
		link = (String)in.readObject();
		int n = in.readInt();
		if (n==0) {
			params = null;
			return;
		}
		params = new String[n];
		for (int i=0; i<n; i++)
			params[i] = (String)in.readObject();
	}
	
	public String getWindowParams() {
		String h = "";
		String w = "";
		if (params != null) { 
			h = params[0];
			w = params[1];
		}
		else if (type < 30) { 
			h = "600";
			w = "800";
		}
		else {
			h = "200";
			w = "400";
		}
		return "toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=" + w + ",height= " + h;  
	}

	public String getHeight() {
		if (params != null) 
			return params[0];
		if (type < 30)  
			return "600";
		return "200";
	}

	public String getWidth() {
		if (params != null) 
			return params[1];
		if (type < 30)  
			return "800";
		return "300";
	}
	
	public Object getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public byte getType() {
		return type;
	}

	public boolean isComplex() {
		return (type>=80 && type<83);	// 80-82 are same complex values just the horizontal alignment is different
	}
	
	public String getNameEncodedTitle() {
		return nameEncoded(title);
	}
	
	public String getNameEncodedLink() {
		return nameEncoded(link);
	}
	
	public Object getNameEncodedValue() {
		if (value==null)
			return null;
		String s = "";
		if (s.getClass() == value.getClass()){
			s = (String)value;
			return (Object) nameEncoded(s);
		}
		return value;
	}
	
	public String getUrlEncodedTitle() {
		try {
			return URLEncoder.encode(title, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e) {
			System.out.println(e.getStackTrace());
			return title;
		}
	}

	public String getUrlEncodedLink() {
		try {
			return URLEncoder.encode(link, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e) {
			System.out.println(e.getStackTrace());
			return link;
		}
	}

	public static String nameEncoded(String s) {
	    String encoded = s.replaceAll("\\W|", "");
	    encoded = encoded.replaceAll("\'", "\\'");
	    encoded = encoded.replaceAll("\"", "\\\"");
	    return encoded;
	}

	public Object getSpareValue() {
		return spareValue;
	}

	public void setSpareValue(Object spareValue) {
		this.spareValue = spareValue;
	}

    public void print() {
	if (null == value)
	    System.out.println("DataItem: value null");
	else 
	    System.out.println("DataItem: value class = "+value.getClass().getName()+" value = "+value);
	System.out.println("DataItem: type = "+type);
	System.out.println("DataItem: title = "+title);
	System.out.println("DataItem: link = "+link);
	if (null == params)
	    System.out.println("DataItem: params null");
else
    System.out.println("DataItem:  params length = "+params.length);
	if (null == spareValue)
	    System.out.println("DataItem: spareValue null");
else
	    System.out.println("DataItem:  spareValue class = "+value.getClass().getName()+" spareValue = "+spareValue);
    }
}
