package gmerg.db;

import java.util.Hashtable;
import java.util.ResourceBundle;

import gmerg.utils.Utility;

/**
 * 
 * @author xingjun 
 * <p>modified by xingjun - 28/-8/2009
 * - changed from 'WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0'
 * - to 'WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4'
 * - column SUB_DB_STATUS_FK will also be involved in deciding if the submission entry is public or not</p>
 *
 */
public class AdvancedSearchDBQuery {
    protected static boolean debug = false;

	  static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	  
 
	  final static public String[] getISHDefaultTitle(){
	      return new String[]{"Gene", Utility.getProject()+" Entry Details", "Source", "Submission Date", 
	    		  	 "Assay Type", "Probe Name", "Stage", 
					 "Age", "Sex", "Genotype", "Tissue", "In Situ Expression", "Specimen Type", "Images"};			  
	  }
	  
	  final static public String[] getISHEditDefaultTitle(){
		  String[] old = getISHDefaultTitle();
		  String[] newTitle = new String[old.length + 1];
		  for(int i = 0; i < newTitle.length-1; i++) {
			  newTitle[i] = old[i];
		  }
		  newTitle[newTitle.length-1] = "Annotation";
		  return newTitle;
	  }
	  
	  final static public String[] getBothDefaultTitle(){
		  return new String[]{ "Gene", Utility.getProject()+" Entry Details", 
				"Source", "Submission Date", 
				"Assay Type", "Probe Name", "Stage", "Age", "Sex", "Genotype","Tissue", 
				"In situ Expression", "Microarray Expression", 
		        "Specimen Type", "Images"};		  
	  }
	  
	  final static public String getMICDefaultSort(){
		  return "NATURAL_SORT(SUB_ACCESSION_ID) ";
	  }
	  
	  final static public String getISHDefaultSort(){
		  return "NATURAL_SORT(RPR_SYMBOL), STG_STAGE_DISPLAY, SPN_SEX";			  
	  }
	  
	  // order: assay type, gene, expression, theiler stage, tissue, sex
	  final static public String getBothDefaultSort(){
		  return "col14, col1, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2, col15";
	  }
	  
	  final static public Hashtable getEMAPID(){
			Hashtable<String, String[]> lookupsection = new Hashtable<String,String[]>();
			lookupsection.put("1",new String[]{// metanephros (11)
					"EMAP:3233","EMAP:3841","EMAP:4587",
					"EMAP:5504","EMAP:6674","EMAP:8226","EMAP:9536","EMAP:10896","EMAP:12256",
					"EMAP:29491","EMAP:30247"}); 
			lookupsection.put("2",new String[]{// early reproductive system (7)
//					 removed by xingjun - 12/01/2009 -required by editors - dont have relevant data
//					"EMAP:1958", "EMAP:1436", "EMAP:1961", 
					"EMAP:3850", "EMAP:2572",
					"EMAP:3226","EMAP:3851",
					"EMAP:2576","EMAP:3229", "EMAP:27645"}); 
			lookupsection.put("3",new String[]{// Male Reproductive System (9)
					"EMAP:28873","EMAP:5532","EMAP:6705","EMAP:8257",
					"EMAP:8443","EMAP:9803","EMAP:11163", "EMAP:29348","EMAP:30157"}); 
			lookupsection.put("4",new String[]{// Female Reproductive System (9)
					"EMAP:28872","EMAP:5523","EMAP:6694","EMAP:8245",
					"EMAP:9557","EMAP:10917","EMAP:12277","EMAP:29396","EMAP:30203"}); 
			lookupsection.put("5",new String[]{// Lower Urinary Tract (39)
					"EMAP:6668", "EMAP:8219","EMAP:9528",
					"EMAP:10888","EMAP:12248","EMAP:29457",
					"EMAP:30374","EMAP:28749","EMAP:28750","EMAP:28751",
					"EMAP:28752","EMAP:29443","EMAP:30418","EMAP:28556",
					"EMAP:30898","EMAP:27572","EMAP:3237","EMAP:3846",
					"EMAP:4593","EMAP:5516",
					// added by xingjun as required by Editors - 04/12/2007
					"EMAP:3238","EMAP:3847","EMAP:4594","EMAP:5517","EMAP:6689","EMAP:2575",
					"EMAP:3239","EMAP:3848","EMAP:4595","EMAP:5521","EMAP:6692","EMAP:8243",
					"EMAP:9553","EMAP:10913","EMAP:12273","EMAP:30416","EMAP:29475",
					"EMAP:30902", "EMAP:31643"});

			// added by xingjun - used for in situ expression profile display - 17/11/2008
			lookupsection.put("6", new String[]{// Mesonephros (11 all parts, all stages)
					"EMAP:2576", "EMAP:3229", "EMAP:27645",
					// added by xingjun - 12/01/2009 - suggested by editors
					"EMAP:29139", "EMAP:28910", "EMAP:29140", "EMAP:28911", "EMAP:29141",
					"EMAP:28912", "EMAP:29142", "EMAP:28913"});

			return lookupsection;
	  }
	  
	  final static public String[] interestedStructures = {
		  "Mesonephros (all parts, all stages)", 
		  "Metanephros",
		  "Lower urinary tract",
		  "Early reproductive system",
		  "Male reproductive system",
		  "Female reproductive system", 
		  "Others" // items down from this item will not be implemented for time being - 13/11/2008
	  };
	  
	  final static public String[] getInterestedAnatomyStructureIds () {
		  return new String[] {"6", "1", "5", "2", "3", "4"};
	  }
	  
	  final static public Hashtable getISHQuickSearchColumns(){
		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
		  lookup.put("All", new String[]{"QIC_RPR_SYMBOL",
				  "QIC_RPR_NAME",
				  "QIC_RSY_SYNONYM",
				  "QIC_ATN_PUBLIC_ID",
				  "QIC_ANO_COMPONENT_NAME",
				  "QIC_RPR_LOCUS_TAG",
				  "QIC_RPR_GENBANK",
				  "QIC_RPR_ENSEMBL",
				  "QIC_RPR_JAX_ACC",
				  "QIC_SUB_ACCESSION_ID",
				  "QIC_RPR_MTF_JAX",
				  "QIC_MAPROBE_ID"});
		  lookup.put("Gene", new String[]{"QIC_RPR_SYMBOL"});
		  lookup.put("GeneSymbol", new String[]{"QIC_RPR_SYMBOL"});
		  lookup.put("Gene Function", new String[]{"QIC_RPR_SYMBOL"});
		  lookup.put("Anatomy", new String[]{"QIC_ATN_PUBLIC_ID"});
		  lookup.put("Accession ID", new String[]{"QIC_RPR_LOCUS_TAG", "QIC_RPR_GENBANK", "QIC_RPR_ENSEMBL", "QIC_RPR_JAX_ACC", "QIC_SUB_ACCESSION_ID", "QIC_RPR_MTF_JAX", "QIC_MAPROBE_ID"});
		  lookup.put("Probe", new String[]{"QIC_PRB_PROBE_NAME", "QIC_PRB_CLONE_NAME"});
		  return lookup;
	  }

	  final static public Hashtable getMICQuickSearchColumns(){
		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
		  lookup.put("All", new String[]{"MBC_GNF_SYMBOL",
				  "QMC_ATN_PUBLIC_ID",
				  "QMC_ANO_COMPONENT_NAME",
				  "MBC_SUB_ACCESSION_ID",
				  "QMC_SER_GEO_ID",
				  "QMC_PLT_GEO_ID",
				  "QMC_SMP_GEO_ID"});
		  lookup.put("Gene", new String[]{"MBC_GNF_SYMBOL" 
				  });
		  lookup.put("GeneSymbol", new String[]{"MBC_GNF_SYMBOL"});
		  lookup.put("Gene Function", new String[]{"MBC_GNF_SYMBOL"});
		  lookup.put("Anatomy", new String[]{"QMC_ATN_PUBLIC_ID"});
		  lookup.put("Accession ID", new String[]{"MBC_SUB_ACCESSION_ID"});
		  

		  return lookup;
	  }
	  //TODO for Sequence Data
	  final static public Hashtable getNGDQuickSearchColumns(){
		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
		  lookup.put("All", new String[]{"ANO_COMPONENT_NAME",
				  "ATN_PUBLIC_ID",
				  "SUB_ACCESSION_ID",
				  "NGS_GEO_ID",
				  "NGR_GEO_ID"});
		  /*lookup.put("Gene", new String[]{"MBC_GNF_SYMBOL" 
				  });
		  lookup.put("GeneSymbol", new String[]{"MBC_GNF_SYMBOL"});
		  lookup.put("Gene Function", new String[]{"MBC_GNF_SYMBOL"});*/
		  lookup.put("Anatomy", new String[]{"ATN_PUBLIC_ID"});
		  lookup.put("Accession ID", new String[]{"SUB_ACCESSION_ID"});
		  

		  return lookup;
	  }
	  
	  /**
	   * modified by xingjun - 24/04/2009 
	   * - changed query for RefSyn_Synonym to remove symbols which are not involed in submissions
	   * <p>modified by xingjun - 09/10/2009 - added element 'RefGeneInfo_synonym' </p>
	   * 
	   * @return
	   */
	  final static public Hashtable getRefTableAndColTofindGeneSymbols() {
		  //each string array contains the main query at [0] and the column to be parameterised at [1]
		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
		  lookup.put("RefProbe_Symbol", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RPR_LOCUS_TAG=RMM_MGIACC WHERE ","RPR_SYMBOL","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefProbe_Name", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RPR_LOCUS_TAG=RMM_MGIACC WHERE ","RPR_NAME","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefGeneInfo_Symbol", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON GNF_ID=RMM_MGIACC WHERE ","GNF_SYMBOL",""});
		  lookup.put("RefGeneInfo_Name", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON GNF_ID=RMM_MGIACC WHERE ","GNF_NAME",""});
		  lookup.put("RefMgiMrk_MGIAcc", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG WHERE ","RMM_MGIACC","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefEnsGene_EnsemblId", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG JOIN REF_ENS_GENE ON RMM_MGIACC = REG_PRIMARY_ACC WHERE ","REG_STABLE","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefSyn_Synonym", new String [] {"SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE ","RSY_SYNONYM",""});
		  lookup.put("RefGeneInfo_synonym", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON RMM_SYMBOL = GNF_SYMBOL JOIN REF_SYNONYM ON RSY_REF=RMM_ID WHERE ","RSY_SYNONYM",""});
		  lookup.put("RefProbe_synonym", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG JOIN REF_SYNONYM ON RSY_REF=RMM_ID WHERE ","RSY_SYNONYM","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefMgiMrkRefSyn_Synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK,REF_SYNONYM WHERE RSY_REF = RMM_ID AND RMM_SYMBOL IN (SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE) AND ","RSY_SYNONYM",""});
		  lookup.put("RefProbe_MTFJax", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_MTF_JAX LIKE 'MTF%' AND ","RPR_MTF_JAX",""});
		  lookup.put("RefGoTerm_GoId", new String [] {"SELECT DISTINCT GOT_ID FROM REF_GO_TERM WHERE ","GOT_TERM",""});
		  lookup.put("RefMgiGoGene_MrkSymbol", new String [] {"SELECT DISTINCT GOG_MRK_SYMBOL FROM REF_MGI_GOGENE WHERE ","GOG_TERM",""});
		  return lookup;
	  }
//	  final static public Hashtable getRefTableAndColTofindGeneSymbols() {
//		  //each string array contains the main query at [0] and the column to be parameterised at [1]
//		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
//		  lookup.put("RefProbe_Symbol", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE ","RPR_SYMBOL"});
//		  lookup.put("RefProbe_Name", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE ","RPR_NAME"});
//		  lookup.put("RefGeneInfo_Symbol", new String [] {"SELECT DISTINCT GNF_SYMBOL FROM REF_GENE_INFO WHERE ","GNF_SYMBOL"});
//		  lookup.put("RefGeneInfo_Name", new String [] {"SELECT DISTINCT GNF_SYMBOL FROM REF_GENE_INFO WHERE ","GNF_NAME"});
//		  lookup.put("RefMgiMrk_MGIAcc", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK WHERE ","RMM_MGIACC"});
//		  lookup.put("RefEnsGene_EnsemblId", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK, REF_ENS_GENE WHERE RMM_MGIACC = REG_PRIMARY_ACC AND ","REG_STABLE"});
//		  lookup.put("RefSyn_Synonym", new String [] {"SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE ","RSY_SYNONYM"});
//		  lookup.put("RefGeneInfo_synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID JOIN REF_GENE_INFO ON RMM_SYMBOL = GNF_SYMBOL WHERE ","RSY_SYNONYM"});
//		  lookup.put("RefMgiMrkRefSyn_Synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK,REF_SYNONYM WHERE RSY_REF = RMM_ID AND RMM_SYMBOL IN (SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE) AND ","RSY_SYNONYM"});
//		  lookup.put("RefProbe_MTFJax", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_MTF_JAX LIKE 'MTF%' AND ","RPR_MTF_JAX"});
//		  lookup.put("RefGoTerm_GoId", new String [] {"SELECT DISTINCT GOT_ID FROM REF_GO_TERM WHERE ","GOT_TERM"});
//		  lookup.put("RefMgiGoGene_MrkSymbol", new String [] {"SELECT DISTINCT GOG_MRK_SYMBOL FROM REF_MGI_GOGENE WHERE ","GOG_TERM"});
//		  return lookup;
//	  }
	  
	  
	  final static public Hashtable getLookup(){
		  Hashtable<String, String> lookup = new Hashtable<String,String>();
			lookup.put("1","Probe Name");
			lookup.put("2","Tissue");
			lookup.put("3","Probe Strain");//probe
			lookup.put("4","Gene Type");
			lookup.put("5","Visualization Method");
			lookup.put("6","Gene Symbol");
			lookup.put("7","Gene Name");
			lookup.put("8","Gene Synonym");
			lookup.put("9","MGI ID");
			lookup.put("10","Ensembl ID");
			lookup.put("11","GenBank ID");
			lookup.put("12","EMAP ID");
			lookup.put("13","Ontology Terms");
			lookup.put("14","Assay Type");
			lookup.put("15","Fixation Method");
			lookup.put("16","Specimen Strain");//specimen
			lookup.put("17","Sex");
			lookup.put("18","Stage");
			lookup.put("19","Age");
			lookup.put("20","Genotype");
			lookup.put("21","Lab Name");
			lookup.put("22","Submission Date");
			lookup.put("23","Platform GEO ID");//platform
			lookup.put("24","Platform Title");
			lookup.put("25","Platform Name");
			lookup.put("26","Series GEO ID");
			lookup.put("27","Series Title");
			lookup.put("28","Sample GEO ID");
			lookup.put("29","Sample Strain");
			lookup.put("30","Sample Sex");
			lookup.put("31","Sample Age");
			lookup.put("32","Sample Stage");
			lookup.put("33","Affymetrix ID");
			lookup.put("34","Clone Name");
			lookup.put("35", "Pattern");
			lookup.put("36", "Location");
		    return lookup;
	  }

	  final static public Hashtable getLookup2(){
			Hashtable<String, String> lookup2 = new Hashtable<String,String>();	  
			lookup2.put("Probe Name","1");
			lookup2.put("Tissue","2");
			lookup2.put("Probe Strain","3");
			lookup2.put("Gene Type","4");
			lookup2.put("Visualization Method","5");
			lookup2.put("Gene Symbol","6");
			lookup2.put("Gene Name","7");
			lookup2.put("Gene Synonym","8");
			lookup2.put("MGI ID","9");
			lookup2.put("Ensembl ID","10");
			lookup2.put("GenBank ID","11");
			lookup2.put("EMAP ID","12");
			lookup2.put("Ontology Terms","13");
			lookup2.put("Assay Type","14");
			lookup2.put("Fixation Method","15");
			lookup2.put("Specimen Strain","16");
			lookup2.put("Sex","17");
			lookup2.put("Stage","18");
			lookup2.put("Age","19");
			lookup2.put("Genotype","20");
			lookup2.put("Lab Name","21");
			lookup2.put("Submission Date","22");
			lookup2.put("Platform GEO ID","23");
			lookup2.put("Platform Title","24");
			lookup2.put("Platform Name","25");
			lookup2.put("Series GEO ID","26");
			lookup2.put("Series Title","27");
			lookup2.put("Sample GEO ID","28");
			lookup2.put("Sample Strain","29");
			lookup2.put("Sample Sex","30");
			lookup2.put("Sample Age","31");
			lookup2.put("Sample Stage","32");
			lookup2.put("Affymetrix ID","33");
			lookup2.put("Clone Name", "34");
			lookup2.put("Pattern", "35");
			lookup2.put("Location", "36");
			return lookup2;
	  }

	  final static public Hashtable getLookupInDB(){
			Hashtable<String, String> lookupInDB = new Hashtable<String,String>();
			lookupInDB.put("Tissue", "QIC_PRB_TISSUE");
			lookupInDB.put("Probe Strain", "QIC_PRB_STRAIN");
			lookupInDB.put("Gene Type", "QIC_SPN_WILDTYPE, QMC_SPN_WILDTYPE");
			lookupInDB.put("Visualization Method", "QIC_PRB_VISUAL_METHOD");
			lookupInDB.put("Assay Type", "QIC_SPN_ASSAY_TYPE, QMC_SPN_ASSAY_TYPE");
			lookupInDB.put("Fixation Method", "QIC_SPN_FIXATION_METHOD, QMC_SPN_FIXATION_METHOD");
			lookupInDB.put("Specimen Strain", "QIC_SPN_STRAIN, QMC_SPN_STRAIN");
			lookupInDB.put("Sex", "QIC_SPN_SEX, QMC_SPN_SEX");
			lookupInDB.put("Stage", "QIC_STG_STAGE_DISPLAY, QMC_STG_STAGE_DISPLAY");
			lookupInDB.put("Age", "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END), " +
					"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END)");
			lookupInDB.put("Genotype", "QIC_SPN_WILDTYPE, QMC_SPN_WILDTYPE");
			lookupInDB.put("Lab Name", "QIC_PER_NAME, QMC_PER_NAME");
			lookupInDB.put("Platform GEO ID", "QMC_PLT_GEO_ID");
			lookupInDB.put("Platform Title", "QMC_PLT_TITLE");
			lookupInDB.put("Platform Name", "QMC_PLT_NAME");
			lookupInDB.put("Sample Strain", "QMC_SMP_STRAIN");
			lookupInDB.put("Sample Sex", "QMC_SMP_SEX");
			lookupInDB.put("Sample Age", "QMC_SMP_DEVELOPMENT_STAGE");
			lookupInDB.put("Sample Stage", "QMC_SMP_THEILER_STAGE");
			lookupInDB.put("Pattern", "QIC_PTN_PATTERN");
			lookupInDB.put("Location", "DESCRIPTION_OID");
			//separate
			lookupInDB.put("Probe Name", "QIC_PRB_PROBE_NAME");
			lookupInDB.put("Gene Symbol", "QIC_RPR_SYMBOL, MBC_GNF_SYMBOL");
			lookupInDB.put("Gene Name", "QIC_RPR_NAME");
			lookupInDB.put("Gene Synonym", "QIC_RSY_SYNONYM");
			lookupInDB.put("MGI ID", "QIC_RPR_LOCUS_TAG");
			lookupInDB.put("GenBank ID", "QIC_RPR_GENBANK");
			lookupInDB.put("Ensembl ID", "QIC_RPR_ENSEMBL");
			lookupInDB.put("Ontology Terms", "QIC_ANO_COMPONENT_NAME, QMC_ANO_COMPONENT_NAME");
			lookupInDB.put("EMAP ID", "QIC_ATN_PUBLIC_ID, QMC_ATN_PUBLIC_ID");
			lookupInDB.put("Submission Date", "QIC_SUB_SUB_DATE, QMC_SUB_SUB_DATE");
			lookupInDB.put("Series GEO ID", "QMC_SER_GEO_ID");
			lookupInDB.put("Series Title", "QMC_SER_TITLE");
			lookupInDB.put("Platform GEO ID", "QMC_PLT_GEO_ID");
			lookupInDB.put("Platform Title", "QMC_PLT_TITLE");
			lookupInDB.put("Platform Name", "QMC_PLT_NAME");
			lookupInDB.put("Sample GEO ID", "QMC_SMP_GEO_ID");
			lookupInDB.put("Affymetrix ID", "MBC_GLI_PROBE_SET_NAME");
			lookupInDB.put("Clone Name", "QIC_PRB_CLONE_NAME");
			return lookupInDB;
	   }

	  final static public Hashtable getLookupInDBTwo(){
			Hashtable<String, String> lookupInDB = new Hashtable<String,String>();
			lookupInDB.put("Tissue", "QIC_PRB_TISSUE");
			lookupInDB.put("Probe Strain", "QIC_PRB_STRAIN");
			lookupInDB.put("Gene Type", "QIC_SPN_WILDTYPE");
			lookupInDB.put("Visualization Method", "QIC_PRB_VISUAL_METHOD");
			lookupInDB.put("Assay Type", "QSC_SPN_ASSAY_TYPE");//????????????????
			lookupInDB.put("Fixation Method", "QSC_SPN_FIXATION_METHOD");//?????????????
			lookupInDB.put("Specimen Strain", "QSC_SPN_STRAIN");// ???????????????
			lookupInDB.put("Sex", "QSC_SPN_SEX");//??????????????????????
			lookupInDB.put("Stage", "QSC_SUB_EMBRYO_STG"); //?????????????????
			lookupInDB.put("Age", "CONCAT(QSC_SPN_STAGE_FORMAT,QSC_SPN_STAGE)");//??????????????
			lookupInDB.put("Genotype", "QIC_SPN_WILDTYPE"); //??????????????????
			lookupInDB.put("Lab Name", "QSC_PER_NAME");//????????????????
			lookupInDB.put("Platform GEO ID", "QMC_PLT_GEO_ID");
			lookupInDB.put("Platform Title", "QMC_PLT_TITLE");
			lookupInDB.put("Platform Name", "QMC_PLT_NAME");
			lookupInDB.put("Sample Strain", "QMC_SMP_STRAIN");
			lookupInDB.put("Sample Sex", "QMC_SMP_SEX");
			lookupInDB.put("Sample Age", "QMC_SMP_DEVELOPMENT_STAGE");
			lookupInDB.put("Sample Stage", "QMC_SMP_THEILER_STAGE");
			lookupInDB.put("Pattern", "QIC_PTN_PATTERN");
			lookupInDB.put("Location", "DESCRIPTION_OID");
			//separate
			lookupInDB.put("Probe Name", "QIC_PRB_PROBE_NAME");
			lookupInDB.put("MGI ID", "QIC_RPR_LOCUS_TAG");
			lookupInDB.put("GenBank ID", "QIC_RPR_GENBANK");
			lookupInDB.put("Ensembl ID", "QIC_RPR_ENSEMBL");
			lookupInDB.put("Ontology Terms", "QSC_ANO_COMPONENT_NAME"); //???????????????
			lookupInDB.put("EMAP ID", "QSC_ATN_PUBLIC_ID"); //??????????//
			lookupInDB.put("Submission Date", "QSC_SUB_SUB_DATE"); // ????????????????//
			lookupInDB.put("Series GEO ID", "QMC_SER_GEO_ID");
			lookupInDB.put("Series Title", "QMC_SER_TITLE");
			lookupInDB.put("Platform GEO ID", "QMC_PLT_GEO_ID");
			lookupInDB.put("Platform Title", "QMC_PLT_TITLE");
			lookupInDB.put("Platform Name", "QMC_PLT_NAME");
			lookupInDB.put("Sample GEO ID", "QMC_SMP_GEO_ID");
			lookupInDB.put("Affymetrix ID", "MBC_GLI_PROBE_SET_NAME");
			lookupInDB.put("Clone Name", "QIC_PRB_CLONE_NAME");
			return lookupInDB;
	   }	  
	  
	  /**
	   * <p>modified by xingjun - 03/09/2008 - 
	   * try to put TG data displayed before array data</p>
	   * @return
	   */
	  final static public Hashtable getLookupTable(){
			Hashtable<String, String> lookupTable = new Hashtable<String,String>();
			lookupTable.put("QIC_RPR_NAME","QSC_ISH_CACHE");//Probe Name
			lookupTable.put("QIC_PRB_TISSUE","QSC_ISH_CACHE");//Tissue
			lookupTable.put("QIC_PRB_STRAIN","QSC_ISH_CACHE");//Probe Strain
			lookupTable.put("QIC_SPN_WILDTYPE","QSC_ISH_CACHE");//Gene Type
			lookupTable.put("QIC_PRB_VISUAL_METHOD","QSC_ISH_CACHE");//Visualization Method
			lookupTable.put("QIC_RPR_SYMBOL, MBC_GNF_SYMBOL","QSC_ISH_CACHE,MIC_BROWSE_CACHE");//Gene Symbol
			lookupTable.put("QIC_RPR_NAME","QSC_ISH_CACHE");//Gene Name
			lookupTable.put("QIC_RSY_SYNONYM","QSC_ISH_CACHE");//Gene Synonym
			lookupTable.put("QIC_RPR_LOCUS_TAG","QSC_ISH_CACHE");//MGI ID
			lookupTable.put("QIC_RPR_GENBANK","QSC_ISH_CACHE");//GenBank ID
			lookupTable.put("QIC_RPR_ENSEMBL","QSC_ISH_CACHE");//Ensembl ID
			lookupTable.put("QIC_ANO_COMPONENT_NAME, QMC_ANO_COMPONENT_NAME","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Ontology Terms
			lookupTable.put("QIC_ATN_PUBLIC_ID, QMC_ATN_PUBLIC_ID","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//EMAP ID
			lookupTable.put("QIC_SPN_ASSAY_TYPE, QMC_SPN_ASSAY_TYPE","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Assay Type
			lookupTable.put("QIC_SPN_FIXATION_METHOD, QMC_SPN_FIXATION_METHOD","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Fixation Method
			lookupTable.put("QIC_SPN_STRAIN, QMC_SPN_STRAIN","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Specimen Strain
			lookupTable.put("QIC_SPN_SEX, QMC_SPN_SEX","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Sex
			lookupTable.put("QIC_STG_STAGE_DISPLAY, QMC_STG_STAGE_DISPLAY","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Stage
			lookupTable.put("TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END), " +
					"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END)",
					"QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Age
			lookupTable.put("QIC_SPN_WILDTYPE, QMC_SPN_WILDTYPE","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Genotype
			lookupTable.put("QIC_PER_NAME, QMC_PER_NAME","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Lab Name
			lookupTable.put("QIC_SUB_SUB_DATE, QMC_SUB_SUB_DATE","QSC_ISH_CACHE as ish,QSC_MIC_CACHE as mic");//Submission Date
			lookupTable.put("QMC_PLT_GEO_ID","QSC_MIC_CACHE");//Platform GEO ID
			lookupTable.put("QMC_PLT_TITLE","QSC_MIC_CACHE");//Platform Title
			lookupTable.put("QMC_PLT_NAME","QSC_MIC_CACHE");//Platform Name
			lookupTable.put("QMC_SER_GEO_ID","QSC_MIC_CACHE");//Seires GEO ID
			lookupTable.put("QMC_SER_TITLE","QSC_MIC_CACHE");//Series Title
			lookupTable.put("QMC_SMP_GEO_ID","QSC_MIC_CACHE");//Sample GEO ID
			lookupTable.put("QMC_SMP_STRAIN","QSC_MIC_CACHE");//Sample Strain
			lookupTable.put("QMC_SMP_SEX","QSC_MIC_CACHE");//Sample Sex
			lookupTable.put("QMC_SMP_DEVELOPMENT_STAGE","QSC_MIC_CACHE");//Sample Age
			lookupTable.put("QMC_SMP_THEILER_STAGE","QSC_MIC_CACHE");//Sample Stage
			lookupTable.put("MBC_GLI_PROBE_SET_NAME","MIC_BROWSE_CACHE");//Affymetrix ID
			lookupTable.put("QIC_PRB_CLONE_NAME","QSC_ISH_CACHE");//Clone Name
			lookupTable.put("QIC_PTN_PATTERN","QSC_ISH_CACHE");//Pattern
			lookupTable.put("QIC_PRB_PROBE_NAME","QSC_ISH_CACHE");//Probe Name	
			lookupTable.put("DESCRIPTION_OID","LKP_LOCATION");//Ensembl ID
			lookupTable.put("QIC_RPR_MTF_JAX","QSC_ISH_CACHE");//Clone name
			return lookupTable;
	   }	  
	  
	  final static public String getISHSelect(){
		  return "(select distinct QIC_RPR_SYMBOL col1, "+
		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') col2,"+  
		"QIC_EXP_STRENGTH col3,"+
		"QIC_SUB_SOURCE col4,"+
		"QIC_SUB_SUB_DATE col5,"+
		"QIC_STG_STAGE_DISPLAY col6,"+
		"QIC_SPN_ASSAY_TYPE col7,"+
		"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
		"QIC_SUB_THUMBNAIL col9,"+
		"QIC_SUB_ACCESSION_ID col10,"+
		"'' col11,'' col12, REPLACE(QIC_SUB_ACCESSION_ID, ':" + "', '" + "no" + "') col13, QIC_SUB_ASSAY_TYPE col14, " +
		" QIC_SPN_SEX col15,"+
		"QIC_PRB_PROBE_NAME col16,QIC_SPN_WILDTYPE col17, QIC_RPR_LOCUS_TAG col18 ";
	  }
	  
	  final static public String fromISHTissue(){		  
		  return "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) " +
          "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
          "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID ";
	  }

	  final static public String fromISHStage(){		  
		  return "LEFT JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK ";
	  }
	  
	  
    final static public String getISHSelectForAnatomy(){
    	return "(select distinct QIC_RPR_SYMBOL col1, "+
    	    "'' col2,"+
    	    "QIC_EXP_STRENGTH col3,"+
    	    "QIC_SUB_SOURCE col4,"+
    	    "QIC_SUB_SUB_DATE col5,"+
    	    "QIC_STG_STAGE_DISPLAY col6,"+
    	    "QIC_SPN_ASSAY_TYPE col7,"+
    	    "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
                   "QIC_SUB_THUMBNAIL col9,"+
    	    "QIC_SUB_ACCESSION_ID col10,"+
    	    "'' col11,'' col12, REPLACE(QIC_SUB_ACCESSION_ID, ':" + "', '" + "no" + "') col13, QIC_SUB_ASSAY_TYPE col14, QIC_SPN_SEX col15,"+
    		"QIC_PRB_PROBE_NAME col16,QIC_SPN_WILDTYPE col17, QIC_RPR_LOCUS_TAG col18 ";
	    }
 
    
	    public static String getISHSelectForAnatomy(String anatomyTerm) {
			  return "(select distinct QIC_RPR_SYMBOL col1, "+
			"QIC_ANO_COMPONENT_NAME col2, "+
	        "QIC_EXP_STRENGTH col3,"+
			"QIC_SUB_SOURCE col4,"+
			"QIC_SUB_SUB_DATE col5,"+
			"QIC_STG_STAGE_DISPLAY col6,"+
			"QIC_SPN_ASSAY_TYPE col7,"+
			"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
			"QIC_SUB_THUMBNAIL col9,"+
			"QIC_SUB_ACCESSION_ID col10,"+
			"'' col11,'' col12, REPLACE(QIC_SUB_ACCESSION_ID, ':" + "', '" + "no" + "') col13, QIC_SUB_ASSAY_TYPE col14, QIC_SPN_SEX col15," +
			"QIC_PRB_PROBE_NAME col16,QIC_SPN_WILDTYPE col17, QIC_RPR_LOCUS_TAG col18 ";
	  }
	  
		  final static public String getMICSelect(){
			  return "(select distinct "+
			"'' col1,"+
			"QMC_ANO_COMPONENT_NAME col2,"+
	                "'' col3,"+
			"QMC_SUB_SOURCE col4,"+
			"QMC_SUB_SUB_DATE col5,"+
			"QMC_STG_STAGE_DISPLAY col6,"+
			"QMC_SPN_ASSAY_TYPE col7,"+
			"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END) col8,"+
			"'' col9,"+
			"QMC_SUB_ACCESSION_ID col10,"+
			"'' col11,"+
			"'' col12, '' col13, 'Microarray' col14, QMC_SPN_SEX col15, '' col16, QMC_SPN_WILDTYPE col17  ";
		  }
	  
	  
	  /**
	   * <p>modified by xingjun - 15/09/2009 - temporarily commented out column MBC_GLI_DETECTION </p>
	   * <p>xingjun - 22/10/2009 - changed concatenation of Age column (col8)</p>
	   * <p>xingjun - 24/06/2011 - changed MBC_PER_NAME to a subquery to pick up possible multiple PIs</p>
	   * @return
	   */
	  final static public String getMICSelectForMerhan(){
		  return "(select distinct "+
		"MBC_GNF_SYMBOL col1,"+
		"MBC_ANO_COMPONENT_NAME col2,"+
                "'' col3,"+
		"MBC_SUB_SOURCE col4,"+
		"MBC_SUB_SUB_DATE col5,"+
		"MBC_STG_STAGE_DISPLAY col6,"+
		"MBC_SPN_ASSAY_TYPE col7,"+
		"concat(TRIM(CASE MBC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(MBC_SPN_STAGE,' ',MBC_SPN_STAGE_FORMAT) ELSE CONCAT(MBC_SPN_STAGE_FORMAT,MBC_SPN_STAGE) END)) col8,"+
		"'' col9,"+
		"MBC_SUB_ACCESSION_ID col10,"+
		"'' col11,"+
		"'' col12, '' col13, 'Microarray' col14, QMC_SPN_SEX col15, '' col16, QMC_SPN_WILDTYPE col17, MBC_MAN_MGI_ID col18 ";
	  }

	  final static public String getMICSelectForAnatomy(){
		  return "(select distinct "+
			"'' col1,"+
			"QMC_ANO_COMPONENT_NAME col2,"+
	                "'' col3,"+
			"QMC_SUB_SOURCE col4,"+
			"QMC_SUB_SUB_DATE col5,"+
			"QMC_STG_STAGE_DISPLAY col6,"+
			"QMC_SPN_ASSAY_TYPE col7,"+
			"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END) col8,"+
			"'' col9,"+
			"QMC_SUB_ACCESSION_ID col10,"+
			"'' col11,"+
			"'' col12, '' col13, 'Microarray' col14, QMC_SPN_SEX col15, '' col16, QMC_SPN_WILDTYPE col17, '' col18 ";
	  }	 
	  
	  final static public String getMICSelectForGEOID(){
		  return "(select distinct "+
		"'' col1,"+
		"QMC_ANO_COMPONENT_NAME col2,"+
                "'' col3,"+
		"QMC_SUB_SOURCE col4,"+
		"QMC_SUB_SUB_DATE col5,"+
		"QMC_STG_STAGE_DISPLAY col6,"+
		"QMC_SPN_ASSAY_TYPE col7,"+
		"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END) col8,"+
		"'' col9,"+
		"QMC_SUB_ACCESSION_ID col10,"+
		"'' col11,"+
		"'' col12, '' col13, 'Microarray' col14 , QMC_SPN_SEX col15, '' col16, QMC_SPN_WILDTYPE col17, '' col18 ";
	  }	
	  
	  final static public String getNGDSelect(){
		  return "(SELECT DISTINCT '' col1, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') col2, '' col3, SUB_SOURCE col4, " +
				  "SUB_SUB_DATE col5, STG_STAGE_DISPLAY col6, SUB_ASSAY_TYPE col7, TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END) col8, " +
				  "'' col9, SUB_ACCESSION_ID col10, '' col11, '' col12, '' col13, 'Sequence' col14, NGS_SEX col15, '' col16, CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE " +
				  "CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN " +
				  "(SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
				  "ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END col17, '' col18 ";
	  }
	  
	  final static public String getISHCount(){
	      return "select count(distinct QIC_SUB_ACCESSION_ID) ";
	  }
	  
	  final static public String getMICCount(){
	      return "select count(distinct QMC_SUB_ACCESSION_ID) ";
	  }
	  
	  
	  
	  final static public String getISHFrom(){
		  return " from QSC_ISH_CACHE LEFT JOIN REF_STAGE ishstage ON ishstage.STG_OID = QIC_SUB_STAGE_FK ";
	  }
	  
	  final static public String getISHFromLocation(){
		  return " from QSC_ISH_CACHE Left join QSC_ISH_LOCATION on QIL_SUB_ACCESSION_ID=QIC_SUB_ACCESSION_ID ";
	  }
	  
	  final static public String getMICFrom(){
		  return 
		" from MIC_BROWSE_CACHE as Gene "+
		"LEFT JOIN REF_STAGE ON STG_OID = MBC_SUB_STAGE_FK "+
		"join QSC_MIC_CACHE as Cache WHERE MBC_SUB_ACCESSION_ID=QMC_SUB_ACCESSION_ID ";
	  }
	  
	  final static public String getMICFromQSCOnly(){
		  return 
		" from QSC_MIC_CACHE as Cache "+
		" where ";
	  }
	  
	  final static public String getMICFromAdvanced(){
		  return 
		" from "+
		"QSC_MIC_CACHE as Cache "+
		"where QMC_SUB_ACCESSION_ID is not null"+
		" ";
	  }	  
	  
	  final static public String getMICFromMerhan(){
		  return " from MIC_BROWSE_CACHE as Gene "+	
		" ";
	  }
	  
	  final static public String getMICFromForAnatomy() {
		  return " FROM QSC_MIC_CACHE as Gene LEFT JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK";
	  }
	  
	  final static public String getNGDFrom(){
		  return 
		" FROM ISH_SUBMISSION JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID JOIN NGD_SAMPLE_SERIES ON NGL_SAMPLE_FK = NGS_OID " +
		"JOIN NGD_SERIES ON NGL_SERIES_FK = NGR_OID JOIN NGD_PROTOCOL ON NGS_PROTOCOL_FK=NGP_OID JOIN ISH_PERSON ON PER_OID = SUB_PI_FK " +
		"JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK=SUB_OID JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=IST_COMPONENT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
		"LEFT JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK "+
		"LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID WHERE SUB_ASSAY_TYPE = 'NextGen' " +
		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	  }
	  
	  final static public String getUnion(){
		  return " union ";
	  }
	  
	  final static public String getLimit(){
		  return " limit 10) ";
	  }
	  
	  final static public String getOrder(){
		  return " order by col1 ";
	  }
	  
	  /**
	   * @author xingjun - 06/12/2007
	   * modified from ying's method with the same name - see method after this one
	   * 
	   * @param id
	   * @param assayType
	   * @return
	   */
	  final static public String getComponentQuery(String id, String assayType) {
		  if (assayType.equalsIgnoreCase("ISH"))
			  return " QIC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE," +
			  "ANA_TIMED_NODE DESCEND_ATN " +
			  "where ANCES_ATN.ATN_PUBLIC_ID in ('" + id + "') " +
			  "and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + ") ";
		  else if (assayType.equalsIgnoreCase("Microarray"))
			  return " QMC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE," +
			  "ANA_TIMED_NODE DESCEND_ATN " +
			  "where ANCES_ATN.ATN_PUBLIC_ID in ('" + id + "') " +
			  "and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + ") ";
		  else
			  return null;
	  }
	  
	  // overload of above method
	  final static public String getComponentQuery(String id) {
		  return " QSC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
		  "from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE," +
		  "ANA_TIMED_NODE DESCEND_ATN " +
		  "where ANCES_ATN.ATN_PUBLIC_ID in ('" + id + "') " +
		  "and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
		  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
		  "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + ") ";
	  }

	  /**
	   * @author xingjun - 06/12/2007
	   * modified from ying's method with the same name - see method after this one
	   * 
	   * @param name
	   * @param assayType
	   * @return
	   */
	  final static public String getComponentNameQuery(String name, String assayType) {
		  if (assayType.equalsIgnoreCase("ISH"))
			  return " QIC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN," +
			  "ANAD_RELATIONSHIP_TRANSITIVE," +
			  "ANA_TIMED_NODE DESCEND_ATN " +
			  "where ANCES_ATN.ATN_PUBLIC_ID in " +
			  "(select ATN_PUBLIC_ID from ANA_TIMED_NODE, ANA_NODE " +
			  "WHERE ATN_NODE_FK=ANO_OID and ANO_COMPONENT_NAME in ('" + name + "')) " +
			  "and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + ") ";
		  else if (assayType.equalsIgnoreCase("Microarray"))
			  return " QMC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN," +
			  "ANAD_RELATIONSHIP_TRANSITIVE," +
			  "ANA_TIMED_NODE DESCEND_ATN " +
			  "where ANCES_ATN.ATN_PUBLIC_ID in " +
			  "(select ATN_PUBLIC_ID from ANA_TIMED_NODE, ANA_NODE " +
			  "WHERE ATN_NODE_FK=ANO_OID and ANO_COMPONENT_NAME in ('" + name + "')) " +
			  "and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + ") ";
		  else
			  return null;
	  }
	  
	  // overload of above method
	  final static public String getComponentNameQuery(String name) {
		  return " QSC_ATN_PUBLIC_ID in (" + "select DESCEND_ATN.ATN_PUBLIC_ID " +
		  "from ANA_TIMED_NODE ANCES_ATN," +
		  "ANAD_RELATIONSHIP_TRANSITIVE," +
		  "ANA_TIMED_NODE DESCEND_ATN " +
		  "where ANCES_ATN.ATN_PUBLIC_ID in " +
		  "(select ATN_PUBLIC_ID from ANA_TIMED_NODE, ANA_NODE " +
		  "WHERE ATN_NODE_FK=ANO_OID and ANO_COMPONENT_NAME in ('" + name + "')) " +
		  "and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK " +
		  "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
		  "and ANCES_ATN.ATN_STAGE_FK    = DESCEND_ATN.ATN_STAGE_FK " + ") ";
	  }

	  final static public String getComponentIDQuery(String[] emapids, String ish) {
		  String ids = "";
		  for(int i = 0; i < emapids.length; i++) {
			  ids += "'"+emapids[i] + "',";
		  }
		  if(emapids.length >= 1) {
			  ids = ids.substring(0, ids.length()-1);
		  }
		  return " " + ish + " in ("+"select DESCEND_ATN.ATN_PUBLIC_ID "+
		    "from ANA_TIMED_NODE ANCES_ATN,"+
	         "ANAD_RELATIONSHIP_TRANSITIVE,"+
	         "ANA_TIMED_NODE DESCEND_ATN "+
	    "where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
	      "and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK " +
	      "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	      "and ANCES_ATN.ATN_STAGE_FK    = DESCEND_ATN.ATN_STAGE_FK "+
		  ") ";
	  }
	  
	  final static public String getPublicISHNumber(String[] emapids) {
		  String ids = "";
		  for(int i = 0; i < emapids.length; i++) {
			  ids += "'"+emapids[i] + "',";
		  }
		  if(emapids.length >= 1) {
			  ids = ids.substring(0, ids.length()-1);
		  }
		  return "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION, ISH_EXPRESSION WHERE SUB_ASSAY_TYPE = 'ISH' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND EXP_SUBMISSION_FK=SUB_OID and "+
		  " EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true)" ;
	  }
	  
	  final static public String getPublicISHTypesNumber(String[] emapids, String type) {
		  String ids = "";
		  for(int i = 0; i < emapids.length; i++) {
			  ids += "'"+emapids[i] + "',";
		  }
		  if(emapids.length >= 1) {
			  ids = ids.substring(0, ids.length()-1);
		  }
		  return "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION, ISH_SPECIMEN, ISH_EXPRESSION WHERE SUB_ASSAY_TYPE = 'ISH' " +
		  		 "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND SPN_SUBMISSION_FK = SUB_OID AND " +
		  		 "EXP_SUBMISSION_FK=SUB_OID AND SPN_ASSAY_TYPE='"+type+"' AND "+
		  " EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true)" ;
	  }
	  
	  // for every fomat except DPC, which is what GUDMAP uses, the format goes first.
	  public static String stageFormatConcat = bundle.getString("project").equals("GUDMAP") ? 
			  "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) " +
			  "ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" : 
			  "CONCAT(SPN_STAGE_FORMAT, SPN_STAGE)";
	  
	// next gen data stores stage in NGS_SAMPLE table.
		  public static String stageFormatConcatForNGD = bundle.getString("project").equals("GUDMAP") ? 
				  "TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) " +
				  "ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END)" : 
				  "CONCAT(NGS_STAGE_FORMAT, NGS_DEV_STAGE)";

	  final static public String getPublicIHCNumber(String[] emapids) {
		  String ids = "";
		  for(int i = 0; i < emapids.length; i++) {
			  ids += "'"+emapids[i] + "',";
		  }
		  if(emapids.length >= 1) {
			  ids = ids.substring(0, ids.length()-1);
		  }
		  return "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION, ISH_EXPRESSION WHERE SUB_ASSAY_TYPE = 'IHC' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND EXP_SUBMISSION_FK=SUB_OID and "+
		  " EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true)" ;
	  }	  
	  
	  final static public String getPublicTransgenicNumber(String[] emapids) {
		  String ids = "";
		  for(int i = 0; i < emapids.length; i++) {
			  ids += "'"+emapids[i] + "',";
		  }
		  if(emapids.length >= 1) {
			  ids = ids.substring(0, ids.length()-1);
		  }
		  return "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION, ISH_EXPRESSION WHERE SUB_ASSAY_TYPE = 'TG' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND EXP_SUBMISSION_FK=SUB_OID and "+
		  " EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
		         " ANAD_RELATIONSHIP_TRANSITIVE, "+
		         " ANA_TIMED_NODE DESCEND_ATN, "+
		         " ANA_NODE, "+
		         " ANAD_PART_OF "+
		    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
		      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
		      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
		      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
		      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
		      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true)" ;
	  }	  
	  
	  //query to find the number of public array submissions in db
	  final static String name128 = "NUMBER_OF_PUBLIC_SUBMISSIONS_ARRAY";
	  final static String query128 = "select count(distinct SUB_ACCESSION_ID) from MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION where SMP_SUBMISSION_FK=SUB_OID and SUB_IS_PUBLIC=1";
		  /*select distinct DESCEND_ATN.ATN_PUBLIC_ID
		    from ANA_TIMED_NODE ANCES_ATN,
		         ANAD_RELATIONSHIP_TRANSITIVE,
		         ANA_TIMED_NODE DESCEND_ATN,
		         ANA_NODE,        
		         ANAD_PART_OF 
		    where ANCES_ATN.ATN_PUBLIC_ID in ('EMAP:2220') 
		      and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK 
		      and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK 
		      and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK 
		      and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK       
		      and ANO_OID = DESCEND_ATN.ATN_NODE_FK 
		      and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true*/
	  
	  final static String name129 = "SAMPLE_SOURCE";
	  final static String query129 = "select concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') from ISH_SUBMISSION "+
	  "join ISH_EXPRESSION on EXP_SUBMISSION_FK=SUB_OID "+
	  "join ANA_TIMED_NODE on ATN_PUBLIC_ID=EXP_COMPONENT_ID "+
	  "join ANA_NODE on ATN_NODE_FK = ANO_OID where SUB_ACCESSION_ID=?";  
	  
	  /*public static String endsBrowseSubmissionArray = "FROM ISH_SUBMISSION " +
	  		"JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID " +
	  		"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
	  		"JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
	  		"JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID " +
	  		"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK " +
	  		"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID " +
	  		"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
	  		"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
	  		"LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID " +
	  		"LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
	  		"WHERE SUB_ASSAY_TYPE = 'Microarray' " +
	  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";*/
	  
	  public static String endsBrowseSubmissionArray = "FROM ISH_SUBMISSION " +
		  		"JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID " +
		  		"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
		  		"JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
		  		"JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID " +
		  		"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK " +
		  		"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID " +
		  		"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
		  		"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
		  		"WHERE SUB_ASSAY_TYPE = 'Microarray' " +
		  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	  
	  public static String endsBrowseSubmissionNGD = "FROM ISH_SUBMISSION " +
		  		"JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID " +
		  		"JOIN NGD_SAMPLE_SERIES ON NGL_SAMPLE_FK = NGS_OID " +
		  		"JOIN NGD_SERIES ON NGL_SERIES_FK = NGR_OID " +
		  		"JOIN NGD_PROTOCOL ON NGS_PROTOCOL_FK=NGP_OID " +
		  		"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK " +
		  		"JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK=SUB_OID " +
		  		"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=IST_COMPONENT " +
		  		"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
		  		"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
		  		"LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID " +
		  		"LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
		  		"WHERE SUB_ASSAY_TYPE = 'NextGen' " +
		  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	  
	  public static String groupBySubmissionArray = "GROUP BY SUB_ACCESSION_ID,SMP_GEO_ID,STG_STAGE_DISPLAY,"+ stageFormatConcat + ", SUB_SOURCE, SUB_SUB_DATE,SMP_SEX,SRM_SAMPLE_DESCRIPTION,SMP_TITLE, SER_GEO_ID, SPN_ASSAY_TYPE ";
	  
	  public static String groupBySubmissionNGD = "GROUP BY SUB_ACCESSION_ID,NGS_GEO_ID,NGS_THEILER_STAGE,"+ stageFormatConcatForNGD + ", SUB_SOURCE, SUB_SUB_DATE,NGS_SEX,NGS_DESCRIPTION,NGS_SAMPLE_NAME, NGR_GEO_ID ";
	  
	  public static String findDescendant = " AND EXP_COMPONENT_ID IN (SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
	    "FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
	    "WHERE ANCES_ATN.ATN_PUBLIC_ID in (COMPONENT_IDS) " +
	    "AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	    "AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	    "AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
	    "AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
	    "AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";

      /* ---query to find summary info on every array entry in the db--- */
	  final static String name50 = "ALL_ENTRIES_ARRAY_FOCUS";
	  final static String query50 = "SELECT DISTINCT SUB_ACCESSION_ID,SMP_GEO_ID," +
			   "STG_STAGE_DISPLAY, " + 
//			   "SMP_THEILER_STAGE, " + 
			   stageFormatConcat +", " +
			   "SUB_SOURCE, " +
			   "SUB_SUB_DATE,SMP_SEX,SRM_SAMPLE_DESCRIPTION,SMP_TITLE, " +
			   "SER_GEO_ID, GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', '), " +
			   "CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
	    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
	    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
	    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
	    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE, " +
			   "SUB_ASSAY_TYPE , SPN_ASSAY_TYPE,  PER_OID " + endsBrowseSubmissionArray;
	  
	  
	  
		final static String[] ISH_BROWSE_ALL_SQL_COLUMNS = {"RPR_SYMBOL",
			"SUB_ACCESSION_ID",
			"SUB_SOURCE",
			"SUB_SUB_DATE", 
			"IF(SUB_CONTROL=0,SUB_ASSAY_TYPE,CONCAT(SUB_ASSAY_TYPE,' control')) SUB_ASSAY_TYPE",
			"RPR_JAX_ACC",
//			"SUB_EMBRYO_STG",
			"STG_STAGE_DISPLAY",
			stageFormatConcat,
			"SPN_SEX",
			"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
	    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
	    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
	    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
	    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE",
			/*"SPN_WILDTYPE",*/
			"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME)",//DEREK 110814 
			"(SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID) EXP_STRENGTH",
			"SPN_ASSAY_TYPE",
			"CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_URL.URL_SUFFIX, IMG_SML_FILENAME)",
			"RPR_LOCUS_TAG",
			"REPLACE(SUB_ACCESSION_ID, ':', 'no')", };
		
		
		
      final static String getISH_BROWSE_ALL_COLUMNS() {
    	  String s = "SELECT DISTINCT ";
    	  for (int i=0; i<ISH_BROWSE_ALL_SQL_COLUMNS.length; i++) {
    		  if (i > 0)
    			  s += ", ";
    		  s +=  ISH_BROWSE_ALL_SQL_COLUMNS[i] ;
    	  }
    	  s += " ";
    	  return s;
      }

      // - commented out clause linked to expression table - no need to have and made the query far too slow
      // - changed back and expression table join will be handled in the DAO code
      // - use LEFT JOIN instead of JOIN to include submissions without expression info - 22/09/2009
	  final static String ISH_BROWSE_ALL_TABLES = "FROM ISH_SUBMISSION " +
			  "JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID " +
              "JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK " +
              "JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
              "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
          "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
          "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
          "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
          	  "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
              "JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
              "AND IMG_TYPE NOT LIKE '%wlz%' AND IMG_ORDER = (SELECT MIN(I.IMG_ORDER) FROM ISH_ORIGINAL_IMAGE I WHERE I.IMG_SUBMISSION_FK = SUB_OID) "+
              "JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = IMG_URL_FK"; 
	                                                  
	  final static String PUBLIC_ENTRIES_Q = " WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	  final static public String getAssayType(String type) {
		  return " AND SUB_ASSAY_TYPE='"+type+"' ";
	  }

	  static String getAssayType(String[] type) {
		  String assayType = " AND (SUB_ASSAY_TYPE = '";
		  int len = type.length;
		  if (len == 1) { // there's only one assay type
			  assayType += type[0] + "' ";
		  } else { // more than one
			  assayType += type[0] + "' ";
			  for (int i=1;i<type.length;i++) {
				  assayType += "OR SUB_ASSAY_TYPE = '" + type[i] + "' ";
			  }
		  }
		  assayType += " ) ";
		  return assayType;
	  }	  	  
	  
	  /* ---query to find summary info on every ish entry in the db--- */
	  final static String name0 = "ALL_ENTRIES_ISH";
	  final static String query0 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("ISH");

	  final static String name1 = "ALL_ENTRIES_IHC";
	  final static String query1 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("IHC");

	  final static String name2 = "ALL_ENTRIES_INSITU";
	  final static String query2 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType(new String[]{"ISH", "IHC"});
	  
	  final static String name3 = "ALL_ENTRIES_TG";
	  final static String query3 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("TG");

	  final static String name144 = "ALL_ISH_IHC_TG";
	  final static String query144 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType(new String[]{"ISH", "IHC", "TG"});;
	  
	  private static String endsBrowseSubmissionISH = ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("ISH");
	  
	  private static String endsBrowseSubmissionIHC = ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("IHC");
	  
	  private static String endsBrowseSubmissionTG = ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType("TG");

	  final static String NUMBER_OF_SUBMISSION = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) ";
	  
	  final static String name38 = "TOTAL_NUMBER_OF_SUBMISSION_ISH";
	  final static String query38 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionISH;
	  
	  final static String name39 = "TOTAL_NUMBER_OF_SUBMISSION_IHC";
	  final static String query39 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionIHC;
	  
	  final static String name40 = "TOTAL_NUMBER_OF_SUBMISSION_IN_SITU";
	  final static String query40 = NUMBER_OF_SUBMISSION + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q + getAssayType(new String[]{"ISH", "IHC"});
	  
	  final static String name41 = "TOTAL_NUMBER_OF_SUBMISSION_ARRAY";
	  final static String query41 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionArray;
	  
	  final static String name42 = "TOTAL_NUMBER_OF_SUBMISSION_TG";
	  final static String query42 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionTG;
	  
	  final static String name137 = "ALL_SERIES";
	  final static String query137 = "SELECT distinct SER_TITLE, SER_GEO_ID, " +
		"(SELECT COUNT(*) FROM MIC_SERIES_SAMPLE WHERE SRM_SERIES_FK = SER_OID) SAMPLE_NUMBER, " +
		"SUB_SOURCE, " +
		"PLT_GEO_ID, SER_OID, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') " +// added group_concat column - 02/03/2010
		"FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, MIC_PLATFORM, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +// added node and timed node table - 02/03/2010
		"WHERE SRM_SERIES_FK = SER_OID " +
		"AND SRM_SAMPLE_FK= SMP_OID " +
		"AND SMP_SUBMISSION_FK=SUB_OID " +
		"AND EXP_SUBMISSION_FK=SUB_OID " +
		"AND PLT_OID = SER_PLATFORM_FK " +
		"AND SUB_PI_FK = PER_OID " +
		"AND ATN_PUBLIC_ID = EXP_COMPONENT_ID AND ATN_NODE_FK = ANO_OID " + // newly added - 02/03/2010
		"AND EXP_COMPONENT_ID IN " + // newly added - 02/03/2010
		"AND SER_PLATFORM_FK " + // newly added - 02/03/2010
		"GROUP BY SER_GEO_ID ";// newly added - 02/03/2010
	  
	  
	  
	  final static String ORDER_BY_EXPERIMENT_NAME = " ORDER BY TRIM(SER_TITLE)";
	  

	  final static String name138 = "ALL_PLATFORM";
	  final static String query138 = "select distinct PLT_GEO_ID, PLT_NAME, PLT_TECHNOLOGY, PLT_MANUFACTURER, " +
	  		"(SELECT COUNT(*) FROM MIC_SERIES S WHERE S.SER_PLATFORM_FK = PLT_OID) SERIES_NUMBER " + // added by xingjun 30/10/2007 - add an extra column to display related series number for given platform
	  		"from MIC_PLATFORM, MIC_SERIES, "+
		  "MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION "+
		  "where SRM_SERIES_FK = SER_OID and SRM_SAMPLE_FK= SMP_OID and SER_PLATFORM_FK=PLT_OID "+
		  "and SMP_SUBMISSION_FK=SUB_OID and EXP_SUBMISSION_FK=SUB_OID ";
	  
	  final static String name139 = "PI_FROM_NAME";
	  final static String query139 = "SELECT DISTINCT PER_NAME, PER_OID FROM ISH_PERSON WHERE PER_TYPE_FK = 'PI' AND PER_OID=? ";
	  
	  /**
	   * <p>modified by xingjun - 18/09/2009
	   * - added bracket into where clause to make it logically correct </p>
	   * @return
	   */
	  final static public String getISHGeneIndex(String prefix, String organ) {
		  String sql =  "";
		  if(!prefix.equals("0-9")) {
			  sql = "select distinct QGI_RPR_SYMBOL, QGI_ISH_PRESENT, QGI_ISH_NOT_DETECTED, QGI_ISH_UNKNOWN, QGI_MIC_PRESENT, QGI_MIC_NOT_DETECTED, QGI_MIC_UNKNOWN "+
			  		" from QSC_GENE_INDEX where QGI_RPR_SYMBOL like '"+prefix+"%' ";
		  } else {
			  sql = "select distinct QGI_RPR_SYMBOL, QGI_ISH_PRESENT, QGI_ISH_NOT_DETECTED, QGI_ISH_UNKNOWN, QGI_MIC_PRESENT, QGI_MIC_NOT_DETECTED, QGI_MIC_UNKNOWN "+
		  		" from QSC_GENE_INDEX where (QGI_RPR_SYMBOL like '0%' or QGI_RPR_SYMBOL like '1%' or "+
			  "QGI_RPR_SYMBOL like '2%' or QGI_RPR_SYMBOL like '3%' or "+
			  "QGI_RPR_SYMBOL like '4%' or QGI_RPR_SYMBOL like '5%' or "+
			  "QGI_RPR_SYMBOL like '6%' or QGI_RPR_SYMBOL like '7%' or "+
			  "QGI_RPR_SYMBOL like '8%' or QGI_RPR_SYMBOL like '9%') ";
		  }
		  if(null != organ && !organ.equals("")) {
			  sql += " AND QGI_ORGAN_KEY='" + organ +"' ";
		  } else {
			  sql += " AND QGI_ORGAN_KEY='0' ";
		  }
		  
			  sql += " order by QGI_RPR_SYMBOL";
		  
		  //System.out.println("INDEX:"+sql);
		  return sql;
	  }
	  
	  final static public String getISHGeneExpresssion1(String symbol, String organ) {
		  String sql = "select count(distinct QIC_SUB_ACCESSION_ID)  from QSC_ISH_CACHE where QIC_RPR_SYMBOL = '"+symbol+"' and QIC_EXP_STRENGTH='present' ";

		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "QIC_ATN_PUBLIC_ID");
		  }
		  return sql;
	  }
	  
	  final static public String getISHGeneExpresssion2(String symbol, String organ) {
		  String sql = "select count(distinct QIC_SUB_ACCESSION_ID)  from QSC_ISH_CACHE where QIC_RPR_SYMBOL = '"+symbol+"' and QIC_EXP_STRENGTH='not detected' ";

		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "QIC_ATN_PUBLIC_ID");
		  }
		  return sql;
	  }
	  
	  final static public String getISHGeneExpresssion3(String symbol, String organ) {
		  String sql = "select count(distinct QIC_SUB_ACCESSION_ID)  from QSC_ISH_CACHE where QIC_RPR_SYMBOL = '"+symbol+"' and QIC_EXP_STRENGTH not in ('present','not detected') ";

		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "QIC_ATN_PUBLIC_ID");
		  }
		  return sql;
	  }
	  
	  final static public String getMicGeneExpresssion1(String symbol, String organ) {
		  String sql = "select count(distinct MBC_SUB_ACCESSION_ID) from MIC_BROWSE_CACHE where MBC_GNF_SYMBOL = '"+symbol+"' and MBC_GLI_DETECTION='P'";
		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "MBC_COMPONENT_ID");
		  }
		  return sql;
	  }
	  
	  final static public String getMicGeneExpresssion2(String symbol, String organ) {
		  String sql = "select count(distinct MBC_SUB_ACCESSION_ID) from MIC_BROWSE_CACHE where MBC_GNF_SYMBOL = '"+symbol+"' and MBC_GLI_DETECTION='A'";
		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "MBC_COMPONENT_ID");
		  }
		  return sql;
	  }
	  
	  final static public String getMicGeneExpresssion3(String symbol, String organ) {
		  String sql = "select count(distinct MBC_SUB_ACCESSION_ID) from MIC_BROWSE_CACHE where MBC_GNF_SYMBOL = '"+symbol+"' and MBC_GLI_DETECTION='M'";
		  if(null != organ && !organ.equals("")) {
			  sql += " AND " + getComponentIDQuery((String[])getEMAPID().get(organ), "MBC_COMPONENT_ID");
		  }
		  return sql;
	  }
	  
	  //query to get a list of gene symbols from a specified reference table. Can only add search params to one column in table 
	  static public String getSymbolsFromGeneInputParamsQuery(String [] input, 
			  String startQuery, String searchColumn, int type){
		  if(input == null)
			  return "";
		  StringBuffer symbolsQ = new StringBuffer(startQuery);
		  //0 == 'like' query ('contains' or 'starts with')
		  if(type == 0) {
			  symbolsQ.append("(");
			  for(int i=0; i<input.length;i++){
	    			if(i==0){
	    				symbolsQ.append(searchColumn+" LIKE ? ");
	    			}
	    			else {
	    				symbolsQ.append("OR "+searchColumn+" LIKE ? ");
	    			}
	    		}
	    		symbolsQ.append(")");
		  }
		  //else type will be 1: equivalent to 'equals'
		  else {
			  symbolsQ.append(searchColumn + " IN (");
			  for(int i=0;i<input.length;i++){
	            	if(i == input.length-1){
	            		symbolsQ.append("?)");
	            	}
	            	else {
	            		symbolsQ.append("?, ");
	            	}
	            }
		  }
		  return symbolsQ.toString();
	  }
	  
	  static public String getSymbolsFromGeneInputParamsQuery(String [] input, 
			  String startQuery, String searchColumn, String order, int type){
		  if(input == null)
			  return "";
		  StringBuffer symbolsQ = new StringBuffer(startQuery);
		  //0 == 'like' query ('contains' or 'starts with')
		  if(type == 0) {
			  symbolsQ.append("(");
			  for(int i=0; i<input.length;i++){
	    			if(i==0){
	    				symbolsQ.append(searchColumn+" RLIKE ? ");
	    			}
	    			else {
	    				symbolsQ.append("OR "+searchColumn+" RLIKE ? ");
	    			}
	    		}
	    		symbolsQ.append(")");
		  }
		  //else type will be 1: equivalent to 'equals'
		  else {
			  symbolsQ.append(searchColumn + " IN (");
			  for(int i=0;i<input.length;i++){
	            	if(i == input.length-1){
	            		symbolsQ.append("?)");
	            	}
	            	else {
	            		symbolsQ.append("?, ");
	            	}
	            }
		  }
		  return symbolsQ.toString() + order;
	  }
	  
	  final static String name140 = "GENE_EXPRESSION_FOR_GIVEN_STRUCTURE";
	  final static String query140 = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
	  		"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
	  		"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
	  		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
	  		"WHERE RPR_LOCUS_TAG = ? " +
	  		"AND EXP_COMPONENT_ID IN " +
	  		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";

	  final static String name141 = "GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE";
	  final static String query141 = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
		"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
		"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
		"WHERE RPR_SYMBOL = ? " +
		"AND EXP_COMPONENT_ID NOT IN " +
		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";
	  
	  final static String name142 = "FIND_CHILD_NODE";
	  final static String query142 = "SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
	  		"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
	  		"WHERE ANCES_ATN.ATN_PUBLIC_ID IN " + // add parent node when assembling the sql
	  		"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	  		"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	  		"AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
	  		"AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
	  		"AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true";

	  // query for counting relevant omim disease number for given geneid
	  final static String name143 = "TOTAL_NUMBER_OF_DISEASE_FOR_GENEID";
	  final static String query143 = "SELECT COUNT(DISTINCT DGA_OMIMID) FROM LNK_GENE_OMIMDIS " +
		  		"JOIN REF_MGI_ORTHOLOGS ON OTH_MGIID=DGA_MGIACC " +
		  		"WHERE ((OTH_MGIID = ?) | (OTH_HUMAN_ENTID = ?)) ";
	  
	  final static String name145 = "ALL_NGD_SERIES";
	  final static String query145 ="SELECT DISTINCT NGR_TITLE, NGR_GEO_ID, SUB_SOURCE, (SELECT COUNT(distinct NGL_SAMPLE_FK) FROM NGD_SAMPLE_SERIES WHERE NGL_SERIES_FK = NGR_OID) SAMPLE_NUMBER, " +
				"NGP_LIBRARY_STRATEGY " +
				"NGR_OID, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') " +
				"FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_PROTOCOL, NGD_SAMPLE, ISH_SUBMISSION, ISH_SP_TISSUE, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +
				"WHERE NGL_SERIES_FK = NGR_OID " +
				"AND NGL_SAMPLE_FK = NGS_OID " +
				"AND NGS_SUBMISSION_FK = SUB_OID " +
				"AND NGS_PROTOCOL_FK=NGP_OID " +
				"AND IST_SUBMISSION_FK=SUB_OID " +
				"AND SUB_PI_FK = PER_OID " +
				"AND ATN_PUBLIC_ID = IST_COMPONENT " +
				"AND ATN_NODE_FK = ANO_OID " +
				"AND IST_COMPONENT IN " +
				"GROUP BY NGR_OID ";
			  
	  /* ---query to find summary info on every array entry in the db--- */
	  final static String name146 = "ALL_ENTRIES_NGD_FOCUS";
	  /*final static String query146 = "SELECT DISTINCT SUB_ACCESSION_ID,  NGS_GEO_ID, NGR_GEO_ID, SUB_SOURCE, " +
	  		"NGP_LIBRARY_STRATEGY, SUB_EMBRYO_STG, " + stageFormatConcatForNGD +", " +
	  		"SUB_SUB_DATE, NGS_SEX, NGS_DESCRIPTION, NGS_SAMPLE_NAME, " +
		    "ALE_GENE, GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', '), "
		   + "SUB_ASSAY_TYPE , PER_OID " + endsBrowseSubmissionNGD;*/
	  /*final static String query146 = "SELECT DISTINCT SUB_ACCESSION_ID,  NGS_GEO_ID, NGR_GEO_ID, SUB_SOURCE, " +
		  		"NGP_LIBRARY_STRATEGY, SUB_EMBRYO_STG, " + stageFormatConcatForNGD +", " +
		  		"SUB_SUB_DATE, NGS_SEX, NGS_DESCRIPTION, NGS_SAMPLE_NAME, " +
		  		"CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE 'NOVALAUE' AS GENOTYPE, " +
			    "GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', '), "
			   + "SUB_ASSAY_TYPE , PER_OID " + endsBrowseSubmissionNGD;*/
	  
	  final static String query146 = "SELECT DISTINCT SUB_ACCESSION_ID,  NGS_GEO_ID, NGR_GEO_ID, SUB_SOURCE, " +
		  		"NGP_LIBRARY_STRATEGY, STG_STAGE_DISPLAY, " + stageFormatConcatForNGD +", " +
		  		"SUB_SUB_DATE, NGS_SEX, NGS_DESCRIPTION, NGS_SAMPLE_NAME, " +
		  		"CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
	    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
	    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
	    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
	    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE, " +
			    "GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', '), "
			   + "SUB_ASSAY_TYPE , PER_OID " + endsBrowseSubmissionNGD;
	  
	  final static String name147 = "NGD_SAMPLE_SOURCE";
	  final static String query147 = "select concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') from ISH_SUBMISSION "+
	  "join ISH_SP_TISSUE on IST_SUBMISSION_FK=SUB_OID "+
	  "join ANA_TIMED_NODE on ATN_PUBLIC_ID=IST_COMPONENT "+
	  "join ANA_NODE on ATN_NODE_FK = ANO_OID where SUB_ACCESSION_ID=?"; 

	  final static String name = "";
	  final static String query = "";

	  // creates an array of all ParamQueries
	  static ParamQuery pqList[] = {
	      new ParamQuery(name0, query0),
	      new ParamQuery(name1, query1),
	      new ParamQuery(name2, query2),
	      new ParamQuery(name3, query3),
	      new ParamQuery(name38, query38),
	      new ParamQuery(name39, query39),
	      new ParamQuery(name40, query40),
	      new ParamQuery(name41, query41),
	      new ParamQuery(name42, query42),
	      new ParamQuery(name50, query50),
	      new ParamQuery(name129, query129),
	      new ParamQuery(name137, query137),
	      new ParamQuery(name138, query138),
	      new ParamQuery(name139, query139),
	      new ParamQuery(name140, query140),
	      new ParamQuery(name141, query141),
	      new ParamQuery(name142, query142),
	      new ParamQuery(name143, query143),
	      new ParamQuery(name144, query144),
	      new ParamQuery(name145, query145),
	      new ParamQuery(name146, query146),
	      new ParamQuery(name147, query147)
	  };

	  //  finds ParamQuery object by name and returns
	  public static ParamQuery getParamQuery(String name) {
	      ParamQuery ret = null;
		
	      for (int i = 0; i < pqList.length; i++) {
		  if (pqList[i].getQueryName().equals(name)) {
		      ret = pqList[i];
		      
		      if (debug)
			  System.out.println("AdvancedSearchDBQuery getParamQuery = "+  ret + "sql = " + ret.getQuerySQL());
		      
		      break;
		  }
	      }
	      return ret;
	  }
}
