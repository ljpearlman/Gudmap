package gmerg.entities.focus.components.model;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

public class MicTutorialBean {
	
    String title = "";
    ArrayList tutorial = new ArrayList();
    ArrayList subImages = new ArrayList();
	
	 public ArrayList getSubImages() {
		return subImages;
	}

	public void setSubImages(ArrayList subImages) {
		this.subImages = subImages;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList getTutorial() {
		return tutorial;
	}

	public void setTutorial(ArrayList tutorial) {
		this.tutorial = tutorial;
	}

	/*
	 * Set display of tutorial page
	 */
	public MicTutorialBean() {
		  ArrayList content = setTutorial((String)getFacesParamValue("tutorial"), (String)getFacesParamValue("lab"));
		  if(null != content && content.size() >= 3) {
			  setTitle((String)content.get(0));
			  setTutorial((ArrayList)content.get(1));
			  setSubImages((ArrayList)content.get(2));
		  }		 
	 }

		public ArrayList setTutorial(String organ, String labid) {
			 ArrayList oneLink = new ArrayList();
			 ArrayList summary = new ArrayList();
			 ArrayList images = new ArrayList();
			 if(null != organ && !organ.equals("")) {
				 if(organ.indexOf("testes") > -1) {		
					 if(null != labid && labid.equals("13")) {
						 oneLink.add("Testes Tutorial Page by Gaido lab");
						 summary.add("not completed");
						 oneLink.add(summary);	
						 summary = null;					 
						 oneLink.add(null);
					 }
				 } else if(organ.indexOf("ovary") > -1) {
					 if(null != labid && labid.equals("13")) {
					 oneLink.add("Ovary Tutorial Page by Gaido lab");
					 summary.add("not completed");
					 oneLink.add(summary);	
					 summary = null;
					 oneLink.add(null);
					 }
				 } else if(organ.indexOf("epididymis") > -1) {
					 if(null != labid && labid.equals("13")) {
					 oneLink.add("Epididymis Tutorial Page by Gaido lab");
					 summary.add("not completed");
					 oneLink.add(summary);	
					 summary = null;
					 oneLink.add(null);
					 }
				 } else if(organ.indexOf("bladder") > -1) {
					 if(null != labid && labid.equals("15")) {
						 oneLink.add("Bladder Tutorial Page by Lessard lab");
						 summary.add("Our laboratory is interested in understanding smooth muscle development, particularly as it relates to development of the mouse urogenital system. Toward that end, we are making use of transgenic mice we generated that express Enhanced Green Fluorescent Protein (EGFP) in smooth muscle to identify and analyze smooth muscle-rich organs in the developing urogenital system. In the experiment illustrated below, the developing mouse lower urogenital tract at gestational age 14 days can be divided into three compartments based on the presence or absence of smooth muscle cells (as revealed by EGFP expression under the control of the SMGA promoter). The developing bladder and urethra regions contain compartments that express EGFP while the urogenital sinus, which separates the bladder and urethra, lacks EGFP expression. These three regions were micro-dissected and total RNA was isolated from each compartment. The RNA was then amplified, biotinylated, and hybridized to the Affymetrix Mouse MOE430_2 microarray chip to determine the gene expression profile for each compartment using the protocols described on the GUDMAP website (under Research/Protocols). Three biological replicates were used for the bladder and urethra samples while four biological replicates were used for the urogenital sinus (UGS). ");
						 summary.add("Four examples of analyses that can be performed and the results generated from these experiments are provided in the form of Excel spreadsheets generated using GeneSifter software.  All data were normalized using the standard RMA protocol within that program.  A comparison between the developing bladder and urogenital sinus regions was made to identify candidate markers for these two regions.  The data shown (e14RMAbladvsUGS.xls) indicate that 1,301 targets show a two-fold or greater difference (student t-test, p <= 0.05) in expression levels between the two compartments.  Likewise, comparing the developing bladder to the urethra region indicates a two-fold or greater difference (student t-test, p <= 0.05) in expression levels for 2,263 targets (e14RMA_BladvsUreth.xls). When all three compartments are compared together using ANOVA analysis (p <= 0.05) with a cutoff of two-fold or greater difference in expression an level in all three groups, 2,074 targets were identified (e14_RMA_ANOVA.xls). To further hone in on smooth muscle related genes, this data was queried for genes showing a similar expression pattern across the three compartments to that of smooth muscle gamma-actin (Actg2).  That is, genes that show high expression in bladder and urethra relative to the UGS.  This data was then sorted based on the relative expression in bladder vs. UGS and 93 targets from this group were found to exhibit this expression pattern with the highest levels of expression being seen in the bladder and urethra and at least a two-fold lower level of expression seen in the urogenital sinus region (ACTG2_Like_sorted.xls).  The utility and validity of this analysis is supported by the fact that the expression of numerous smooth muscle related genes is much higher in the bladder relative to the UGS compartment; these include smooth muscle gamma-actin (Actg2), smooth muscle myosin (MyH11), calponin (cnn1), myocardin (Myocd), transgelin (tagln) and many others.");
						 oneLink.add(summary);	
						 summary = null;
						 images.add("../images/focus/e14microdisssectionJL.jpg");
						 oneLink.add(images);
					 }
				 } else if(organ.indexOf("kidney") > -1) {
					 if(null != labid && labid.equals("16")) {
						 oneLink.add("Kidney Tutorial Page by Potter lab");
						 summary.add("Gene expression profiles of different components of the developing kidney are compared. This series shows the changing gene expression patterns of the forming nephron. E11.5 metanephric mesenchyme is compared to E12.5 renal vesicles, which are compared to E15.5 S-shaped bodies, which are compared to both E15.5 proximal tubules and E15.5 renal corpuscles (glomeruli).");
						 summary.add("Components were collected by laser capture microdissection, the RNA purified, and target prepared and hybridized to Affymetrix MOE430 version2 microarrays. For details of the procedures refer to protocols.");
						 summary.add("Biological triplicates, or more, were analyzed for each component.");
						 summary.add("The data was analyzed with GeneSifter software, using RMA normalization, performing a pairwise t-test, requiring a fold change of equal to or greater than two, and a P value of equal to or less than 0.05.");
						 summary.add("The Excel data includes log base 2 transformed expression levels (mean and standard error of the mean), ratio or fold change, up or down change (in second component), P value, several gene identifiers including symbol and name, chromosome location of gene and ontology information.");	
						 oneLink.add(summary);	
						 summary = null;
						 images.add("../images/focus/Glomerulus.jpg");
						 images.add("../images/focus/Medullary UB.jpg");
						 images.add("../images/focus/Pelvic mesenchyme.jpg");
						 images.add("../images/focus/Proximal tubule.jpg");
						 images.add("../images/focus/S-shaped body.jpg");
						 images.add("../images/focus/wt rv composite.jpg");
						 images.add("../images/focus/Corticalcollectingduct.jpg");
						 images.add("../images/focus/Proximaltubule.jpg");
						 images.add("../images/focus/Ureteralsmoothmuscle.jpg");
						 images.add("../images/focus/Urothelium.jpg");
						 oneLink.add(images);					 
					 }
				 }
			 } 
	         images = null;
			 return oneLink;
		}

	    public static String getFacesParamValue(String name) {
	        return (String) FacesContext
	                            .getCurrentInstance()
	                                .getExternalContext()
	                                    .getRequestParameterMap()
	                                        .get(name);
	    }			
		
}
