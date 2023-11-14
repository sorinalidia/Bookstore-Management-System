package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";

    private static final String USER = "Sorina";
    private static final String PASSWORD = "JustinBieber28";
    private static final int TIMEOUT = 5;


    private Connection connection;

    public JDBConnectionWrapper(String schema){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);
            //Bootstrap
            createTables();

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

   /* private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS book(" +
                "id bigint NOT NULL AUTO_INCREMENT," +
                "author varchar(500) NOT NULL," +
                "title varchar(500) NOT NULL," +
                "publishedDate datetime DEFAULT NULL," +
                "PRIMARY KEY(id),"+
                "UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);
    }*/
   /* private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS book(" +
                "id bigint NOT NULL AUTO_INCREMENT," +
                "author varchar(500) NOT NULL," +
                "title varchar(500) NOT NULL," +
                "publishedDate datetime DEFAULT NULL," +
                "runtime int, " + // Add runtime column for EBooks
                "format varchar(100), " + // Add format column for AudioBooks
                "PRIMARY KEY(id)," +
                "UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);
    }*/
   private void createTables() throws SQLException {
       Statement statement = connection.createStatement();

       // Create Book table
       String bookTableSql = "CREATE TABLE IF NOT EXISTS book(" +
               "id bigint NOT NULL AUTO_INCREMENT," +
               "author varchar(500) NOT NULL," +
               "title varchar(500) NOT NULL," +
               "publishedDate datetime DEFAULT NULL," +
               "PRIMARY KEY(id)," +
               "UNIQUE KEY id_UNIQUE(id)" +
               ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
       statement.execute(bookTableSql);

       // Create EBook table
       String eBookTableSql = "CREATE TABLE IF NOT EXISTS ebook(" +
               "id bigint NOT NULL," + // Foreign key referencing book.id
               "format varchar(100)," +
               "PRIMARY KEY(id)," +
               "FOREIGN KEY (id) REFERENCES book(id)" +
               ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
       statement.execute(eBookTableSql);

       // Create AudioBook table
       String audioBookTableSql = "CREATE TABLE IF NOT EXISTS audiobook(" +
               "id bigint NOT NULL," + // Foreign key referencing book.id
               "runtime int," +
               "PRIMARY KEY(id)," +
               "FOREIGN KEY (id) REFERENCES book(id)" +
               ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
       statement.execute(audioBookTableSql);
   }


    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }

}