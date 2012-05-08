package gmerg.beans;

import gmerg.entities.submission.GeneStrip;
import java.util.ArrayList;
import gmerg.utils.FacesUtil;

public class InsituExpressionBarChartBean {
	public GeneStrip getGeneStrip() {
		String geneSymbol = FacesUtil.getRequestParamValue("gene");
		
		GeneStrip[] strip = new GeneStrip[1];
		String expressions = new String("<script>var val=0 + ',' + 0 + ',' + 0 + ',' + 0 + ',' + 0 + ',' +"+ 
		   			"0 + ',' + 0 + ',' + 0 + ',' + 0 + ',' + 0 + ',' + "+
		   			"0 + ',' + 0 + ',' + 5.41 + ',' + 14.29 + ',' + 0 + ',' + "+
		   			"0 + ',' + 0 + ',' + 0;prepareGraph('euxassay_008119', val)</script>"+geneSymbol);
		strip[0] = new GeneStrip();
		strip[0].setExpressions(expressions);
		return strip[0];
	}
}
