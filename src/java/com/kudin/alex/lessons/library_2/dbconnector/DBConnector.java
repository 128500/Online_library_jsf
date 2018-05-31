
package com.kudin.alex.lessons.library_2.dbconnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class provides the static method to
 * connect to the database.
 * 
 * @author ALEKSANDR KUDIN
 * @since Apr 22, 2018
 */
public class DBConnector {
    
    
    static InitialContext ic;
    static DataSource ds;
    
    public static Connection getConnection(){
        
        try{
            ic = new InitialContext();
            ds = (DataSource)ic.lookup("jdbc/Library");
            return ds.getConnection();
        }
        catch(NamingException | SQLException e){
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, e);
        }
        throw new IllegalStateException("Coudn't get connection to the database");
    }
}
