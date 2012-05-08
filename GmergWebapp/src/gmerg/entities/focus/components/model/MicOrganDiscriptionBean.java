package gmerg.entities.focus.components.model;

import java.util.ArrayList;

import javax.faces.context.FacesContext;


public class MicOrganDiscriptionBean {
	String prefix = "http://www.gudmap.org/Gudmap/arrayData";
    String title = null;
    String pi = null;
    String secondTitle = null;
    ArrayList summary = null;
    ArrayList design = null;
    String[] links = null;
    ArrayList[] genelist = null;
    ArrayList heading = null;
	
	public ArrayList getHeading() {
		return heading;
	}

	public void setHeading(ArrayList heading) {
		this.heading = heading;
	}
    
	/*
	 * Set display of organ description page
	 */
	public MicOrganDiscriptionBean() {
//		System.out.println("PARAM:"+getFacesParamValue("component"));
		  ArrayList content = setContent(getFacesParamValue("component"));	
		  //System.out.println("ORGAN:"+organDetail+":"+content.size());
		  if(null != content && content.size() >= 7) {
			  setTitle((String)content.get(0));
			  setPi((String)content.get(1));
			  setSecondTitle((String)content.get(2));
			  setSummary((ArrayList)content.get(3));
			  setDesign((ArrayList)content.get(4));
			  setLinks((String[])content.get(5));
			  setGenelist((ArrayList[])content.get(6));
			  setHeading((ArrayList)content.get(7));
		  }    			  		
	}

    public static String getFacesParamValue(String name) {
        return (String) FacesContext
                            .getCurrentInstance()
                                .getExternalContext()
                                    .getRequestParameterMap()
                                        .get(name);
    }	
	
	public ArrayList setContent(String organ) {
		 ArrayList oneLink = new ArrayList();
		 ArrayList summary = new ArrayList();
		 ArrayList geneList = new ArrayList();
		 ArrayList[] onelab = new ArrayList[13];
		 if(null != organ && !organ.equals("")) {
			 if(organ.indexOf("testes") > -1) {				 
				 oneLink.add("Testes Microarray Summary"); 
				 oneLink.add("Kevin Gaido, The Hamner Institutes for Health Sciences, 6 Davis Dr., gaido@TheHamner.org"); 
				 oneLink.add("Gene expression in whole testes at gd 11, 12, 14, 16, 18 and pnd 2");
				 summary.add("The overall objective of this experiment was to map the temporal and spatial dynamics of gene expression in the fetal mouse testis at key developmental timepoints. During this period of testicular development the Sertoli cells form cords around the germ cells. The Sertoli cells begin to produce anti-Mullerian hormone that drives regression of the Mullerian ducts. While the fetal Leydig cells begin to produce testosterone, which drives male reproductive tract development. Testicular-specific vascular development is also occurring at this time. "+" The goal of this study was to identify cell-specific genes that can be used as biomarkers for key differentiation events.");
				 oneLink.add(summary);	
				 summary = null;
				 summary = new ArrayList();
				 summary.add("Day to day comparison in whole testes throughout development with triplicates at each time point. Each sample is a pair of testes from a single animal and each animal is taken from a different dam.");
				 oneLink.add(summary);		
				 summary = null;
				 oneLink.add(new String[]{"http://www.gudmap.org/About/Tutorial/DevMRS.html#IGS",		 				
				                        "http://www.gudmap.org/Research/Protocols/Gaido.html", "../images/focus/testes_development.mov"});
				 onelab[0]=new ArrayList();
				 onelab[0].add("Gaido Lab");
				 onelab[1]=new ArrayList();
				 onelab[1].add("");
				 onelab[2]=new ArrayList();
				 onelab[2].add(new String[]{prefix + "/ciit/march_2006/ciit_160306/data/GUDMAP_Gaido_160306_1215.zip","GUDMAP_Gaido_160306_1215.zip"});
				 onelab[2].add(new String[]{"",""});
				 onelab[3]=new ArrayList();
				 onelab[3].add("");
				 onelab[4]=new ArrayList();
				 onelab[4].add(new String[]{prefix + "/ciit/november_2006/ciit_031106/data/all_present_10_06.1.xls", "all_present_10_06.1.xls"});
				 onelab[5]=new ArrayList();
				 onelab[5].add("");
				 onelab[6]=new ArrayList();
				 onelab[6].add(new String[]{"",""});
				 //////////////// commented as Chris Armit request - 02/09/2008 - xingjun
//				 onelab[7]=new ArrayList();
//				 onelab[7].add("");
//				 onelab[8]=new ArrayList();
//				 onelab[8].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/testes_day_to_day.xls", "testes_day_to_day.xls"});
//				 onelab[9]=new ArrayList();
//				 onelab[9].add("");
//				 onelab[10]=new ArrayList();
//				 onelab[10].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/testes_cluster.xls", "testes_cluster.xls"});
//				 onelab[11]=new ArrayList();
//				 onelab[11].add("");
//				 onelab[12]=new ArrayList();
//				 onelab[12].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/ovary_v_testis.xls","ovary_v_testis.xls"});
				 //////////////// commented as Chris Armit request - 02/09/2008 - xingjun
				 oneLink.add(onelab);
				 
				 summary = null;
				 summary = new ArrayList();
				 summary.add(" 1. Raw data");
				 summary.add(" 2. Average Present/Absent list");
				 //////////////// commented as Chris Armit request - 02/09/2008 - xingjun
//				 summary.add(" 3. Significantly changed over time relative to gd 11");
//				 summary.add(" 4. Significantly changed from day to day");
//				 summary.add(" 5. Cluster Analysis");
//				 summary.add(" 6. Significantly different from ovary over time");
				 //////////////// commented as Chris Armit request - 02/09/2008 - xingjun
				 oneLink.add(summary);
				 
			 } else if(organ.indexOf("ovary") > -1) {
				 oneLink.add("Ovary Microarray Summary");				 
				 oneLink.add("Kevin Gaido, The Hamner Institutes for Health Sciences, 6 Davis Dr., gaido@TheHamner.org");
				 oneLink.add("Gene expression in whole ovary at gd 11, 12, 14, 16, 18, and pnd 2");
				 summary.add("The overall objective of this experiment was to map the temporal and spatial dynamics of gene expression in the fetal mouse ovary at key developmental time points. During this period of ovarian development Wnt4 and Follistatin initiate a signaling cascade that inhibits coelomic vessel formation and promotes germ cell survival. Primordial follicles develop consisting of post-meiotic oocytes, surrounded by granulosa cells.");
				 oneLink.add(summary);		
				 summary = null;
				 summary = new ArrayList();			 
				 summary.add("Day to day comparisons of gene expression in whole ovary throughout fetal development with triplicate arrays at each timepoint. Each sample is a pair of ovaries from a single animal and each animal is taken from a different dam (except at early time points where some pooling was necessary).");
				 oneLink.add(summary);		
				 summary = null;			 
				 oneLink.add(new String[]{"http://www.gudmap.org/About/Tutorial/DevMRS.html#IGS",
						 				"http://www.gudmap.org/Research/Protocols/Gaido.html"});
				 onelab[0]=new ArrayList();
				 onelab[0].add("Gaido Lab");
				 onelab[1]=new ArrayList();
				 onelab[1].add("");
				 onelab[2]=new ArrayList();
				 onelab[2].add(new String[]{prefix + "/ciit/june_2006/ciit_220606/data/Gaido_June22_2006.zip","Gaido_June22_2006.zip"});
				 onelab[2].add(new String[]{"",""});
				 onelab[3]=new ArrayList();
				 onelab[3].add("");
				 onelab[4]=new ArrayList();
				 onelab[4].add(new String[]{prefix + "/ciit/november_2006/ciit_031106/data/all_present_10_06.1.xls", "all_present_10_06.1.xls"});
				 onelab[5]=new ArrayList();
				 onelab[5].add("");
				 //////////////// commented as Chris Armit request - 05/09/2008 - xingjun
//				 onelab[6]=new ArrayList();
//				 onelab[6].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/Ovary_sig_gd11.xls","Ovary_sig_gd11.xls"});
//				 onelab[7]=new ArrayList();
//				 onelab[7].add("");
//				 onelab[8]=new ArrayList();
//				 onelab[8].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/Ovary_Day_to_day.xls","Ovary_Day_to_day.xls"});
//				 onelab[9]=new ArrayList();//11
//				 onelab[9].add("");
//				 onelab[10]=new ArrayList();//12
//				 onelab[10].add(new String[]{prefix + "/ciit/january_2007/ciit_240107/data/ovary_v_testis.xls","ovary_v_testis.xls"});
				 //////////////// commented as Chris Armit request - 05/09/2008 - xingjun
				 oneLink.add(onelab);
				 
				 summary = null;
				 summary = new ArrayList();
				 summary.add(" 1. Raw data");
				 summary.add(" 2. Average Present/Absent list");
				 //////////////// commented as Chris Armit request - 05/09/2008 - xingjun
//				 summary.add(" 3. Significantly changed over time relative to gd 11");
//				 summary.add(" 4. Significantly changed from day to day");
//				 summary.add(" 5. Significantly different from Testis over time");//6
				 //////////////// commented as Chris Armit request - 05/09/2008 - xingjun
				 oneLink.add(summary);
			 } else if(organ.indexOf("epididymis") > -1) {
				 oneLink.add("Epididymis Microarray Summary");				 
				 oneLink.add("Kevin Gaido, The Hamner Institutes for Health Sciences, 6 Davis Dr., gaido@TheHamner.org");
				 oneLink.add("Gene expression in epididymides at gd 12, 14, 16, 18, and pnd 2");
				 summary.add("The overall objective of this experiment was to map the temporal and spatial dynamics of gene expression in the fetal mouse epididymis at key developmental time points. During this period of epididymal development, testosterone produced by the fetal testicular Leydig cell stimulates transition of mesenchymal cells to epithelial cells. Proliferation of the epithelial cells leads to ductal coiling.");
				 oneLink.add(summary);		
				 summary = null;
				 summary = new ArrayList();			 
				 summary.add("Day to day comparisons of gene expression in whole epididymis throughout fetal development with triplicate arrays at each timepoint. Each sample is a pair of epididymides from a single animal and each animal is taken from a different dam (except at early time points where some pooling was necessary).");
				 oneLink.add(summary);		
				 summary = null;			 
				 oneLink.add(new String[]{"http://www.gudmap.org/About/Tutorial/DevMRS.html#IGS",
						 				"http://www.gudmap.org/Research/Protocols/Gaido.html"});
				 onelab[0]=new ArrayList();
				 onelab[0].add("Gaido Lab");
				 onelab[1]=new ArrayList();
				 onelab[1].add("");
				 onelab[2]=new ArrayList();
				 onelab[2].add(new String[]{prefix + "/ciit/june_2006/ciit_090606/data/GAIDO_JUNE_08_2006.zip","GAIDO_JUNE_08_2006.zip"});
				 onelab[3]=new ArrayList();
				 onelab[3].add("");
				 onelab[4]=new ArrayList();
				 onelab[4].add(new String[]{"", ""});
				 onelab[5]=new ArrayList();
				 onelab[5].add("");
				 onelab[6]=new ArrayList();
				 onelab[6].add(new String[]{"",""});
				 onelab[7]=new ArrayList();
				 onelab[7].add("");
				 onelab[8]=new ArrayList();
				 onelab[8].add(new String[]{"",""});
				 oneLink.add(onelab);
				 
				 summary = null;
				 summary = new ArrayList();
				 summary.add(" 1. Raw data");
				 summary.add(" 2. Average Present/Absent list");
				 summary.add(" 3. Significantly changed over time relative to gd 11");
				 summary.add(" 4. Significantly changed from day to day");
				 oneLink.add(summary);
			 } else if(organ.indexOf("bladder") > -1) {
				 oneLink.add("Bladder Microarray Summary"); 
				 oneLink.add("James Lessard, Cincinnati Children's Hospital Research Foundation, TCHRF 3047 3333 Burnet Ave., james.lessard@cchmc.org");
				 oneLink.add("not completed");
				 summary.add("Our laboratory is interested in understanding smooth muscle development, particularly as it relates to development of the mouse urogenital system. Toward that end, we are making use of transgenic mice we generated that express Enhanced Green Fluorescent Protein (EGFP) in smooth muscle to identify and analyze smooth muscle-rich organs in the developing urogenital system. In the experiment illustrated below, the developing mouse lower urogenital tract at gestational age 14 days can be divided into three compartments based on the presence or absence of smooth muscle cells (as revealed by EGFP expression under the control of the SMGA promoter). The developing bladder and urethra regions contain compartments that express EGFP while the urogenital sinus, which separates the bladder and urethra, lacks EGFP expression. These three regions were micro-dissected and total RNA was isolated from each compartment. The RNA was then amplified, biotinylated, and hybridized to the Affymetrix Mouse MOE430_2 microarray chip to determine the gene expression profile for each compartment using the protocols described on the GUDMAP website (under Research/Protocols). Three biological replicates were used for the bladder and urethra samples while four biological replicates were used for the urogenital sinus (UGS). ");			 				
				 oneLink.add(summary);		
				 summary = null;
				 summary = new ArrayList();			 
				 summary.add("Four examples of analyses that can be performed and the results generated from these experiments are provided in the form of Excel spreadsheets generated using GeneSifter software.  All data were normalized using the standard RMA protocol within that program.  A comparison between the developing bladder and urogenital sinus regions was made to identify candidate markers for these two regions.  The data shown (e14RMAbladvsUGS.xls) indicate that 1,301 targets show a two-fold or greater difference (student t-test, p <= 0.05) in expression levels between the two compartments.  Likewise, comparing the developing bladder to the urethra region indicates a two-fold or greater difference (student t-test, p <= 0.05) in expression levels for 2,263 targets (e14RMA_BladvsUreth.xls). When all three compartments are compared together using ANOVA analysis (p <= 0.05) with a cutoff of two-fold or greater difference in expression an level in all three groups, 2,074 targets were identified (e14_RMA_ANOVA.xls). To further hone in on smooth muscle related genes, this data was queried for genes showing a similar expression pattern across the three compartments to that of smooth muscle gamma-actin (Actg2).  That is, genes that show high expression in bladder and urethra relative to the UGS.  This data was then sorted based on the relative expression in bladder vs. UGS and 93 targets from this group were found to exhibit this expression pattern with the highest levels of expression being seen in the bladder and urethra and at least a two-fold lower level of expression seen in the urogenital sinus region (ACTG2_Like_sorted.xls).  The utility and validity of this analysis is supported by the fact that the expression of numerous smooth muscle related genes is much higher in the bladder relative to the UGS compartment; these include smooth muscle gamma-actin (Actg2), smooth muscle myosin (MyH11), calponin (cnn1), myocardin (Myocd), transgelin (tagln) and many others. ");
				 oneLink.add(summary);		
				 summary = null;				 
				 oneLink.add(new String[]{"tutorial.html?tutorial=bladder&lab=15",
						 				"http://www.gudmap.org/Research/Protocols/Lessard.html"});	
				 onelab[0]=new ArrayList();
				 onelab[0].add("Lessard Lab");
				 onelab[1]=new ArrayList();
				 onelab[1].add("");//"gudmapdbarray?query=series&id=GUDMAP:2198";
				 onelab[2]=new ArrayList();
				 onelab[2].add(new String[]{prefix + "/cchmc/october_2006/cchmc_061006/data/GUDMAP_Lessard_061006_0840.zip","GUDMAP_Lessard_061006_0840.zip"});//prefix + "/cchmc/march_2006/Lessard-Szucsik_Day1_Bladder.zip";
				 onelab[3]=new ArrayList();
				 onelab[3].add("");
				 onelab[4]=new ArrayList();
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_231006/data/e14RMABLADvsUGSl1.xls", "e14RMABLADvsUGSl1.xls"});
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_231006/data/e14RMABLADvsUGSl2.xls", "e14RMABLADvsUGSl2.xls"});
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_231006/data/e14RMAbladvsUGS.xls", "e14RMAbladvsUGS.xls"});
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_271006/data/ACTG2_Like_sorted.xls", "ACTG2_Like_sorted.xls"});
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_271006/data/e14RMA_BladvsUreth.xls", "e14RMA_BladvsUreth.xls"});
				 onelab[4].add(new String[]{prefix + "/cchmc/october_2006/cchmc_271006/data/e14_RMA_ANOVA.xls", "e14_RMA_ANOVA.xls"});
				 onelab[5]=new ArrayList();
				 onelab[5].add("");
				 onelab[6]=new ArrayList();
				 onelab[6].add(new String[]{"",""});
				 onelab[7]=new ArrayList();
				 onelab[7].add("");
				 onelab[8]=new ArrayList();
				 onelab[8].add(new String[]{"",""});
				 onelab[9]=new ArrayList();
				 onelab[9].add("");
				 onelab[10]=new ArrayList();
				 onelab[10].add(new String[]{"",""});
				 onelab[11]=new ArrayList();
				 onelab[11].add("");
				 onelab[12]=new ArrayList();
				 onelab[12].add(new String[]{"",""});
				 oneLink.add(onelab);
				 
				 summary = null;
				 summary = new ArrayList();
				 summary.add(" 1. Raw data");
				 summary.add(" 2. Average Present/Absent list");
				 summary.add(" 3. Significantly changed over time relative to gd 11");
				 summary.add(" 4. Significantly changed from day to day");
				 summary.add(" 5. Cluster Analysis");
				 summary.add(" 6. Significantly different from ovary over time");
				 oneLink.add(summary);
			 } else if(organ.indexOf("kidney") > -1) {
				 oneLink.add("Kidney Microarray Summary"); 
				 oneLink.add("Steve Potter, Cincinnati Children's Hospital Research Foundation, TCHRF 3007 3333  Burnet Ave., steve.potter@cchmc.org");
				 oneLink.add("not completed");
				 summary.add("Gene expression profiles of different components of the developing kidney are compared. This series shows the changing gene expression patterns of the forming nephron. E11.5 metanephric mesenchyme is compared to E12.5 renal vesicles, which are compared to E15.5 S-shaped bodies, which are compared to both E15.5 proximal tubules and E15.5 renal corpuscles (glomeruli).");
				 oneLink.add(summary);		
				 summary = null;
				 summary = new ArrayList();	
				 summary.add("Components were collected by laser capture microdissection, the RNA purified, and target prepared and hybridized to Affymetrix MOE430 version2 microarrays. For details of the procedures refer to protocols.");
				 summary.add("Biological triplicates, or more, were analyzed for each component.");
				 summary.add("The data was analyzed with GeneSifter software, using RMA normalization, performing a pairwise t-test, requiring a fold change of equal to or greater than two, and a P value of equal to or less than 0.05.");
				 summary.add("The Excel data includes log base 2 transformed expression levels (mean and standard error of the mean), ratio or fold change, up or down change (in second component), P value, several gene identifiers including symbol and name, chromosome location of gene and ontology information.");
				 oneLink.add(summary);		
				 summary = null;
				 oneLink.add(new String[]{"tutorial.html?tutorial=kidney&lab=16",
						 				"http://www.gudmap.org/Research/Protocols/Potter.html"});	
				 onelab[0]=new ArrayList();
				 onelab[0].add("Potter Lab");
				 onelab[1]=new ArrayList();
				 onelab[1].add("");
				 onelab[2]=new ArrayList();
				 onelab[2].add(new String[]{prefix + "/potter/november_2006/potter_091106/data/GUDMAP_Potterlab_110806_1423.tgz","GUDMAP_Potterlab_110806_1423.tgz"});
				 onelab[3]=new ArrayList();
				 onelab[3].add("processed_genelists.jsf?component=kidney&lab=16");
				 onelab[4]=new ArrayList();
				 onelab[4].add(new String[]{prefix + "/potter/october_2006/potter_171006/data/GUDMAP_Potterlab_Pairwise_101706_1400.zip","GUDMAP_Potterlab_Pairwise_101706_1400.zip"});
				 onelab[5]=new ArrayList();
				 onelab[5].add("");
				 onelab[6]=new ArrayList();
				 onelab[6].add(new String[]{"",""});
				 onelab[7]=new ArrayList();
				 onelab[7].add("");
				 onelab[8]=new ArrayList();
				 onelab[8].add(new String[]{"",""});
				 onelab[9]=new ArrayList();
				 onelab[9].add("");
				 onelab[10]=new ArrayList();
				 onelab[10].add(new String[]{"",""});
				 onelab[11]=new ArrayList();
				 onelab[11].add("");
				 onelab[12]=new ArrayList();
				 onelab[12].add(new String[]{"",""});
				 oneLink.add(onelab);	
				 
				 summary = null;
				 summary = new ArrayList();
				 summary.add(" 1. Raw data");
				 summary.add(" 2. Average Present/Absent list");
				 summary.add(" 3. Significantly changed over time relative to gd 11");
				 summary.add(" 4. Significantly changed from day to day");
				 summary.add(" 5. Cluster Analysis");
				 summary.add(" 6. Significantly different from ovary over time");
				 oneLink.add(summary);
			 }
		}
		onelab = null;
		return oneLink;
	}


	public ArrayList getDesign() {
		return design;
	}


	public void setDesign(ArrayList design) {
		this.design = design;
	}


	public ArrayList[] getGenelist() {
		return genelist;
	}


	public void setGenelist(ArrayList[] genelist) {
		this.genelist = genelist;
	}


	public String[] getLinks() {
		return links;
	}


	public void setLinks(String[] links) {
		this.links = links;
	}


	public String getPi() {
		return pi;
	}


	public void setPi(String pi) {
		this.pi = pi;
	}


	public String getSecondTitle() {
		return secondTitle;
	}


	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}


	public ArrayList getSummary() {
		return summary;
	}


	public void setSummary(ArrayList summary) {
		this.summary = summary;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}	 			
	
}
