package gmerg.db;

import java.util.ResourceBundle;

public class AnnotationToolQuery {
	  static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	  
/** backup - start */
	  final static public String[] interestedStructures = {
		  "Mesonephros (all parts, all stages)", 
		  "Metanephros",
		  "Lower urinary tract",
		  "Early reproductive system",
		  "Male reproductive system",
		  "Female reproductive system", 
		  "Nephrogenic interstitium",
		  "Others", // items down from this item will not be implemented for time being - 13/11/2008
		  "Cap mesenchyme",
		  "Pretubular aggregate",
		  "Early tubule",
		  "Collecting duct (all parts all stages, including primitive collecting duct)",
		  "Renal vascular system",
		  "urethra",
		  "Ureter",
		  "Bladder"
	  };
	  
	  // Mesonephros (all parts, all stages)
	  final static public String[] MesonephrosEmapIds = {
		  "EMAP:1436", "EMAP:1961", "EMAP:2576", "EMAP:3229", "EMAP:27645", "EMAP:1437", 
		  "EMAP:1962", "EMAP:1438", "EMAP:1963", "EMAP:1439", "EMAP:1964", "EMAP:2577", 
		  "EMAP:3230", "EMAP:2579", "EMAP:3232", "EMAP:27592", "EMAP:27593", "EMAP:30563", 
		  "EMAP:30564", "EMAP:30566", "EMAP:30567", "EMAP:30569", "EMAP:30570", "EMAP:30572", 
		  "EMAP:30573", "EMAP:27660", "EMAP:27662",  
		  "EMAP:27664", "EMAP:27666", "EMAP:30575", "EMAP:30577", "EMAP:30579", "EMAP:30581"
	  };
	  
	  // Metanephros
	  final static public String[] MetanephrosEmapIds = {
		  "EMAP:3233","EMAP:3841","EMAP:4587",
          "EMAP:5504","EMAP:6674","EMAP:8226","EMAP:9536","EMAP:10896","EMAP:12256",
          "EMAP:29491","EMAP:30247"
	  };
	  
	  // Lower urinary tract
	  final static public String[] LowerUrinarySystemEmapIds = {
		  "EMAP:6668", "EMAP:8219","EMAP:9528",
          "EMAP:10888","EMAP:12248","EMAP:29457",
          "EMAP:30374","EMAP:28749","EMAP:28750","EMAP:28751",
          "EMAP:28752","EMAP:29443","EMAP:30418","EMAP:28556",
          "EMAP:30898","EMAP:27572","EMAP:3237","EMAP:3846",
          "EMAP:4593","EMAP:5516",
          "EMAP:3238","EMAP:3847","EMAP:4594","EMAP:5517","EMAP:6689","EMAP:2575",
          "EMAP:3239","EMAP:3848","EMAP:4595","EMAP:5521","EMAP:6692","EMAP:8243",
          "EMAP:9553","EMAP:10913","EMAP:12273","EMAP:30416","EMAP:29475"
      };
	  
	  // Early reproductive system
	  final static public String[] EarlyReproductiveSystemEmapIds = {
		  "EMAP:3850", "EMAP:1958", "EMAP:2572",
          "EMAP:3226","EMAP:3851","EMAP:1436",
          "EMAP:1961","EMAP:2576","EMAP:3229", "EMAP:27645"
	  };
	  
	  // Male reproductive system
	  final static public String[] MaleReproductiveSystemEmapIds = {
		  "EMAP:28873","EMAP:5532","EMAP:6705","EMAP:8257",
          "EMAP:8443","EMAP:9803","EMAP:11163", "EMAP:29348","EMAP:30157"
	  };
	  
	  // Female reproductive system
	  final static public String[] FemaleReproductiveSystemEmapIds = {
		  "EMAP:28872","EMAP:5523","EMAP:6694","EMAP:8245",
		  "EMAP:9557","EMAP:10917","EMAP:12277","EMAP:29396","EMAP:30203"
	  };
/** backup - end */	  
	  
	  
	  final static String name213 = "GET_STAGE_FROM_SUBMISSION";
	  final static String query213 = "SELECT SUB_EMBRYO_STG FROM ISH_SUBMISSION WHERE SUB_ACCESSION_ID=?";
	  
	  final static String name214 = "DELETE_FROM_EXPRESSION_WITH_DIFFERENT_STAGE";
	  final static String query214 = "delete FROM ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANA_TIMED_NODE, ANA_STAGE "
									+" WHERE STG_NAME=? AND EXP_COMPONENT_ID = ATN_PUBLIC_ID "
									+" AND ATN_NODE_FK=ANO_OID AND ATN_STAGE_FK=STG_OID "
									+" AND EXP_SUBMISSION_FK=SUB_OID AND SUB_ACCESSION_ID=?";

	  final static String name1 = "INCOMPLETED_SUBMISSION_BATCH_ID";
	  final static String query1 = "SELECT DISTINCT SUB_BATCH FROM ISH_SUBMISSION " +
	  		"WHERE SUB_DB_STATUS_FK = 2 AND SUB_PI_FK = ?";
	  
	  final static String name2 = "MAX_SUBMISSION_BATCH_ID";
	  final static String query2 = "SELECT BAT_OID FROM REF_BATCH";
	  
	  final static String name3 = "UPDATE_MAX_SUBMISSION_BATCH_ID";
	  final static String query3 = "UPDATE REF_BATCH SET BAT_OID = ?";
	  
	  // mofidied by xingjun - 15/08/2008
	  // more information need to be retrieved
	  // xingjun - 16/10/2009 - added column SUB_DB_STATUS_FK
	  final static String name4 = "GET_ISH_SUBMISSIONS_BY_BATCH_ID";
//	  final static String query4 = "SELECT SUB_ACCESSION_ID, SUB_BATCH FROM ISH_SUBMISSION WHERE SUB_BATCH = ?";
	  final static String query4 = "SELECT SUB_ACCESSION_ID, SUB_BATCH, LSB_USER, TIMESTAMPDIFF(MINUTE, LSB_LOCKED_TIME, NOW()), RPV_PRIVILEGE_FK , EDH_MODIFIED_BY, MAX(EDH_MODIFIED_TIME), SUB_DB_STATUS_FK FROM ISH_SUBMISSION " +
	  		"JOIN LOG_EDITING_HISTORY ON EDH_ACCESSION_ID = SUB_ACCESSION_ID " +
	  		"LEFT JOIN LCK_SUBMISSION ON LSB_ACCESSION_ID = SUB_ACCESSION_ID " +
	  		"LEFT JOIN SEC_USER_PRIVILEGE ON RPV_USER_FK = LSB_USER_FK " +
	  		"WHERE SUB_BATCH = ? " +
	  		"GROUP BY natural_sort(SUB_ACCESSION_ID)";
	  	  
	  final static String name5 = "MAX_SUBMISSION_TEMP_ID";
	  final static String query5 = "SELECT SEQ_TEMP_PREFIX, SEQ_TEMP_ID FROM REF_SEQUENCES";
	  
	  final static String name6 = "UPDATE_MAX_SUBMISSION_TEMP_ID";
	  final static String query6 = "UPDATE REF_SEQUENCES SET SEQ_TEMP_ID = ?";
	  
	  // modified by xingjun - 31/07/2008
	  // changed the initial value 'ISH' for SUB_ASSAY_TYPE column to '' - when user create a new 
	  // temp submission, it could be ISH, IHC, or Tg. Better not to specify it when first insert it
	  // leave it to later after spreadsheet data has been loaded in
	  // xingjun - 06/11/2009 - take SUB_LOCAL_STATUS_FK out from the sql - DH need that column for storing xml file status
	  final static String name7 = "INSERT_TEMP_SUBMISSION";
	  final static String query7 = "INSERT INTO ISH_SUBMISSION (SUB_OID, SUB_SUBMITTER_FK, SUB_PI_FK, " +
//	  		"SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, SUB_LOCAL_STATUS_FK, SUB_DB_STATUS_FK, SUB_PROJECT_FK, " +
	  		"SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, SUB_DB_STATUS_FK, SUB_PROJECT_FK, " +
	  		"SUB_AUTHOR_REF, SUB_BATCH, SUB_NAMESPACE, SUB_OS_ACCESSION, SUB_LOCAL_ID, SUB_SOURCE, " +
	  		"SUB_VALIDATION, SUB_ASSAY_TYPE, SUB_CONTROL, SUB_AUTHORS, SUB_EMBRYO_STG, SUB_ASSESSMENT, " +
	  		"SUB_CONFIDENCE, SUB_SUB_DATE, SUB_ENTRY_DATE, SUB_MODIFIED_DATE  , SUB_LOCALDB_NAME, " +
	  		"SUB_IS_PUBLIC, SUB_IS_DELETED, SUB_ACCESSION_ID, SUB_MODIFIED_BY, SUB_VERSION_NO, " +
	  		"SUB_LAB_ID, SUB_ARCHIVE_ID) " +
//	  		"VALUES (?, ?, ?, 1, 1, 3, 2, 'GUDMAP', 0, ?, 'http://www.gudmap.org', " + // 4 parameters
	  		"VALUES (?, ?, ?, 1, 1, 2, 'GUDMAP', 0, ?, 'http://www.gudmap.org', " + // 4 parameters
	  		"'UNASSIGNED', '', '', '', '', 0, '', '', '', 0, '1000-01-01', '1000-01-01', " +
	  		"CURRENT_TIMESTAMP, '', 0, 0, ?, '', 1, ?, 0) "; // 2 parameters - modified by xingjun - 03/10/2008 -derek will use labid to store temp sub id 
	  
	  final static String name8 = "COMPONENT_AND_EXPRESSION_INFO";
	  final static String query8 = "SELECT EXP_COMPONENT_ID, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, PTN_PATTERN, ENT_VALUE, EXP_OID FROM ISH_EXPRESSION " +
	  		"LEFT JOIN ISH_EXPRESSION_NOTE ON ENT_EXPRESSION_FK = EXP_OID " +
	  		"LEFT JOIN ISH_PATTERN ON PTN_EXPRESSION_FK = EXP_OID " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED)))";
	  
	  final static String name23 = "EXPRESSION_AND_PATTERN_INFO";
	  final static String query23 = "SELECT EXP_COMPONENT_ID, EXP_STRENGTH, PTN_PATTERN, EXP_OID FROM ISH_EXPRESSION " +
	  		"LEFT JOIN ISH_PATTERN ON PTN_EXPRESSION_FK = EXP_OID " +
	  		"WHERE EXP_SUBMISSION_FK = ? " +
	  		"AND EXP_COMPONENT_ID = ?";
	  
	  final static String name9 = "DELETE_LOCATION_BY_SUB_COMP_ID";
	  final static String query9 = "DELETE FROM ISH_LOCATION " +
	  		"WHERE LCN_PATTERN_FK = " +
	  		"(SELECT PTN_OID FROM ISH_PATTERN WHERE PTN_EXPRESSION_FK = " +
	  		"(SELECT EXP_OID FROM ISH_EXPRESSION " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?))";
	  
	  final static String name10 = "DELETE_PATTERN_BY_SUB_COMP_ID";
	  final static String query10 = "DELETE FROM ISH_PATTERN " +
	  		"WHERE PTN_EXPRESSION_FK = " +
	  		"(SELECT EXP_OID FROM ISH_EXPRESSION " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?)";
	  
	  final static String name11 = "UPDATE_SUBMISSION_STAGE";
	  final static String query11 = "UPDATE ISH_SUBMISSION SET SUB_EMBRYO_STG = ? " +
	  		"WHERE SUB_OID = ?";
	  
	  final static String name12 = "INSERT_SUBMISSION";
	  // modified by xingjun - 12/11/2008 - removed SUB_LAB_ID
	  // derek set a unique index in the ish_submission table and want to use this column 
	  // to store other information. I think we should not specify its value here
	  // xingjun - 06/11/2009 - take SUB_LOCAL_STATUS_FK out from the sql - DH need that column for storing xml file status
	  final static String query12 = "INSERT INTO ISH_SUBMISSION (SUB_OID, SUB_ACCESSION_ID, SUB_EMBRYO_STG, " +
	  		"SUB_ASSAY_TYPE, SUB_IS_PUBLIC, SUB_ARCHIVE_ID, SUB_IS_DELETED, SUB_SUBMITTER_FK, SUB_PI_FK, " +
//	  		"SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, SUB_LOCAL_STATUS_FK, SUB_DB_STATUS_FK, SUB_PROJECT_FK, " +
	  		"SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, SUB_DB_STATUS_FK, SUB_PROJECT_FK, " +
	  		"SUB_AUTHOR_REF, SUB_BATCH, SUB_NAMESPACE, SUB_OS_ACCESSION, SUB_LOCAL_ID, SUB_SOURCE, " +
	  		"SUB_VALIDATION, SUB_CONTROL, SUB_ASSESSMENT, SUB_CONFIDENCE, SUB_LOCALDB_NAME) " +
//	  		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 25 parameters
	  		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 25 parameters
	  
	  final static String name13 = "DELETE_SUBMISSION";
	  final static String query13 = "DELETE FROM ISH_SUBMISSION WHERE SUB_ACCESSION_ID = ?";
	  
	  final static String name14 = "UPDATE_SUBMISSION_DB_STATUS_BY_BATCH";
	  final static String query14 = "UPDATE ISH_SUBMISSION " +
	  		"SET SUB_DB_STATUS_FK = ? WHERE SUB_BATCH = ?";
	  
	  final static String name22 = "UPDATE_BATCH_USER_STATUS_BY_BATCH";
	  final static String query22 = "UPDATE ISH_BATCH_USER " +
	  		"SET BSR_STATUS = ? WHERE BSR_BATCH_ID = ?";
	  
	  
	  // for a specified submission,
	  // find parent nodes for a specified node in the specified stage
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
	  		                       "AND PARENTS.ATN_PUBLIC_ID IN ";

	  // for a specified submission,
	  // find kid nodes for a specified node in the specified stage
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
	  		                        "AND KIDS.ATN_PUBLIC_ID IN ";

	  // modified by xingjun - 12/12/2008
	  // added criteria to only retrieve log info that record db status 
	  // changed from 1 (new) to 2 (incomplete); not from 3 (awaiting metadata) to 2.
	  // in latter case administrator change the status back sometimes to allow users to
	  // re-edit their data
	  // modified by xingjun - 18/12/2008 - need to find completed batch as well
	  final static String name17 = "GET_ISH_BATCHES_WITH_MODIFICATION_TIME_BY_PI";
//	  final static String query17 = "SELECT SUB_BATCH, EDH_MODIFIED_BY, MAX(EDH_MODIFIED_TIME) FROM ISH_SUBMISSION " +
//		"JOIN LOG_EDITING_HISTORY ON EDH_ACCESSION_ID = SUB_ACCESSION_ID " +
//		"WHERE SUB_DB_STATUS_FK = 2 AND SUB_PI_FK = ? " +
//		"AND EDH_OLD_VALUE < 2 " +
//		"GROUP BY SUB_BATCH, EDH_MODIFIED_BY";
	  final static String query17 = "SELECT SUB_BATCH, EDH_MODIFIED_BY, MAX(EDH_MODIFIED_TIME), SUB_DB_STATUS_FK FROM ISH_SUBMISSION " +
	  		"JOIN LOG_EDITING_HISTORY ON EDH_ACCESSION_ID = SUB_ACCESSION_ID " +
	  		"WHERE SUB_DB_STATUS_FK < 4 AND SUB_PI_FK = ? " +
	  		"AND (EDH_OLD_VALUE < 2 OR EDH_OLD_VALUE = 2) " +
	  		"GROUP BY SUB_BATCH DESC ";
	  	  
	  
	  final static String name18 = "GET_ISH_BATCHES_WITH_CREATION_TIME_BY_PI";
//	  final static String query18 = "SELECT EDH_NEW_VALUE, EDH_MODIFIED_BY, EDH_MODIFIED_TIME FROM LOG_EDITING_HISTORY " +
//	  		"JOIN ISH_BATCH_USER ON BSR_BATCH_ID = CAST(EDH_NEW_VALUE AS UNSIGNED) " +
//	  		"WHERE EDH_COLUMN = 'BSR_BATCH_ID' " +
//	  		"AND EDH_TYPE = 'insert' " +
//	  		"AND BSR_PI = ?";
	  final static String query18 = "SELECT H0.EDH_ITEM, H0.EDH_MODIFIED_BY, MAX(H0.EDH_MODIFIED_TIME) MAX_MOD_TIME, '2' FROM LOG_EDITING_HISTORY H0 " +
	  		"JOIN ISH_BATCH_USER ON BSR_BATCH_ID = CAST(H0.EDH_ITEM AS UNSIGNED) AND BSR_PI = ? " +
	  		"WHERE H0.EDH_TABLE = 'ISH_BATCH_USER' " +
	  		"AND H0.EDH_NEW_VALUE = '2' " +
	  		"GROUP BY H0.EDH_ITEM DESC " +
	  		"HAVING MAX_MOD_TIME >= (SELECT MAX(H1.EDH_MODIFIED_TIME) FROM LOG_EDITING_HISTORY H1 WHERE H1.EDH_ITEM = H0.EDH_ITEM)";
	  
	  final static String name19 = "INSERT_BATCH_USER_INFO";
	  final static String query19 = "INSERT INTO ISH_BATCH_USER (BSR_BATCH_ID, BSR_PI, BSR_CREATED_BY, BSR_STATUS) " +
	  		"VALUES (?, ?, ?, 2)";
	  
	  final static String name20 = "DELETE_BATCH";
	  final static String query20 = "DELETE FROM ISH_BATCH_USER WHERE BSR_BATCH_ID = ?";
	  
	  final static String name21 = "GET_ISH_SUBMISSION_ID_BY_BATCH_ID";
	  final static String query21 = "SELECT SUB_ACCESSION_ID FROM ISH_SUBMISSION WHERE SUB_BATCH = ?";
	  
	  final static String name24 = "GET_BATCH_STATUS";
	  final static String query24 = "SELECT BSR_STATUS FROM ISH_BATCH_USER WHERE BSR_BATCH_ID = ? ";
	  

	  final static String name100 = "GET_LOG_HISTORY_DATE";
	  final static String query100 = "select distinct SUBSTR(EDH_MODIFIED_TIME,1 , 10) from LOG_EDITING_HISTORY";

	  final static String name101 = "GET_USERS_IN_DATE";
	  final static String query101 = "select distinct EDH_MODIFIED_BY from  LOG_EDITING_HISTORY where  SUBSTR(EDH_MODIFIED_TIME,1 , 10)=?";

	  final static String name102 = "GET_SECTIONS";
	  final static String query102 = "select distinct EDH_SECTION from LOG_EDITING_HISTORY where SUBSTR(EDH_MODIFIED_TIME,1 , 10)=? and EDH_MODIFIED_BY=? and EDH_ACCESSION_ID=?";

	  final static String name103 = "GET_SUBMISSIONS";
	  final static String query103 = "select distinct EDH_ACCESSION_ID from LOG_EDITING_HISTORY where  SUBSTR(EDH_MODIFIED_TIME,1 , 10)=? and EDH_MODIFIED_BY=?";

	  final static String name104 = "GET_ITEMS";
	  final static String query104 = "select distinct EDH_ITEM, EDH_OLD_VALUE, EDH_NEW_VALUE from LOG_EDITING_HISTORY where  SUBSTR(EDH_MODIFIED_TIME,1 , 10)=? and EDH_MODIFIED_BY=? and EDH_ACCESSION_ID=? and EDH_SECTION=?";

	  final static String name105 = "GET_HISTORY";
	  final static String query105 = "select distinct SUBSTR(EDH_MODIFIED_TIME,1 , 10),EDH_MODIFIED_BY, EDH_ACCESSION_ID, EDH_SECTION, EDH_ITEM, EDH_COLUMN_LABEL, EDH_OLD_VALUE, EDH_NEW_VALUE from LOG_EDITING_HISTORY group by SUBSTR(EDH_MODIFIED_TIME,1 , 10),EDH_MODIFIED_BY, EDH_ACCESSION_ID, EDH_SECTION, EDH_ITEM order by SUBSTR(EDH_MODIFIED_TIME,1 , 10) DESC LIMIT 0, 100";

	  final static String name106 = "SUBMISSION_SUMMARY";
	  final static String query106 = "SELECT SUB_OID, SUB_ACCESSION_ID, SUB_EMBRYO_STG, SUB_DB_STATUS_FK FROM ISH_SUBMISSION WHERE SUB_ACCESSION_ID = ? ";
	  
	  
	  final static String name = "";
	  final static String query = "";
	  
	  
	  static ParamQuery pqList[] = {
	      new ParamQuery(name213, query213),
	      new ParamQuery(name214, query214),
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
	      new ParamQuery(name18, query18),
	      new ParamQuery(name19, query19),
	      new ParamQuery(name20, query20),
	      new ParamQuery(name21, query21),
	      new ParamQuery(name22, query22),
	      new ParamQuery(name23, query23),
	      new ParamQuery(name24, query24),
	      new ParamQuery(name100, query100),
	      new ParamQuery(name101, query101),
	      new ParamQuery(name102, query102),
	      new ParamQuery(name103, query103),
	      new ParamQuery(name104, query104),
	      new ParamQuery(name105, query105),
	      new ParamQuery(name106, query106)
	  };
	  
	  // finds ParamQuery object by name and returns
	  public static ParamQuery getParamQuery(String name) {
		  for (int i = 0; i < pqList.length; i++) {
			  if (pqList[i].getQueryName().equals(name)) {
				  return pqList[i];
			  }
	      }
	      return null;
	  }	  
}
