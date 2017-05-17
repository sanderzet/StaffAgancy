package ua.pp.sanderzet.staffagancy;

import org.jetbrains.annotations.Nullable;

import java.sql.*;

/**
 * Created by sander on 22.04.17.
 */
public class dbSqlite {

    public static Connection conn = null;
    public static Statement stat = null;
    public static ResultSet res = null;

    public static Connection connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db/sa.db");

return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        public static void createDB () {
            try {
                stat = conn.createStatement();
//Creating table 'persons' if not exist
                String sql = "CREATE TABLE if not exists 'persons' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'lastName' text, 'firstName' text, 'passport' text UNIQUE, 'phone'  text, " +
                        "'dataOfContract' text, 'sanBook' text, " +
                        "'endOfVisa' text, 'fileNumber' text);";
                stat.execute(sql);
//                Creating table 'job' if it`s not exist without primary key
                sql = "CREATE TABLE if not exists 'jobs' ('idPerson' INTEGER, 'place' text, 'firm' text, 'position' text," +
                        "'start' text, 'end' text);";
                stat.execute(sql);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
public static int writeDB (String str) {
        try {
           int result = stat.executeUpdate(str);
            return result;

        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

}

public static void delRowDb (String str) {

    try {
        stat.executeUpdate(str);
    } catch (SQLException e) {
        e.printStackTrace();
    }

}

    @Nullable
    public static ResultSet readDB (String str)
    {
        res = null;
        try {
           res = stat.executeQuery(str);
        }
        catch (Exception e) {
            e.printStackTrace();

        } finally {
return res;
        }

    }

    public static void closeDB() {
        try {
            res.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

