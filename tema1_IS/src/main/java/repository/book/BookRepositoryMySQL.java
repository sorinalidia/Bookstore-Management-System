package repository.book;

import model.Book;
import model.Order;
import model.builder.BookBuilder;
import model.builder.OrderBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<Book> book = Optional.empty();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                book = Optional.of(getBookFromResultSet(resultSet));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return book;
    }

    /**
     *
     * How to reproduce a sql injection attack on insert statement
     *
     *
     * 1) Uncomment the lines below and comment out the PreparedStatement part
     * 2) For the Insert Statement DROP TABLE SQL Injection attack to succeed we will need multi query support to be added to our connection
     * Add to JDBConnectionWrapper the following flag after the DB_URL + schema concatenation: + "?allowMultiQueries=true"
     * 3) book.setAuthor("', '', null); DROP TABLE book; -- "); // this will delete the table book
     * 3*) book.setAuthor("', '', null); SET FOREIGN_KEY_CHECKS = 0; SET GROUP_CONCAT_MAX_LEN=32768; SET @tables = NULL; SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()); SELECT IFNULL(@tables,'dummy') INTO @tables; SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables); PREPARE stmt FROM @tables; EXECUTE stmt; DEALLOCATE PREPARE stmt; SET FOREIGN_KEY_CHECKS = 1; --"); // this will delete all tables. You are not required to know the table name anymore.
     * 4) Run the program. You will get an exception on findAll() method because the test_library.book table does not exist anymore
     */


    // ALWAYS use PreparedStatement when USER INPUT DATA is present
    // DON'T CONCATENATE Strings!

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?, ?);";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, book.getEmployeeId());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setDate(4, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(5, book.getQuantity());
            preparedStatement.setBigDecimal(6, book.getPrice());

            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean buyBook(Long customerId, Book book) {
        String insertOrderSql = "INSERT INTO `order` VALUES (null,?,?,?, ?, ?, ?);";
        String updateBookSql = "UPDATE book SET quantity = quantity - 1 WHERE id = ?;";

        try {
            PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql);
            insertOrderStatement.setLong(1, customerId);
            insertOrderStatement.setLong(2, book.getEmployeeId());
            insertOrderStatement.setLong(3, book.getId());
            insertOrderStatement.setDate(4, Date.valueOf(LocalDate.now()));
            insertOrderStatement.setInt(5, 1);
            insertOrderStatement.setBigDecimal(6, book.getPrice());

            int rowsInsertedOrder = insertOrderStatement.executeUpdate();

            PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSql);
            updateBookStatement.setLong(1, book.getId());

            int rowsUpdatedBook = updateBookStatement.executeUpdate();

            return (rowsInsertedOrder == 1 && rowsUpdatedBook == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        String sql = "SELECT * FROM `order` WHERE customer_id = ?;";

        List<Order> orders = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
    @Override
    public List<Order> findAllOrders(){
        String sql = "SELECT * FROM `order`;";

        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean removeBookById(Long bookId) {
        String sql = "DELETE FROM book WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bookId);

            int rowsDeleted = preparedStatement.executeUpdate();

            return rowsDeleted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setQuantity(resultSet.getInt("quantity"))
                .setPrice(resultSet.getBigDecimal("price"))
                .build();
    }
    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        return new OrderBuilder()
                .setId(resultSet.getLong("id"))
                .setCustomerId(resultSet.getLong("customer_id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setBookId(resultSet.getLong("book_id"))
                .setPurchaseDate(resultSet.getDate("purchase_date").toLocalDate())
                .build();
    }
}