package com.kudin.alex.lessons.library_2.daos;

import com.kudin.alex.lessons.library_2.dbconnector.DBConnector;
import com.kudin.alex.lessons.library_2.entities.Genre;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since Apr 26, 2018
 */
@ManagedBean(name="genreDAO")
@ApplicationScoped
public class GenreDAO {

    List<Genre> genreList;

    public void createGenreList() {
        genreList = new ArrayList<>();

        Connection con = null;
        ResultSet rs = null;
        try {

            con = DBConnector.getConnection();
            rs = con.createStatement().executeQuery("SELECT * FROM genre AS g ORDER BY g.name");

            while (rs.next()) {
                Genre g = new Genre(rs.getString("name"));
                g.setId(rs.getLong("id"));
                genreList.add(g);
            }
        } catch (SQLException e) {
            Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenreDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<Genre> getGenreList() {
        if (genreList != null) {
            return genreList;
        } else {
            createGenreList();
            return genreList;
        }
    }
}
