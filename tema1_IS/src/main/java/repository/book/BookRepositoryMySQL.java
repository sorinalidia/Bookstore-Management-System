package repository.book;

import model.Book;
import model.EBook;
import model.AudioBook;
import model.PhysicalBook;
import model.builder.BookBuilder;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository<Book> {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book ;";

        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book,ebook,audiobook WHERE id = ?;";
        Optional<Book> book = Optional.empty();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {
        String bookSql = "INSERT INTO book (author, title, publishedDate) VALUES (?, ?, ?);";
        String ebookSql = "INSERT INTO ebook (id, format) VALUES (?, ?);";
        String audiobookSql = "INSERT INTO audiobook (id, runtime) VALUES (?, ?);";

        try {
            PreparedStatement bookStatement = connection.prepareStatement(bookSql);
            bookStatement.setString(1, book.getAuthor());
            bookStatement.setString(2, book.getTitle());

            if (book.getPublishedDate() != null) {
                bookStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            } else {
                bookStatement.setNull(3, Types.DATE);
            }

            int rowsInserted = bookStatement.executeUpdate();

            if (rowsInserted == 1) {
                long bookId = getLastInsertedId();
                if (bookId != -1) {
                    if (book instanceof EBook) {
                        PreparedStatement ebookStatement = connection.prepareStatement(ebookSql);
                        ebookStatement.setLong(1, bookId);
                        ebookStatement.setString(2, ((EBook) book).getFormat());
                        ebookStatement.executeUpdate();
                    } else if (book instanceof AudioBook) {
                        PreparedStatement audiobookStatement = connection.prepareStatement(audiobookSql);
                        audiobookStatement.setLong(1, bookId);
                        audiobookStatement.setInt(2, ((AudioBook) book).getRunTime());
                        audiobookStatement.executeUpdate();
                    }
                    return true;
                }
            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long getLastInsertedId() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as last_id");
            if (resultSet.next()) {
                return resultSet.getLong("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void removeAll() {
        try {
            String deleteAudioBookSql = "DELETE FROM audiobook;";
            String deleteEBookSql = "DELETE FROM ebook;";
            String deleteBookSql = "DELETE FROM book;";

            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteAudioBookSql);
            statement.executeUpdate(deleteEBookSql);
            statement.executeUpdate(deleteBookSql);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.getString("runtime") != null) {
            return new BookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setRunTime(resultSet.getInt("runtime"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                    .buildAudioBook();
        } else if (resultSet.getString("format") != null) {
            return new BookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setFormat(resultSet.getString("format"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                    .buildEBook();
        } else {
            return new BookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setTitle(resultSet.getString("title"))
                    .setAuthor(resultSet.getString("author"))
                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                    .build();
        }
    }
}