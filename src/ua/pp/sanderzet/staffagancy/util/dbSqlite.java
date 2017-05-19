package ua.pp.sanderzet.staffagancy.util;

import org.jetbrains.annotations.Nullable;
import ua.pp.sanderzet.staffagancy.model.Job;
import ua.pp.sanderzet.staffagancy.model.Person;

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
public static int upgradeDb(String str) {
        try {
           int result = stat.executeUpdate(str);
            return result;

        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

}

public static int insertPersonDb(Person person) {
        ResultSet res = null;
    PreparedStatement preparedStatement1 = null;
    String sql = "INSERT INTO persons (lastName,firstName,passport,phone,dataOfContract,sanBook,endOfVisa, fileNumber) "  +
            "values(?,?,?,?,?,?,?,?)" ;
    try {

        preparedStatement1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement1.setString(1,person.getLastName());
        preparedStatement1.setString(2,person.getFirstName());
        preparedStatement1.setString(3,person.getPassport());
        preparedStatement1.setString(4,person.getPhone());
        preparedStatement1.setString(5,DateUtil.format(person.getDataOfContract()));
        preparedStatement1.setString(6,person.getSanBook());
        preparedStatement1.setString(7,DateUtil.format(person.getEndOfVisa()));
        preparedStatement1.setString(8,person.getFileNumber());


        preparedStatement1.executeUpdate();
res = preparedStatement1.getGeneratedKeys();
int id = 0;
if (res.next()) id = res.getInt(1);

return id;


    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
finally {
        try {
            res.close();
            preparedStatement1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public static int insertJobDb(Job job) {
        ResultSet res = null;
    PreparedStatement preparedStatement1 = null;
    String sql = "INSERT INTO jobs (idPerson,place,firm,position,start,end) "  +
            "values(?,?,?,?,?,?)" ;
    try {

        preparedStatement1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement1.setInt(1,job.getIdPerson());
        preparedStatement1.setString(2,job.getPlace());
        preparedStatement1.setString(3,job.getFirm());
        preparedStatement1.setString(4,job.getPosition());
        preparedStatement1.setString(5,DateUtil.format(job.getStart()));
        preparedStatement1.setString(6,DateUtil.format(job.getEnd()));


        preparedStatement1.executeUpdate();
res = preparedStatement1.getGeneratedKeys();
int id = 0;
if (res.next()) id = res.getInt(1);

return id;


    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
finally {
        try {
            res.close();
            preparedStatement1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

