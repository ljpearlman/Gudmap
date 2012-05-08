package gmerg.db;

import java.sql.*;

import java.util.List;

import java.io.*;

public class DAO {
    public DAO() {
    }

    public static List getISHSubmissions() throws IOException {


        String browseAllQuery =
            "SELECT DISTINCT SUB_ACCESSION_ID," + " RPR_SYMBOL," +
            " SUB_EMBRYO_STG," + " TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)," +
            " PER_LAB," + " SUB_SUB_DATE," + " SUB_ASSAY_TYPE," +
            " SPN_ASSAY_TYPE," + " CONCAT(IMG_URL.URL_URL, " +
            " I.IMG_FILEPATH," + " IMG_URL.URL_SUFFIX, " +
            " I.IMG_FILENAME) " + " FROM ISH_SUBMISSION " +
            " JOIN ISH_PROBE  ON SUB_OID = PRB_SUBMISSION_FK " +
            " JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +
            "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
            "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
            "JOIN ISH_ORIGINAL_IMAGE I ON SUB_OID = I.IMG_SUBMISSION_FK " +
            "AND I.IMG_OID = " + "(SELECT MIN(I1.IMG_OID) " +
            "FROM ISH_ORIGINAL_IMAGE I1 " +
            "WHERE I1.IMG_SUBMISSION_FK = SUB_OID) " +
            "JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = 14" +
            " WHERE SUB_IS_PUBLIC=1 LIMIT 0, 20";

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://tamdhu/GudmapGX_test";
            String username = "gudmap_test";
            String password = "gudmap_test";

            conn = DriverManager.getConnection(url, username, password);

            Statement stmt = conn.createStatement();
            ResultSet resSet = null;

            resSet = stmt.executeQuery(browseAllQuery);

            ResultSetMetaData resSetData = resSet.getMetaData();
            int columnCount = resSetData.getColumnCount();

            if (resSet.first()) {
                resSet.beforeFirst();

                List<String[]> results = null;

                while (resSet.next()) {
                    String[] columns = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        columns[i] = resSet.getString(i + 1);
                    }
                    results.add(columns);
                }
                return results;
            }
            return null;
        } catch (SQLException e) {
            // TODO
        } catch (ClassNotFoundException e) {
            // TODO
        }
        return null;
    }
}
