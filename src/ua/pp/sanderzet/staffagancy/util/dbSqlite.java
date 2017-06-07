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

    public static Connection connect(String nameDb){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(nameDb);
            stat = conn.createStatement();


            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        public static void createDB () {
            try {
//Creating table 'persons' if not exist
                String sql = "CREATE TABLE if not exists persons (id INTEGER PRIMARY KEY ," +
                        "lastName text, firstName text, passport text , phone  text, " +
                        "dateOfContract text, sanBook text, " +
                        "endOfVisa text, fileNumber text, " +
                        "dateQuit text, document text, usualNote text, " +
                        "criticalNote text)";
                stat.execute(sql);
//                Creating table 'job' if it`s not exist without primary key
                sql = "CREATE TABLE if not exists jobs (idPerson INTEGER, place TEXT, firm TEXT, position TEXT," +
                        "startJob TEXT, endJob TEXT, transitionJob TEXT);";
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
    String sql = "INSERT INTO persons (lastName,firstName,passport,phone,dateOfContract,sanBook,endOfVisa,fileNumber," +
            "dateQuit,document,usualNote,criticalNote) "  +
            "values(?,?,?,?,?,?,?,?,?,?,?,?)" ;
    try {

        preparedStatement1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement1.setString(1,person.getLastName());
        preparedStatement1.setString(2,person.getFirstName());
        preparedStatement1.setString(3,person.getPassport());
        preparedStatement1.setString(4,person.getPhone());
        preparedStatement1.setString(5,DateUtil.format(person.getDateOfContract()));
        preparedStatement1.setString(6,person.getSanBook());
        preparedStatement1.setString(7,DateUtil.format(person.getEndOfVisa()));
        preparedStatement1.setString(8,person.getFileNumber());
        preparedStatement1.setString(9,DateUtil.format(person.getDateQuit()));
        preparedStatement1.setString(10,person.getDocument());
        preparedStatement1.setString(11,person.getUsualNote());
        preparedStatement1.setString(12,person.getCriticalNote());


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


//For transition from odl db to new we need the same personId

    public static int insertPersonInNewDb(Person person) {
        PreparedStatement preparedStatement1 = null;
        String sql = "INSERT INTO persons (lastName,firstName,passport,phone,dateOfContract,sanBook,endOfVisa,fileNumber," +
                "dateQuit,document,usualNote,criticalNote,id) "  +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
        try {

            preparedStatement1 = conn.prepareStatement(sql);
            preparedStatement1.setString(1,person.getLastName());
            preparedStatement1.setString(2,person.getFirstName());
            preparedStatement1.setString(3,person.getPassport());
            preparedStatement1.setString(4,person.getPhone());
            preparedStatement1.setString(5,DateUtil.format(person.getDateOfContract()));
            preparedStatement1.setString(6,person.getSanBook());
            preparedStatement1.setString(7,DateUtil.format(person.getEndOfVisa()));
            preparedStatement1.setString(8,person.getFileNumber());
            preparedStatement1.setString(9,DateUtil.format(person.getDateQuit()));
            preparedStatement1.setString(10,person.getDocument());
            preparedStatement1.setString(11,person.getUsualNote());
            preparedStatement1.setString(12,person.getCriticalNote());
            preparedStatement1.setInt(13,person.getId());


            preparedStatement1.executeUpdate();
            int id = 0;

            return id;


        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            try {
                preparedStatement1.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



public static int insertJobDb(Job job) {
        ResultSet res = null;
    PreparedStatement preparedStatement1 = null;
    String sql = "INSERT INTO jobs (idPerson, place, firm, position, startJob, endJob," +
            "transitionJob) values(?,?,?,?,?,?,?)" ;
    try {

        preparedStatement1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement1.setInt(1,job.getIdPerson());
        preparedStatement1.setString(2,job.getPlace());
        preparedStatement1.setString(3,job.getFirm());
        preparedStatement1.setString(4,job.getPosition());
        preparedStatement1.setString(5,DateUtil.format(job.getStartJob()));
        preparedStatement1.setString(6,DateUtil.format(job.getEndJob()));
        preparedStatement1.setString(7,DateUtil.format(job.getTransitionJob()));



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
            if (res != null) res.close();
            if (stat != null)stat.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

