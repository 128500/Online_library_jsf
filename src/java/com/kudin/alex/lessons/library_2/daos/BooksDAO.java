package com.kudin.alex.lessons.library_2.daos;

import com.kudin.alex.lessons.library_2.dbconnector.DBConnector;
import com.kudin.alex.lessons.library_2.entities.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 27, 2018
 */
public class BooksDAO {

    private List<Book> bList;

    private static final String SQL_PREFETCH = "SELECT COUNT(b.id) AS quantity FROM book b ";
    private static final String SQL_HEAD = "SELECT b.id, b.name, b.page_count, b.isbn, b.publish_year, "
            + "g.name AS genre, a.fio AS author, p.name AS publisher FROM book b "
            + "INNER JOIN genre g ON g.id = b.genre_id "
            + "INNER JOIN author a ON a.id = b.author_id INNER JOIN publisher p ON p.id = b.publisher_id ";

    @FunctionalInterface
    interface Command {

        ResultSet execute(PreparedStatement ps) throws SQLException;
    }

    /**
     * Retrieves data of books from the database
     *
     * @param sql string of SQL to what data to retrieve
     * @param com functional interface Command with method execute to set up
     * PreparedStatement and execute it
     *
     * @return retrieved data as an ArrayList of book entities or empty
     * ArrayList
     */
    private List<Book> fetch(String sql, Command com) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        try {
            con = DBConnector.getConnection();
            ps = con.prepareStatement(sql);
            rs = com.execute(ps);
            if (rs != null) {
                while (rs.next()) {
                    books.add(createBook(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return books;
    }

    /**
     * Retrieves data from the database as byte array (images and PDF)
     *
     * @param sql string of SQL to what data to retrieve
     * @param com functional interface Command with method execute to set up
     * PreparedStatement and execute it
     *
     * @return retrieved data as byte array
     */
    private byte[] fetchData(String sql, Command com) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnector.getConnection();
            ps = con.prepareStatement(sql);
            rs = com.execute(ps);
            if (rs.next()) {
                return rs.getBytes(1);
            } else {
                return new byte[0];
            }
        } catch (SQLException e) {
            Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        throw new IllegalStateException("Couldn't get book image!");
    }

    private int fetchQuantity(String sql, Command com) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int quantity = 0;
        try {
            con = DBConnector.getConnection();
            ps = con.prepareStatement(sql);
            rs = com.execute(ps);
            if (rs != null) {
                while (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return quantity;
    }

    /**
     * Retrieves all book names that are currently in the database
     */
    public List<Book> getBooksNames() {
        String sql = "SELECT name FROM book AS b ORDER BY b.name";
        return fetch(sql, rs -> {
            return rs.executeQuery();
        });
    }

    public List<Book> getBookList() {
        if (bList != null) {
            return bList;
        } else {
            getBooksNames();
            return bList;
        }
    }

    public List<Book> getBooksByGenreID(long genreId) {

        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("WHERE b.genre_id = ? ORDER BY b.name");

        return fetch(sql.toString(), ps -> {
            ps.setLong(1, genreId);
            return ps.executeQuery();
        });
    }

    public int prefetchBooksByGenreID(long genreId) {

        StringBuilder sql = new StringBuilder(SQL_PREFETCH);
        sql.append("WHERE b.genre_id = ? ORDER BY b.name");

        return fetchQuantity(sql.toString(), ps -> {
            ps.setLong(1, genreId);
            return ps.executeQuery();
        });
    }

    public List<Book> getBooksByAuthor(String author) {

        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("WHERE b.author_id = (SELECT a.id FROM author a WHERE a.fio LIKE ? LIMIT 0,1) ORDER BY b.name");

        return fetch(sql.toString(), ps -> {
            ps.setNString(1, "%" + author + "%");
            return ps.executeQuery();
        });
    }

    public int prefetchBooksByAuthor(String author) {

        StringBuilder sql = new StringBuilder(SQL_PREFETCH);
        sql.append("WHERE b.author_id = (SELECT a.id FROM author a WHERE a.fio LIKE ? LIMIT 0,1) ORDER BY b.name");

        return fetchQuantity(sql.toString(), ps -> {
            ps.setNString(1, "%" + author + "%");
            return ps.executeQuery();
        });
    }

    public List<Book> getBooksByBookName(String bookName) {

        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("WHERE b.name LIKE ? ORDER BY b.name");

        return fetch(sql.toString(), ps -> {
            ps.setNString(1, "%" + bookName + "%");
            return ps.executeQuery();
        });
    }

    
    public int prefetchBooksByBookName(String bookName) {

        StringBuilder sql = new StringBuilder(SQL_PREFETCH);
        sql.append("WHERE b.name LIKE ? ORDER BY b.name");

        return fetchQuantity(sql.toString(), ps -> {
            ps.setNString(1, "%" + bookName + "%");
            return ps.executeQuery();
        });
    }
    
    
    public List<Book> getBooksByLetter(String letter) {

        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("WHERE (SELECT SUBSTRING(b.name, 1, 1)) = ? ORDER BY b.name");

        return fetch(sql.toString(), ps -> {
            ps.setNString(1, letter);
            return ps.executeQuery();
        });
    }

    
    public int prefetchBooksByLetter(String letter) {

        StringBuilder sql = new StringBuilder(SQL_PREFETCH);
        sql.append("WHERE (SELECT SUBSTRING(b.name, 1, 1)) = ? ORDER BY b.name");

        return fetchQuantity(sql.toString(), ps -> {
            ps.setNString(1, letter);
            return ps.executeQuery();
        });
    }
    
    
    public List<Book> getRandomBooks() {
        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("ORDER BY RAND() LIMIT 0,3");

        return fetch(sql.toString(), ps -> {
            return ps.executeQuery();
        });
    }

    public byte[] getBookImageById(final long id) {

        String sql = "SELECT image FROM book WHERE book.id = ?";

        return fetchData(sql, ps -> {
            ps.setLong(1, id);
            return ps.executeQuery();
        });
    }

    public byte[] getBookInPDFById(final long id) {
        String sql = "SELECT content FROM book WHERE book.id = ?";

        return fetchData(sql, ps -> {
            ps.setLong(1, id);
            return ps.executeQuery();
        });
    }

    private Book createBook(ResultSet rs) {
        Book b = new Book();

        try {
            b.setId(rs.getLong("id"));
            b.setName(rs.getString("name"));
            b.setIsbn(rs.getString("isbn"));
            b.setPageCount(rs.getInt("page_count"));
            b.setPublishDate(rs.getDate("publish_year"));
            b.setGenre(rs.getString("genre"));
            b.setAuthor(rs.getString("author"));
            b.setPublisher(rs.getString("publisher"));
            /*b.setContent(new byte[0]);*/
 /*b.setImage(new ImageIcon(rs.getBytes("image")).getImage());*/
        } catch (SQLException ex) {
            Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }
}
