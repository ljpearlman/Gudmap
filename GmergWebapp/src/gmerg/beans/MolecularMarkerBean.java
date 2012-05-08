package gmerg.beans;

import gmerg.assemblers.MolecularMarkerAssembler;
import java.util.ArrayList;

public class MolecularMarkerBean {
	ArrayList markerList;
	ArrayList header;
	MolecularMarkerAssembler mmAssembler;
	
	public ArrayList getMarkerList() {
		
		mmAssembler = new MolecularMarkerAssembler();
		markerList = mmAssembler.getMarkerCandidates();
		mmAssembler = null;
		return markerList;
		
//		ArrayList list = new ArrayList();
//		list.add(new Object[]{"Kidney", "Zzz3", "TS01-TS21", new Object[]{"GUDMAP:1", "GUDMAP:2"}, new Object[]{"GUDMAP:3", "GUDMAP:4"}, new Object[]{"GUDMAP:5", "GUDMAP:6"}});
//		list.add(new Object[]{"Bladder", "Cd24a", "TS01-TS21", new Object[]{"GUDMAP:1", "GUDMAP:2"}, new Object[]{"GUDMAP:3", "GUDMAP:4"}, new Object[]{"GUDMAP:5", "GUDMAP:6"}});
//		return list;
	}

	public ArrayList getHeader() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Tissue");
		list.add("Gene Symbol");
		list.add("Stage Range");
		list.add("ISH");
		list.add("IHC");
		list.add("Array");
		return list;
	}	
}
