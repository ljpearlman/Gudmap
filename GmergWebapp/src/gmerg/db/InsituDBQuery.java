/**
 * this file stores all sqls linked to insitu data
 */
package gmerg.db;

import java.util.ResourceBundle;
import gmerg.utils.Utility;

/**
 * @author xingjun
 *
 */
public class InsituDBQuery {
    protected static boolean debug = false;
	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");

	// find the distribution of theiler stage of the insitu data
	final static String name0 = "GENE_THEILER_STAGES_INSITU";
	final static String query0 = "SELECT DISTINCT STG_STAGE_DISPLAY FROM ISH_SUBMISSION " +
			"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
			"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
			"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
			"WHERE SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
			"AND RPR_SYMBOL = ? " +
			"ORDER BY NATURAL_SORT(STG_STAGE_DISPLAY)";

	// find equivalent dpc value for given theiler stage
	final static String name1 = "EQUIVALENT_DPC_STAGE_FOR_THEILER_STAGE";
	final static String query1 = "SELECT STG_DPC_PREFIX, STG_DPC_VALUE FROM REF_STAGE WHERE STG_VALUE = ?";
	  
	// find number of gene involved in the submission
	// in situ
	final static String name2 = "NUMBER_OF_INVOLVED_GENE";
	final static String query2 = "SELECT COUNT(DISTINCT RPR_SYMBOL) FROM REF_PROBE " +
			"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"AND SUB_ASSAY_TYPE = ? ";
	
	// tg
	final static String name16 = "NUMBER_OF_INVOLVED_GENE_TG";
	final static String query16 = "SELECT COUNT(DISTINCT ALE_GENE) FROM ISH_ALLELE " +
			"JOIN LNK_SUB_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
			"JOIN ISH_SUBMISSION ON SAL_SUBMISSION_FK = SUB_OID " +
			"WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"AND SUB_ASSAY_TYPE = ? " +
			"AND SAL_ORDER = 1 ";
	
	// find number of gene involved in the submission
		// WISH/SISH/OPT
	final static String name17 = "NUMBER_OF_INVOLVED_GENE_ISH_TYPES";
	final static String query17 = "SELECT COUNT(DISTINCT RPR_SYMBOL) TOTAL FROM REF_PROBE JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
								  "JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID JOIN ISH_SPECIMEN ON SPN_SUBMISSION_FK = SUB_OID WHERE " +
								  "SUB_IS_DELETED = '0' AND SUB_IS_PUBLIC = '1' AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE = 'ISH' AND SPN_ASSAY_TYPE = ? ";
	
	/*final static String name3 = "INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID";
	final static String query3 = "SELECT SUB_ACCESSION_ID, SUB_EMBRYO_STG, SPN_ASSAY_TYPE, CONCAT(I.URL_URL, IMG_FILEPATH, 'medium/',IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) " +
			"FROM ISH_SUBMISSION " +
			"JOIN ISH_ORIGINAL_IMAGE ON IMG_SUBMISSION_FK = SUB_OID " +
			"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " +
			"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " +
			"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
			"WHERE IMG_TYPE NOT LIKE '%wlz%' AND CONCAT(SUB_OID, '_', IMG_FILENAME) IN " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"ORDER BY SUB_EMBRYO_STG, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";*/
	final static String name3 = "INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID";
	final static String query3 = "SELECT SUB_ACCESSION_ID, STG_STAGE_DISPLAY, SPN_ASSAY_TYPE, CONCAT(I.URL_URL, IMG_FILEPATH, 'medium/',IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME), CONCAT(SUB_OID,'_',IMG_FILENAME) " +
			"FROM ISH_SUBMISSION " +
			"JOIN ISH_ORIGINAL_IMAGE ON IMG_SUBMISSION_FK = SUB_OID " +
			"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " +
			"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " +
			"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
			"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
			"WHERE IMG_TYPE NOT LIKE '%wlz%' AND CONCAT(SUB_OID, '_', IMG_FILENAME) IN " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"ORDER BY STG_STAGE_DISPLAY, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";
	
	final static String name11 = "INSITU_SUBMISSION_IMAGE_ID_BY_GENE_SYMBOL";
	final static String query11 = "SELECT CONCAT(SUB_OID, '_', IMG_FILENAME) FROM ISH_ORIGINAL_IMAGE " +
			"JOIN ISH_SUBMISSION ON IMG_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
			"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
			"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
			"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
			"WHERE RPR_SYMBOL = ? AND IMG_TYPE NOT LIKE '%wlz%' " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"ORDER BY STG_STAGE_DISPLAY, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";
	  
	final static String name12 = "GET_RELEVANT_IMAGE_IDS";
	final static String query12 = "SELECT CONCAT(SUB_OID, '_', IMG_FILENAME) FROM ISH_ORIGINAL_IMAGE " +
			"JOIN ISH_SUBMISSION ON IMG_SUBMISSION_FK = SUB_OID " +
			"WHERE CONCAT(SUB_OID, '_', IMG_FILENAME) IN  AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK <= ? " +
			"ORDER BY NATURAL_SORT(CONCAT(SUB_OID, '_', IMG_FILENAME))";
	  
	// modified by xingjun - 24/09/2009 - appended query to get microarray related gene
	// ish symbol + ish synonym + array symbol + array synonym (added on 09/10/2009)
	final static String name4 = "GENE_SYMBOLS_AND_SYNONYMS";
	final static String query4 = "(SELECT DISTINCT RPR_SYMBOL GENE FROM REF_PROBE " +
			"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"WHERE RPR_SYMBOL <> '' " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC' OR SUB_ASSAY_TYPE = 'TG') " +
			"AND RPR_SYMBOL LIKE ?) " + // 1
			"UNION " +
			"(SELECT DISTINCT RSY_SYNONYM GENE FROM REF_SYNONYM " +
			"JOIN REF_MGI_MRK ON RSY_REF = RMM_ID " +
			"JOIN REF_PROBE ON RMM_SYMBOL = RPR_SYMBOL " +
			"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"WHERE RSY_SYNONYM <> '' " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC' OR SUB_ASSAY_TYPE = 'TG') " +
			"AND RSY_SYNONYM LIKE ?) " + // 2
			"UNION " +
			"(SELECT DISTINCT GNF_SYMBOL GENE " +
			"FROM REF_GENE_INFO WHERE GNF_SYMBOL LIKE ?) " + // 3
			"UNION " +
			"(SELECT DISTINCT RSY_SYNONYM GENE FROM REF_SYNONYM " +
			"JOIN REF_MGI_MRK ON RSY_REF = RMM_ID " +
			"JOIN REF_GENE_INFO ON RMM_SYMBOL = GNF_SYMBOL WHERE RSY_SYNONYM LIKE ?) " + // 4
			"ORDER BY NATURAL_SORT(GENE) " +
			"LIMIT ? "; // 5
	  
	final static String name5 = "ANNATOMY_TERMS";
	// query to find out anatomical terms in cache tables
	final static String query5 = "SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE " +
			"JOIN ANAD_PART_OF_PERSPECTIVE ON ANO_OID = POP_NODE_FK " +
			"WHERE POP_PERSPECTIVE_FK = '" + Utility.getPerspective() + "' " +
			"AND ANO_COMPONENT_NAME LIKE ? " +
			"ORDER BY ANO_COMPONENT_NAME LIMIT ? ";


	final static String name6 = "GO_TERMS";
	final static String query6 = "SELECT DISTINCT GOT_TERM FROM REF_GO_TERM " +
			"JOIN REF_MGI_GOGENE ON GOT_ID = GOG_TERM " +
			"JOIN REF_PROBE ON GOG_MRK_ACC = RPR_LOCUS_TAG " +
			"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
			"AND GOT_TERM LIKE ? " +
			"LIMIT ?";
	  
	final static String name7 = "GET_GENE_SYMBOL_BY_SYNONYM";
	final static String query7 = "SELECT RMM_SYMBOL FROM REF_MGI_MRK " +
			"JOIN REF_SYNONYM ON RMM_ID = RSY_REF " +
			"WHERE RSY_SYNONYM = ? ";
	  
	final static String name8 = "GET_GENE_SYMBOL_BY_MGI_ID";
	final static String query8 = "SELECT RPR_SYMBOL FROM REF_PROBE WHERE RPR_LOCUS_TAG = ? ";
	  
	final static String name9 = "APPLICATION_VERSION";
	final static String query9 = "SELECT MIS_SOFT_VERSION FROM REF_MISC ";
	  
	final static String name10 = "GET_RELEVANT_SUBMISSION_IDS";
	final static String query10 = "SELECT SUB_ACCESSION_ID FROM ISH_SUBMISSION " +
			"WHERE SUB_ACCESSION_ID IN AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK <= ? " +
			"ORDER BY NATURAL_SORT(SUB_ACCESSION_ID)";
	  
	final static String name13 = "CHROME_DETAIL_BY_SYMBOL";
	final static String query13 = "SELECT DISTINCT RPR_SYMBOL, REG_CHROME_NAME, REG_CHROM_START, REG_CHROM_END " +
			"FROM REF_PROBE " +
			"JOIN REF_ENS_GENE ON RPR_ENSEMBL = REG_STABLE " +
			"WHERE RPR_SYMBOL = ? ";
	
	final static String BROWSE_ALL_TABLES_TG = "FROM ISH_SUBMISSION " +
	"JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID " +
	"JOIN ISH_ALLELE ON SAL_ALE_OID_FK=ALE_OID " +
	"JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
	"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
	"JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " +
"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
	"LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
	"LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
	"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +                                                  
	"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
	"AND IMG_ORDER = (SELECT MIN(I.IMG_ORDER) FROM ISH_ORIGINAL_IMAGE I WHERE I.IMG_SUBMISSION_FK = SUB_OID AND I.IMG_TYPE NOT LIKE '%wlz%') " +
	"JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = IMG_URL_FK " +
	"AND SAL_ORDER = 1 ";
	
	
	final static String[] TG_BROWSE_ALL_SQL_COLUMNS = {
		"ALE_GENE AS RPR_SYMBOL",
		"SUB_ACCESSION_ID",
		"SUB_SOURCE",
		"SUB_SUB_DATE",
		"IF(SUB_CONTROL=0,SUB_ASSAY_TYPE,CONCAT(SUB_ASSAY_TYPE,' control')) SUB_ASSAY_TYPE",
		"'' RPR_JAX_ACC",
//		"SUB_EMBRYO_STG",
		"STG_STAGE_DISPLAY",
		"TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) AGE",
		"SPN_SEX",
		"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE",
		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME)",
		"(SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID) EXP_STRENGTH",
		"SPN_ASSAY_TYPE",
		"CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME) THUMBNAIL",
		"REPLACE(SUB_ACCESSION_ID, ':', 'no')",
	};
	
    final static String getTG_BROWSE_ALL_COLUMNS() {
  	  String s = "SELECT DISTINCT ";
  	  for (int i=0; i<TG_BROWSE_ALL_SQL_COLUMNS.length; i++) {
  		  if (i > 0)
  			  s += ", ";
  		  s +=  TG_BROWSE_ALL_SQL_COLUMNS[i] ;
  	  }
  	  s += " ";
  	  return s;
    }
    
	final static String PUBLIC_ENTRIES_Q = " WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";

	final static String name14 = "ALL_ENTRIES_TG";
	final static String query14 = getTG_BROWSE_ALL_COLUMNS() + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q;

	final static String NUMBER_OF_SUBMISSION = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) ";
	
	final static String NUMBER_OF_GENE_SYMBOL = "SELECT COUNT(DISTINCT RPR_SYMBOL) ";
	
	final static String NUMBER_OF_GENE_SYMBOL_TG = "SELECT COUNT(DISTINCT ALE_GENE) ";
	
	final static String NUMBER_OF_THEILER_STAGE = "SELECT COUNT(DISTINCT STG_STAGE_DISPLAY) ";
	
	final static String NUMBER_OF_GIVEN_STAGE = "SELECT COUNT(DISTINCT TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)) ";
	
	final static String NUMBER_OF_LAB = "SELECT COUNT(DISTINCT PER_SURNAME) ";
	
	final static String NUMBER_OF_SUBMISSION_DATE = "SELECT COUNT(DISTINCT SUB_SUB_DATE) ";
	
	final static String NUMBER_OF_ASSAY_TYPE = "SELECT COUNT(DISTINCT SUB_ASSAY_TYPE) ";
	
	final static String NUMBER_OF_SPECIMEN_TYPE = "SELECT COUNT(DISTINCT SPN_ASSAY_TYPE) ";
	
	final static String NUMBER_OF_SEX = "SELECT COUNT(DISTINCT SPN_SEX) ";
	
	final static String NUMBER_OF_PROBE_NAME = "SELECT COUNT(DISTINCT '') ";
	
	/*final static String NUMBER_OF_GENOTYPE = "SELECT COUNT(DISTINCT SPN_WILDTYPE) ";*/
	
	final static String NUMBER_OF_GENOTYPE = "SELECT COUNT(DISTINCT CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE " +
						"WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID " +
						"AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
						"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) ELSE " +
						"(SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE " +
						"SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END )  ";

	
	final static String NUMBER_OF_PROBE_TYPE = "SELECT COUNT(DISTINCT '') ";
	
	/*final static String NUMBER_OF_IMAGE = "SELECT COUNT(DISTINCT CONCAT(IMG_FILEPATH,IMG_SML_FILENAME)) ";*/
	
	final static String NUMBER_OF_IMAGE = "SELECT COUNT(IMG_OID) FROM ISH_SUBMISSION JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " ;

	final static String NUMBER_OF_ISH_EXPRESSION = "SELECT COUNT(DISTINCT EXP_STRENGTH) "; 
	
	final static String NUMBER_OF_TISSUES = "SELECT COUNT(DISTINCT ANO_COMPONENT_NAME) "; 	
	
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
	
	final static public String getAssayType(String type) {
		return " AND SUB_ASSAY_TYPE = '"+ type + "' ";
	}
	
	final static String name101 = "TOTAL_NUMBER_OF_SUBMISSION_TG";
	final static String query101 = NUMBER_OF_SUBMISSION + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name102 = "TOTAL_NUMBER_OF_GENE_SYMBOL_TG";
	final static String query102 = NUMBER_OF_GENE_SYMBOL_TG + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG") + "AND SAL_ORDER = 1 ";
	
	final static String name103 = "TOTAL_NUMBER_OF_THEILER_STAGE_TG";
	final static String query103 = NUMBER_OF_THEILER_STAGE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name104 = "TOTAL_NUMBER_OF_GIVEN_STAGE_TG";
	final static String query104 = NUMBER_OF_GIVEN_STAGE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name105 = "TOTAL_NUMBER_OF_LAB_TG";
	final static String query105 = NUMBER_OF_LAB + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name106 = "TOTAL_NUMBER_OF_SUBMISSION_DATE_TG";
	final static String query106 = NUMBER_OF_SUBMISSION_DATE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name107 = "TOTAL_NUMBER_OF_ASSAY_TYPE_TG";
	final static String query107 = NUMBER_OF_ASSAY_TYPE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name108 = "TOTAL_NUMBER_OF_SPECIMEN_TYPE_TG";
	final static String query108 = NUMBER_OF_SPECIMEN_TYPE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name109 = "TOTAL_NUMBER_OF_SEX_TG";
	final static String query109 = NUMBER_OF_SEX + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name110 = "TOTAL_NUMBER_OF_PROBE_NAME_TG";
	final static String query110 = NUMBER_OF_PROBE_NAME + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name111 = "TOTAL_NUMBER_OF_GENOTYPE_TG";
	final static String query111 = NUMBER_OF_GENOTYPE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name112 = "TOTAL_NUMBER_OF_PROBE_TYPE_TG";
	final static String query112 = NUMBER_OF_PROBE_TYPE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	final static String name113 = "TOTAL_NUMBER_OF_IMAGE_TG";
	/*final static String query113 = NUMBER_OF_IMAGE + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");*/
	final static String query113 = NUMBER_OF_IMAGE + PUBLIC_ENTRIES_Q + "AND IMG_TYPE NOT LIKE '%wlz%' " +  getAssayType("TG");
	
	final static String name114 = "TOTAL_NUMBER_OF_ISH_EXPRESSION_TG";
	final static String query114 = NUMBER_OF_ISH_EXPRESSION + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");

	final static String name115 = "TOTAL_NUMBER_OF_TISSUES_TG";
	final static String query115 = NUMBER_OF_TISSUES + BROWSE_ALL_TABLES_TG + PUBLIC_ENTRIES_Q + getAssayType("TG");
	
	public static String stageFormatConcat = bundle.getString("project").equals("GUDMAP") ?
			"TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) " +
			"WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" :
				"CONCAT(SPN_STAGE_FORMAT, SPN_STAGE)";

	final static String name15 = "SUBMISSION_IMAGE_DETAIL_TG";
	final static String query15 = 
		"SELECT SUB_ACCESSION_ID, ALE_GENE AS RPR_SYMBOL, RPR_NAME, " +
		"STG_STAGE_DISPLAY, " + stageFormatConcat + ", SUB_ASSAY_TYPE, SPN_ASSAY_TYPE, " +
		"IMG_FILEPATH, IMG_FILENAME " +
		"FROM ISH_ALLELE " +
		"LEFT JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID " +
		"LEFT JOIN REF_PROBE ON ALE_GENE = RPR_SYMBOL " +
		"JOIN LNK_SUB_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
		"JOIN ISH_SUBMISSION ON SAL_SUBMISSION_FK = SUB_OID " +
		"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
		"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
		"JOIN REF_URL ON URL_OID = IMG_URL_FK " +
		" WHERE SUB_ACCESSION_ID = ? ORDER BY IMG_ORDER LIMIT ?,1";
	
	final static String name = "";
	final static String query = "";
	  
	static ParamQuery pqList[] = {
		new ParamQuery(name0, query0),
		new ParamQuery(name1, query1),
		new ParamQuery(name2, query2),
		new ParamQuery(name3, query3),
		new ParamQuery(name4, query4),
		new ParamQuery(name5, query5),
		new ParamQuery(name6, query6),
		new ParamQuery(name7, query7),
		new ParamQuery(name8, query8),
		new ParamQuery(name9, query9),
		new ParamQuery(name10, query10),
		new ParamQuery(name11, query11),
		new ParamQuery(name12, query12),
		new ParamQuery(name13, query13),
		new ParamQuery(name14, query14),
		new ParamQuery(name15, query15),
		new ParamQuery(name16, query16),
		new ParamQuery(name17, query17),
		new ParamQuery(name101, query101),
		new ParamQuery(name102, query102),
		new ParamQuery(name103, query103),
		new ParamQuery(name104, query104),
		new ParamQuery(name105, query105),
		new ParamQuery(name106, query106),
		new ParamQuery(name107, query107),
		new ParamQuery(name108, query108),
		new ParamQuery(name109, query109),
		new ParamQuery(name110, query110),
		new ParamQuery(name111, query111),
		new ParamQuery(name112, query112),
		new ParamQuery(name113, query113),
		new ParamQuery(name114, query114),
		new ParamQuery(name115, query115)
		
	};
	
	// finds ParamQuery object by name and returns
	public static ParamQuery getParamQuery(String name) {
	    ParamQuery ret = null;
	    for (int i = 0; i < pqList.length; i++) {
		if (pqList[i].getQueryName().equals(name)) {
		    ret = pqList[i];
		    
		    if (debug)
			System.out.println("sql = "+  ret.getQuerySQL().toLowerCase());
		    
		    break;
		}
	    }
	    return ret;
	}	  
}
