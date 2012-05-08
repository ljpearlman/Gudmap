package gmerg.beans;

import java.util.ArrayList;

public class MicroarrayFocusBean {
	ArrayList organ1;
	ArrayList organs;

	public ArrayList getOrgan1() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Kevin Gaido", "http://www.gudmap.org/About/Projects/Gaido.html"});
		return list;
	}
	
	public ArrayList getOrgans() {
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		list.add(new Object[]{"http://www.gudmap.org", getOrgan1(), " mouse testis & cell-specific search "});
		return list;
	}
}
