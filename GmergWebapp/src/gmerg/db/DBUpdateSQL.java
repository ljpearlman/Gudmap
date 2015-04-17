/**
 * 
 */
package gmerg.db;

import java.util.ResourceBundle;

/**
 * @author xingjun
 *
 */
public class DBUpdateSQL {
    protected static boolean debug = false;

	  static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	  
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
	  
	  // find table map object by id and returns
	  public static ParamMap getTableMap(String id) {
		  for (int i=0;i<tableList.length;i++) {
			  if (tableList[i].getParamId().equals(id)) {
				  return tableList[i];
			  }
		  }
		  return null;
	  }
	  
	  // find column map object by id and returns
	  public static ParamMap getColumnMap(String id) {
		  for (int i=0;i<columnList.length;i++) {
			  if (columnList[i].getParamId().equals(id)) {
				  return columnList[i];
			  }
		  }
		  return null;
	  }

	  // linked to deleting selected submission function
	  final static String name0 = "DELETE_SELECTED_SUBMISSIONS";
	  final static String query0 = "UPDATE ISH_SUBMISSION SET SUB_IS_DELETED = 1 WHERE ";
	  
	  // modified by xingjun - 04/07/2008 - make it accommodate the termporary submission id as well
	  // permanent submission id format: GUDMAP:XXXXX
	  // temporary submission id format: 999999XXXXX
	  final static String name1 = "INSERT_EXPRESSION_NOTE";
	  final static String query1 = "INSERT INTO ISH_EXPRESSION_NOTE (ENT_EXPRESSION_FK, ENT_VALUE, ENT_SEQ) " +
	  		"VALUES ((SELECT EXP_OID FROM ISH_EXPRESSION  " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?), ?, ?) ";
	  
	  final static String name2 = "DELETE_EXPRESSION_NOTE";
	  final static String query2 = "DELETE FROM ISH_EXPRESSION_NOTE WHERE ENT_EXPRESSION_FK = " +
	  		"(SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?) ";
	  
	  final static String name3 = "UPDATE_EXPRESSION_NOTE";
	  final static String query3 = "UPDATE ISH_EXPRESSION_NOTE " +
	  		"SET ENT_VALUE = ? " +
	  		"WHERE ENT_EXPRESSION_FK = " +
	  		"(SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?) " +
	  		"AND ENT_SEQ = 1 ";
	  
	  final static String name4 = "UPDATE_EXPRESSION";
	  final static String query4 = "UPDATE ISH_EXPRESSION " +
	  		"SET EXP_STRENGTH = ? " +
//	  		"WHERE EXP_SUBMISSION_FK = ? " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ? ";
	  
	  final static String name5 = "UPDATE_EXPRESSION_STRENGTH";
	  final static String query5 = "UPDATE ISH_EXPRESSION " +
		"SET EXP_ADDITIONAL_STRENGTH = ? " +
//		"WHERE EXP_SUBMISSION_FK = ? " +
		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
		"AND EXP_COMPONENT_ID = ? ";
	  
	  // delete pattern by submission id, component id, and pattern value
	  final static String name6 = "DELETE_PATTERN_BY_IDS";
	  final static String query6 = "DELETE FROM ISH_PATTERN " +
	  		"WHERE PTN_EXPRESSION_FK = " +
	  		"(SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ?) " +
	  		"AND PTN_PATTERN = ? ";
	  
	  final static String name7 = "INSERT_PATTERN";
	  final static String query7 = "INSERT INTO ISH_PATTERN (PTN_OID, PTN_EXPRESSION_FK, PTN_PATTERN) " +
	  		"VALUES(?, " +
	  		" (SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"  WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"  WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"  AND EXP_COMPONENT_ID = ?), " +
	  		"?) ";
	  
	  final static String name8 = "UPDATE_LOCATION";
	  final static String query8 = "UPDATE ISH_LOCATION " +
	  		"SET LCN_LOCATION = ? " +
	  		"WHERE LCN_OID = ? ";
	  
	  final static String name9 = "DELETE_LOCATION_BY_ID";
	  final static String query9 = "DELETE FROM ISH_LOCATION WHERE LCN_OID = ? ";
	  
	  final static String name14 = "GET_LOCATION_ID_BY_PATTERN";
	  final static String query14 = "SELECT LCN_OID FROM ISH_LOCATION " +
	  		"WHERE LCN_PATTERN_FK IN ( " +
	  		"SELECT PTN_OID FROM ISH_PATTERN " +
	  		"WHERE PTN_EXPRESSION_FK = (SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"                           WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"                           WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"                           AND EXP_COMPONENT_ID = ?) " +
	  		"AND PTN_PATTERN = ? " +
	  		")";
	  
	  final static String name10 = "INSERT_LOCATION";
	  final static String query10 = "INSERT INTO ISH_LOCATION (LCN_PATTERN_FK, LCN_LOCATION) " +
	  		"VALUES((SELECT PTN_OID FROM ISH_PATTERN " +
	  		"        WHERE PTN_EXPRESSION_FK = (SELECT EXP_OID FROM ISH_EXPRESSION " +
//	  		"                                   WHERE EXP_SUBMISSION_FK = CAST(SUBSTRING(?,8) AS UNSIGNED) " +
	  		"                                   WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"                                   AND EXP_COMPONENT_ID = ?) " +
	  		"        AND PTN_PATTERN = ?), " +
	  		"?)";
	  
	  final static String name11 = "DELETE_PATTERN_BY_ID";
	  final static String query11 = "DELETE FROM ISH_PATTERN WHERE PTN_OID = ? ";
	  
	  final static String name12 = "GET_MAX_PATTERN_ID";
//	  final static String query12 = "SELECT MAX(PTN_OID) FROM ISH_PATTERN ";
	  final static String query12 = "SELECT SEQ_PATTERN FROM REF_SEQUENCES ";

	  final static String name13 = "WRITE_USER_INTO_LOG_TABLE";
	  final static String query13 = "UPDATE LOG_EDITING_USER " +
	  		"SET EDU_TYPE = ?, " +
	  		"EDU_LAST_MODIFIED_BY = ?, " +
	  		"EDU_LAST_MODIFYING_TIME = (SELECT NOW()) " +
	  		"WHERE EDU_TABLE = ? " +
	  		"AND EDU_COLUMN = ? ";
	  
	  final static String name15 = "DELETE_EXPRESSION";
	  final static String query15 = "DELETE FROM ISH_EXPRESSION " +
//	  		"WHERE EXP_SUBMISSION_FK = ? " +
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ? ";
	  
	  final static String name16 = "GET_MAX_EXPRESSION_ID";
//	  final static String query16 = "SELECT MAX(EXP_OID) FROM ISH_EXPRESSION";
	  final static String query16 = "SELECT SEQ_EXPRESSION FROM REF_SEQUENCES";
	  
	  final static String name17 = "UPDATE_SEQUENCE_NUMBER_FOR_EXPRESSION";
	  final static String query17 = "UPDATE REF_SEQUENCES SET SEQ_EXPRESSION = ?";
	  
	  final static String name18 = "UPDATE_SEQUENCE_NUMBER_FOR_PATTERN";
	  final static String query18 = "UPDATE REF_SEQUENCES SET SEQ_PATTERN = ?";
	  
	  final static String name19 = "INSERT_EXPRESSION";
	  final static String query19 = "INSERT INTO ISH_EXPRESSION (EXP_OID, EXP_SUBMISSION_FK, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, EXP_COMPONENT_ID) " +
//	  		"VALUES (?, ?, ?, ?, ?) "; modified to enalble it to deal with the termporary subid as well as the permament subid
	  		"VALUES (?, (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))), ?, ?, ?) ";
	  
	  final static String name20 = "FIND_EXPRESSION";
	  final static String query20 = "SELECT COUNT(*) FROM ISH_EXPRESSION " +
//	  		"WHERE EXP_SUBMISSION_FK = ? " + modified to enable it to deal with the temp subid as well as the permament subid
	  		"WHERE EXP_SUBMISSION_FK = (SELECT IF(SUBSTR(?,1,6)='999999', ?, CAST(SUBSTRING(?,8) AS UNSIGNED))) " +
	  		"AND EXP_COMPONENT_ID = ? ";
	  
	  final static String name21 = "SIGN_OFF_ANNOTATION";
	  final static String query21 = "UPDATE ISH_SUBMISSION " +
	  		"SET SUB_DB_STATUS_FK = ? " +
	  		"WHERE SUB_ACCESSION_ID = ? ";
	  
	  final static String name22 = "SIGN_OFF_ANNOTATION_STATUS";
	  final static String query22 = "UPDATE ISH_SUBMISSION " +
		"SET SUB_DB_STATUS_FK = ? " +
  		"WHERE SUB_ACCESSION_ID = ? AND SUB_DB_STATUS_FK = ?";
	  
	  final static String name23 = "DELETE_COLLECTION_BY_ID";
	  final static String query23 = "DELETE FROM CLN_COLLECTION WHERE CLN_OID = ?";
	  
	  final static String name24 = "SET_PUBLIC_SUBMISSION";
	  final static String query24 = "UPDATE ISH_SUBMISSION " +
	  		"SET SUB_IS_PUBLIC = ? " +
	  		"WHERE SUB_ACCESSION_ID = ? ";

	  // xingjun - 25/07/2008 - CLN_LAST_UPDATED column added
	  // xingjun - 06/10/2009 - CLN_FOCUS_GROUP_NAME column added
	  final static String name25 = "INSERT_COLLECTION";
//	  final static String query25 = "INSERT INTO CLN_COLLECTION (CLN_OID, CLN_NAME, CLN_USER_FK, CLN_TYPE, CLN_STATUS, CLN_DESCRIPTION, CLN_FOCUS_GROUP, CLN_LAST_UPDATED) " +
//		"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	  final static String query25 = "INSERT INTO CLN_COLLECTION (CLN_OID, CLN_NAME, CLN_USER_FK, CLN_TYPE, CLN_STATUS, CLN_DESCRIPTION, CLN_FOCUS_GROUP, CLN_LAST_UPDATED, CLN_FOCUS_GROUP_NAME) " +
	  		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  
	  final static String name26 = "INSERT_COLELCTION_ITEM";
	  final static String query26 = "INSERT INTO CLN_COLLECTION_ITEM (CLI_OID, CLI_COLLECTION_FK, CLI_VALUE) " +
	  		"VALUES (?, ?, ?)";
	  
	  // xingjun - 06/10/2009 - CLN_FOCUS_GROUP_NAME column added
	  final static String name27 = "UPDATE_COLLECTION_SUMMARY";
	  final static String query27 = "UPDATE CLN_COLLECTION " +
	  		"SET CLN_NAME = ?, " +
	  		"CLN_STATUS = ?, " +
	  		"CLN_DESCRIPTION = ?, " +
	  		"CLN_FOCUS_GROUP = ?, " +
	  		"CLN_FOCUS_GROUP_NAME = ?, " +
	  		"CLN_LAST_UPDATED = ? " +
	  		"WHERE CLN_OID = ?";

	  final static String name28 = "INSERT_SUBMISSION_STATUS_NOTE";
//	  final static String query28 = "INSERT INTO XPR_STATUS_NOTES (STN_OID, STN_SUBMISSION_FK, STN_NOTE) " +
	  final static String query28 = "INSERT INTO XPR_STATUS_NOTES (STN_SUBMISSION_FK, STN_NOTE) " +
	  		"VALUES (?, ?)";
	  
	  final static String name29 = "DELETE_STATUS_NOTE_BY_SUBMISSION_ID";
	  final static String query29 = "DELETE FROM XPR_STATUS_NOTES WHERE STN_SUBMISSION_FK = ?";
	  
	  final static String name30 = "UPDATE_SUBMISSION_STATUS_NOTE";
	  final static String query30 = "UPDATE XPR_STATUS_NOTES SET STN_NOTE = ? " +
	  		"WHERE STN_OID = ?";
  
	  final static String name31 = "DELETE_STATUS_NOTE_BY_STATUS_NOTE_ID";
	  final static String query31 = "DELETE FROM XPR_STATUS_NOTES WHERE STN_OID = ?";
	  
	  final static String name32 = "GET_PI_FROM_SUB_ID";
	  final static String query32 = "select SUB_PI_FK, SUB_SUB_DATE, SUB_ASSAY_TYPE, SUB_DB_STATUS_FK " +
	  		"from ISH_SUBMISSION where SUB_ACCESSION_ID = ?";
	  
	  // modified by xingjun - 15/08/2008
	  // table structure changed, have to change sql accordingly
	  final static String name33 = "UPDATE_SUBMISSION_LOCKING_INFO";
//	  final static String query33 = "UPDATE LCK_SUBMISSION SET LSB_USER = ?, LSB_LOCKED_TIME = NOW() " +
	  final static String query33 = "UPDATE LCK_SUBMISSION SET LSB_USER_FK = ?, LSB_LOCKED_TIME = NOW() " +
	  		"WHERE LSB_ACCESSION_ID = ?";
	  
	  // modified by xingjun - 15/08/2008
	  // table structure changed, have to change sql accordingly
	  final static String name34 = "INSERT_SUBMISSION_LOCKING_INFO";
//	  final static String query34 = "INSERT INTO LCK_SUBMISSION (LSB_ACCESSION_ID, LSB_USER, LSB_LOCKED_TIME) VALUES (?, ?, NOW())";
	  final static String query34 = "INSERT INTO LCK_SUBMISSION (LSB_ACCESSION_ID, LSB_USER_FK, LSB_LOCKED_TIME) VALUES (?, ?, NOW())";
	  
	  final static String name35 = "DELETE_SUBMISSION_LOCKING_INFO";
	  final static String query35 = "DELETE FROM LCK_SUBMISSION WHERE LSB_ACCESSION_ID = ?";
	  
	  // modified by xingjun - 15/08/2008
	  // table structure changed, have to change sql accordingly
	  final static String name36 = "DELETE_USER_LOCKING_INFO";
	  final static String query36 = "DELETE FROM LCK_SUBMISSION WHERE LSB_USER_FK = ?";
	  
	  // change submission status by given pi and date
	  // xingjun - 23/02/2010 - added extra criteria, namely status
	  // xingjun - 01/07/2011 - added extra criteria, namely archive id
	  final static String name37 = "UPDATE_SUBMISSION_STATUS_BY_PI_N_DATE_N_STATE";
	  final static String query37 = "UPDATE ISH_SUBMISSION " +
	  		"SET SUB_DB_STATUS_FK = ?, " +
	  		"SUB_IS_PUBLIC = ? " + 
	  		"WHERE SUB_SUB_DATE = ? " +
	  		"AND SUB_PI_FK = ? " +
	  		"AND SUB_DB_STATUS_FK = ? " +
	  		"AND SUB_ARCHIVE_ID = ? ";
	    
	  final static String name38 = "REMOVE_COLLECTION_ITEMS";
	  final static String query38 = "DELETE FROM CLN_COLLECTION_ITEM WHERE CLI_COLLECTION_FK = ? AND CLI_VALUE = ?";
	  
	  final static String name = "";
	  final static String query = "";
	  
	  
	  // creates an array of all ParamQueries
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
	      new ParamQuery(name18, query18),
	      new ParamQuery(name19, query19),
	      new ParamQuery(name20, query20),
	      new ParamQuery(name21, query21),
	      new ParamQuery(name22, query22),
	      new ParamQuery(name23, query23),
	      new ParamQuery(name24, query24),
	      new ParamQuery(name25, query25),
	      new ParamQuery(name26, query26),
	      new ParamQuery(name27, query27),
	      new ParamQuery(name28, query28),
	      new ParamQuery(name29, query29),
	      new ParamQuery(name30, query30),
	      new ParamQuery(name31, query31),
	      new ParamQuery(name32, query32),
	      new ParamQuery(name33, query33),
	      new ParamQuery(name34, query34),
	      new ParamQuery(name35, query35),
	      new ParamQuery(name36, query36),
	      new ParamQuery(name37, query37),
	      new ParamQuery(name38, query38)
	  };
	  
	  /** table list */
	  final static String tableId0 = "EXPRESSION";
	  final static String tableName0 = "ISH_EXPRESSION";
	  
	  final static String tableId1 = "EXPRESSION_NOTE";
	  final static String tableName1 = "ISH_EXPRESSION_NOTE";
	  
	  final static String tableId2 = "PATTERN";
	  final static String tableName2 = "ISH_PATTERN";
	  
	  final static String tableId3 = "PATTERN_LOCATION";
	  final static String tableName3 = "ISH_LOCATION";
	  
	  final static String tableId4 = "STATUS_NOTE";
	  final static String tableName4 = "XPR_STATUS_NOTES";
	  
	  final static String tableId5 = "REF_BATCH";
	  final static String tableName5 = "REF_BATCH";
	  
	  final static String tableId6 = "SUBMISSION";
	  final static String tableName6 = "ISH_SUBMISSION";
	  
	  final static String tableId7 = "BATCH_USER";
	  final static String tableName7 = "ISH_BATCH_USER";
	  
	  final static String tableId = "";
	  final static String tableName = "";
	  
	  static ParamMap tableList[] = {
		  new ParamMap(tableId0, tableName0),
		  new ParamMap(tableId1, tableName1),
		  new ParamMap(tableId2, tableName2),
		  new ParamMap(tableId3, tableName3),
		  new ParamMap(tableId4, tableName4),
		  new ParamMap(tableId5, tableName5),
		  new ParamMap(tableId6, tableName6),
		  new ParamMap(tableId7, tableName7)
	  };
	  
	  /** column list */
	  final static String columnId0 = "EXPRESSION_EXPRESSION";
	  final static String columnName0 = "EXP_STRENGTH";
	  
	  final static String columnId1 = "EXPRESSION_STRENGTH";
	  final static String columnName1 = "EXP_ADDITIONAL_STRENGTH";
	  
	  final static String columnId2 = "EXPRESSION_NOTE";
	  final static String columnName2 = "ENT_VALUE";
	  
	  final static String columnId3 = "PATTERN";
	  final static String columnName3 = "PTN_PATTERN";
	  
	  final static String columnId4 = "LOCATION";
	  final static String columnName4 = "LCN_LOCATION";
	  
	  final static String columnId5 = "STATUS_NOTE";
	  final static String columnName5 = "STN_NOTE";
	  
	  final static String columnId6 = "MAX_SUBMISSION_BATCH";
	  final static String columnName6 = "BAT_OID";
	  
	  final static String columnId7 = "SUBMISSION_STAGE";
	  final static String columnName7 = "STG_STAGE_DISPLAY";
	  
	  final static String columnId8 = "SUBMISSION_MULTI";
	  final static String columnName8 = "SUBMISSION_MULTI";
	  
	  final static String columnId9 = "BATCH_USER_MULTI";
	  final static String columnName9 = "BATCH_USER_MULTI";
	  
	  final static String columnId = "";
	  final static String columnName = "";
	  
	  
	  static ParamMap columnList[] = {
		  new ParamMap(columnId0, columnName0),
		  new ParamMap(columnId1, columnName1),
		  new ParamMap(columnId2, columnName2),
		  new ParamMap(columnId3, columnName3),
		  new ParamMap(columnId4, columnName4),
		  new ParamMap(columnId5, columnName5),
		  new ParamMap(columnId6, columnName6),
		  new ParamMap(columnId7, columnName7),
		  new ParamMap(columnId8, columnName8),
		  new ParamMap(columnId9, columnName9)
	  };
}
