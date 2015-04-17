package gmerg.db;

import java.util.ResourceBundle;

import gmerg.utils.Utility;

/**
 * @author gudmap developers
 *
 */

public class DBQuery {

    static protected boolean debug = false;

  static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
  
    public static String dataSource = "SELECT DISTINCT SUB_SOURCE FROM ISH_SUBMISSION WHERE SUB_IS_PUBLIC=1 ORDER BY SUB_SOURCE DESC";

  // for every fomat except DPC, which is what GUDMAP uses, the format goes first.
  public static String stageFormatConcat = bundle.getString("project").equals("GUDMAP") ? 
		  "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) " +
		  "WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" :
          "CONCAT(SPN_STAGE_FORMAT, SPN_STAGE)";
  
	final static String[] ISH_BROWSE_ALL_SQL_COLUMNS = {
		  "RPR_SYMBOL",
		  "SUB_ACCESSION_ID", 
		  "SUB_SOURCE",
		  "SUB_SUB_DATE",
		  "IF(SUB_CONTROL=0,SUB_ASSAY_TYPE,CONCAT(SUB_ASSAY_TYPE,' control')) SUB_ASSAY_TYPE",
		  "RPR_JAX_ACC",
//		  "SUB_EMBRYO_STG",
		  "STG_STAGE_DISPLAY",
		  stageFormatConcat,
		  "SPN_SEX",
		  "CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
		    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
		    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
		    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
		    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE",
		 /* "SPN_WILDTYPE",*/
		  "GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME)",
		  "(SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID) EXP_STRENGTH",
//		  "SUB_INSITU_EXP",
//		  "GROUP_CONCAT(DISTINCT EXP_STRENGTH)",
		  "SPN_ASSAY_TYPE",
		  "CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME)",
		  "RPR_LOCUS_TAG",
		  "REPLACE(SUB_ACCESSION_ID, ':', 'no')"
		  };

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

  
  // added by xingjun 15/11/2007
  final static String ISH_BROWSE_DEFAULT_ORDER_BY_COL = "NATURAL_SORT(TRIM(RPR_SYMBOL)), STG_STAGE_DISPLAY, SPN_SEX";
  

  final static String ISH_BROWSE_ALL_TABLES = "FROM ISH_SUBMISSION " +
                                                  "JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK " +
                                                  "JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
                                                  "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
                                                  "JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " +
                                              "LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +    
                                              "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
                                              "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
                                              "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +                                                  "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
//                                            "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
                                                  "JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
                                                  "AND IMG_TYPE NOT LIKE '%wlz%' AND IMG_ORDER = (SELECT MIN(I.IMG_ORDER) FROM ISH_ORIGINAL_IMAGE I WHERE I.IMG_SUBMISSION_FK = SUB_OID) "+
                                                  "JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = IMG_URL_FK ";

  
  
  final static String PUBLIC_ENTRIES_Q = " WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
  final static String FOR_ANNOTATION_ENTRIES_Q = " WHERE STA_OID = ? AND SUB_IS_DELETED = 0 ";
                                                  
  final static String GENES_IN_COMPONENT_COLS = "SELECT distinct APO_FULL_PATH, EXP_COMPONENT_ID,STG_STAGE_DISPLAY, RPR_SYMBOL, COUNT(DISTINCT SUB_ACCESSION_ID) ";
  final static String GENES_IN_COMPONENT_TABLES = "FROM ISH_SUBMISSION, ISH_EXPRESSION, ISH_PROBE, ANA_NODE, ANA_TIMED_NODE, ANAD_PART_OF, REF_PROBE " +
		  								  "LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
  		                                  "WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
  		                                  "AND SUB_OID = PRB_SUBMISSION_FK " +
  		                                  "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
  		                                  "AND ATN_NODE_FK = ANO_OID " +
  		                                  "AND APO_NODE_FK = ANO_OID " +
  		                                  "AND APO_IS_PRIMARY_PATH = true " +
  		                                  "AND EXP_SUBMISSION_FK = SUB_OID " +
  		                                  "AND PRB_MAPROBE = RPR_OID ";
                                                  
  final static String TOTAL_GENES_IN_COMP_COLS = "SELECT COUNT(distinct APO_FULL_PATH, EXP_COMPONENT_ID,STG_STAGE_DISPLAY, RPR_SYMBOL) ";
  
  final static String ORDER_BY_REF_PROBE_SYMBOL = " ORDER BY natural_sort(TRIM(RPR_SYMBOL))";
  
// natural_sort only works on <255 chars final static String ORDER_BY_LAB_AND_EXPERIMENT = " ORDER BY PER_SURNAME, NATURAL_SORT(TRIM(SER_TITLE))";
final static String ORDER_BY_LAB_AND_EXPERIMENT = " ORDER BY PER_SURNAME";

//natural_sort only works on <255 charsfinal static String NGD_ORDER_BY_LAB_AND_EXPERIMENT = " ORDER BY PER_SURNAME, NATURAL_SORT(TRIM(NGR_TITLE))";
final static String NGD_ORDER_BY_LAB_AND_EXPERIMENT = " ORDER BY PER_SURNAME";

  final static String ORDER_BY_ISH_PROBE_SYMBOL = " ORDER BY natural_sort(TRIM(RPR_SYMBOL))";
  
  final static String JOIN_EXPRESSION_START = " JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID AND ";
  
  final static String ANATOMY_PERSPECTIVE_TERMS = "SELECT DISTINCT ANO_COMPONENT_NAME AS COL1 FROM ANA_NODE, ANAD_PART_OF_PERSPECTIVE WHERE ANO_OID = POP_NODE_FK AND !POP_IS_ANCESTOR AND POP_PERSPECTIVE_FK =  '"+ Utility.getPerspective() + "' "+ 
                                                  "UNION " +
                                                  "SELECT DISTINCT SYN_SYNONYM AS COL1 FROM ANA_SYNONYM, ANA_NODE, ANAD_PART_OF_PERSPECTIVE WHERE SYN_OBJECT_FK = ANO_OID AND ANO_OID = POP_NODE_FK AND POP_PERSPECTIVE_FK = '"+ Utility.getPerspective() + "' AND !POP_IS_ANCESTOR " +
                                                  "ORDER BY natural_sort(COL1)";
  
  /* ---query to find oid of a particular submission--- */
  final static String name124 = "SUBMISSION_OID";
  final static String query124 = "SELECT DISTINCT SUB_OID FROM ISH_SUBMISSION WHERE SUB_ACCESSION_ID = ?";

  /* ---query to find summary info on every ish entry in the db--- */
  final static String name0 = "ALL_ENTRIES_ISH";
  final static String query0 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q ;

  final static String name201 = "ALL_ENTRIES_ISH_FOR_ANNOTATION";
  final static String query201 = getISH_BROWSE_ALL_COLUMNS() + ",STA_OID " + ISH_BROWSE_ALL_TABLES + " JOIN REF_STATUS ON STA_OID=SUB_DB_STATUS_FK " + FOR_ANNOTATION_ENTRIES_Q ;

  // include submissions public and non-public
  final static String name232 = "ALL_COLLECTION_ENTRIES_ISH";
  final static String query232 = "SELECT DISTINCT SUB_ACCESSION_ID QSC_SUB_ACCESSION_ID, " +
  		"RPR_SYMBOL QSC_RPR_SYMBOL, STG_STAGE_DISPLAY QSC_SUB_EMBRYO_STG, " + 
  		stageFormatConcat + " QSC_AGE, PER_SURNAME QSC_PER_NAME, SPN_ASSAY_TYPE QSC_SPN_ASSAY_TYPE, " +
  		"IF(SUB_CONTROL=0,SUB_ASSAY_TYPE,CONCAT(SUB_ASSAY_TYPE,' control')) QSC_ASSAY_TYPE, " +
  		"SUB_SUB_DATE QSC_SUB_SUB_DATE, SPN_SEX QSC_SPN_SEX, PRB_PROBE_NAME QSC_PRB_PROBE_NAME, " +
  		"SPN_WILDTYPE QSC_SPN_WILDTYPE, " +
  		"PRB_PROBE_TYPE QSC_PROBE_TYPE, " + 
  		"CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME) QSC_SUB_THUMBNAIL, " +
  		"'' QSC_TISSUE, '' QSC_SMP_TITLE, '' QSC_SAMPLE_DESCRIPTION, '' QSC_SER_GEO_ID, RPR_LOCUS_TAG QSC_RPR_LOCUS_TAG " + 
  		ISH_BROWSE_ALL_TABLES;
    
  /* ---Set of Strings and Names for each parameterised query involving a submission--- */

  // find details of a particular submission
  final static String name1 = "SUBMISSION_DETAILS";
  final static String query1 = "SELECT SUB_ACCESSION_ID, STG_STAGE_DISPLAY, SUB_ASSAY_TYPE, " +
  		"SUB_MODIFIED_DATE, SUB_MODIFIED_BY, SUB_VERSION_NO, SUB_IS_PUBLIC, SUB_ARCHIVE_ID, " +
  		"SUB_IS_DELETED, SUB_SUBMITTER_FK, SUB_PI_FK, SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, " +
  		"SUB_DB_STATUS_FK, SUB_PROJECT_FK, SUB_BATCH, " +
  		"SUB_NAMESPACE, SUB_OS_ACCESSION, SUB_LOCAL_ID, SUB_SOURCE, SUB_VALIDATION, " +
  		"SUB_CONTROL, SUB_ASSESSMENT, SUB_CONFIDENCE, SUB_LOCALDB_NAME, " +
  		"SUB_LAB_ID, SUB_ACCESSION_ID_2, SUB_OID, STG_NAME FROM ISH_SUBMISSION " +
  		"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
  		"WHERE SUB_ACCESSION_ID = ?";

  final static String name243 = "SUBMISSION_NOTES";
  final static String query243 = "SELECT SNT_VALUE, SNT_TYPE FROM ISH_SUBMISSION_NOTE WHERE  (CONCAT('GUDMAP:', SNT_SUBMISSION_FK) = ?)";

    final static String name244 = "ALLELE_INFO_BY_SUBMISSION_ID";
    final static String query244 = "SELECT ALE_GENE, ALE_MUTATED_GENE_ID, ALE_MGI_ALLELE_ID, "+
	"ALE_LAB_NAME_ALLELE, ALE_ALLELE_NAME, ALE_NON_PAIRED , ALE_TYPE, "+
	"SAL_FIRST_CHROM, SAL_SECOND_CHROM, SAL_TG_REPORTER, SAL_TG_VISUALISATION, SAL_NOTES "+  
	"FROM LNK_SUB_ALLELE LEFT JOIN  ISH_ALLELE ON SAL_ALE_OID_FK=ALE_OID WHERE  (CONCAT('GUDMAP:', SAL_SUBMISSION_FK) = ?) ORDER BY SAL_ORDER ";

  // find details of a probe linked in a submission
  final static String name2 = "SUBMISSION_PROBE";
  final static String query2 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, /* 1-3 */ " +
                              "RPR_LOCUS_TAG, PRB_SOURCE, PRB_STRAIN, /* 4-6 */ " +
                              "PRB_TISSUE, PRB_PROBE_TYPE, PRB_GENE_TYPE, /* 7-9 */ "+
                              "PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, /* 10-12 */ " +
                              "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), /* 13-14 */ " +
                              "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END) as prbName_url, /* 15 */ "+
                              "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), RPR_TYPE, /* 16-17 */ " +
                              "RPR_5_LOC, RPR_3_LOC, /* 18-19 */ " +
                              "RPR_5_PRIMER, RPR_3_PRIMER, /* 20-21 */ " +
                              "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " + 
                              "WHEN 'MGI:' THEN CONCAT(GENE_URL.URL_URL, RPR_LOCUS_TAG) " + 
                              "ELSE /* non MGI */ '' END, /* 22 */" +
                              "RPR_CLONE_NAME_2, PRB_LAB_ID /* 23-24 */" +
                              "FROM ISH_PROBE " + 
                              "JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ? " +
                              "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_OID = 4 " + 
                              "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " + 
                              "JOIN REF_URL GENE_URL ON GENE_URL.URL_TYPE = 'jax_gene' " + 
                              "LEFT JOIN REF_MGI_PRB ON RPR_JAX_ACC = RMP_MGIACC " + 
                              "LEFT JOIN REF_MGI_MRK ON RPR_LOCUS_TAG = RMM_MGIACC " +
                              "LEFT JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = " + 
                              "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
                              "WHEN 'MGI:'     THEN  'jax_gene' " + 
                              "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
                              "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
                              "END";

  // find probe notes linked to a submission
  final static String name3 = "SUBMISSION_PRBNOTE";
  final static String query3 = "SELECT PNT_VALUE FROM ISH_PROBE_NOTE, ISH_SUBMISSION WHERE PNT_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC=1 AND SUB_ACCESSION_ID = ? ORDER BY PNT_SEQ";

  // find maprobe notes for linked to a ish submission -- 02/05/2007
  final static String name125 = "SUBMISSION_MAPROBE_NOTE";
  final static String query125 = "SELECT RPN_NOTES FROM REF_PRB_NOTES, ISH_SUBMISSION, ISH_PROBE, REF_PROBE " +
  		"WHERE SUB_ACCESSION_ID = ? " +
  		"AND PRB_SUBMISSION_FK = SUB_OID " +
  		"AND PRB_MAPROBE = RPR_OID " +
  		"AND RPN_PROBE_FK = RPR_OID " +
  		"AND RPN_ISDELETED = 0 " +
  		"ORDER BY RPN_OID DESC";

  // find maprobe full sequence added by ying
  final static String name126 = "SUBMISSION_FULL_SEQUENCE";
  final static String query126 = "select distinct RPS_SEQUENCE, RPS_RPR_FK FROM REF_PROBE_SEQUENCE, REF_PROBE, ISH_PROBE, ISH_SUBMISSION"+
								 "	where RPR_OID = PRB_MAPROBE "+ 
								 "	AND PRB_SUBMISSION_FK = SUB_OID "+
								 "	AND RPS_RPR_FK=RPR_OID "+
								 "	AND SUB_ACCESSION_ID = ? "+
								 "	ORDER BY RPS_RPR_FK, RPS_SEQ ASC";  
  
  //query2 (find details of a specimen likned to a submission)
  final static String name4 = "SUBMISSION_SPECIMEN";
  final static String query4 = "SELECT SPN_WILDTYPE, SPN_ASSAY_TYPE, SPN_FIXATION_METHOD, SPN_EMBEDDING, SPN_STRAIN, SPN_STAGE_FORMAT, SPN_STAGE, STG_STAGE_DISPLAY, SPN_SEX, SPN_TISSUE_TYPE, SPN_STAGE_ADD_VALUE, SPN_SPECIES  FROM ISH_SPECIMEN, ISH_SUBMISSION LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID WHERE SPN_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND SUB_ACCESSION_ID = ?";

  //query36 (find specimen notes linked to a submission)
  final static String name5 = "SPECIMEN_NOTE";
  final static String query5 = "SELECT MNT_VALUE FROM ISH_SPECIMEN_NOTE, ISH_SUBMISSION WHERE SUB_OID = MNT_SUBMISSION_FK AND SUB_ACCESSION_ID = ? ORDER BY MNT_SEQ";

  //query6 (find original images of a particular submission)
  //ORDER BY NEW IMG_ORDER COLUMN
  final static String name6 = "SUBMISSION_IMAGES";
  final static String query6 = "SELECT SUB_ACCESSION_ID, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME), INT_VALUE, SPN_ASSAY_TYPE, CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND  IMG_SUBMISSION_FK = SUB_OID  JOIN ISH_SPECIMEN ON SPN_SUBMISSION_FK= SUB_OID JOIN REF_URL I ON I.URL_OID = IMG_URL_FK JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK LEFT JOIN ISH_IMAGE_NOTE ON INT_IMAGE_FK = IMG_OID WHERE SUB_IS_DELETED = 0 AND IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' ORDER BY IMG_ORDER ";
  //final static String query6 = "SELECT SUB_ACCESSION_ID, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME), INT_VALUE, SPN_ASSAY_TYPE, CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND  IMG_SUBMISSION_FK = SUB_OID  JOIN ISH_SPECIMEN ON SPN_SUBMISSION_FK= SUB_OID JOIN REF_URL I ON I.URL_OID = IMG_URL_FK JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK LEFT JOIN ISH_IMAGE_NOTE ON INT_IMAGE_FK = IMG_OID WHERE SUB_IS_DELETED = 0 AND IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' ORDER BY IMG_OID ";

  final static String name7 = "SUBMISSION_IMAGE_DETAIL";
  final static String query7 = "SELECT SUB_ACCESSION_ID, RPR_SYMBOL, RPR_NAME, " +
  		"STG_STAGE_DISPLAY, " + stageFormatConcat + ", SUB_ASSAY_TYPE, SPN_ASSAY_TYPE, " +
  				"IMG_FILEPATH, IMG_FILENAME " +
  				"FROM ISH_PROBE " +
  				"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
  				"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
  				"JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
  				"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
  				"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  				"JOIN REF_URL ON URL_OID = IMG_URL_FK " + 
  				"WHERE IMG_TYPE NOT LIKE '%wlz%' AND SUB_ACCESSION_ID = ? " +
  				"ORDER BY IMG_ORDER ";

  final static String name245 = "SUBMISSION_WLZ_DETAIL";
  final static String query245 = "SELECT SUB_ACCESSION_ID, RPR_SYMBOL, RPR_NAME, " +
  		"STG_STAGE_DISPLAY, " + stageFormatConcat + ", SUB_ASSAY_TYPE, SPN_ASSAY_TYPE, " +
  				"CONCAT(I.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME), " +
  				"CONCAT(C.URL_URL, '?greyImg=', IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) " +
  				"FROM ISH_PROBE " +
  				"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
  				"JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
  				"JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
  				"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
  				"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  				"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " + 
  				"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " + 
  				" WHERE IMG_TYPE='opt_wlz_merged'  AND SUB_ACCESSION_ID = ? " +
  				"ORDER BY IMG_ORDER ";
  
  final static String name32 = "SUBMISSION_AUTHOR";

  final static String query32 = "SELECT GROUP_CONCAT(AUT_NAME ORDER BY LSA_ORDER SEPARATOR ', ') AS AUTHOR FROM ISH_AUTHORS, LNK_SUB_AUTHORS, "+
		  						"ISH_SUBMISSION WHERE AUT_OID=LSA_AUT_FK AND LSA_SUB_FK=SUB_OID AND SUB_ACCESSION_ID = ?";


  //query3 (find PI of a submission)
  final static String name8 = "SUBMISSION_PI";
  final static String query8 = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, PER_FAX, PER_OID FROM ISH_PERSON, ISH_SUBMISSION WHERE PER_OID = SUB_PI_FK AND SUB_ACCESSION_ID = ?";

  final static String name236 = "SUBMISSION_PIS";
  final static String query236 = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, PER_FAX, PER_OID " +
  		"FROM ISH_PERSON " +
  		"WHERE PER_OID IN ( " +
  		"SELECT GDT_PERSON_FK FROM REF_GROUP_DETAIL " +
  		"JOIN REF_GROUP ON GDT_GROUP_FK = GRP_OID " +
  		"JOIN REF_SUBMISSION_PERSON_GRP ON SPG_GROUP_FK = GRP_OID " +
  		"WHERE SPG_SUBMISSION_FK = ?)";

  //query4 (find submitter of a submission)
  final static String name9 = "SUBMISSION_SUBMITTER";
  final static String query9 = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, PER_FAX, PER_OID FROM ISH_PERSON, ISH_SUBMISSION WHERE PER_OID = SUB_SUBMITTER_FK AND SUB_IS_PUBLIC=1 AND SUB_ACCESSION_ID = ?";

  //query (find if a submission has any linked expression annotation)
  final static String name10 = "SUB_HAS_ANNOTATION";
  final static String query10 = "SELECT COUNT(*) FROM ISH_EXPRESSION, ISH_SUBMISSION WHERE EXP_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ?";

  //query (find the name of the stage for a submission)
  final static String name11 = "SUB_STAGE_NAME";
  final static String query11 = "SELECT STG_NAME,SPN_SPECIES,STG_ANATOMY  FROM ISH_SUBMISSION, ISH_SPECIMEN, REF_STAGE WHERE SUB_OID = SPN_SUBMISSION_FK AND STG_OID = SUB_STAGE_FK AND SUB_ACCESSION_ID = ?";
 // final static String query11 = "SELECT CONCAT(STG_PREFIX,SUB_EMBRYO_STG),SPN_SPECIES,SUB_EMBRYO_STG  FROM ISH_SUBMISSION, ISH_SPECIMEN, REF_STAGE WHERE SUB_OID = SPN_SUBMISSION_FK AND SUB_ACCESSION_ID = ?";
//  final static String query11 = "SELECT CONCAT(STG_PREFIX,SUB_EMBRYO_STG) FROM ISH_SUBMISSION, ISH_SPECIMEN, REF_STAGE WHERE SUB_OID = SPN_SUBMISSION_FK AND SPN_SPECIES = STG_SPECIES_FK AND SUB_ACCESSION_ID = ?";

  //query (map human to mouse stage)
  final static String name274 = "MAP_STAGE_NAME";
  final static String query274 = "SELECT STG_VALUE FROM REF_STAGE, REF_HUMAN_STAGE WHERE HSS_MOUSE_FK = STG_OID AND HSS_CARNEGIE = ?";
  
  //query 26 (find expression mapping for a particular component in a submission)
  final static String name13 = "EXPRESSION_DETAIL";
  final static String query13 = "SELECT EXP_COMPONENT_ID, ANO_COMPONENT_NAME, APO_FULL_PATH, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, EXP_OID, STG_STAGE_DISPLAY, SUB_OID, SUB_DB_STATUS_FK " +
    "FROM ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANAD_PART_OF, ANA_TIMED_NODE,REF_STAGE  " +
    "WHERE EXP_SUBMISSION_FK = SUB_OID " +
    "AND SUB_ACCESSION_ID = ? " + // 1
    "AND SUB_STAGE_FK = STG_OID " +
    "AND EXP_COMPONENT_ID = ? " + // 2
    "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
    "AND ANO_OID = ATN_NODE_FK " +
    "AND APO_NODE_FK = ANO_OID " +
    "AND APO_IS_PRIMARY_PATH = 1";

  //query 27 (find expression notes for a particular component in a submission)
  final static String name14 = "EXPRESSION_NOTE";
  final static String query14 = "SELECT ENT_VALUE " +
  		                        "FROM ISH_EXPRESSION, ISH_EXPRESSION_NOTE, ISH_SUBMISSION " +
  		                        "WHERE ENT_EXPRESSION_FK = EXP_OID " +
  		                        "AND EXP_SUBMISSION_FK = SUB_OID " +
  		                        "AND SUB_ACCESSION_ID = ? " +
                                "AND EXP_COMPONENT_ID = ? ";

  final static String name272 = "DENSITY_NOTE";
  final static String query272 = "SELECT DNN_VALUE " +
  		                        "FROM ISH_EXPRESSION, ISH_SUBMISSION, ISH_DENSITY_NOTE, ISH_DENSITY " +
  		                        "WHERE DNN_DENSITY_FK = DEN_OID " +
  		                        "AND DEN_EXPRESSION_FK = EXP_OID " +
  		                        "AND EXP_SUBMISSION_FK = SUB_OID " +
  		                        "AND SUB_ACCESSION_ID = ? " +
                                "AND EXP_COMPONENT_ID = ? ";

  final static String name273 = "DENSITY_DETAIL";
  final static String query273 = "SELECT DEN_RELATIVE_TO_TOTAL, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE " +
  		                        "FROM ISH_EXPRESSION, ISH_SUBMISSION, ISH_DENSITY " +
  		                        "WHERE DEN_EXPRESSION_FK = EXP_OID " +
  		                        "AND EXP_SUBMISSION_FK = SUB_OID " +
  		                        "AND SUB_ACCESSION_ID = ? " +
                                "AND EXP_COMPONENT_ID = ? ";
  
  final static String name117 = "EXPRESSION_PATTERNS";
  final static String query117 = "SELECT DISTINCT PTN_OID, PTN_PATTERN FROM ISH_PATTERN WHERE PTN_EXPRESSION_FK = ?";
                                 
  final static String name118 = "EXP_PATTERN_LOCATIONS";
  final static String query118 = "SELECT DISTINCT LCN_LOCATION, LCN_PATTERN_FK, LCN_OID FROM ISH_LOCATION WHERE LCN_PATTERN_FK = ?";
  
  final static String name172 = "COMPONENT_NAME_FROM_ATN_PUBLIC_ID";
  final static String query172 = "SELECT ANO_COMPONENT_NAME FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ATN_PUBLIC_ID = ?";
  
  // for a specified submission,
  // find parent nodes for a specified node in the specified stage whose strength are 'not detected')
  final static String name15 = "PARENT_NODES";
  final static String query15 ="SELECT PARENTS.ATN_PUBLIC_ID " +
  		                       "FROM ANA_TIMED_NODE KID, ANA_TIMED_NODE PARENTS, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE " +
  		                       "WHERE KID.ATN_PUBLIC_ID = ? " + // 1
  		                       "AND PARENTS.ATN_STAGE_FK = STG_OID " +
  		                       "AND KID.ATN_STAGE_FK = STG_OID " +
  		                       "AND STG_NAME = ? " +            // 2
  		                       "AND KID.ATN_NODE_FK = RTR_DESCENDENT_FK " +
  		                       "AND PARENTS.ATN_NODE_FK = RTR_ANCESTOR_FK " +
  		                       "AND KID.ATN_NODE_FK != RTR_ANCESTOR_FK " +
  		                       "AND PARENTS.ATN_PUBLIC_ID IN " +
  		                       "(SELECT EXP_COMPONENT_ID " +
  		                       " FROM ISH_EXPRESSION, ISH_SUBMISSION " +
  		                       " WHERE SUB_ACCESSION_ID = ? " + // 3
  		                       " AND EXP_SUBMISSION_FK = SUB_OID " +
  		                       " AND EXP_STRENGTH = 'not detected')";

  // query 32 (for a specified submission,
  //           find kid nodes for a specified node in the specified stage whose strength are 'present')
  final static String name16 = "CHILD_NODES";
  final static String query16 = "SELECT KIDS.ATN_PUBLIC_ID " +
  		                        "FROM ANA_TIMED_NODE KIDS, ANA_TIMED_NODE PARENT, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE " +
  		                        "WHERE PARENT.ATN_PUBLIC_ID = ? " + // 1
  		                        "AND PARENT.ATN_STAGE_FK = STG_OID " +
  		                        "AND KIDS.ATN_STAGE_FK = STG_OID " +
  		                        "AND STG_NAME = ? " +               // 2
  		                        "AND KIDS.ATN_NODE_FK = RTR_DESCENDENT_FK " +
  		                        "AND PARENT.ATN_NODE_FK = RTR_ANCESTOR_FK " +
  		                        "AND PARENT.ATN_NODE_FK != RTR_DESCENDENT_FK " +
  		                        "AND KIDS.ATN_PUBLIC_ID IN " +
  		                        "(SELECT EXP_COMPONENT_ID " +
   		                       " FROM ISH_EXPRESSION, ISH_SUBMISSION " +
  		                       " WHERE SUB_ACCESSION_ID = ? " +     // 3
  		                       " AND EXP_SUBMISSION_FK = SUB_OID " +
  		                        " AND EXP_STRENGTH = 'present')";

  // query 33 (find the stage of specified submission)
  final static String name17 = "SUBMISSION_STAGE";
  final static String query17 = "SELECT SUB_OID, STG_STAGE_DISPLAY FROM ISH_SUBMISSION LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID WHERE SUB_ACCESSION_ID = ?";

  // query 34 (find the current max oid for expression)
  final static String name18 = "MAX_EXPRESSION_ID";
  final static String query18 = "SELECT SEQ_EXPRESSION FROM REF_SEQUENCES";

  // query 35 (find component detail)
  final static String name19 = "COMPONENT_DETAIL";
  final static String query19 = "SELECT ATN_PUBLIC_ID, ANO_COMPONENT_NAME, APO_FULL_PATH " +
  		                        "FROM ANA_NODE, ANAD_PART_OF, ANA_TIMED_NODE " +
  		                        "WHERE ATN_PUBLIC_ID = ? " + // 1
  		                        "AND ANO_OID = ATN_NODE_FK " +
  		                        "AND APO_NODE_FK = ANO_OID " +
  		                        "AND APO_IS_PRIMARY_PATH = 1";

  //query7 (find publications linked to a submission)
  final static String name20 = "LINKED_PUBLICATIONS";
  final static String query20 = "SELECT LPU_AUTHORS,LPU_YEAR,LPU_TITLE,LPU_JOURNAL,LPU_VOLUME,LPU_ISSUE,LPU_PAGES,LPU_DB,LPU_ACCESSION FROM ISH_LINKED_PUBLICATION, ISH_SUB_PUB, ISH_SUBMISSION WHERE SUB_OID = SLN_SUBMISSION_FK AND LPU_OID = SLN_LINKEDPUB_FK AND SUB_ACCESSION_ID = ? AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";

  //query8 and query30 (find acknowledgements linked to a submission)
  final static String name21 = "SUB_ACKNOWLEDGEMENTS";
  final static String query21 = "SELECT AKN_REASON FROM ISH_ACKNOWLEDGEMENT, ISH_SUBMISSION WHERE SUB_ACCESSION_ID = ?  AND AKN_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";

  final static String name23 = "MUS_SUB_LINKED_SUBMISSIONS";
  final static String query23 = "SELECT LSU_RESOURCE, SUB_ACCESSION_ID AS ACCESSION, LST_TYPE, LSU_URL " + 
                                "FROM ISH_LINKED_SUBMISSION, XPR_LINKED_TYPE, ISH_SUBMISSION " + 
                                "WHERE LSU_ACCESSION = ? " + 
                                "AND LSU_SUBMISSION_FK = SUB_OID " + 
                                "AND LSU_OID = LST_LSU_FK " + 
                                "UNION " + 
                                "SELECT LSU_RESOURCE, LSU_ACCESSION AS ACCESSION, LST_TYPE, LSU_URL " + 
                                "FROM ISH_LINKED_SUBMISSION, ISH_SUBMISSION, XPR_LINKED_TYPE " + 
                                "WHERE LSU_SUBMISSION_FK = ? " + 
                                "AND SUB_OID = LSU_SUBMISSION_FK " + 
                                "AND LST_LSU_FK = LSU_OID " +
                                "ORDER BY natural_sort(ACCESSION), LST_TYPE ";
                                
  final static String name136 = "XLAEV_SUB_LINKED_SUBMISSIONS";
  final static String query136 = "SELECT LSU_RESOURCE, LSU_ACCESSION, LST_TYPE, LSU_URL " + 
                                 "FROM ISH_LINKED_SUBMISSION, ISH_SUBMISSION, XPR_LINKED_TYPE " + 
                                 "WHERE LSU_SUBMISSION_FK = ? " + 
                                 "AND SUB_OID = LSU_SUBMISSION_FK " + 
                                 "AND LST_LSU_FK = LSU_OID";

  
  /* ---strings and names for parameterised quey involving lab stats--- */

  // find numbers of submission for given lab, grouped by submission date, and assay type
  // SUB_IS_PUBLIC = 1 & SUB_DB_STATUS_FK = 4
  final static String name24 = "TOTOAL_SUBMISSION_OF_LAB_DB";
  final static String query24 = "SELECT SUB_SUB_DATE, COUNT(DISTINCT SUB_ACCESSION_ID), SUB_ASSAY_TYPE, SUB_ARCHIVE_ID " +
  		"FROM ISH_SUBMISSION " +
  		"WHERE SUB_PI_FK = ? " +
  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
  		"GROUP BY SUB_SUB_DATE DESC, SUB_ARCHIVE_ID DESC, SUB_ASSAY_TYPE";

  final static String name203 = "TOTAL_SUBMISSION_OF_LAB_DB_ANNOTATION";
  final static String query203 = "SELECT SUB_SUB_DATE, COUNT(DISTINCT SUB_ACCESSION_ID), SUB_ASSAY_TYPE, SUB_ARCHIVE_ID " +
  		                        "FROM ISH_SUBMISSION,REF_STATUS " +
  		                        "WHERE SUB_PI_FK = ? " +
  		                        //modified by ying
  		                        "AND SUB_DB_STATUS_FK = STA_OID AND STA_OID=?  AND SUB_IS_DELETED = 0 " +
  		                        "GROUP BY SUB_SUB_DATE DESC, SUB_ARCHIVE_ID DESC, SUB_ASSAY_TYPE";


  // find name and id of all PIs)
  final static String name25 = "ALL_PIS";
  final static String query25 = "SELECT DISTINCT PER_NAME, PER_OID FROM ISH_PERSON WHERE PER_TYPE_FK = 'PI' ORDER BY PER_NAME ";

  final static String name26 = "LAB_LATEST_ENTRY";
  final static String query26 = "SELECT MAX(SUB_ENTRY_DATE) FROM ISH_SUBMISSION WHERE SUB_PI_FK = ?";

  final static String name27 = "TOTOAL_SUBMISSION_OF_LAB_FTP";
  final static String query27 = "SELECT FTP_DATE_SENT, FTP_TOTAL_SUBMISSIONS, FTP_ASSAY_TYPE, FTP_ARCHIVE_ID " +
  		                        "FROM REF_FTP_SUBMISSION " +
  		                        "WHERE FTP_PI_FK = ? " +
  		                        "AND FTP_ISDELETED = 0 " +
  		                        "ORDER BY FTP_DATE_SENT DESC, FTP_ARCHIVE_ID DESC, FTP_ASSAY_TYPE";

  /* ---set of Strings and Names for each parameterised query involving a gene--- */

  //query11 (gene info on a gene)
  final static String name28 = "GENE_INFO";
  final static String query28 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_LOCUS_TAG, RPR_SYNONYMS, " +
  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " + 
  		                        "WHEN 'MGI:' THEN CONCAT(GENE_URL.URL_URL, RPR_LOCUS_TAG) " + 
  		                        "ELSE /* NON MGI */ '' " + 
  		                        "END, " +
  		                        "RPR_ENSEMBL, CONCAT(ENS_URL.URL_URL,RPR_ENSEMBL), " +
  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " +
		                        "WHEN 'MGI:' THEN CONCAT(GO_URL.URL_URL, RPR_LOCUS_TAG) " + 
		                        "ELSE /* non-MGI */ '' " + 
		                        "END, " +
  		                        "CONCAT(OMIM_URL.URL_URL,RPR_SYMBOL), " +
  		                        "CONCAT(ENTREZ_URL.URL_URL,RPR_SYMBOL), " +
  		                        "REG_CHROM_START, REG_CHROM_END, REG_CHROME_NAME, M.MIS_ENS_GENEBUILD, " +
                                "CONCAT(GENECARDS_URL.URL_URL,RPR_SYMBOL,GENECARDS_URL.URL_SUFFIX), " +
                                "CONCAT(HGNC_SYMBOL_SEARCH_URL.URL_URL,RPR_SYMBOL), " +
                                "CONCAT(UCSC_URL.URL_URL, REG_CHROME_NAME, ':', REG_CHROM_START, '-', REG_CHROM_END, UCSC_URL.URL_SUFFIX) " + // added by xingjun - 30/04/2009
  		                        "FROM REF_MISC M, ISH_PROBE " +
  		                        "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
  		                        "LEFT JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
  		                        "LEFT JOIN REF_ENS_GENE ON REG_STABLE = RPR_ENSEMBL " +
  		                        "LEFT JOIN REF_MGI_MRK ON RMM_MGIACC = RPR_LOCUS_TAG " +
  		                        "JOIN REF_URL GENE_URL " +
  		                        "JOIN REF_URL GO_URL " +
  		                        "JOIN REF_URL ENS_URL " +
  		                        "JOIN REF_URL OMIM_URL " +
  		                        "JOIN REF_URL ENTREZ_URL " +
                                "JOIN REF_URL GENECARDS_URL "+
                                "JOIN REF_URL HGNC_SYMBOL_SEARCH_URL "+
                                "JOIN REF_URL UCSC_URL " + // added by xingjun - 30/04/2009
  		                        "WHERE GENE_URL.URL_TYPE = 'jax_gene' " + 
  		                        "AND GO_URL.URL_TYPE = 'go_gene' " +
  		                        "AND ENS_URL.URL_TYPE = 'ens_gene' " +
  		                        "AND OMIM_URL.URL_TYPE = 'omim_gene' " +
  		                        "AND ENTREZ_URL.URL_TYPE = 'entrez_all' " +
                                "AND GENECARDS_URL.URL_TYPE = 'genecards_gene' " +
                                "AND HGNC_SYMBOL_SEARCH_URL.URL_TYPE = 'hgnc_symbol_search' " +
                                "AND UCSC_URL.URL_TYPE = 'ucsc_genome' " + // added by xingjun - 30/04/2009
  		                        "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
  		                        "AND RPR_SYMBOL = ?";

  final static String name278 = "GENE_INFO_BY_SYMBOLID";
  final static String query278 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_LOCUS_TAG, RPR_SYNONYMS, " +
  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " + 
  		                        "WHEN 'MGI:' THEN CONCAT(GENE_URL.URL_URL, RPR_LOCUS_TAG) " + 
  		                        "ELSE /* NON MGI */ '' " + 
  		                        "END, " +
  		                        "RPR_ENSEMBL, CONCAT(ENS_URL.URL_URL,RPR_ENSEMBL), " +
  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " +
		                        "WHEN 'MGI:' THEN CONCAT(GO_URL.URL_URL, RPR_LOCUS_TAG) " + 
		                        "ELSE /* non-MGI */ '' " + 
		                        "END, " +
  		                        "CONCAT(OMIM_URL.URL_URL,RPR_SYMBOL), " +
  		                        "CONCAT(ENTREZ_URL.URL_URL,RPR_SYMBOL), " +
  		                        "REG_CHROM_START, REG_CHROM_END, REG_CHROME_NAME, M.MIS_ENS_GENEBUILD, " +
                                "CONCAT(GENECARDS_URL.URL_URL,RPR_SYMBOL,GENECARDS_URL.URL_SUFFIX), " +
                                "CONCAT(HGNC_SYMBOL_SEARCH_URL.URL_URL,RPR_SYMBOL), " +
                                "CONCAT(UCSC_URL.URL_URL, REG_CHROME_NAME, ':', REG_CHROM_START, '-', REG_CHROM_END, UCSC_URL.URL_SUFFIX), " + // added by xingjun - 30/04/2009
  		                        "RMM_SPECIES " +  
  		                        "FROM REF_MISC M, ISH_PROBE " +  
  		                        "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
  		                        "LEFT JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
  		                        "LEFT JOIN REF_ENS_GENE ON REG_STABLE = RPR_ENSEMBL " +
  		                        "LEFT JOIN REF_MGI_MRK ON RMM_MGIACC = RPR_LOCUS_TAG " +
  		                        "JOIN REF_URL GENE_URL " +
  		                        "JOIN REF_URL GO_URL " +
  		                        "JOIN REF_URL ENS_URL " +
  		                        "JOIN REF_URL OMIM_URL " +
  		                        "JOIN REF_URL ENTREZ_URL " +
                                "JOIN REF_URL GENECARDS_URL "+
                                "JOIN REF_URL HGNC_SYMBOL_SEARCH_URL "+
                                "JOIN REF_URL UCSC_URL " + // added by xingjun - 30/04/2009
  		                        "WHERE GENE_URL.URL_TYPE = 'jax_gene' " + 
  		                        "AND GO_URL.URL_TYPE = 'go_gene' " +
  		                        "AND ENS_URL.URL_TYPE = 'ens_gene' " +
  		                        "AND OMIM_URL.URL_TYPE = 'omim_gene' " +
  		                        "AND ENTREZ_URL.URL_TYPE = 'entrez_all' " +
                                "AND GENECARDS_URL.URL_TYPE = 'genecards_gene' " +
                                "AND HGNC_SYMBOL_SEARCH_URL.URL_TYPE = 'hgnc_symbol_search' " +
                                "AND UCSC_URL.URL_TYPE = 'ucsc_genome' " + // added by xingjun - 30/04/2009
  		                        "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
  		                        "AND RPR_LOCUS_TAG = ?";
  
  final static String name242 = "GENE_INFO_IUPHAR";
  final static String query242 = "SELECT DISTINCT CONCAT(IUPHAR_URL.URL_URL, IUP_IUPHAR_ID), "+
  		                        "CONCAT(IUPHAR_2_URL.URL_URL, IUP_IUPHAR_ID), IUP_URL_TYPE " +
  		                        "FROM REF_IUPHAR "+
  		                        "JOIN REF_URL IUPHAR_URL " +
  		                        "JOIN REF_URL IUPHAR_2_URL " +
  		                        "WHERE IUPHAR_URL.URL_TYPE = 'iuphar_gene' " + 
  		                        "AND IUPHAR_2_URL.URL_TYPE = 'iuphar_gene2' " +
  		                        "AND (IUP_SYMBOL = ? OR IUP_MGIACC = ?) ";

  //query to find ish submissions linked to a specific gene symbol
  final static String name30 = "GENE_RELATED_SUBMISSIONS_INSITU";
  final static String query30 = "SELECT DISTINCT SUB_ACCESSION_ID, 'ish_submission.html', STG_STAGE_DISPLAY, SPN_ASSAY_TYPE,  " + 
                                "CASE WHEN (EXP_SUBMISSION_FK > 0) THEN 'with annotation' " + 
                                "ELSE 'without annotation' " + 
                                "END, " +
                                "CASE WHEN (SPN_SEX = 'unknown') THEN 'unknown sex' " + 
                                "ELSE SPN_SEX " + 
                                "END, " +
                                "RPR_JAX_ACC, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '), " + 
                                "CASE WHEN (LOCATE(';',GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ')) > 0) THEN " + 
                                "CONCAT(SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '),'; ',1),'...') " +
                                "ELSE GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') " +
                                "END, " + 
                                "CASE WHEN (CONCAT(RPR_PREFIX,RPR_OID) =  RPR_JAX_ACC) THEN '' ELSE CONCAT(RPR_PREFIX,RPR_OID) END, " +
                          		"CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN " +
                           		"CONCAT('http://www.informatics.jax.org/accession/', RPR_JAX_ACC) " +
                           		"ELSE 'probe.html' END, " +
                          		"GROUP_CONCAT(DISTINCT ALE_ALLELE_NAME ORDER BY SAL_ORDER)  " +
                                "FROM ISH_SUBMISSION " + 
                                "JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " + 
                                "JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " + 
                                "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " + 
                                "LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
                                "LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " + 
                                "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
                                "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
                                "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
                                "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC " +
                                "LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +                                
                                "WHERE RPR_SYMBOL = ? " + 
                                "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
                                "AND (SUB_ASSAY_TYPE = 'ISH' ||SUB_ASSAY_TYPE = 'IHC'||SUB_ASSAY_TYPE = 'TG') " +
//                                "AND SUB_ASSAY_TYPE = ? " + // mantis 1026
                                "GROUP BY SUB_OID " +
                                "ORDER BY STG_STAGE_DISPLAY, natural_sort(SUB_ACCESSION_ID)";

  //query to find ish submissions linked to a specific gene id
  final static String name275 = "GENEID_RELATED_SUBMISSIONS_ISH";
  final static String query275 = "SELECT DISTINCT SUB_ACCESSION_ID, 'ish_submission.html', STG_STAGE_DISPLAY, SPN_ASSAY_TYPE,  " + 
                                "CASE WHEN (EXP_SUBMISSION_FK > 0) THEN 'with annotation' " + 
                                "ELSE 'without annotation' " + 
                                "END, " +
                                "CASE WHEN (SPN_SEX = 'unknown') THEN 'unknown sex' " + 
                                "ELSE SPN_SEX " + 
                                "END, " +
                                "RPR_JAX_ACC, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '), " + 
                                "CASE WHEN (LOCATE(';',GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ')) > 0) THEN " + 
                                "CONCAT(SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '),'; ',1),'...') " +
                                "ELSE GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') " +
                                "END, " + 
                                "CASE WHEN (CONCAT(RPR_PREFIX,RPR_OID) =  RPR_JAX_ACC) THEN '' ELSE CONCAT(RPR_PREFIX,RPR_OID) END, " +
                          		"CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN " +
                           		"CONCAT('http://www.informatics.jax.org/accession/', RPR_JAX_ACC) " +
                           		"ELSE 'probe.html' END, " +
                          		"GROUP_CONCAT(DISTINCT ALE_ALLELE_NAME ORDER BY SAL_ORDER)  " +
                                "FROM ISH_SUBMISSION " + 
                                "JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " + 
                                "JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " + 
                                "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " + 
                                "LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
                                "LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " + 
                                "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
                                "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
                                "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
                                "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC " +
                                "LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +                                
                                "WHERE RPR_LOCUS_TAG = ? " + 
                                "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
                                "AND SUB_ASSAY_TYPE = ? " +
                                "GROUP BY SUB_OID " +
                                "ORDER BY STG_STAGE_DISPLAY, natural_sort(SUB_ACCESSION_ID)";

  
  //query to find maprobs associated with a particular gene entry
  final static String name31 = "GENE_RELATED_MAPROBE";
  final static String query31 = "SELECT DISTINCT '', RPR_JAX_ACC, CONCAT(RPR_PREFIX,RPR_OID), " + 
  		"CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN " +
   		"CONCAT('http://www.informatics.jax.org/accession/', RPR_JAX_ACC) " +
   		"ELSE 'probe.html' END " +
  		"FROM REF_PROBE " +
  		"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
  		"JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
  		"LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC " +  		
  		"WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
  		"AND RPR_LOCUS_TAG = ? ";
//    	"AND RPR_SYMBOL = ? ";  
    	
  final static String name173 = "TOTAL_GENE_RELATED_ARRAYS";
  final static String query173 = "SELECT COUNT(*) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";
  
  final static String name279 = "TOTAL_GENEID_RELATED_ARRAYS";
  final static String query279 = "SELECT COUNT(*) FROM MIC_BROWSE_CACHE WHERE MBC_MAN_MGI_ID = ?";

  
    final static String name174 = "TOTAL_GENE_PROBEIDS";
    final static String query174 = "SELECT COUNT(DISTINCT MBC_GLI_PROBE_SET_NAME) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";

    final static String name175 = "TOTAL_GENE_SIGNAL";
    final static String query175 = "SELECT COUNT(DISTINCT MBC_GLI_SIGNAL) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";
    
    final static String name176 = "TOTAL_GENE_DETECTION";
    final static String query176 = "SELECT COUNT(DISTINCT MBC_GLI_DETECTION) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";
    
    final static String name177 = "TOTAL_GENE_PVALUE";
    final static String query177 = "SELECT COUNT(DISTINCT MBC_GLI_P_VALUE) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";

    //query to find microarray submissions linked to a specific gene symbol
  final static String name33 = "GENE_RELATED_SUBMISSIONS_ARRAY";
  final static String query33 = "SELECT DISTINCT SUB_ACCESSION_ID, MBC_GLI_PROBE_SET_NAME, MBC_GLI_SIGNAL, MBC_GLI_DETECTION, MBC_GLI_P_VALUE " +
                                "FROM MIC_BROWSE_CACHE, ISH_SUBMISSION " +
                                "WHERE SUB_ACCESSION_ID = MBC_SUB_ACCESSION_ID AND MBC_GNF_SYMBOL = ? ";

  //query to find mgi info linked to a specific probe set
  final static String name35 = "GENE_INFO_FOR_ARRAY";
  final static String query35 = "SELECT RMM_SYMBOL, RMM_ID, RMM_MGIACC, CONCAT(GENE_URL.URL_URL, RMM_MGIACC), REG_STABLE, " + 
                                "CONCAT(ENS_URL.URL_URL, REG_STABLE), " + 
                                "CONCAT(GO_URL.URL_URL, RMM_MGIACC), " + 
                                "CONCAT(OMIM_URL.URL_URL, RMM_SYMBOL), " + 
                                "CONCAT(ENTREZ_URL.URL_URL, RMM_SYMBOL), " + 
                                "REG_CHROM_START, REG_CHROM_END, REG_CHROME_NAME, M.MIS_ENS_GENEBUILD, " + 
                                "CONCAT(GENECARDS_URL.URL_URL,RMM_SYMBOL,GENECARDS_URL.URL_SUFFIX), " + 
                                "CONCAT(HGNC_SYMBOL_SEARCH_URL.URL_URL,RMM_SYMBOL)  FROM REF_MISC M, REF_MGI_MRK " + 
                                "LEFT JOIN REF_ENS_GENE " + 
                                " ON RMM_MGIACC = REG_PRIMARY_ACC " + 
                                "JOIN REF_URL GENE_URL  JOIN REF_URL ENS_URL  JOIN REF_URL GO_URL  JOIN REF_URL OMIM_URL  JOIN REF_URL ENTREZ_URL  JOIN REF_URL GENECARDS_URL " + 
                                "JOIN REF_URL HGNC_SYMBOL_SEARCH_URL " + 
                                "WHERE GENE_URL.URL_TYPE = 'jax_gene' " + 
                                "AND ENS_URL.URL_TYPE = 'ens_gene'  " +
                                "AND GO_URL.URL_TYPE = 'go_gene'  " +
                                "AND OMIM_URL.URL_TYPE = 'omim_gene'  " +
                                "AND ENTREZ_URL.URL_TYPE = 'entrez_all'  " +
                                "AND GENECARDS_URL.URL_TYPE = 'genecards_gene'  " +
                                "AND HGNC_SYMBOL_SEARCH_URL.URL_TYPE = 'hgnc_symbol_search' " + 
                                "AND RMM_SYMBOL = ?";

  //query to find entrez info linked to a specific probe set
  final static String name36 = "GENE_SYNONYM_INFO_ARRAY";
  final static String query36 = "SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE RSY_REF = ?";
		                        
  //find details of a specific maprobe
  final static String name204 = "MAPROBE_DETAILS";
  final static String query204 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, RPR_LOCUS_TAG, "+  
                                 "PRB_SOURCE, PRB_STRAIN, PRB_TISSUE, PRB_PROBE_TYPE, "+
                                 "PRB_GENE_TYPE, PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, "+  
                                 "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), "+
                                 "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END), "+
                                 "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), "+  
                                 "RPR_TYPE, RPR_5_LOC, RPR_3_LOC, RPR_5_PRIMER, RPR_3_PRIMER, '', " +
                                 "RPR_CLONE_NAME_2, PRB_LAB_ID "+ 
                                 "FROM REF_PROBE "+
                                 "JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID "+
                                 "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK "+
                                 "JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = "+
                                 "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
                                 "WHEN 'MGI:'     THEN  'jax_gene' " + 
                                 "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
                                 "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
                                 "END " +
                                 "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_TYPE = 'genbank_sequence' "+
                                 "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC "+
                                 "WHERE RPR_JAX_ACC = ? "+
                                 "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";

  final static String name239 = "MAPROBE_DETAILS_EXTRA";
  final static String query239 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, RPR_LOCUS_TAG, "+  
                                 "PRB_SOURCE, PRB_STRAIN, PRB_TISSUE, PRB_PROBE_TYPE, "+
                                 "PRB_GENE_TYPE, PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, "+  
                                 "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), "+
                                 "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END), "+
                                 "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), "+  
                                 "RPR_TYPE, RPR_5_LOC, RPR_3_LOC, RPR_5_PRIMER, RPR_3_PRIMER, '', " +
                                 "RPR_CLONE_NAME_2, PRB_LAB_ID "+ 
                                 "FROM REF_PROBE "+
                                 "JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID "+
                                 "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK "+
                                 "JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = "+
                                 "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
                                 "WHEN 'MGI:'     THEN  'jax_gene' " + 
                                 "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
                                 "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
                                 "END " +
                                 "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_TYPE = 'genbank_sequence' "+
                                 "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC "+
                                 "WHERE RPR_JAX_ACC = ? "+
                                 "AND  PRB_MAPROBE = ? "+
                                 "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
 
  
  private static String endsBrowseSubmissionISH = ISH_BROWSE_ALL_TABLES + PUBLIC_ENTRIES_Q;
  
  /* queries to find unique totals for each of the 'collection' columns */
  private static String endsCollectionSubmissionISH = " FROM ISH_SUBMISSION " +
                        " JOIN ISH_PROBE  ON SUB_OID = PRB_SUBMISSION_FK " +
                        " JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
                        " JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK" +
                        " JOIN REF_URL U ON U.URL_OID = 1 LEFT " +
                        " JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
                        " LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
                        " AND IMG_ORDER = " +
                        " (SELECT MIN(I.IMG_ORDER) " +
                        " FROM ISH_ORIGINAL_IMAGE I " +
                        " WHERE I.IMG_SUBMISSION_FK = SUB_OID AND I.IMG_TYPE NOT LIKE '%wlz%') " +
                        " JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = IMG_URL_FK ";

  static String expPresentQuerySection = "";
  
  // ISH totals
  final static String NUMBER_OF_SUBMISSION = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) ";
  
  final static String NUMBER_OF_GENE_SYMBOL = "SELECT COUNT(DISTINCT RPR_SYMBOL) ";
  
  final static String NUMBER_OF_THEILER_STAGE = "SELECT COUNT(DISTINCT STG_STAGE_DISPLAY) ";
  
  final static String NUMBER_OF_GIVEN_STAGE = "SELECT COUNT(DISTINCT TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)) ";
  
  final static String NUMBER_OF_LAB = "SELECT COUNT(DISTINCT PER_SURNAME) ";
  
  final static String NUMBER_OF_SUBMISSION_DATE = "SELECT COUNT(DISTINCT SUB_SUB_DATE) ";
  
  final static String NUMBER_OF_ASSAY_TYPE = "SELECT COUNT(DISTINCT SUB_ASSAY_TYPE) ";
  
  final static String NUMBER_OF_SPECIMEN_TYPE = "SELECT COUNT(DISTINCT SPN_ASSAY_TYPE) ";
  
  //final static String NUMBER_OF_IMAGE = "SELECT COUNT(DISTINCT CONCAT(IMG_FILEPATH, IMG_SML_FILENAME)) ";
  //final static String NUMBER_OF_IMAGE = "SELECT COUNT(IMG_OID) FROM ISH_SUBMISSION JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK ";
  final static String NUMBER_OF_IMAGE = "SELECT COUNT(IMG_OID) FROM ISH_SUBMISSION JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID ";

  final static String name38 = "TOTAL_NUMBER_OF_SUBMISSION";
  final static String query38 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionISH;
  
  final static String name39 = "TOTAL_NUMBER_OF_GENE_SYMBOL";
  final static String query39 = NUMBER_OF_GENE_SYMBOL + endsBrowseSubmissionISH;
  
  final static String name40 = "TOTAL_NUMBER_OF_THEILER_STAGE";
  final static String query40 = NUMBER_OF_THEILER_STAGE + endsBrowseSubmissionISH;;
  
  final static String name41 = "TOTAL_NUMBER_OF_GIVEN_STAGE";
  final static String query41 = NUMBER_OF_GIVEN_STAGE + endsBrowseSubmissionISH;;
  
  final static String name42 = "TOTAL_NUMBER_OF_LAB";
  final static String query42 = NUMBER_OF_LAB + endsBrowseSubmissionISH;;
  
  final static String name43 = "TOTAL_NUMBER_OF_SUBMISSION_DATE";
  final static String query43 = NUMBER_OF_SUBMISSION_DATE + endsBrowseSubmissionISH;
  
  final static String name44 = "TOTAL_NUMBER_OF_ASSAY_TYPE";
  final static String query44 = NUMBER_OF_ASSAY_TYPE + endsBrowseSubmissionISH;
  
  final static String name45 = "TOTAL_NUMBER_OF_SPECIMEN_TYPE";
  final static String query45 = NUMBER_OF_SPECIMEN_TYPE + endsBrowseSubmissionISH;
  
  final static String name46 = "TOTAL_NUMBER_OF_IMAGE";
  final static String query46 = NUMBER_OF_IMAGE + PUBLIC_ENTRIES_Q + " AND IMG_TYPE NOT LIKE '%wlz%'" ;
  /*final static String query46 = NUMBER_OF_IMAGE + endsBrowseSubmissionISH;*/
//  final static String query46 = "SELECT COUNT(IMG_OID) FROM ISH_ORIGINAL_IMAGE, ISH_SUBMISSION, ISH_SPECIMEN "+
//		  						"WHERE IMG_SUBMISSION_FK=SUB_OID AND SPN_SUBMISSION_FK=SUB_OID " +
//		  						"AND SUB_IS_DELETED=0 AND SUB_DB_STATUS_FK=4 AND SUB_IS_PUBLIC=1";
  
  final static String NUMBER_OF_SEX = "SELECT COUNT(DISTINCT SPN_SEX) ";
  
  final static String NUMBER_OF_CONFIDENCE_LEVEL = "SELECT COUNT(DISTINCT ) "; // NOT FINISHED YET
  
  final static String NUMBER_OF_PROBE_NAME = "SELECT COUNT(DISTINCT RPR_JAX_ACC) ";
  
  final static String NUMBER_OF_ANTIBODY_NAME = "SELECT COUNT(DISTINCT ) "; // NOT FINISHED YET
  
  final static String NUMBER_OF_ANTIBODY_GENE_SYMBOL = "SELECT COUNT(DISTINCT ) "; // NOT FINISHED YET
  
  final static String NUMBER_OF_GENOTYPE = "SELECT COUNT(DISTINCT SPN_WILDTYPE) ";
  
  final static String NUMBER_OF_PROBE_TYPE = "SELECT COUNT(DISTINCT PRB_PROBE_TYPE) ";

  final static String NUMBER_OF_ISH_EXPRESSION = "SELECT COUNT(DISTINCT EXP_STRENGTH) "; 

  final static String NUMBER_OF_TISSUES = "SELECT COUNT(DISTINCT ANO_COMPONENT_NAME) "; 

  
  final static String name129 = "TOTAL_NUMBER_OF_SEX";
  final static String query129 = NUMBER_OF_SEX + endsBrowseSubmissionISH;

  final static String name130 = "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL"; // NOT FINISHED YET
  final static String query130 = NUMBER_OF_CONFIDENCE_LEVEL + endsBrowseSubmissionISH;

  final static String name131 = "TOTAL_NUMBER_OF_PROBE_NAME";
  final static String query131 = NUMBER_OF_PROBE_NAME + endsBrowseSubmissionISH;

  final static String name132 = "TOTAL_NUMBER_OF_ANTIBODY_NAME"; // NOT FINISHED YET
  final static String query132 = NUMBER_OF_ANTIBODY_NAME + endsBrowseSubmissionISH;

  final static String name133 = "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL"; // NOT FINISHED YET
  final static String query133 = NUMBER_OF_ANTIBODY_GENE_SYMBOL + endsBrowseSubmissionISH;

  final static String name134 = "TOTAL_NUMBER_OF_GENOTYPE";
  final static String query134 = NUMBER_OF_GENOTYPE + endsBrowseSubmissionISH;

  final static String name135 = "TOTAL_NUMBER_OF_PROBE_TYPE";
  final static String query135 = NUMBER_OF_PROBE_TYPE + endsBrowseSubmissionISH;
  
  final static String name268 = "TOTAL_NUMBER_OF_TISSUES";
  final static String query268 = NUMBER_OF_TISSUES + endsBrowseSubmissionISH;

  final static String name246 = "TOTAL_NUMBER_OF_ISH_EXPRESSION";
  final static String query246 = NUMBER_OF_ISH_EXPRESSION + endsBrowseSubmissionISH;

  // query to build anatomy tree for displaying annotation
  final static String name47 = "ANNOT_TREE_CONTENT";
  final static String query47 = "SELECT APO_DEPTH,APO_SEQUENCE, PATN.ATN_PUBLIC_ID, PARENT.ANO_COMPONENT_NAME, CONCAT('(',strt.STG_NAME,'-',end.sTG_NAME,')') AS 'RANGE', " +
                                 "(select count(*) " +
                                 "from ANA_RELATIONSHIP, ANA_NODE CHILD, ANA_TIMED_NODE CATN, ANA_STAGE CSTG " +
                                 "where REL_PARENT_FK = PARENT.ANO_OID " +
                                 "and REL_CHILD_FK  = CHILD.ANO_OID " +
                                 "and CATN.ATN_NODE_FK = CHILD.ANO_OID " +
                                 "and CSTG.STG_OID = CATN.ATN_STAGE_FK " +
                                 "and CSTG.STG_SEQUENCE = stg.STG_SEQUENCE) as kids," +
                                 "case when !APO_IS_PRIMARY_PATH OR ANO_IS_GROUP THEN 1 ELSE 0 END AS IP, " +
                                 "exp.EXP_STRENGTH AS EXP, exp.EXP_ADDITIONAL_STRENGTH, exp.EXP_OID, CASE WHEN ENT_VALUE IS NULL THEN 0 ELSE 1 END AS E_NOTE, " +
                                 "DEN_RELATIVE_TO_AGE, DEN_RELATIVE_TO_TOTAL, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE, "+  
                                 "DNN_VALUE , CASE WHEN DNN_VALUE IS NULL THEN 0 ELSE 1 END AS D_NOTE " +
                               "FROM ANA_NODE PARENT " +
                               "JOIN ANAD_PART_OF " +
                                 "ON APO_NODE_FK = PARENT.ANO_OID AND APO_FULL_PATH NOT LIKE '%mouse.embryo%' " +
                               "JOIN ANAD_PART_OF_PERSPECTIVE " +
                               "ON POP_PERSPECTIVE_FK = '" + bundle.getString("perspective") + "' "+
                                 " AND POP_APO_FK = APO_OID " +
                               "JOIN ANA_TIMED_NODE PATN  " +
                                 "ON PARENT.ANO_OID = PATN.ATN_NODE_FK " +
                               "JOIN ANA_STAGE stg " +
                                 "ON stg.STG_OID = PATN.ATN_STAGE_FK " +
                                 "AND stg.STG_NAME = ? " +
                               "JOIN ANA_STAGE strt " +
                                 "ON APO_PATH_START_STAGE_FK = strt.STG_OID " +
                               "JOIN ANA_STAGE end " +
                                 "ON APO_PATH_END_STAGE_FK = end.STG_OID AND  stg.STG_SEQUENCE BETWEEN strt.STG_SEQUENCE AND end.STG_SEQUENCE " +
                               "LEFT JOIN ISH_EXPRESSION exp " +
                                 "ON exp.EXP_SUBMISSION_FK = (select SUB_OID from ISH_SUBMISSION where SUB_ACCESSION_ID = ? ) " +
                                 "AND exp.EXP_COMPONENT_ID = ATN_PUBLIC_ID " +
                               "LEFT JOIN ISH_EXPRESSION_NOTE " +
                                 "ON exp.EXP_OID = ENT_EXPRESSION_FK " +
                                 "LEFT JOIN ISH_DENSITY ON DEN_EXPRESSION_FK = EXP_OID "+
                                 "LEFT JOIN ISH_DENSITY_NOTE ON DNN_DENSITY_FK = DEN_OID "+
                               "GROUP BY PARENT.ANO_COMPONENT_NAME,'RANGE',PATN.ATN_PUBLIC_ID, APO_SEQUENCE, APO_DEPTH,strt.STG_NAME,end.STG_NAME " +
                               "ORDER BY APO_SEQUENCE";
                               
  final static String name127 = "ANNOT_LIST";
  final static String query127 = "SELECT DISTINCT EXP_OID, ATN_PUBLIC_ID, ANO_COMPONENT_NAME, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, CASE WHEN ENT_VALUE IS NULL THEN 0 ELSE 1 END AS E_NOTE, "+
		  						 "DEN_RELATIVE_TO_AGE, DEN_RELATIVE_TO_TOTAL, DEN_COMPONENT_ID, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE "+
                                 "FROM ANA_NODE "+
                                 "JOIN ANA_TIMED_NODE "+
                                 "ON ANO_OID = ATN_NODE_FK "+
                                 "JOIN ISH_EXPRESSION "+
                                 "ON EXP_COMPONENT_ID = ATN_PUBLIC_ID "+
                                 "JOIN ISH_SUBMISSION "+
                                 "ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ? " +
                                 "LEFT JOIN ISH_EXPRESSION_NOTE " +
                                 "ON ENT_EXPRESSION_FK = EXP_OID "+
                                 "LEFT JOIN ISH_DENSITY ON DEN_EXPRESSION_FK = EXP_OID "+
                                 "ORDER BY ANO_COMPONENT_NAME";
  
  /** microarray */
  /* --- common part of some queries ---*/
  public static String endsBrowseSubmissionArray = "FROM ISH_SUBMISSION,MIC_SAMPLE,ISH_SPECIMEN," +
                                   "MIC_SERIES_SAMPLE,MIC_SERIES,ISH_PERSON,ISH_EXPRESSION, ANA_TIMED_NODE, ANA_NODE ANA_NODE " +
                                   "WHERE SMP_SUBMISSION_FK = SUB_OID " +
                                   "AND SRM_SAMPLE_FK = SMP_OID "+
                                   "AND EXP_SUBMISSION_FK=SUB_OID "+
                                   "AND SRM_SERIES_FK = SER_OID "+
                                   "AND PER_OID = SUB_PI_FK "+
                                   "AND SUB_OID = SPN_SUBMISSION_FK "+
                                   "AND SUB_ASSAY_TYPE = 'Microarray' "+
                                   "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 "+
                                   "AND ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
                                   "AND ATN_NODE_FK = ANO_OID ";

  /* ---query to find summary info on every array entry in the db--- */
  final static String name50 = "ALL_ENTRIES_ARRAY";
  final static String query50 = "SELECT DISTINCT SUB_ACCESSION_ID,SMP_GEO_ID,SMP_THEILER_STAGE," +stageFormatConcat+", " +
  "IF ((SELECT COUNT(*) FROM REF_SUBMISSION_PERSON_GRP WHERE SPG_SUBMISSION_FK = SUB_OID) > 0, (SELECT GRP_DESCRIPTION FROM REF_GROUP JOIN REF_SUBMISSION_PERSON_GRP ON SPG_GROUP_FK = GRP_OID WHERE SPG_SUBMISSION_FK = SUB_OID), SUB_SOURCE) SUB_SOURCE, " +
  "SUB_SUB_DATE,SMP_SEX,SRM_SAMPLE_DESCRIPTION,SMP_TITLE, " +
  "SER_GEO_ID, GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')')SEPARATOR ', '), SUB_ASSAY_TYPE , SPN_ASSAY_TYPE,  PER_OID " + endsBrowseSubmissionArray;
    
  /* queries to find unique totals for each of the 'browse all' columns */
  // need to modify these sql
  final static String name51 = "TOTAL_NUMBER_OF_SUBMISSION_ARRAY";
  final static String query51 = NUMBER_OF_SUBMISSION + endsBrowseSubmissionArray;
  
  final static String name61 = "SUBMISSION_SUPPLIMENTARY_FILES_ARRAY";
  final static String query61 = "SELECT DISTINCT SMP_FILE_LOCATION,SMP_CEL_FILENAME, SMP_CHP_FILENAME, SMP_RPT_FILENAME, SMP_EXP_FILENAME, SMP_TXT_FILENAME " +
                                "FROM MIC_SAMPLE " +
                                "JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND SMP_SUBMISSION_FK = SUB_OID";

  final static String name62 = "SUBMISSION_SAMPLE";
  final static String query62 = "SELECT SMP_GEO_ID, SMP_TITLE, SMP_SOURCE, SMP_ORGANISM, SMP_STRAIN, SMP_MUTATION, " +
  		"SMP_SEX, SMP_DEVELOPMENT_STAGE, SMP_THEILER_STAGE, SMP_DISSECT_METHOD, SMP_MOLECULE, SMP_A260_280, " +
  		"SMP_RNA_EXTRACT_PROTOCOL, SMP_TGT_AMP_MANUFACTURER, SMP_TGT_AMP_PROTOCOL, SMP_ROUNDS_OF_AMP, SMP_AMT_LAB_TGT_HYB_TO_ARY, " +
  		"SMP_LABEL, SMP_ARY_HYB_PROTOCOL, SMP_GCOS_TGT_VAL, SMP_DATA_ANA_METHOD, SMP_REFERENCE_USED, SRM_SAMPLE_DESCRIPTION, SMP_EXPERIMENTAL_DESIGN, " + 
  		"SMP_LABEL_PROTOCOL, SMP_SCAN_PROTOCOL, SMP_POOL_SIZE, SMP_POOLED_SAMPLE, TRIM(SMP_DEVELOPMENT_LANDMARK) " +
  		"FROM MIC_SAMPLE " +
  		"JOIN ISH_SUBMISSION ON SMP_SUBMISSION_FK = SUB_OID " +
  		"JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
  		"WHERE SUB_ACCESSION_ID = ? ";


  //TODO this wont be needed any more if new implementation of series page is adopted
  final static String name63 = "SUBMISSION_SERIES";
  final static String query63 = "SELECT SER_GEO_ID, SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN, SER_OID " +
                                "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
                                "WHERE SUB_ACCESSION_ID = ? " +
                                "AND SUB_OID=SMP_SUBMISSION_FK " +
                                "AND SRM_SAMPLE_FK=SMP_OID " +
                                "AND SRM_SERIES_FK = SER_OID";
                                
  // xingjun - 02/03/2010 - added extra column: list of components  
  final static String name165 = "SERIES_DATA";
  final static String query165 = "SELECT SER_GEO_ID, COUNT(distinct SRM_SAMPLE_FK), SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN, " +
		  						 "GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', '), SUB_ARCHIVE_ID, SUB_BATCH " + 
                                   "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANA_TIMED_NODE " + 
                                   "WHERE SER_GEO_ID = ? " + 
                                   "AND SRM_SERIES_FK = SER_OID " +
                                   "AND SRM_SAMPLE_FK = SMP_OID " +
                                   "AND SMP_SUBMISSION_FK = SUB_OID " +
                                   "AND EXP_SUBMISSION_FK=SUB_OID " +
                                   "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
                                   "AND ATN_NODE_FK = ANO_OID " +
                                   "GROUP BY SER_GEO_ID, SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN ";
                                   
  
    //SEARCH BY GEO ID
   /* final static String SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, SMP_GEO_ID, SRM_SAMPLE_ID, " +
    		"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE,  SRM_SAMPLE_DESCRIPTION, " +
    		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";*/
    
  final static String SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, SMP_GEO_ID, SRM_SAMPLE_ID, " +
  		"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE 'NOVALUE' END AS GENOTYPE,  SRM_SAMPLE_DESCRIPTION, " +
  		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
    
    final static String SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE = "FROM MIC_SAMPLE, MIC_SERIES_SAMPLE, MIC_SERIES, ISH_SUBMISSION " + 
                                             "WHERE SER_GEO_ID = ? " + 
                                             "AND SER_OID = SRM_SERIES_FK " + 
                                             "AND SRM_SAMPLE_FK = SMP_OID " + 
                                             "AND SMP_SUBMISSION_FK = SUB_OID";
                                             
   final static String SAMPLE_SERIES_TABS_WITH_GROUP_BY_CLAUSE = "FROM MIC_SAMPLE, MIC_SERIES_SAMPLE, MIC_SERIES, ISH_SUBMISSION, ISH_EXPRESSION, ISH_SPECIMEN, ANA_NODE, ANA_TIMED_NODE " + 
    		"WHERE SER_GEO_ID = ? " +
    		"AND SER_OID = SRM_SERIES_FK " +
    		"AND SRM_SAMPLE_FK = SMP_OID " +
    		"AND SMP_SUBMISSION_FK = SUB_OID " +
    		"AND EXP_SUBMISSION_FK=SUB_OID " +
    		"AND SPN_SUBMISSION_FK=SUB_OID " +
    		"AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
    		"AND ATN_NODE_FK = ANO_OID " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4" + // added by xingjun - 10/10/2011 - only retrieve public entries
    		"GROUP BY SMP_OID ";

    final static String name166 = "SERIES_SAMPLES";
    final static String query166 = SAMPLE_SERIES_COLS + SAMPLE_SERIES_TABS_WITH_GROUP_BY_CLAUSE;
    final static String ORDER_BY_PROJECT_ID = " ORDER BY CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,':')+1) AS SIGNED)";
                                   
    
    final static String name167 = "TOTAL_SAMPLES";
    final static String query167 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) " + SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name168 = "TOTAL_GEO_IDS";
    final static String query168 = "SELECT COUNT(DISTINCT SMP_GEO_ID) " + SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name169 = "TOTAL_SAMPLE_IDS";
    final static String query169 = "SELECT COUNT(DISTINCT SRM_SAMPLE_ID) " + SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name170 = "TOTAL_SAMPLE_DESCRIPTIONS";
    final static String query170 = "SELECT COUNT(DISTINCT SRM_SAMPLE_DESCRIPTION) " + SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    // xingjun - 03/03/2010 - for time being use the same sql as TOTAL_SAMPLE_DESCRIPTIONS
    final static String name233 = "TOTAL_COMPONENT";
    final static String query233 = "SELECT COUNT(DISTINCT SRM_SAMPLE_DESCRIPTION) " + SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
                                             
    //TODO this wont be needed any more if new implementation of series page is adopted
  final static String name64 = "SAMPLE_NUMBER_OF_SERIES";
  final static String query64 = "SELECT COUNT(SMP_GEO_ID) " +
                                "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE " +
                                "WHERE SRM_SAMPLE_FK=SMP_OID " +
                                "AND SRM_SERIES_FK = SER_OID " +
                                "AND SER_OID IN" +
                                " (SELECT SER_OID " +
                                "  FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
                                "  WHERE SUB_ACCESSION_ID = ? " +
                                "  AND SUB_OID=SMP_SUBMISSION_FK " +
                                "  AND SRM_SAMPLE_FK=SMP_OID " +
                                "  AND SRM_SERIES_FK = SER_OID)";
  
  final static String name102 = "SERIES_SAMPLE";
  final static String query102 = "select SUB_ACCESSION_ID, SMP_GEO_ID, SRM_SAMPLE_ID, SRM_SAMPLE_DESCRIPTION " +
                                 "from MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE , ISH_SUBMISSION " +
                                 "where SRM_SAMPLE_FK = SMP_OID " +
                                 "and SRM_SERIES_FK = SER_OID " +
                                 "and SMP_SUBMISSION_FK = SUB_OID " +
                                 "and SER_OID in" +
                                 " (select SER_OID " +
                                 "  from MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
                                 "  where SUB_ACCESSION_ID = ? " +
                                 "  and SUB_OID=SMP_SUBMISSION_FK " +
                                 "  and SRM_SAMPLE_FK=SMP_OID " +
                                 "  and SRM_SERIES_FK = SER_OID) ";

  final static String name65 = "SUBMISSION_PLATFORM";
  final static String query65 = "SELECT PLT_GEO_ID, PLT_TITLE, PLT_NAME, PLT_DISTRIBUTION, PLT_TECHNOLOGY, PLT_ORGANISM, PLT_MANUFACTURER, PLT_MANUFACTURER_PROTOCOL, PLT_CATALOGUE_NO " +
                                "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, MIC_PLATFORM " +
                                "WHERE SUB_ACCESSION_ID = ? " +
                                "AND SUB_OID=SMP_SUBMISSION_FK " +
                                "AND SRM_SAMPLE_FK=SMP_OID " +
                                "AND SRM_SERIES_FK = SER_OID " +
                                "AND SER_PLATFORM_FK=PLT_OID";

  final static String name66 = "SUBMISSION_GENE_LIST_ITEM";
  final static String query66 = "SELECT MBC_GNF_SYMBOL, MBC_GLI_SIGNAL, MBC_GLI_DETECTION, MBC_GLI_P_VALUE, MBC_GLI_PROBE_SET_NAME " +
                                "FROM MIC_BROWSE_CACHE " +
                                "WHERE MBC_SUB_ACCESSION_ID = ?";
  
  final static String name67 = "TOTAL_NUMBER_OF_GENE_LIST_ITEM";
  final static String query67 = "SELECT COUNT(*) FROM MIC_BROWSE_CACHE WHERE MBC_SUB_ACCESSION_ID = ?";
  
  // gene query -- input gene info (symbol, name, synonym)
  final static String select_expression_gene_query = getISH_BROWSE_ALL_COLUMNS() +
                                                     "FROM ISH_SUBMISSION ";
  
  final static String join_expression_gene_query= ISH_BROWSE_ALL_TABLES + 
                                                  "LEFT JOIN REF_MGI_MRK " + 
                                                  "ON RMM_SYMBOL=RPR_SYMBOL "+
                                                  "LEFT JOIN REF_SYNONYM " + 
                                                    "ON RSY_REF = RMM_ID ";
                                                  
  final static String JOIN_EXPRESSION_TABLE = "JOIN ISH_EXPRESSION AS E ON SUB_OID = E.EXP_SUBMISSION_FK ";
  
  final static String name68 = "SUBMISSION_GENE_QUERY_IGNORE_EXPRESSION";
  final static String query68 = getISH_BROWSE_ALL_COLUMNS() + join_expression_gene_query + "WHERE ";
                                // need append gene info and stage condition and other where condition if applicable
                                // and order by string if specified
                                // geneInfo + stageCondition + orderByString;
  
  final static String name69 = "SUBMISSION_GENE_QUERY_CONCERN_EXPRESSION";
  final static String query69 = getISH_BROWSE_ALL_COLUMNS() + join_expression_gene_query +
                                "JOIN ISH_EXPRESSION AS E ON SUB_OID = E.EXP_SUBMISSION_FK "
                                +"WHERE ";
                                
                                // need append gene info and stage condition and other where condition if applicable
                                // and order by string if specified
                                // geneInfo + stageCondition + orderByString;
                                
  final static String name70 = "WHERE_CONDITION_GENE_QUERY";
  final static String query70 = " AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
                                "" +
                                "";
  
  final static String name71 = "TOTAL_NUMBER_OF_SUBMISSION_QENE_QUERY";
  final static String query71 = NUMBER_OF_SUBMISSION;
  
  final static String NUMBER_OF_GENE_SYMBOL_GENE_QUERY = "SELECT COUNT(DISTINCT RPR_SYMBOL) ";
  
  final static String name72 = "TOTAL_NUMBER_OF_GENE_SYMBOL_QENE_QUERY";
  final static String query72 = NUMBER_OF_GENE_SYMBOL_GENE_QUERY ;
  
  final static String name73 = "TOTAL_NUMBER_OF_THEILER_STAGE_QENE_QUERY";
  final static String query73 = NUMBER_OF_THEILER_STAGE ;
  
  final static String name74 = "TOTAL_NUMBER_OF_GIVEN_STAGE_QENE_QUERY";
  final static String query74 = NUMBER_OF_GIVEN_STAGE;
  
  final static String name75 = "TOTAL_NUMBER_OF_LAB_QENE_QUERY";
  final static String query75 = NUMBER_OF_LAB;
  
  final static String name76 = "TOTAL_NUMBER_OF_SUBMISSION_DATE_QENE_QUERY";
  final static String query76 = NUMBER_OF_SUBMISSION_DATE;
  
  final static String name77 = "TOTAL_NUMBER_OF_ASSAY_TYPE_QENE_QUERY";
  final static String query77 = NUMBER_OF_ASSAY_TYPE;
  
  final static String name78 = "TOTAL_NUMBER_OF_SPECIMEN_TYPE_QENE_QUERY";
  final static String query78 = NUMBER_OF_SPECIMEN_TYPE;
  
  final static String NUMBER_OF_IMAGE_GENE_QUERY = "SELECT COUNT(DISTINCT CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME), REPLACE(SUB_ACCESSION_ID, '" + ":" + "', '" + "no" + "')) ";
  
  final static String name79 = "TOTAL_NUMBER_OF_IMAGE_QENE_QUERY";
  final static String query79 = NUMBER_OF_IMAGE_GENE_QUERY ;
  
  final static String name80 = "JOIN_EXPRESSION_GENE_QUERY";
  final static String query80 = join_expression_gene_query;
  
  final static String name81 = "WITH_EXPRESSION_GENE_QUERY";
  final static String query81 = "JOIN ISH_EXPRESSION AS E ON SUB_OID = E.EXP_SUBMISSION_FK ";
  
  // component count query
  final static String select_expression_component_count_query = "SELECT DISTINCT APO_FULL_PATH, EXP_COMPONENT_ID," +
  		                                                        "STG_STAGE_DISPLAY, RPR_SYMBOL, " +
  		                                                        "COUNT(DISTINCT SUB_ACCESSION_ID) " +
  		                                                        "FROM ISH_SUBMISSION ";
  
  final static String join_expression_component_count_query = "JOIN ISH_EXPRESSION AS E ON E.EXP_SUBMISSION_FK = SUB_OID " +
  		                                                      "JOIN REF_MGI_MRK " +
  		                                                      "LEFT JOIN REF_SYNONYM ON RSY_REF = RMM_ID " +
  		                                                      "LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
  		                                                      "JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK " +
  		                                                      "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
  		                                                      "JOIN REF_URL ON URL_OID = 1 " +
  		                                                      "JOIN ANA_NODE " +
  		                                                      "JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
  		                                                      "  AND ATN_NODE_FK = ANO_OID " +
  		                                                      "JOIN ANAD_PART_OF ON APO_NODE_FK = " +
  		                                                      "  ANO_OID AND APO_IS_PRIMARY = true " +
  		                                                      "JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
  		                                                      "JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  		                                                      "  AND IMG_ORDER = " +
  		                                                      "    (SELECT MIN(I.IMG_ORDER) FROM ISH_ORIGINAL_IMAGE I " +
  		                                                      "     WHERE I.IMG_SUBMISSION_FK = SUB_OID AND I.IMG_TYPE NOT LIKE '%wlz%') " +
  		                                                      "JOIN REF_PROBE WHERE ";
  
  final static String name103 = "COMPONENT_COUNT_QUERY";
  final static String query103 = GENES_IN_COMPONENT_COLS + GENES_IN_COMPONENT_TABLES;
  // need append gene info and stage condition
  // and order by string if specified
  // geneInfo + stageCondition + orderByString;

  final static String name105 = "GROUP_BY_CLAUSE_COMPONENT_COUNT_QUERY";
  final static String query105 = "GROUP BY APO_FULL_PATH, EXP_COMPONENT_ID, STG_STAGE_DISPLAY, RPR_SYMBOL ";


  // component query
  final static String select_expression_component_query = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES;
  
  static String endsComponentQueryWithoutStagePart1 = "JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID AND EXP_STRENGTH = 'present' " +
  		                                   " AND EXP_COMPONENT_ID IN " +
  		                                   "  (SELECT DESCEND_ATN.ATN_PUBLIC_ID " +
  		                                   "   FROM ANA_TIMED_NODE ANCES_ATN,ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN " +
  		                                   "   WHERE ANCES_ATN.ATN_PUBLIC_ID IN ( ";
  
  final static String endsComponentQueryWithoutStagePart2 = " ) " +
                                                            "   AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
                                                            "   AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK" +
                                                            "   AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK ) " + PUBLIC_ENTRIES_Q;
  
  final static String endsComponentQueryWithStage =         " JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID AND EXP_STRENGTH = 'present' " +
  		                                            " AND EXP_COMPONENT_ID IN " +
  		                                            "  (SELECT DESCEND_ATN.ATN_PUBLIC_ID " +
  		                                            "   FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN, ANA_STAGE " +
  		                                            "   WHERE ANCES_ATN.ATN_PUBLIC_ID = ? " +
  		                                            "   AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
  		                                            "   AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
  		                                            "   AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
  		                                            "   AND ANCES_ATN.ATN_STAGE_FK  = STG_OID and STG_NAME = ?) " + PUBLIC_ENTRIES_Q;
  
  final static String name82 = "COMPONENT_QUERY_WITHOUT_STAGE_START";
  static String query82 = select_expression_component_query + endsComponentQueryWithoutStagePart1;
  
  final static String name93 = "COMPONENT_QUERY_WITHOUT_STAGE_END";
  final static String query93 = endsComponentQueryWithoutStagePart2;
  
  final static String name83 = "COMPONENT_QUERY_WITH_STAGE";
  final static String query83 = select_expression_component_query + endsComponentQueryWithStage;
  
  final static String name84 = "PI_NAME";
  final static String query84 = "SELECT PER_NAME FROM ISH_PERSON WHERE PER_TYPE_FK = 'PI' AND PER_OID = ? ";
  
  /** ---anatomy query--- */
  // used for anatomy structure page
  final static String name85 = "STAGES_IN_PERSPECTIVE";
  final static String query85 =  "SELECT DISPLAY.STG_NAME " +
                                 "FROM ANA_STAGE DISPLAY " +
                                 "WHERE DISPLAY.STG_SEQUENCE " +
                                 "BETWEEN " +
                                 "  (SELECT S1.STG_SEQUENCE FROM ANA_STAGE S1 WHERE S1.STG_OID = " +
                                 "    (SELECT MIN(APO_PATH_START_STAGE_FK) FROM ANAD_PART_OF WHERE APO_NODE_FK IN " +
                                 "      (SELECT NIP_NODE_FK FROM ANA_NODE_IN_PERSPECTIVE WHERE NIP_PERSPECTIVE_FK = '"+bundle.getString("perspective")+"'))) " +
                                 "AND " +
                                 "  (SELECT S2.STG_SEQUENCE FROM ANA_STAGE S2 WHERE S2.STG_OID = " +
                                 "    (SELECT MAX(APO_PATH_END_STAGE_FK) FROM ANAD_PART_OF WHERE APO_NODE_FK IN " +
                                 "      (SELECT NIP_NODE_FK FROM ANA_NODE_IN_PERSPECTIVE WHERE NIP_PERSPECTIVE_FK = '"+bundle.getString("perspective")+"'))) " +
                                 " ORDER BY DISPLAY.STG_SEQUENCE";

  final static String name234 = "STAGES_IN_PERSPECTIVE_FROM_TS17";
  final static String query234 =  "SELECT DISPLAY.STG_NAME, IF(REF_STAGE.STG_DPC_VALUE='',REF_STAGE.STG_DPC_PREFIX,REF_STAGE.STG_DPC_VALUE) " +
  		"FROM ANA_STAGE DISPLAY " +
  		"JOIN REF_STAGE ON REF_STAGE.STG_OID = DISPLAY.STG_NAME " +
  		"WHERE DISPLAY.STG_SEQUENCE " +
  		"BETWEEN 16 AND " +
  		"(SELECT S2.STG_SEQUENCE FROM ANA_STAGE S2 WHERE S2.STG_OID = " +
  		" (SELECT MAX(APO_PATH_END_STAGE_FK) FROM ANAD_PART_OF WHERE APO_NODE_FK IN " +
  		"  (SELECT NIP_NODE_FK FROM ANA_NODE_IN_PERSPECTIVE WHERE NIP_PERSPECTIVE_FK = 'Urogenital'))) " +
  		"ORDER BY DISPLAY.STG_SEQUENCE ";

  final static String name86 = "ANNOTATED_STAGE_RANGE";
  final static String query86 = "SELECT DISPLAY.STG_NAME " +
                                "FROM ANA_STAGE DISPLAY " +
                                "WHERE DISPLAY.STG_SEQUENCE " +
                                "BETWEEN " +
                                " (SELECT MIN(RANGE1.STG_SEQUENCE) " +
                                "  FROM ANA_STAGE RANGE1, ANA_TIMED_NODE, ISH_EXPRESSION, ISH_SUBMISSION " +
                                "  WHERE RANGE1.STG_OID = ATN_STAGE_FK " +
                                "  AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
                                "  AND EXP_SUBMISSION_FK = SUB_OID " +
                                "  AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
                                "  AND SUB_ASSAY_TYPE = 'ISH') " +
                                "AND " +
                                "  (SELECT MAX(RANGE2.STG_SEQUENCE) " +
                                "   FROM ANA_STAGE RANGE2, ANA_TIMED_NODE, ISH_EXPRESSION, ISH_SUBMISSION " +
                                "   WHERE RANGE2.STG_OID = ATN_STAGE_FK " +
                                "   AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
                                "   AND EXP_SUBMISSION_FK = SUB_OID " +
                                "   AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
                                "   AND SUB_ASSAY_TYPE = 'ISH') " +
                                "ORDER BY STG_SEQUENCE ";

  final static String name87 = "STAGE_SEQUENCE";
  final static String query87 = "SELECT STG_SEQUENCE FROM ANA_STAGE WHERE STG_NAME = ?";

  // query to build anatomy tree for querying db
  final static String name88 = "QUERY_TREE_CONTENT";
  final static String query88 = "SELECT DISTINCT APO_DEPTH, APO_SEQUENCE, PARENT.ANO_OID,PARENT.ANO_COMPONENT_NAME, CONCAT(as1.STG_NAME,'-',as2.STG_NAME) as rnge, "+
                                 "(select count(*) "+
                                  "from ANA_RELATIONSHIP, ANA_NODE CHILD, ANA_TIMED_NODE CATN, ANA_STAGE CSTG "+
                                  "where REL_PARENT_FK = PARENT.ANO_OID "+
                                  "and REL_CHILD_FK  = CHILD.ANO_OID "+
                                  "and CATN.ATN_NODE_FK = CHILD.ANO_OID "+
                                  "and CSTG.STG_OID = CATN.ATN_STAGE_FK "+
                                  "and CSTG.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE) as kids,"+
                                  "case when !APO_IS_PRIMARY OR ANO_IS_GROUP THEN 1 ELSE 0 END AS IP, "+
                                  "'','', '', 0 "+
                                  "'', '', '', '', "+  
                                  "'', '' " +
                              "FROM ANA_NODE PARENT "+
                               "JOIN ANAD_PART_OF "+
                                 "ON ANO_OID = APO_NODE_FK AND APO_FULL_PATH NOT LIKE '%mouse.embryo%' "+
                               "JOIN ANAD_PART_OF_PERSPECTIVE " +
                               "ON POP_PERSPECTIVE_FK = '" + bundle.getString("perspective") + "' "+
                                 " AND POP_APO_FK = APO_OID " +
                               "JOIN ANA_STAGE qs1 "+
                                 "ON qs1.STG_NAME = ? "+
                               "JOIN ANA_STAGE qs2 "+
                                 "ON qs2.STG_NAME = ? "+
                               "JOIN ANA_STAGE as1 "+
                                 "ON as1.STG_OID = APO_PATH_START_STAGE_FK  "+
                                "JOIN ANA_STAGE as2 "+
                                  "ON as2.STG_OID = APO_PATH_END_STAGE_FK "+
                                "WHERE as1.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE   AND qs2.STG_SEQUENCE "+
                                "OR as2.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE  "+
                                "OR (as1.STG_SEQUENCE < qs1.STG_SEQUENCE AND as2.STG_SEQUENCE > qs2.STG_SEQUENCE ) "+
                                "ORDER BY APO_SEQUENCE";
  
  final static String name89 = "ANATOMY_PUBLIC_ID_FROM_NODE_ID";
  final static String query89 = "SELECT ATN_PUBLIC_ID " +
                                "FROM ANA_TIMED_NODE, ANA_STAGE tns, ANA_STAGE qs1, ANA_STAGE qs2 " +
                                "WHERE qs1.STG_NAME = ? " +
                                "AND qs2.STG_NAME = ? " +
                                "AND tns.STG_OID = ATN_STAGE_FK " +
                                "AND tns.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE " +
                                "AND  ATN_NODE_FK ";
  
  final static String name90 = "GENE_IN_COMPONENT_START";
  final static String query90 = getISH_BROWSE_ALL_COLUMNS() +
                                                            "FROM ISH_SUBMISSION " +
                                                            "JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK " +
                                                            "JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
                                                            "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
                                                            "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
                                                            "JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID " +
                                                            "  AND EXP_STRENGTH = ? " +
                                                            "  AND EXP_COMPONENT_ID in " +
                                                            "   (select DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
                                                            "    from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN " +
                                                            "    where ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
                                                            "    and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
                                                            "    and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
                                                            "    and ANCES_ATN.ATN_PUBLIC_ID ";

  final static String name91 = "GENE_EXPRESSED_IN_COMPONENT_END";
  final static String query91 = "JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  		                                                "  AND IMG_ORDER = " +
  		                                                "   (SELECT MIN(I.IMG_ORDER) " +
  		                                                "    FROM ISH_ORIGINAL_IMAGE I " +
  		                                                "    WHERE I.IMG_SUBMISSION_FK = SUB_OID AND I.IMG_TYPE NOT LIKE '%wlz%') " +
  		                                                "JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = 14 " + PUBLIC_ENTRIES_Q;
  
  final static String name92 = "GENE_NOT_DETECTED_IN_COMPONENT_END";
  final static String query92 = "JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  		                                                  "  AND IMG_ORDER = " +
  		                                                  "    (SELECT MIN(I.IMG_ORDER) " +
  		                                                  "     FROM ISH_ORIGINAL_IMAGE I " +
  		                                                  "     WHERE I.IMG_SUBMISSION_FK = SUB_OID) " + PUBLIC_ENTRIES_Q;
  
  /** ---used for entry page--- */
  // query to find the number of total ISH submissions users sent to GUDMAP DB
  final static String name94 = "TOTAL_NUMBER_OF_SUBMISSIONS_ISH";
  final static String query94 = "SELECT SUM(FTP_TOTAL_SUBMISSIONS) FROM REF_FTP_SUBMISSION WHERE FTP_ASSAY_TYPE='ISH' AND FTP_ISDELETED=0 ";

  // query to find the number of total Microarray submissions users sent to GUDMAP DB
  final static String name95 = "TOTAL_NUMBER_OF_SUBMISSIONS_ARRAY";
  final static String query95 = "SELECT SUM(FTP_TOTAL_SUBMISSIONS) FROM REF_FTP_SUBMISSION WHERE FTP_ASSAY_TYPE='Microarray' AND FTP_ISDELETED=0";

  //query to find the number of public ISH submissions in the db
  final static String name96 = "NUMBER_OF_PUBLIC_SUBMISSIONS_ISH";
  final static String query96 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION WHERE SUB_ASSAY_TYPE LIKE '%ISH%' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
  
  //query to find the number of public array submissions in db
  final static String name97 = "NUMBER_OF_PUBLIC_SUBMISSIONS_ARRAY";
  final static String query97 = "select count(distinct SUB_ACCESSION_ID) from MIC_SAMPLE, ISH_SUBMISSION where SMP_SUBMISSION_FK=SUB_OID and SUB_IS_PUBLIC=1";

  // query to find the number of total IHC submissions users sent to GUDMAP DB
  final static String name142 = "TOTAL_NUMBER_OF_SUBMISSIONS_IHC";
  final static String query142 = "SELECT SUM(FTP_TOTAL_SUBMISSIONS) FROM REF_FTP_SUBMISSION WHERE FTP_ASSAY_TYPE='IHC' AND FTP_ISDELETED=0 ";

  // query to find the number of public IHC submissions in db
  final static String name143 = "NUMBER_OF_PUBLIC_SUBMISSIONS_IHC";
  final static String query143 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION WHERE SUB_ASSAY_TYPE LIKE '%IHC%' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
  
  final static String name222 = "NUMBER_OF_PUBLIC_SUBMISSIONS_TG";
  final static String query222 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION WHERE SUB_ASSAY_TYPE ='TG' AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
    
//query to find the number of public WISH submissions in db
  final static String name269 = "NUMBER_OF_PUBLIC_SUBMISSIONS_WISH";
  final static String query269 = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM REF_PROBE, ISH_PROBE, ISH_SUBMISSION, ISH_SPECIMEN WHERE " +
		  						 "PRB_MAPROBE = RPR_OID AND PRB_SUBMISSION_FK = SUB_OID AND SPN_SUBMISSION_FK = SUB_OID AND SUB_IS_DELETED = '0' AND " +
		  						 "SUB_IS_PUBLIC = 1 AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE = 'ISH' AND SPN_ASSAY_TYPE = 'wholemount'";

  //query to find the number of public SISH submissions in db
  final static String name270 = "NUMBER_OF_PUBLIC_SUBMISSIONS_SISH";
  final static String query270 = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM REF_PROBE, ISH_PROBE, ISH_SUBMISSION, ISH_SPECIMEN WHERE " +
		  						 "PRB_MAPROBE = RPR_OID AND PRB_SUBMISSION_FK = SUB_OID AND SPN_SUBMISSION_FK = SUB_OID AND SUB_IS_DELETED = '0' AND " +
		  						 "SUB_IS_PUBLIC = 1 AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE = 'ISH' AND SPN_ASSAY_TYPE = 'section'";
  
//query to find the number of public OPT submissions in db
  final static String name271 = "NUMBER_OF_PUBLIC_SUBMISSIONS_OPT";
  final static String query271 = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM REF_PROBE, ISH_PROBE, ISH_SUBMISSION, ISH_SPECIMEN WHERE " +
		  						 "PRB_MAPROBE = RPR_OID AND PRB_SUBMISSION_FK = SUB_OID AND SPN_SUBMISSION_FK = SUB_OID AND SUB_IS_DELETED = '0' AND " +
		  						 "SUB_IS_PUBLIC = 1 AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE = 'ISH' AND SPN_ASSAY_TYPE = 'opt-wholemount'";
 
  
//query to find the number of nextgen sequences in db
  final static String name276 = "NUMBER_OF_NEXTGEN_SEQUENCES";
  final static String query276 = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM ISH_SUBMISSION WHERE SUB_IS_DELETED = '0' AND SUB_IS_PUBLIC = '1' AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE = 'NextGen'";
  
  //query to find date of last modification by editor
  final static String name99 = "LAST_ENTRY_DATE_EDITORIAL";
  final static String query99 = "select max(SUB_MODIFIED_DATE) from ISH_SUBMISSION";

  // query to find last software update date
  final static String name100 = "LAST_SOFTWARE_UPDATE_DATE";
  final static String query100 = "SELECT MIS_SOFT_UPDATE FROM REF_MISC";

  //query to find the latest date of entry in db
  final static String name101 = "LAST_ENTRY_DATE_DB";
  final static String query101 = "select max(SUB_ENTRY_DATE) from ISH_SUBMISSION";

  // query to find the number of related array submissions for specific gene
  final static String name107 = "NUMBER_RELATED_SUBMISSIONS_FOR_GENE_ARRAY";
  final static String query107 = "select count(distinct MBC_SUB_ACCESSION_ID) from MIC_BROWSE_CACHE where MBC_GNF_SYMBOL = ? ";

  final static String name109 = "SAMPLE_INFO_BY_SERIES_ID";
  final static String query109 = "select SMP_OID, SMP_TITLE, SMP_CEL_FILENAME " +
  		"from MIC_SAMPLE, MIC_SERIES_SAMPLE, MIC_SERIES " +
  		"where SMP_OID = SRM_SAMPLE_FK and SRM_SERIES_FK = SER_OID and SER_GEO_ID = ? " +
  		"order by natural_sort(SMP_THEILER_STAGE), natural_sort (SMP_TITLE)";

  final static String name110 = "SERIES_INFO";
  final static String query110 = "select SER_GEO_ID from MIC_SERIES order by natural_sort(SER_GEO_ID)";

  final static String name111 = "GENELIST_INFO_RAW";
  final static String query111 = "select GLI_SERIAL_NO, GLI_SIGNAL, GLI_DETECTION from MIC_GENE_LIST_ITEM where GLI_SAMPLE_FK = ? and GLI_SERIAL_NO < 1000";// for test only

  final static String name112 = "GENELIST_INFO_WITH_SYMBOL_RAW";
  final static String query112 = "select GL.GLI_SERIAL_NO, GL.GLI_SIGNAL, GL.GLI_DETECTION, GL.GLI_PROBE_SET_NAME, GN.GNF_SYMBOL " +
  "from MIC_GENE_LIST_ITEM GL " +
  "left join MIC_PROBE_SET PS on GL.GLI_PROBE_SET_NAME = PS.PRS_PROBE_SET_ID " +
  "left join MIC_PROBE_SET_GENE PG on PS.PRS_OID = PG.PSG_PROBE_SET_FK " +
  "left join REF_GENE_INFO GN on GN.GNF_OID = PG.PSG_GENE_INFO_FK " +
  "where GLI_SAMPLE_FK = ? and GL.GLI_SERIAL_NO < 1000 " +
  "order by GL.GLI_PROBE_SET_NAME ";

  // used for microarray focus: genelist
  final static String name113 = "PROCESSED_GENELIST_COMPONENT_NAME";
  final static String query113 = "select distinct IGA_COMPONENT_NAME from INT_GENE_LIST_ANA where IGA_PARENT_NAME LIKE ";

  final static String name114 = "PROCESSED_GENELIST_HEADER";
  final static String query114 = "select IGL_OID, IGL_SERIAL_NUMBER, IGL_FILE_NAME, PER_SURNAME, IGL_LAB_NAME, IGL_COMPONENT, IGL_STATISTICS, IGL_DATA_TRANSFORMATION, IGL_TESTS, IGL_UP_REGULATED, IGL_DOWN_REGULATED from INT_GENE_LIST, ISH_PERSON ";

  final static String name115 = "PROCESSED_GENELIST_EXTERNAL_LINK";
  final static String query115 = "select URL_URL, URL_SUFFIX, URL_INSTITUTE, URL_SHORT_NAME from REF_URL where URL_TYPE like 'genelist%' order by URL_TYPE";

  final static String name116 = "PROCESSED_GENELIST_ITEM";
  final static String query116 = "SELECT IGI_GRP1_MEAN, IGI_GRP2_MEAN, IGI_GRP1_SEM, IGI_GRP2_SEM, IGI_RATIO, IGI_DIRECTION, IGI_P_VALUE, IGI_GENE_IDENTIFIER, IGI_OTHER_ID, IGI_GENE_NAME, IGI_UG_CLUSTER, IGI_LOCUSLINK, IGI_GENE_ID, IGI_CHROMOSOME, IGI_ONTOLOGIES, IGI_ISH_ENTRY_COUNT, OMM_OMIM_ID " +
  	"FROM INT_GENE_LIST_ITEM LEFT JOIN REF_OMIM ON OMM_GENE_SYMBOL=IGI_GENE_ID " +
	"WHERE IGI_GENE_LIST_OID = ? ";

  
  final static String name121 = "PERSON_BY_ID";
  final static String query121 = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, PER_FAX, PER_OID FROM ISH_PERSON WHERE PER_OID = ?";

  // find total number of entries for processed gene list
  final static String name122 = "TOTAL_NUMBER_OF_pROCESSED_GENE_LIST_ITEMS";
  final static String query122 = "SELECT COUNT(*) FROM INT_GENE_LIST_ITEM WHERE IGI_GENE_LIST_OID = ? ";
  
  final static String name128 = "LOGIN_DETAILS";
  final static String query128 = "SELECT USR_UNAME, USR_PASSWD, USR_USER_TYPE, PRV_OID, USP_USER_FK, USP_PI_ID, USR_FORENAME FROM REF_USER " +
  		"JOIN SEC_USER_PI ON USP_USER_FK = USR_OID " +
  		"JOIN SEC_USER_PRIVILEGE ON RPV_USER_FK = USR_OID " +
  		"JOIN SEC_PRIVILEGE ON PRV_OID = RPV_PRIVILEGE_FK " +
  		"WHERE USR_UNAME = ? AND USR_PASSWD = ?";

  final static String name138 = "TOTAL_NUMBER_OF_EXPERIMENT_NAME";
  final static String query138 = "SELECT COUNT(DISTINCT SER_TITLE) FROM MIC_SERIES ";

  final static String name162 = "TOTAL_NUMBER_OF_SAMPLE_NUMBERS";
  final static String query162 = "SELECT COUNT(DISTINCT (SELECT COUNT(*) FROM MIC_SERIES_SAMPLE WHERE SRM_SERIES_FK = SER_OID)) FROM MIC_SERIES ";
  
  final static String name140 = "TOTAL_NUMBER_OF_SERIES_GEO_ID";
  final static String query140 = "SELECT COUNT(DISTINCT SER_GEO_ID) FROM MIC_SERIES ";

  // find the number of series available in database
  final static String name141 = "TOTAL_NUMBER_OF_SERIES";
  final static String query141 = "SELECT COUNT(DISTINCT SER_OID) FROM MIC_SERIES, MIC_PLATFORM  ";
  
  final static String name197 = "TOTAL_NUMBER_OF_SERIES_LAB";
  final static String query197 = "SELECT COUNT(DISTINCT PER_SURNAME) FROM MIC_SERIES ";
  
  final static String name198 = "TOTAL_NUMBER_OF_SERIES_PLATFORM";
  final static String query198 = "SELECT COUNT(DISTINCT PLT_GEO_ID) FROM MIC_PLATFORM " +
  		"JOIN MIC_SERIES ON PLT_OID = SER_PLATFORM_FK ";
  

  /** queries linked to ish non-renal submission display  */
  // additional criteria
  final static String ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_CRITERIA =
	  "AND SUB_ACCESSION_ID NOT IN " +
  		"(SELECT DISTINCT SUB_ACCESSION_ID " +
  		"FROM ISH_SUBMISSION, ISH_EXPRESSION, ANA_TIMED_NODE, ANAD_PART_OF_PERSPECTIVE " +
  		"WHERE POP_PERSPECTIVE_FK = 'Renal' " +
  		"AND EXP_STRENGTH = 'present'  " +
  		"AND SUB_OID = EXP_SUBMISSION_FK " +
  		"AND EXP_COMPONENT_ID = ATN_PUBLIC_ID " +
  		"AND POP_NODE_FK = ATN_NODE_FK) ";
  
  final static String ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_JOIN =
	  "JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK AND EXP_STRENGTH = 'present' " +
	  "JOIN ANA_TIMED_NODE ON EXP_COMPONENT_ID = ATN_PUBLIC_ID " +
	  "JOIN ANAD_PART_OF_PERSPECTIVE ON POP_NODE_FK = ATN_NODE_FK AND POP_PERSPECTIVE_FK = 'Urogenital' ";
  
  final static String name144 = "ISH_NON_RENAL_SUBMISSIONS";
//  final static String query144 = ISH_BROWSE_ALL_COLUMNS + ISH_BROWSE_ALL_TABLES
  final static String query144 = getISH_BROWSE_ALL_COLUMNS() + ISH_BROWSE_ALL_TABLES
  + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_JOIN
  + PUBLIC_ENTRIES_Q + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_CRITERIA;
  
  final static String name145 = "ISH_NUMBER_OF_SUBMISSIONS_NON_RENAL";
  final static String query145 = NUMBER_OF_SUBMISSION + ISH_BROWSE_ALL_TABLES
  + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_JOIN
  + PUBLIC_ENTRIES_Q + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_CRITERIA;
  
  final static String ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA = ISH_BROWSE_ALL_TABLES
  + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_JOIN
  + PUBLIC_ENTRIES_Q + ISH_NON_RENAL_SUBMISSION_QUERY_ADDITIONAL_CRITERIA;

  // total number of columns  
  final static String name146 = "TOTAL_NUMBER_OF_SUBMISSION_NON_RENAL";
  final static String query146 = NUMBER_OF_SUBMISSION + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name147 = "TOTAL_NUMBER_OF_GENE_SYMBOL_NON_RENAL";
  final static String query147 = NUMBER_OF_GENE_SYMBOL + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name148 = "TOTAL_NUMBER_OF_THEILER_STAGE_NON_RENAL";
  final static String query148 = NUMBER_OF_THEILER_STAGE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name149 = "TOTAL_NUMBER_OF_GIVEN_STAGE_NON_RENAL";
  final static String query149 = NUMBER_OF_GIVEN_STAGE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name150 = "TOTAL_NUMBER_OF_LAB_NON_RENAL";
  final static String query150 = NUMBER_OF_LAB + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name151 = "TOTAL_NUMBER_OF_SUBMISSION_DATE_NON_RENAL";
  final static String query151 = NUMBER_OF_SUBMISSION_DATE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name152 = "TOTAL_NUMBER_OF_ASSAY_TYPE_NON_RENAL";
  final static String query152 = NUMBER_OF_ASSAY_TYPE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name153 = "TOTAL_NUMBER_OF_SPECIMEN_TYPE_NON_RENAL";
  final static String query153 = NUMBER_OF_SPECIMEN_TYPE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name154 = "TOTAL_NUMBER_OF_SEX_NON_RENAL";
  final static String query154 = NUMBER_OF_SEX + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name155 = "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL_NON_RENAL"; // leave for future
  final static String query155 = NUMBER_OF_CONFIDENCE_LEVEL + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name157 = "TOTAL_NUMBER_OF_ANTIBODY_NAME_NON_RENAL"; // leave for future
  final static String query157 = NUMBER_OF_ANTIBODY_NAME + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name158 = "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL_NON_RENAL"; // leave for future
  final static String query158 = NUMBER_OF_ANTIBODY_GENE_SYMBOL + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name159 = "TOTAL_NUMBER_OF_GENOTYPE_NON_RENAL";
  final static String query159 = NUMBER_OF_GENOTYPE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name160 = "TOTAL_NUMBER_OF_PROBE_TYPE_NON_RENAL";
  final static String query160 = NUMBER_OF_PROBE_TYPE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  final static String name161 = "TOTAL_NUMBER_OF_IMAGE_NON_RENAL";
  final static String query161 = NUMBER_OF_IMAGE + ISH_NON_RENAL_SUBMISSIONS_QUERY_CRITERIA;

  // find details of a antibody linked in a submission
  final static String name163 = "SUBMISSION_ANTIBODY";
  final static String query163 = "SELECT DISTINCT  CONCAT(ATB_PREFIX,ATB_OID), ATB_NAME, "+
      "RPR_JAX_ACC, RPR_SYMBOL, RPR_NAME, RPR_LOCUS_TAG, RPR_GENBANK, RPR_5_LOC, RPR_3_LOC, "+
      "ATB_TYPE, ATB_PROD_METHOD, ATB_CLONE_ID, ATB_SP_IMMUNIZED, ATB_PURIFICATION_MD, ATB_IMM_ISOTYPE, ATB_CHAIN, ATB_DIRECT_LABEL,"+
      "ATL_DETECTION_NOTES, ATL_DILUTION, ATL_LAB_ID, " +
      "SUP_COMPANY, SUP_CAT_NUM, SUP_LOT_NUM, " +
      "ATL_SEC_ANTIBODY, ATL_DETECTION_METHOD " +
		"FROM ISH_ANTIBODY " +
                                "LEFT JOIN REF_PROBE ON ATB_OID=RPR_OID " +
		"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " +  
		"JOIN ISH_SUBMISSION ON SUB_OID = ATL_SUBMISSION_FK " +
  		"JOIN LNK_SUP_ANTIBODY ON LAS_ATB_OID_FK = ATB_OID " +
  		"JOIN GEN_SUPPLIER ON LAS_SUP_FK = SUP_OID " +
  		"WHERE (CONCAT('GUDMAP:',ATL_SUBMISSION_FK) = ?)";
  
  // find species specificity linked to the antibody
  final static String name164 = "ANTIBODY_SPECIES_SPECIFICITY";
  final static String query164 = "SELECT ATB_OID,ABP_SPECIES " +
	  		"FROM ISH_ANTIBODY " +
	  		"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " + 
	  		"JOIN ISH_ATB_SPECIFICITY ON ABP_ATB_FK = ATB_OID " +
	  		"WHERE (CONCAT('GUDMAP:',ATL_SUBMISSION_FK) = ?)";

  // find species specificity linked to the antibody
  final static String name240 = "ANTIBODY_VARIANTS";
  final static String query240 = "SELECT ATB_OID,ABV_VARIANT_NAME " +
  		"FROM ISH_ANTIBODY " +
  		"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " + 
  		"JOIN ISH_ATB_VARIANTS ON ABV_ATB_FK = ATB_OID " +
  		"WHERE (CONCAT('GUDMAP:',ATL_SUBMISSION_FK) = ?)";

  // find details of a antibody 
  final static String name241 = "ANTIBODY_DETAILS";
  final static String query241 = "SELECT DISTINCT  CONCAT(ATB_PREFIX,ATB_OID),ATB_NAME,RPR_JAX_ACC,RPR_SYMBOL,RPR_NAME,RPR_LOCUS_TAG,RPR_GENBANK,'',''," +
		"ATB_TYPE,ATB_PROD_METHOD,ATB_CLONE_ID,ATB_SP_IMMUNIZED,ATB_PURIFICATION_MD,ATB_IMM_ISOTYPE,ATB_CHAIN," +
		"ATB_DIRECT_LABEL,ATL_DETECTION_NOTES,ATL_DILUTION,ATL_LAB_ID, " +
		"SUP_COMPANY,SUP_CAT_NUM,SUP_LOT_NUM, " +
		"ATL_SEC_ANTIBODY,ATL_DETECTION_METHOD, " +
		"ABN_VALUE " +
		"FROM ISH_ANTIBODY " +
		"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " +  
		"LEFT JOIN REF_PROBE ON RPR_OID = ATB_OID " +
		"LEFT JOIN ISH_ANTIBODY_NOTES ON ABN_ATB_OID_FK = ATB_OID " +
		"JOIN ISH_SUBMISSION ON SUB_OID = ATL_SUBMISSION_FK " +
  		"JOIN LNK_SUP_ANTIBODY ON LAS_ATB_OID_FK = ATB_OID " +
  		"JOIN GEN_SUPPLIER ON LAS_SUP_FK = SUP_OID " +
  		"WHERE (CONCAT(ATB_PREFIX,ATB_OID) = ?)";
  
  // find all image notes for one specific ISH submission
  final static String name171 = "ISH_SUBMISSION_ALL_IMAGE_NOTES";
  final static String query171 = "SELECT IMG_FILENAME, IFNULL(INT_VALUE, '') " +
  		"FROM ISH_SUBMISSION " +
  		"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
  		"LEFT JOIN ISH_IMAGE_NOTE ON IMG_OID = INT_IMAGE_FK " +
  		"WHERE IMG_TYPE NOT LIKE '%wlz%' AND SUB_ACCESSION_ID = ? " +
  		"ORDER BY IMG_ORDER ";
  
  final static String name214 = "ISH_SUBMISSION_PUBLIC_IMGS";
  final static String query214 = "SELECT DISTINCT IMG_FILENAME FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND IMG_SUBMISSION_FK = SUB_OID WHERE IMG_TYPE NOT LIKE '%wlz%' AND SUB_IS_DELETED = 0 AND IMG_IS_PUBLIC = 1 ORDER BY IMG_ORDER";

  
  final static String name178 = "COLLECTION_SUBMISSION_IN_SITU_PUBLIC";
  final static String query178 = "SELECT DISTINCT QIC_SUB_ACCESSION_ID QSC_SUB_ACCESSION_ID, QIC_RPR_SYMBOL QSC_RPR_SYMBOL, QIC_STG_STAGE_DISPLAY QSC_SUB_EMBRYO_STG, " +
  		"CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) QSC_AGE, QIC_PER_NAME QSC_PER_NAME, QIC_SPN_ASSAY_TYPE QSC_SPN_ASSAY_TYPE, " +
  		"SUB_ASSAY_TYPE QSC_ASSAY_TYPE, QIC_SUB_SUB_DATE QSC_SUB_SUB_DATE, QIC_SPN_SEX QSC_SPN_SEX, QIC_PRB_PROBE_NAME QSC_PRB_PROBE_NAME, " +
  		"QIC_SPN_WILDTYPE QSC_SPN_WILDTYPE, " +
  		"PRB_PROBE_TYPE QSC_PROBE_TYPE, QIC_SUB_THUMBNAIL QSC_SUB_THUMBNAIL, '' QSC_TISSUE, '' QSC_SMP_TITLE, '' QSC_SAMPLE_DESCRIPTION, '' QSC_SER_GEO_ID " +
  		"FROM QSC_ISH_CACHE, ISH_SUBMISSION, ISH_PROBE " +
  		"WHERE QIC_SUB_ACCESSION_ID = SUB_ACCESSION_ID " +
  		"AND PRB_SUBMISSION_FK = SUB_OID ";
  
  final static String name179 = "COLLECTION_SUBMISSION_ARRAY_PUBLIC";
  final static String query179 = "SELECT DISTINCT QMC_SUB_ACCESSION_ID QSC_SUB_ACCESSION_ID, '' QSC_RPR_SYMBOL, QMC_STG_STAGE_DISPLAY QSC_SUB_EMBRYO_STG, " +
  		"CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) QSC_AGE, QMC_PER_NAME QSC_PER_NAME, QMC_SPN_ASSAY_TYPE QSC_SPN_ASSAY_TYPE, " +
  		"'Microarray' QSC_ASSAY_TYPE, QMC_SUB_SUB_DATE QSC_SUB_SUB_DATE, QMC_SPN_SEX QSC_SPN_SEX, '' QSC_PRB_PROBE_NAME, " +
  		"QMC_SPN_WILDTYPE QSC_SPN_WILDTYPE, '' QSC_PROBE_TYPE, '' QSC_SUB_THUMBNAIL, " +
  		"GROUP_CONCAT(DISTINCT QMC_ANO_COMPONENT_NAME SEPARATOR ', ') QSC_TISSUE, SMP_TITLE QSC_SMP_TITLE, SMP_DESCRIPTION QSC_SAMPLE_DESCRIPTION, QMC_SER_GEO_ID QSC_SER_GEO_ID, '' QSC_RPR_LOCUS_TAG " +
  		"FROM QSC_MIC_CACHE, MIC_SAMPLE " +
  		"WHERE SMP_SUBMISSION_FK = CAST(SUBSTR(QMC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) ";

  final static String name180 = "TOTAL_NUMBER_OF_SUBMISSION_MIX";
  final static String query180 = "SELECT COUNT(DISTINCT QSC_SUB_ACCESSION_ID) FROM ";
  
  final static String name181 = "TOTAL_NUMBER_OF_GENE_SYMBOL_MIX";
  final static String query181 = "SELECT COUNT(DISTINCT QSC_RPR_SYMBOL) FROM ";

  final static String name182 = "TOTAL_NUMBER_OF_THEILER_STAGE_MIX";
  final static String query182 = "SELECT COUNT(DISTINCT QSC_SUB_EMBRYO_STG) FROM ";
  
  final static String name183 = "TOTAL_NUMBER_OF_AGE_MIX";
  final static String query183 = "SELECT COUNT(DISTINCT QSC_AGE) FROM ";
  
  final static String name184 = "TOTAL_NUMBER_OF_LAB_MIX";
  final static String query184 = "SELECT COUNT(DISTINCT QSC_PER_NAME) FROM ";
  
  final static String name185 = "TOTAL_NUMBER_OF_SUBMISSION_DATE_MIX";
  final static String query185 = "SELECT COUNT(DISTINCT QSC_SUB_SUB_DATE) FROM ";
  
  final static String name186 = "TOTAL_NUMBER_OF_ASSAY_TYPE_MIX";
  final static String query186 = "SELECT COUNT(DISTINCT QSC_ASSAY_TYPE) FROM ";
  
  final static String name187 = "TOTAL_NUMBER_OF_SPECIMEN_TYPE_MIX";
  final static String query187 = "SELECT COUNT(DISTINCT QSC_SPN_ASSAY_TYPE) FROM ";
  
  final static String name188 = "TOTAL_NUMBER_OF_SEX_MIX";
  final static String query188 = "SELECT COUNT(DISTINCT QSC_SPN_SEX) FROM ";
  
  final static String name189 = "TOTAL_NUMBER_OF_PROBE_NAME_MIX";
  final static String query189 = "SELECT COUNT(DISTINCT QSC_PRB_PROBE_NAME) FROM ";
  
  final static String name190 = "TOTAL_NUMBER_OF_GENOTYPE_MIX";
  final static String query190 = "SELECT COUNT(DISTINCT QSC_SPN_WILDTYPE) FROM ";
  
  final static String name191 = "TOTAL_NUMBER_OF_PROBE_TYPE_MIX";
  final static String query191 = "SELECT COUNT(DISTINCT QSC_PROBE_TYPE) FROM ";
  
  final static String name192 = "TOTAL_NUMBER_OF_THUMBNAIL_MIX";
  final static String query192 = "SELECT COUNT(DISTINCT QSC_SUB_THUMBNAIL) FROM ";
  
  final static String name193 = "TOTAL_NUMBER_OF_TISSUE_MIX";
  final static String query193 = "SELECT COUNT(DISTINCT QSC_TISSUE) FROM ";
  
  final static String name194 = "TOTAL_NUMBER_OF_SAMPLE_TITLE_MIX";
  final static String query194 = "SELECT COUNT(DISTINCT QSC_SMP_TITLE) FROM ";
  
  final static String name195 = "TOTAL_NUMBER_OF_SAMPLE_DESCRIPTION_MIX";
  final static String query195 = "SELECT COUNT(DISTINCT QSC_SAMPLE_DESCRIPTION) FROM ";
  
  final static String name196 = "TOTAL_NUMBER_OF_SERIES_MIX";
  final static String query196 = "SELECT COUNT(DISTINCT QSC_SER_GEO_ID) FROM ";
  
  // separate the query based on assay type. added another 2 queries for array data
  final static String name199 = "TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU";
  final static String query199 = "SELECT COUNT(*) FROM ISH_SUBMISSION WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";

  final static String name202 = "TOTAL_NUMBER_OF_ANNOTATION_SUBMISSIONS_FOR_SPECIFIC_LAB";
  final static String query202 = "SELECT COUNT(*) FROM ISH_SUBMISSION,REF_STATUS WHERE STA_OID=SUB_DB_STATUS_FK AND SUB_PI_FK = ? AND SUB_ASSAY_TYPE = ? AND STA_OID = ? AND SUB_IS_DELETED = 0 ";

  // need to count the number of the combination of columns instead of only submissions
  final static String name225 = "TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_ARRAY";
  final static String query225 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) " +
  "FROM ISH_SUBMISSION,MIC_SAMPLE,ISH_SPECIMEN, MIC_SERIES_SAMPLE, MIC_SERIES, " +
  "ISH_PERSON, ISH_EXPRESSION, ANA_TIMED_NODE, ANA_NODE ANA_NODE " +
  "WHERE SMP_SUBMISSION_FK = SUB_OID " +
  "AND SRM_SAMPLE_FK = SMP_OID "+
  "AND EXP_SUBMISSION_FK=SUB_OID "+
  "AND SRM_SERIES_FK = SER_OID "+
  "AND PER_OID = SUB_PI_FK "+
  "AND SUB_OID = SPN_SUBMISSION_FK "+
  "AND ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
  "AND ATN_NODE_FK = ANO_OID " +
  "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
  
  final static String name226 = "TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_TG";
  final static String query226 = query199;
  
  final static String name200 = "MIC_IMAGES";
  final static String query200 = "SELECT SUB_ACCESSION_ID, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND  IMG_SUBMISSION_FK = SUB_OID  JOIN REF_URL I ON I.URL_OID = IMG_URL_FK JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK WHERE SUB_IS_DELETED = 0 AND IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE='Microarray' ORDER BY IMG_ORDER ";
  
  /** retrieve collection info */
  // only retrieve own collection entries
  final static String name205 = "COLLECTION_BROWSE_EXCLUSIVE";
  final static String query205 = "SELECT CLN_OID, CLN_NAME, CLN_DESCRIPTION, CLN_USER_FK, USR_UNAME, CLN_FOCUS_GROUP, COUNT(*) CLN_NUMBER, " +
  		"CLN_STATUS, CLN_LAST_UPDATED, CLN_TYPE " +
  		",CLN_FOCUS_GROUP_NAME " +
  		"FROM CLN_COLLECTION " +
  		"JOIN REF_USER ON USR_OID = CLN_USER_FK " +
  		"JOIN CLN_COLLECTION_ITEM ON CLI_COLLECTION_FK = CLN_OID " +
  		"WHERE CLN_TYPE = ? " +
  		"AND CLN_USER_FK = ? " +
  		"GROUP BY CLN_OID";

  // only retrieve others' shared collection entries
  final static String name206 = "COLLECTION_BROWSE_OTHERS";
  final static String query206 = "SELECT CLN_OID, CLN_NAME, CLN_DESCRIPTION, CLN_USER_FK, USR_UNAME, CLN_FOCUS_GROUP, COUNT(*) CLN_NUMBER, " +
	"CLN_STATUS, CLN_LAST_UPDATED, CLN_TYPE " +
		",CLN_FOCUS_GROUP_NAME " +
	"FROM CLN_COLLECTION " +
	"JOIN REF_USER ON USR_OID = CLN_USER_FK " +
	"JOIN CLN_COLLECTION_ITEM ON CLI_COLLECTION_FK = CLN_OID " +
	"WHERE CLN_TYPE = ? " +
	"AND CLN_USER_FK <> ? AND CLN_STATUS = 1 " +
	"GROUP BY CLN_OID";

  // retrieve both own and others' shared collection entries
  final static String name207 = "COLLECTION_BROWSE_ALL";
  final static String query207 = "(" + query205 + ")" + " UNION " + "(" + query206 + ")";
  
  final static String COLLECTION_DEFAULT_ORDER_BY_COL = " CLN_NAME ";
  
  final static String name208 = "COLLECTION_DETAIL_BY_ID";
  final static String query208 = "SELECT CLN_OID, CLN_NAME, CLN_USER_FK, USR_UNAME, CLN_TYPE, CLN_STATUS, CLN_DESCRIPTION, CLN_FOCUS_GROUP, CLN_LAST_UPDATED, COUNT(*) CLN_ENTYIES " +
  		"FROM CLN_COLLECTION " +
  		"JOIN CLN_COLLECTION_ITEM ON CLI_COLLECTION_FK = CLN_OID " +
  		"JOIN REF_USER ON USR_OID = CLN_USER_FK " +
  		"WHERE CLN_OID = ? " +
  		"GROUP BY CLN_OID";
  
  final static String name209 = "COLLECTION_DETAIL_BY_NAME";
  final static String query209 = "SELECT CLN_OID, CLN_NAME, CLN_USER_FK, USR_UNAME, CLN_TYPE, CLN_STATUS, CLN_DESCRIPTION, CLN_FOCUS_GROUP, CLN_LAST_UPDATED, COUNT(*) CLN_ENTYIES " +
  		"FROM CLN_COLLECTION " +
  		"JOIN CLN_COLLECTION_ITEM ON CLI_COLLECTION_FK = CLN_OID " +
  		"JOIN REF_USER ON USR_OID = CLN_USER_FK " +
  		"WHERE CLN_USER_FK = ? " +
  		"AND CLN_NAME = ? " +
  		"GROUP BY CLN_OID";
    
  final static String name210 = "COLLECTION_ITEM_LIST";
  final static String query210 = "SELECT CLI_VALUE, CLN_TYPE FROM CLN_COLLECTION_ITEM, CLN_COLLECTION WHERE CLI_COLLECTION_FK = CLN_OID AND CLI_COLLECTION_FK = ?";
  
  final static String name227 = "REMOVED_SUBMISSION_LIST";
  final static String query227 = "SELECT SUB_ACCESSION_ID FROM ISH_SUBMISSION WHERE SUB_IS_DELETED = 1";
  
  final static String name211 = "SUBMISSION_STATUS_NOTES_BY_ID";
  final static String query211 = "SELECT STN_OID, STN_SUBMISSION_FK, STN_NOTE FROM XPR_STATUS_NOTES WHERE STN_SUBMISSION_FK = ?";
    
  final static String name212 = "MAX_COLLECTION_ID";
  final static String query212 = "SELECT MAX(CLN_OID) FROM CLN_COLLECTION";
    
  final static String name213 = "MAX_COLLECTION_ITEM_ID";
  final static String query213 = "SELECT MAX(CLI_OID) FROM CLN_COLLECTION_ITEM";
    
  final static String name215 = "PATTERN_LIST";
  final static String query215 = "select DESCRIPTION_OID from LKP_PATTERN ORDER BY DESCRIPTION_OID";
    
  final static String name216 = "LOCATION_LIST";
  final static String query216 = "SELECT DESCRIPTION_OID FROM LKP_LOCATION ORDER BY DESCRIPTION_OID";
  
  final static String name217 = "COMPONENT_LIST_AT_GIVEN_STAGE";
  final static String query217 = "SELECT DISTINCT SUBSTR(ATN_PUBLIC_ID, 6), CONCAT(ANO_COMPONENT_NAME, '(', ATN_PUBLIC_ID, ')') COMPONENT FROM ANA_TIMED_NODE " +
  		"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
  		"JOIN ANA_STAGE ON ATN_STAGE_FK = STG_OID " +
  		"JOIN ANAD_PART_OF_PERSPECTIVE ON POP_NODE_FK = ANO_OID AND POP_PERSPECTIVE_FK = 'Urogenital' " +
  		"WHERE STG_NAME = ? " +
  		"ORDER BY NATURAL_SORT(COMPONENT)";
  
  final static String name218 = "ANATOMY_TERM_FROM_ID";
  final static String query218 = "SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ATN_PUBLIC_ID = ? UNION SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE WHERE ANO_PUBLIC_ID = ?";

  // table structure changed, have to change sql accordingly
  final static String name219 = "GET_LOCKING_INFO_BY_SUBMISSION_ID";
  final static String query219 = "SELECT LSB_ACCESSION_ID, USR_UNAME, TIMESTAMPDIFF(MINUTE, LSB_LOCKED_TIME, NOW()) FROM LCK_SUBMISSION " +
  		"JOIN REF_USER ON USR_OID = LSB_USER_FK " +
  		"WHERE LSB_ACCESSION_ID = ?";
    
  final static String name221 = "GET_LOCKING_INFO_BY_BATCH_ID";
  final static String query221 = "SELECT LSB_ACCESSION_ID, LSB_USER_FK, TIMESTAMPDIFF(MINUTE, LSB_LOCKED_TIME, NOW()) FROM LCK_SUBMISSION " +
  		"JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = LSB_ACCESSION_ID " +
  		"WHERE SUB_BATCH = ?";
    
  // find synonym(s) for given symbol - no matter if there's corresponding submissions
  final static String name228 = "GET_GENE_SYNONYM_BY_SYMBOL";
  final static String query228 = "SELECT RSY_SYNONYM FROM REF_SYNONYM " +
  		"JOIN REF_MGI_MRK ON RSY_REF = RMM_ID " +
  		"WHERE RMM_SYMBOL = ?";

  // find synonym(s) for given symbolid - no matter if there's corresponding submissions
  final static String name280 = "GET_GENE_SYNONYM_BY_SYMBOLID";
  final static String query280 = "SELECT RSY_SYNONYM FROM REF_SYNONYM " +
  		"JOIN REF_MGI_MRK ON RSY_REF = RMM_ID " +
  		"WHERE RMM_MGIACC = ?";
 
  final static String name229 = "ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOL";
  final static String query229 = "SELECT DISTINCT PRS_PROBE_SET_ID FROM MIC_PROBE_SET " +
  		"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
  		"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
//  		"WHERE GNF_SYMBOL = ? " +
  		"WHERE GNF_ID = ? " +
 		"AND PRS_PLATFORM_ID = ?";

  final static String name277 = "ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOLID";
  final static String query277 = "SELECT DISTINCT PRS_PROBE_SET_ID FROM MIC_PROBE_SET " +
  		"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
  		"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
  		"WHERE GNF_ID = ? " +
  		"AND PRS_PLATFORM_ID = ?";
 
  //   - added extra criteria: section (master table can be delimited as sections, then groups)
  //   - extra column need to be retrieved: median of the expression values
  final static String name230 = "ARRAY_EXPRESSION_OF_GIVEN_PROBE_SET_IDS";
  final static String query230 = "SELECT AME_M_HEADER_FK, AME_PROBE_SET_FK, AME_VALUE_RMA, AMS_MEDIAN, AMS_STD FROM MIC_ANAL_MSTR_EXPRESSION " +
  		"JOIN MIC_ANAL_MSTR_GRP_HEADER ON AME_M_HEADER_FK = AMH_OID " +
  		"JOIN MIC_PROBE_SET ON AME_PROBE_SET_FK = PRS_OID " +
  		"JOIN MIC_ANAL_MASTER_GROUP ON AMG_OID = AMH_AM_GROUP_FK " +
  		"JOIN MIC_ANAL_MSTR_EXP_SUMMARY ON AMS_A_MASTER_FK = AMH_A_MASTER_FK AND AMS_PROBE_SET_FK = PRS_OID " +
  		"WHERE PRS_PROBE_SET_ID " +
  		"AND AMH_A_MASTER_FK = ? " +
  		"AND AMG_SECTION_FK = ? " +
  		"ORDER BY AME_M_HEADER_FK, FIELD(PRS_PROBE_SET_ID, PROBE_SET_ID_ARG) ";


	final static String name235 = "GET_SUBMISSION_TISSUE";
	final static String query235 = "SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE " +
		"JOIN ANA_TIMED_NODE ON ATN_NODE_FK = ANO_OID " +
		"JOIN ISH_SP_TISSUE ON IST_COMPONENT  = ATN_PUBLIC_ID " +
		"WHERE IST_SUBMISSION_FK = SUBSTR(?,8)";
 
	final static String name237 = "MAPROBE_PRBNOTE";
	final static String query237 = "SELECT PNT_SUBMISSION_FK,PNT_VALUE FROM ISH_PROBE_NOTE, ISH_PROBE WHERE PNT_SUBMISSION_FK = PRB_SUBMISSION_FK AND PRB_MAPROBE = ? ORDER BY PNT_SEQ";
	
	final static String name238 = "MAPROBE_NOTE";
	final static String query238 = "SELECT RPN_NOTES FROM REF_PRB_NOTES, REF_PROBE WHERE RPR_OID = ? AND RPN_PROBE_FK = RPR_OID AND RPN_ISDELETED = 0 ORDER BY RPN_OID DESC";
	
	//final static String NGD_SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, NGS_GEO_ID, NGS_SAMPLE_NAME, NGP_LIBRARY_STRATEGY, "+
	//"CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE (SELECT GROUP_CONCAT(DISTINCT ALE_ALLELE_NAME SEPARATOR ', ') FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID)  END AS GENOTYPE, NGS_DESCRIPTION, " +
	//"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
	
	//SEARCH BY GEO ID
	/*final static String NGD_SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, NGS_GEO_ID, NGS_SAMPLE_NAME, NGP_LIBRARY_STRATEGY, "+
			"CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE, NGS_DESCRIPTION, " +
			"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";*/
	
	final static String NGD_SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, NGS_GEO_ID, NGS_SAMPLE_NAME, NGP_LIBRARY_STRATEGY, "+
			"CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' ELSE 'NOVALUE' END AS GENOTYPE, NGS_DESCRIPTION, " +
    		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
	
	final static String NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE = "FROM NGD_SAMPLE, NGD_SAMPLE_SERIES, NGD_SERIES, NGD_PROTOCOL, ISH_SUBMISSION " + 
	                                             "WHERE NGR_GEO_ID = ? " + 
	                                             "AND NGL_SERIES_FK = NGR_OID " + 
	                                             "AND NGL_SAMPLE_FK = NGS_OID " + 
	                                             "NGS_PROTOCOL_FK=NGP_OID " +
	                                             "AND NGS_SUBMISSION_FK = SUB_OID";
	                                             
	final static String NGD_SAMPLE_SERIES_TABS_WITH_GROUP_BY_CLAUSE = "FROM NGD_SAMPLE, NGD_SAMPLE_SERIES, NGD_SERIES, NGD_PROTOCOL, ISH_SUBMISSION, ISH_SP_TISSUE, ANA_NODE, ANA_TIMED_NODE " + 
	    		"WHERE NGR_GEO_ID = ? " +
	    		"AND NGL_SERIES_FK = NGR_OID " +
	    		"AND NGL_SAMPLE_FK = NGS_OID " +
	    		"AND NGS_SUBMISSION_FK = SUB_OID " +
	    		"NGS_PROTOCOL_FK=NGP_OID " +
	    		"AND IST_SUBMISSION_FK=SUB_OID " +
	    		"AND ATN_PUBLIC_ID = IST_COMPONENT " +
	    		"AND ATN_NODE_FK = ANO_OID " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4" + // added by xingjun - 10/10/2011 - only retrieve public entries
	    		"GROUP BY NGS_OID ";

	final static String name247 = "NGD_SERIES_SAMPLES";
	final static String query247 = NGD_SAMPLE_SERIES_COLS + NGD_SAMPLE_SERIES_TABS_WITH_GROUP_BY_CLAUSE;
	
	final static String name248 = "NGD_SERIES_DATA";
	final static String query248 = "SELECT NGR_GEO_ID, COUNT(distinct NGL_SAMPLE_FK), NGR_TITLE, NGR_SUMMARY, NGR_OVERALL_DESIGN, SUB_ARCHIVE_ID, SUB_BATCH " + 
            "FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION " + 
            "WHERE NGR_GEO_ID = ? " + 
            "AND NGL_SERIES_FK = NGR_OID " +
            "AND NGL_SAMPLE_FK = NGS_OID " +
            "AND NGS_SUBMISSION_FK = SUB_OID "+
            "GROUP BY NGR_GEO_ID, NGR_TITLE, NGR_SUMMARY,NGR_OVERALL_DESIGN ";
	/*final static String query248 = "SELECT NGR_GEO_ID, COUNT(distinct NGL_SAMPLE_FK), NGR_TITLE, NGR_SUMMARY, NGR_TYPE, NGR_OVERALL_DESIGN, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', '), SUB_ARCHIVE_ID " + 
	                                   "FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANA_TIMED_NODE " + 
	                                   "WHERE NGR_GEO_ID = ? " + 
	                                   "AND NGL_SERIES_FK = NGR_OID " +
	                                   "AND NGL_SAMPLE_FK = NGS_OID " +
	                                   "AND NGS_SUBMISSION_FK = SUB_OID " +
	                                   "AND EXP_SUBMISSION_FK=SUB_OID " +
	                                   "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
	                                   "AND ATN_NODE_FK = ANO_OID " +
	                                   "GROUP BY NGR_GEO_ID, NGR_TITLE, NGR_SUMMARY, NGR_TYPE, NGR_OVERALL_DESIGN ";*/
	
	final static String name249 = "NGD_SUBMISSION_SERIES";
	final static String query249 = "SELECT NGR_GEO_ID, NGR_TITLE, NGR_SUMMARY, NGR_CREATED_BY, NGR_OVERALL_DESIGN, NGR_OID " +
	                                "FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION " +
	                                "WHERE SUB_ACCESSION_ID = ? " +
	                                "AND SUB_OID=NGS_SUBMISSION_FK " +
	                                "AND NGL_SAMPLE_FK=NGS_OID " +
	                                "AND NGL_SERIES_FK = NGR_OID";
	
	final static String name250 = "NGD_SAMPLE_NUMBER_OF_SERIES";
	final static String query250 = "SELECT COUNT(NGL_SAMPLE_FK) " +
	                                "FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE " +
	                                "WHERE NGL_SAMPLE_FK=NGS_OID " +
	                                "AND NGL_SERIES_FK = NGR_OID " +
	                                "AND NGR_OID IN" +
	                                " (SELECT NGR_OID " +
	                                "  FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION " +
	                                "  WHERE SUB_ACCESSION_ID = ? " +
	                                "  AND SUB_OID=NGS_SUBMISSION_FK " +
	                                "  AND NGL_SAMPLE_FK=NGS_OID " +
	                                "  AND NGL_SERIES_FK = NGR_OID)";
	
	final static String name251 = "NGD_SERIES_SAMPLE";
	final static String query251 = "select SUB_ACCESSION_ID, NGS_GEO_ID, NGS_SAMPLE_NAME, NGS_DESCRIPTION " +
	                                 "from NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE , ISH_SUBMISSION " +
	                                 "where NGL_SAMPLE_FK = NGS_OID " +
	                                 "and NGL_SERIES_FK = NGR_OID " +
	                                 "and NGS_SUBMISSION_FK = SUB_OID " +
	                                 "and NGR_OID in" +
	                                 " (select NGR_OID " +
	                                 "  from NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION " +
	                                 "  where SUB_ACCESSION_ID = ? " +
	                                 "  and SUB_OID=NGS_SUBMISSION_FK " +
	                                 "  and NGL_SAMPLE_FK=NGS_OID " +
	                                 "  and NGL_SERIES_FK = NGR_OID) ";
	
	final static String name252 = "TOTAL_NUMBER_OF_NGD_EXPERIMENT_NAME";
	final static String query252 = "SELECT COUNT(DISTINCT NGR_TITLE) FROM NGD_SERIES ";

	final static String name253 = "TOTAL_NUMBER_OF_NGD_SAMPLE_NUMBERS";
	final static String query253 = "SELECT COUNT(DISTINCT (SELECT COUNT(*) FROM NGD_SAMPLE_SERIES WHERE NGL_SERIES_FK = NGR_OID)) FROM NGD_SERIES ";
	  
	final static String name254 = "TOTAL_NUMBER_OF_NGD_SERIES_GEO_ID";
	final static String query254 = "SELECT COUNT(DISTINCT NGR_GEO_ID) FROM NGD_SERIES ";

	  // find the number of series available in database
	final static String name255 = "TOTAL_NUMBER_OF_NGD_SERIES";
	final static String query255 = "SELECT COUNT(DISTINCT NGR_OID) FROM NGD_SERIES ";
	  
	final static String name256 = "TOTAL_NUMBER_OF_NGD_SERIES_LAB";
	final static String query256 = "SELECT COUNT(DISTINCT PER_SURNAME) FROM NGD_SERIES ";
	
	final static String name257 = "TOTAL_NGD_SAMPLES";
    final static String query257 = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) " + NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name258 = "TOTAL_NGD_GEO_IDS";
    final static String query258 = "SELECT COUNT(DISTINCT NGS_GEO_ID) " + NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name259 = "TOTAL_NGD_SAMPLE_IDS";
    final static String query259 = "SELECT COUNT(DISTINCT NGS_SAMPLE_NAME) " + NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name260 = "TOTAL_NGD_SAMPLE_DESCRIPTIONS";
    final static String query260 = "SELECT COUNT(DISTINCT NGS_DESCRIPTION) " + NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    // xingjun - 03/03/2010 - for time being use the same sql as TOTAL_SAMPLE_DESCRIPTIONS
    final static String name261 = "TOTAL_NGD_COMPONENT";
    final static String query261 = "SELECT COUNT (DISTINCT IST_COMPONENT) FROM NGD_SAMPLE, NGD_SAMPLE_SERIES, " +
    		"NGD_SERIES, ISH_SUBMISSION, ISH_SP_TISSUE, ANA_NODE, ANA_TIMED_NODE "  +            
             "WHERE NGL_SERIES_FK = NGR_OID "+      
                        "AND NGL_SAMPLE_FK = NGS_OID "+  
                        "AND NGS_SUBMISSION_FK = SUB_OID "+
                        "AND IST_SUBMISSION_FK=SUB_OID "+  
                        "AND ATN_PUBLIC_ID = IST_COMPONENT "+  
                        "AND ATN_NODE_FK = ANO_OID "+  
                        "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
    
    final static String name262 = "TOTAL_NGD_LIBRARY_STRATEGY";
    final static String query262 = "SELECT COUNT(DISTINCT NGP_LIBRARY_STRATEGY) " + NGD_SAMPLE_SERIES_TABS_WITHOUT_GROUP_BY_CLAUSE;
    
    final static String name263 = "SUBMISSION_SUPPLEMENTARY_FILES_NGD";
    final static String query263 = "SELECT DISTINCT NGF_FILEPATH,NGF_FILENAME, NGF_RAW, NGF_FILESIZE " +
                                  "FROM NGD_FILES " +
                                  "JOIN NGD_SAMPLE ON NGF_SAMPLE_FK=NGS_OID " +
                                  "JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ? ";
    
    /*final static String query263 = "SELECT DISTINCT NULL,NULL, NULL, NULL, COUNT(*) AS RECORDCOUNT " +
            "FROM NGD_FILES " +
            "JOIN NGD_SAMPLE ON NGF_SAMPLE_FK=NGS_OID " +
            "JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ? " +
            "UNION ALL "+
            "SELECT DISTINCT NGF_FILEPATH,NGF_FILENAME, NGF_RAW, NGF_FILESIZE, NULL " +
            "FROM NGD_FILES " +
            "JOIN NGD_SAMPLE ON NGF_SAMPLE_FK=NGS_OID " +
            "JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID AND SUB_ACCESSION_ID = ? " ;*/
    
    final static String name264 = "SUBMISSION_SAMPLE_NGD";
    final static String query264 = "SELECT NGS_GEO_ID, NGS_DESCRIPTION, NGS_SAMPLE_NAME, NGS_SPECIES, NGS_STRAIN, NGS_GENOTYPE, " +
    		"NGS_SEX, CASE WHEN NGS_STAGE_FORMAT='dpc' THEN CONCAT(NGS_DEV_STAGE,NGS_STAGE_FORMAT) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END age, " +
    		"STG_STAGE_DISPLAY, NGS_POOLED_SAMPLE, NGS_POOL_SIZE, NGS_EXPERIMENTAL_METHOD, NGS_SAMPLE_NOTES, " +
    		"NGS_LIBRARY_POOL_SIZE, NGS_LIBRARY_READS, NGS_READ_LENGTH, NGS_MEAN_INSERT_SIZE " +
    		"FROM NGD_SAMPLE " +
    		"JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID " +
    		"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
    		"WHERE SUB_ACCESSION_ID = ? ";
    
    
    final static String name265 = "PROTOCOL_SAMPLE_NGD";
    final static String query265 = "SELECT NGP_NAME, NGP_LIBRARY_CON_PROT, NGP_LIBRARY_STRATEGY, NGP_EXTRACTED_MOLECULE, NGP_RNA_ISOLATION_METHOD, NGP_SEQUENCING_METHOD, " +
    		"NGP_LABEL_METHOD,NGP_INSTRUMENT_MODEL, " +
    		"NGP_PAIRED_END, NGP_PROT_ADDITIONAL_NOTES, NGP_DNA_EXTRACT_METHOD, NGP_ANTIBODY, CONCAT(USR_FORENAME,' ',USR_SURNAME) CREATED_BY " +
    		"FROM NGD_PROTOCOL " +
    		"JOIN NGD_SAMPLE ON NGS_PROTOCOL_FK = NGP_OID " +
    		"JOIN ISH_SUBMISSION ON NGS_SUBMISSION_FK = SUB_OID " +
    		"JOIN REF_USER ON USR_OID=NGP_CREATED_BY " +
    		"WHERE SUB_ACCESSION_ID = ? ";
        
    final static String name266 = "DATA_PROCESSING_SAMPLE_NGD";
    final static String query266 = "SELECT NGC_PRO_STEP, NGC_BUILD, NGC_ALIGNED_GENOME, NGC_UNALIGNED_GENOME, NGC_RNA_READS, NGC_5_3_RATIO, " +
    		"NGC_FORMAT_CONTENT,NGF_NUMBER_OF_READS, NGF_BEFORE_CLEAN_UP_READS, NGF_PAIRED_END, NGF_FILENAME, NGF_FILETYPE, NGF_RAW " +
    		"FROM ISH_SUBMISSION " +
    		"JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID " +
    		"JOIN NGD_FILES ON NGF_SAMPLE_FK = NGS_OID " +
    		"LEFT OUTER JOIN NGD_DATA_PROCESSING ON NGC_FILE_FK=NGF_OID " +
    		"WHERE SUB_ACCESSION_ID = ? " +
    		"ORDER BY NGF_RAW DESC, NGF_OID ASC ";
    
    final static String name267 = "NGD_IMAGES";
    final static String query267 = "SELECT SUB_ACCESSION_ID, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_ACCESSION_ID = ? AND  IMG_SUBMISSION_FK = SUB_OID  JOIN REF_URL I ON I.URL_OID = IMG_URL_FK JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK WHERE SUB_IS_DELETED = 0 AND IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE='NextGen' ORDER BY IMG_ORDER ";
   
	
  final static String name = "";
  final static String query = "";
    
  
  // creates an array of all ParamQueries
  static ParamQuery pqList[] = {
      new ParamQuery(name0, query0),
      new ParamQuery(name1,query1),
      new ParamQuery(name2,query2),
      new ParamQuery(name3,query3),
      new ParamQuery(name4,query4),
      new ParamQuery(name5,query5),
      new ParamQuery(name6,query6),
      new ParamQuery(name7,query7),
      new ParamQuery(name8,query8),
      new ParamQuery(name9,query9),
      new ParamQuery(name10,query10),
      new ParamQuery(name11,query11),
      new ParamQuery(name13,query13),
      new ParamQuery(name14,query14),
      new ParamQuery(name15,query15),
      new ParamQuery(name16,query16),
      new ParamQuery(name17,query17),
      new ParamQuery(name18,query18),
      new ParamQuery(name19,query19),
      new ParamQuery(name20,query20),
      new ParamQuery(name21,query21),
      new ParamQuery(name23,query23),
      new ParamQuery(name24,query24),
      new ParamQuery(name25,query25),
      new ParamQuery(name26,query26),
      new ParamQuery(name27,query27),
      new ParamQuery(name28,query28),
      new ParamQuery(name30,query30),
      new ParamQuery(name31,query31),
      new ParamQuery(name32,query32),
      new ParamQuery(name33,query33),
      new ParamQuery(name35,query35),
      new ParamQuery(name36,query36),
      new ParamQuery(name38,query38),
      new ParamQuery(name39,query39),
      new ParamQuery(name40,query40),
      new ParamQuery(name41,query41),
      new ParamQuery(name42,query42),
      new ParamQuery(name43,query43),
      new ParamQuery(name44,query44),
      new ParamQuery(name45,query45),
      new ParamQuery(name46,query46),
      new ParamQuery(name47,query47),
      new ParamQuery(name50,query50),
      new ParamQuery(name51,query51),
      new ParamQuery(name61,query61),
      new ParamQuery(name62,query62),
      new ParamQuery(name63,query63),
      new ParamQuery(name64,query64),
      new ParamQuery(name65,query65),
      new ParamQuery(name66,query66),
      new ParamQuery(name67,query67),
      new ParamQuery(name68,query68),
      new ParamQuery(name69,query69),
      new ParamQuery(name70,query70),
      new ParamQuery(name71,query71),
      new ParamQuery(name72,query72),
      new ParamQuery(name73,query73),
      new ParamQuery(name74,query74),
      new ParamQuery(name75,query75),
      new ParamQuery(name76,query76),
      new ParamQuery(name77,query77),
      new ParamQuery(name78,query78),
      new ParamQuery(name79,query79),
      new ParamQuery(name80,query80),
      new ParamQuery(name81,query81),
      new ParamQuery(name82,query82),
      new ParamQuery(name83,query83),
      new ParamQuery(name84,query84),
      new ParamQuery(name85,query85),
      new ParamQuery(name86,query86),
      new ParamQuery(name87,query87),
      new ParamQuery(name88,query88),
      new ParamQuery(name89,query89),
      new ParamQuery(name90,query90),
      new ParamQuery(name91,query91),
      new ParamQuery(name92,query92),
      new ParamQuery(name93,query93),
      new ParamQuery(name94,query94),
      new ParamQuery(name95,query95),
      new ParamQuery(name96,query96),
      new ParamQuery(name97,query97),
      new ParamQuery(name99,query99),
      new ParamQuery(name100,query100),
      new ParamQuery(name101,query101),
      new ParamQuery(name102,query102),
      new ParamQuery(name103,query103),
      new ParamQuery(name105,query105),
      new ParamQuery(name107,query107),
      new ParamQuery(name109,query109),
      new ParamQuery(name110,query110),
      new ParamQuery(name111,query111),
      new ParamQuery(name112,query112),
      new ParamQuery(name113,query113),
      new ParamQuery(name114,query114),
      new ParamQuery(name115,query115),
      new ParamQuery(name116,query116),
      new ParamQuery(name117,query117),
      new ParamQuery(name118,query118),
      new ParamQuery(name121,query121),
      new ParamQuery(name122,query122),
      new ParamQuery(name124,query124),
      new ParamQuery(name125,query125),
      new ParamQuery(name126,query126),
      new ParamQuery(name127,query127),
      new ParamQuery(name128,query128),
      new ParamQuery(name129,query129),
      new ParamQuery(name130,query130),
      new ParamQuery(name131,query131),
      new ParamQuery(name132,query132),
      new ParamQuery(name133,query133),
      new ParamQuery(name134,query134),
      new ParamQuery(name135,query135),
      new ParamQuery(name136,query136),
      new ParamQuery(name138,query138),
      new ParamQuery(name140,query140),
      new ParamQuery(name141,query141),
      new ParamQuery(name142,query142),
      new ParamQuery(name143,query143),
      new ParamQuery(name144,query144),
      new ParamQuery(name145,query145),
      new ParamQuery(name146,query146),
      new ParamQuery(name147,query147),
      new ParamQuery(name148,query148),
      new ParamQuery(name149,query149),
      new ParamQuery(name150,query150),
      new ParamQuery(name151,query151),
      new ParamQuery(name152,query152),
      new ParamQuery(name153,query153),
      new ParamQuery(name154,query154),
      new ParamQuery(name155,query155),
      new ParamQuery(name157,query157),
      new ParamQuery(name158,query158),
      new ParamQuery(name159,query159),
      new ParamQuery(name160,query160),
      new ParamQuery(name161,query161),
      new ParamQuery(name162,query162),
      new ParamQuery(name163,query163),
      new ParamQuery(name164,query164),
      new ParamQuery(name165,query165),
      new ParamQuery(name166,query166),
      new ParamQuery(name167,query167),
      new ParamQuery(name168,query168),
      new ParamQuery(name169,query169),
      new ParamQuery(name170,query170),
      new ParamQuery(name171,query171),
      new ParamQuery(name172,query172),
      new ParamQuery(name174,query174),
      new ParamQuery(name173,query173),
      new ParamQuery(name175,query175),
      new ParamQuery(name176,query176),
      new ParamQuery(name177,query177),
      new ParamQuery(name178,query178),
      new ParamQuery(name179,query179),
      new ParamQuery(name180,query180),
      new ParamQuery(name181,query181),
      new ParamQuery(name182,query182),
      new ParamQuery(name183,query183),
      new ParamQuery(name184,query184),
      new ParamQuery(name185,query185),
      new ParamQuery(name186,query186),
      new ParamQuery(name187,query187),
      new ParamQuery(name188,query188),
      new ParamQuery(name189,query189),
      new ParamQuery(name190,query190),
      new ParamQuery(name191,query191),
      new ParamQuery(name192,query192),
      new ParamQuery(name193,query193),
      new ParamQuery(name194,query194),
      new ParamQuery(name195,query195),
      new ParamQuery(name196,query196),
      new ParamQuery(name197,query197),
      new ParamQuery(name198,query198),
      new ParamQuery(name199,query199),
      new ParamQuery(name200,query200),
      new ParamQuery(name201,query201),
      new ParamQuery(name202,query202),
      new ParamQuery(name203,query203),
      new ParamQuery(name204,query204),
      new ParamQuery(name205,query205),
      new ParamQuery(name206,query206),
      new ParamQuery(name207,query207),
      new ParamQuery(name208,query208),
      new ParamQuery(name209,query209),
      new ParamQuery(name210,query210),
      new ParamQuery(name211,query211),
      new ParamQuery(name212,query212),
      new ParamQuery(name213,query213),
      new ParamQuery(name214,query214),
      new ParamQuery(name215,query215),
      new ParamQuery(name216,query216),
      new ParamQuery(name217,query217),
      new ParamQuery(name218,query218),
      new ParamQuery(name219,query219),
      new ParamQuery(name221,query221),
      new ParamQuery(name222,query222),
      new ParamQuery(name225,query225),
      new ParamQuery(name226,query226),
      new ParamQuery(name227,query227),
      new ParamQuery(name228,query228),
      new ParamQuery(name229,query229),
      new ParamQuery(name230,query230),
      new ParamQuery(name232,query232),
      new ParamQuery(name233,query233),
      new ParamQuery(name234,query234),
      new ParamQuery(name235,query235),
      new ParamQuery(name236,query236),
      new ParamQuery(name237,query237),
      new ParamQuery(name238,query238),
      new ParamQuery(name239,query239),
      new ParamQuery(name240,query240),
      new ParamQuery(name241,query241),
      new ParamQuery(name242,query242),
      new ParamQuery(name243,query243),
      new ParamQuery(name244,query244),
      new ParamQuery(name245,query245),
      new ParamQuery(name246,query246), 
      new ParamQuery(name247,query247),
      new ParamQuery(name248,query248),
      new ParamQuery(name249,query249),
      new ParamQuery(name250,query250),
      new ParamQuery(name251,query251),
      new ParamQuery(name252,query252), 
      new ParamQuery(name253,query253),
      new ParamQuery(name254,query254),
      new ParamQuery(name255,query255),
      new ParamQuery(name256,query256),
      new ParamQuery(name257,query257),
      new ParamQuery(name258,query258), 
      new ParamQuery(name259,query259),
      new ParamQuery(name260,query260),
      new ParamQuery(name261,query261),
      new ParamQuery(name262,query262),
      new ParamQuery(name263,query263),
      new ParamQuery(name264,query264),
      new ParamQuery(name265,query265),
      new ParamQuery(name266,query266),
      new ParamQuery(name267,query267),
      new ParamQuery(name268,query268),
      new ParamQuery(name269,query269),
      new ParamQuery(name270,query270),
      new ParamQuery(name271,query271),
      new ParamQuery(name272,query272),
      new ParamQuery(name273,query273),
      new ParamQuery(name274,query274),
      new ParamQuery(name275,query275),
      new ParamQuery(name276,query276),
      new ParamQuery(name277,query277),
      new ParamQuery(name278,query278),
      new ParamQuery(name279,query279),
      new ParamQuery(name280,query280)
   };

  // finds ParamQuery object by name and returns
  public static ParamQuery getParamQuery(String name) {
      ParamQuery ret = null;

	  for (int i = 0; i < pqList.length; i++) {
		  if (pqList[i].getQueryName().equals(name)) {
			  ret = pqList[i];

			  if (debug)
			      System.out.println("sql = "+  ret.getQuerySQL());

			  break;
		  }
      }
      return ret;
  }

  // query to find edit history of submissions
  static String submissionHistoryQuery = "SELECT SBH_ACCESSION_ID, " +  // 0
  		                                 "SBH_MODIFIED_ITEM, " +        // 1
  		                                 "SBH_OLD_VALUE, " +            // 2
  		                                 "SBH_NEW_VALUE, " +            // 3
  		                                 "SBH_OLD_VER_NO, " +           // 4
  		                                 "SBH_NEW_VER_NO, " +           // 5
  		                                 "SBH_MODIFIED_BY, " +          // 6
  		                                 "SBH_MODIFIED_TIME " +        // 7
  		                                 "FROM REF_SUBMISSION_HISTORY";

    public static String getComponentRelations(int numEnteredComps, String col1, String col2) {
    	
    	StringBuffer componentRealtionsQ = new StringBuffer("SELECT DISTINCT "+col1 + 
    			" FROM ANA_TIMED_NODE DESCEND_ATN " +
    			"JOIN ANAD_RELATIONSHIP_TRANSITIVE " +
    			"ON DESCEND_ATN.ATN_NODE_FK = RTR_DESCENDENT_FK " +
    			"JOIN ANA_TIMED_NODE ANCES_ATN " +
    			"ON ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
    			"WHERE ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
    			"AND "+col2+" IN (");
    	
    	for(int i=0;i<numEnteredComps;i++){
        	if(i == numEnteredComps-1){
        		componentRealtionsQ.append("?)");
        	}
        	else {
        		componentRealtionsQ.append("?, ");
        	}
        }
    	
    	return componentRealtionsQ.toString();
    }
    
    public static String getTimedComponentsFromSynsAndIdsQuery() {
    	
    	String compsFromSynsAndIdsQ = "SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ANO_COMPONENT_NAME = ?" +
    			" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE, ANA_SYNONYM WHERE SYN_OBJECT_FK = ANO_OID AND ANO_OID = ATN_NODE_FK AND SYN_SYNONYM = ?" +
    			"  UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ATN_PUBLIC_ID = ?" +
    			" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ANO_PUBLIC_ID = ?";
    	
    	return compsFromSynsAndIdsQ;
    }
    
    public static String getComponentsForSynsAndIdsQuery(int numEnteredComps) {
    	StringBuffer compsFromSynsAndIdsQ = new StringBuffer("SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ANO_COMPONENT_NAME IN (");
    	
        for(int i=0;i<numEnteredComps;i++){
        	if(i == numEnteredComps-1){
        		compsFromSynsAndIdsQ.append("?)");
        	}
        	else {
        		compsFromSynsAndIdsQ.append("?, ");
        	}
        }
        compsFromSynsAndIdsQ.append(" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE, ANA_SYNONYM WHERE SYN_OBJECT_FK = ANO_OID AND ANO_OID = ATN_NODE_FK AND SYN_SYNONYM IN (");
        
        for(int i=0;i<numEnteredComps;i++){
        	if(i == numEnteredComps-1){
        		compsFromSynsAndIdsQ.append("?)");
        	}
        	else {
        		compsFromSynsAndIdsQ.append("?, ");
        	}
        }
        
        compsFromSynsAndIdsQ.append(" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ATN_PUBLIC_ID IN (");
        for(int i=0;i<numEnteredComps;i++){
        	if(i == numEnteredComps-1){
        		compsFromSynsAndIdsQ.append("?)");
        	}
        	else {
        		compsFromSynsAndIdsQ.append("?, ");
        	}
        }
        
        compsFromSynsAndIdsQ.append(" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ANO_PUBLIC_ID IN (");
        for(int i=0;i<numEnteredComps;i++){
        	if(i == numEnteredComps-1){
        		compsFromSynsAndIdsQ.append("?)");
        	}
        	else {
        		compsFromSynsAndIdsQ.append("?, ");
        	}
        }
        
        return compsFromSynsAndIdsQ.toString();
    }

    /**
     * @param nodeIds - abstract organism ids found in ANA_NODE tbl of db
     */
    public static String getAnatPubIdsFromNodeIdsQuery(String [] nodeIds) {
        String anatPubIdsFromNodeIdsQuery = "SELECT ATN_PUBLIC_ID " +
            "FROM ANA_TIMED_NODE, ANA_STAGE tns, ANA_STAGE qs1, ANA_STAGE qs2 " +
            "WHERE qs1.STG_NAME = ? " +
            "AND qs2.STG_NAME = ? " +
            "AND tns.STG_OID = ATN_STAGE_FK " +
            "AND tns.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE " +
            "AND  ATN_NODE_FK IN (";

        StringBuffer nodeparams = new StringBuffer("");

        for (int i = 0; i < nodeIds.length; i++) {
            if (i == nodeIds.length - 1) {
                nodeparams.append("?");
            }
            else {
                nodeparams.append("?, ");
            }
        }
        nodeparams.append(")");

        anatPubIdsFromNodeIdsQuery += nodeparams.toString();
        
        return anatPubIdsFromNodeIdsQuery;
    }
    
    public static String getExprInAnySelComponentsQ(String [] publicIds, String [] posAnnoTypes){
        StringBuffer genesExpressed = new StringBuffer("EXP_STRENGTH IN (");
        
        StringBuffer annotationParams = new StringBuffer("");
        for(int i=0;i<posAnnoTypes.length;i++){
            if (i == posAnnoTypes.length - 1) {
                annotationParams.append("'"+posAnnoTypes[i]+"') ");
            }
            else {
                annotationParams.append("'"+posAnnoTypes[i]+"', ");
            }
        }
        
        genesExpressed.append(annotationParams);
        genesExpressed.append("AND EXP_COMPONENT_ID in " +
                              "(select DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
                              "from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
                              "ANA_TIMED_NODE DESCEND_ATN " +
                              "where ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
                              "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
                              "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
                              "and ANCES_ATN.ATN_PUBLIC_ID IN (");

        StringBuffer publicIdparams = new StringBuffer("");
        for (int i = 0; i < publicIds.length; i++) {
            if (i == publicIds.length - 1) {
                publicIdparams.append("?");
            }
            else {
                publicIdparams.append("?, ");
            }
        }
        publicIdparams.append(")) ");
        
        genesExpressed.append(publicIdparams);
        
        return genesExpressed.toString();
    }
    
    public static String getNotDetExprInAnySelComponentsQ(String [] publicIds){
        StringBuffer genesNotDetected = new StringBuffer(" EXP_STRENGTH = 'not detected' " +
                                                         "AND EXP_COMPONENT_ID in "+
                                                         "(select DISTINCT ANCES_ATN.ATN_PUBLIC_ID " +
                                                         "from ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
                                                         "ANA_TIMED_NODE DESCEND_ATN " +
                                                         "where ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
                                                         "and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
                                                         "and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
                                                         "and DESCEND_ATN.ATN_PUBLIC_ID IN (");
        StringBuffer publicIdparams = new StringBuffer("");
        for (int i = 0; i < publicIds.length; i++) {
            if (i == publicIds.length - 1) {
                publicIdparams.append("?");
            }
            else {
                publicIdparams.append("?, ");
            }
        }
        publicIdparams.append(")) ");
        
        genesNotDetected.append(publicIdparams);
        
        return genesNotDetected.toString();
        
    }
    
    public static String getExprInAllSelComponentsQ(String [] publicIds, String [] posAnnoTypes){
        StringBuffer genesExpressed = new StringBuffer(
            "SELECT COUNT(DISTINCT ANCES_ATN.ATN_NODE_FK) " + 
            "FROM ISH_EXPRESSION, ANA_TIMED_NODE DESCEND_ATN, ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE SUB_STG, ANA_STAGE QS1, ANA_STAGE QS2 " + 
            "WHERE EXP_STRENGTH IN (");
       
        StringBuffer annotationParams = new StringBuffer("");
        for(int i=0;i<posAnnoTypes.length;i++){
            if (i == posAnnoTypes.length - 1) {
                annotationParams.append("'"+posAnnoTypes[i]+"') ");
            }
            else {
                annotationParams.append("'"+posAnnoTypes[i]+"', ");
            }
        }
        
        genesExpressed.append(annotationParams);
        
        genesExpressed.append(                         
            "AND EXP_COMPONENT_ID = DESCEND_ATN.ATN_PUBLIC_ID " + 
            "AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + 
            "AND ANCES_ATN.ATN_NODE_FK IN (");
            
        StringBuffer publicIdparams = new StringBuffer("");
        for (int i = 0; i < publicIds.length; i++) {
            if (i == publicIds.length - 1) {
                publicIdparams.append("?");
            }
            else {
                publicIdparams.append("?, ");
            }
        }
        publicIdparams.append(") ");
        
        genesExpressed.append(publicIdparams);
        
        genesExpressed.append(
            "AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " + 
            "AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " + 
            "AND SUB_STG.STG_OID = DESCEND_ATN.ATN_STAGE_FK " + 
            "AND QS1.STG_NAME = ? " + 
            "AND QS2.STG_NAME = ? " + 
            "AND SUB_STG.STG_SEQUENCE BETWEEN QS1.STG_SEQUENCE AND QS2.STG_SEQUENCE " + 
            "AND EXP_SUBMISSION_FK = SUB_OID)");
            
        return genesExpressed.toString();
    }
    
    public static String getNotDetExprInAllSelComponentsQ(String [] publicIds) {
        StringBuffer genesNotDetected = new StringBuffer(
            "SELECT COUNT(DISTINCT DESCEND_ATN.ATN_NODE_FK) " + 
            "FROM ISH_EXPRESSION, ANA_TIMED_NODE DESCEND_ATN, ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE SUB_STG, ANA_STAGE QS1, ANA_STAGE QS2 " + 
            "WHERE EXP_STRENGTH = 'not detected' " + 
            "AND EXP_COMPONENT_ID = ANCES_ATN.ATN_PUBLIC_ID " + 
            "AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " + 
            "AND DESCEND_ATN.ATN_NODE_FK IN (");
            
        StringBuffer publicIdparams = new StringBuffer("");
        for (int i = 0; i < publicIds.length; i++) {
            if (i == publicIds.length - 1) {
                publicIdparams.append("?");
            }
            else {
                publicIdparams.append("?, ");
            }
        }
        publicIdparams.append(") ");
        
        genesNotDetected.append(publicIdparams);
        
        genesNotDetected.append(
        " AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " + 
        "AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " + 
        "AND SUB_STG.STG_OID = ANCES_ATN.ATN_STAGE_FK " + 
        "AND QS1.STG_NAME = ? " + 
        "AND QS2.STG_NAME = ? " + 
        "AND SUB_STG.STG_SEQUENCE BETWEEN QS1.STG_SEQUENCE AND QS2.STG_SEQUENCE " + 
        "AND EXP_SUBMISSION_FK = SUB_OID) ");
        
        return genesNotDetected.toString();
    }
}
