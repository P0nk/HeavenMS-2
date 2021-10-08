package tools.mapletools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharsetChanger {
    
    public static void main(String[] args) {
        String CHARSET_NAME = "utf8";
        String CHARSET_COLLATE = "utf8_general_ci";
        String DATABASE_NAME = "cosmic";
        
        try (Connection con = SimpleDatabaseConnection.getConnection()) {
            try (ResultSet rs = con.prepareStatement("SELECT CONCAT('ALTER TABLE `', tbl.`TABLE_SCHEMA`, '`.`', tbl.`TABLE_NAME`, '` CONVERT TO CHARACTER SET " + CHARSET_NAME + " COLLATE " + CHARSET_COLLATE + ";') FROM `information_schema`.`TABLES` tbl WHERE tbl.`TABLE_SCHEMA` = '" + DATABASE_NAME + "'").executeQuery()) {
                PreparedStatement ps;
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                    try {
                        ps = con.prepareStatement(rs.getString(1));
                        ps.execute();
                        ps.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (SQLException e) {
        }
    }
    
}
