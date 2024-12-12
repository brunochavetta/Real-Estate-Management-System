package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class DAO {

    Dotenv dotenv = Dotenv.load();

    protected Connection conection = null;
    protected ResultSet result = null;
    protected Statement statement = null;

    private final String USER = dotenv.get("USER");
    private final String PASSWORD = dotenv.get("PASSWORD");
    private final String DATABASE = dotenv.get("DATABASE");
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";

    protected void connectBase() throws ClassNotFoundException, SQLException {
        try {
            Class.forName(DRIVER);
            String urlDataBase = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true";
            conection = DriverManager.getConnection(urlDataBase, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    protected void disconnectBase() throws Exception {
        try {
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (conection != null && !conection.getAutoCommit()) {
                    conection.rollback();
                }
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                try {
                    if (conection != null) {
                        conection.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    protected void insertModifyDelete(String SQL) throws Exception {
        try {
            connectBase();
            conection.setAutoCommit(false);
            statement = conection.createStatement();
            statement.executeUpdate(SQL);
            conection.commit();
        } catch (SQLException | ClassNotFoundException e) {
            if (conection != null) {
                conection.rollback();
            }
            System.out.println(e);
        } finally {
            try {
                if (conection != null) {
                    conection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                disconnectBase();
            }
        }
    }

    protected void consultBase(String SQL) throws Exception {
        try {
            connectBase();
            statement = conection.createStatement();
            result = statement.executeQuery(SQL);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

}
