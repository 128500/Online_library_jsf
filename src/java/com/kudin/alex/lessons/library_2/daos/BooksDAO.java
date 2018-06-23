package com.kudin.alex.lessons.library_2.daos;

import com.kudin.alex.lessons.library_2.dbconnector.DBConnector;
import com.kudin.alex.lessons.library_2.entities.Book;
import com.kudin.alex.lessons.library_2.enums.SearchType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 27, 2018
 */
public class BooksDAO {

    private static final String SQL_PREFETCH = "SELECT COUNT(b.id) AS quantity FROM book b ";
    private static final String SQL_HEAD = "SELECT b.id, b.name, b.page_count, b.isbn, b.publish_year, "
            + "g.name AS genre, a.fio AS author, p.name AS publisher FROM book b "
            + "INNER JOIN genre g ON g.id = b.genre_id "
            + "INNER JOIN author a ON a.id = b.author_id INNER JOIN publisher p ON p.id = b.publisher_id ";

    @FunctionalInterface
    interface Command<T> {
        T execute(PreparedStatement ps) throws SQLException;
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
    private <T> T fetchData(String sql, Command<T> command) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        T genericData = null;
        try {
            con = DBConnector.getConnection();
            ps = con.prepareStatement(sql);
            genericData = command.execute(ps);
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

        return genericData;
    }
    

    public List<Book> getRandomBooks() {
        StringBuilder sql = new StringBuilder(SQL_HEAD);
        sql.append("ORDER BY RAND() LIMIT 0,3");

        return fetchData(sql.toString(), ps -> {
            return createBooksList(ps.executeQuery());
        });
    }

    /**
     * Retrieves a book cover image from database
     * @param id id of the book
     * @return array of bytes or empty array if there is no image
     */
    public byte[] getBookImageById(final long id) {
        String sql = "SELECT image FROM book WHERE book.id = ?";
        return fetchByteData(sql, id);
    }

    /**
     * Retrieves content of a book (PDF format)
     * @param id id of the book
     * @return array of bytes or empty array if there is no content
     */
    public byte[] getBookInPDFById(final long id) {
        String sql = "SELECT content FROM book WHERE book.id = ?";
        return fetchByteData(sql, id);
    }
    
    /**
     * Retrieves books from database according to the given parameters for the search
     * @param params parameters for the search
     * @return list of found books or empty list if there are no suitable books
     */
    public List<Book> fetchBooks(Map<String, String> params){
        
        String sql = createSQL(params, false);
        
        return fetchData(sql, ps -> {
            ps = this.setParameters(ps, params);
            ResultSet resultSet =  ps.executeQuery();
            if(resultSet != null) {
            return createBooksList(resultSet);
            }
            else return Collections.EMPTY_LIST;
        });
    }
    
    /**
     * Creates SQL request according to the given parameters for search
     * @param params parameters for the search
     * @param prefetch if true creates SQL for pre fetching data
     * @return SQl request as a string
     */
    private String createSQL(Map<String, String> params, boolean prefetch){
        StringBuilder sql;
        if(prefetch) sql = new StringBuilder(SQL_PREFETCH);
        else sql = new StringBuilder(SQL_HEAD);
        
        if(params.containsKey("genre_id")){
            sql.append("WHERE b.genre_id = ? ORDER BY b.name");
        }
        
        if(params.containsKey("search_type")){
            if(params.get("search_type").equals(SearchType.AUTHOR.toString())){
                sql.append("WHERE b.author_id = (SELECT a.id FROM author a WHERE a.fio LIKE ? LIMIT 0,1) ORDER BY b.name");
            }
             
            if(params.get("search_type").equals(SearchType.TITLE.toString())){
                sql.append("WHERE b.name LIKE ? ORDER BY b.name");
            }
        }
        
        if(params.containsKey("letter")) {
            sql.append("WHERE (SELECT SUBSTRING(b.name, 1, 1)) = ? ORDER BY b.name");
        }
        
        if(!prefetch) sql.append(" LIMIT ?, ?");
        
        return sql.toString();
    }
    
    public int prefetchQuantity(Map<String, String> params){
        String sql = this.createSQL(params, true);
        
        return (int) fetchData(sql, ps -> {
            ps = this.setParameters(ps, params);
            ResultSet resultSet =  ps.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    return resultSet.getInt("quantity");
                }
            }
            return 0;
        });
    }
    
    private PreparedStatement setParameters(PreparedStatement ps, Map<String, String> params) throws SQLException{
        if (params.containsKey("search_line")) ps.setNString(1, "%"+params.get("search_line")+"%");
        if (params.containsKey("letter")) ps.setNString(1, params.get("letter"));
        if (params.containsKey("genre_id")) ps.setLong(1, Long.valueOf(params.get("genre_id")));
        if (params.containsKey("position")) ps.setInt(2, Integer.valueOf(params.get("position")));
        if (params.containsKey("quantity")) ps.setInt(3, Integer.valueOf(params.get("quantity")));
        return ps;
    }
    
    /**
     * Creates a list of Book entities
     * @param rs ResultSet object retrieved from database
     * @return created list of Book entities
     * @throws SQLException 
     */
    private List<Book> createBooksList(ResultSet rs) throws SQLException{
        List<Book> books = new ArrayList<>();
        while(rs.next()){
            books.add(createBook(rs));
        }
        return books;
    }

    /**
     * Creates a Book entity from a current row of ResultSet given as a parameter
     * @param rs ResultSet
     * @return created Book entity
     */
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
    
    /**
     * Retrieves byte data from database (images and PDF files)
     * @param sql SQL request
     * @param id id of searched book
     * @return array of bytes or empty array if there is no found data
     */
    private byte[] fetchByteData(String sql, long id){
        return (byte[])fetchData(sql, ps -> {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBytes(1);
            } else {
                return new byte[0];
            }
        });
    }
}
