
package com.kudin.alex.lessons.library_2.controllers;

import com.kudin.alex.lessons.library_2.daos.BooksDAO;
import com.kudin.alex.lessons.library_2.entities.Book;
import com.kudin.alex.lessons.library_2.enums.SearchType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 27, 2018
 */

@ManagedBean(name="booksController")
@RequestScoped
public class BooksController {
   
private static BooksDAO booksDAO = new BooksDAO();
    
    public List<Book> returnBooksList(){
        List<Book> booksList = null;
        
        Map<String, String> attributes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        if(attributes.containsKey("genre_id")) booksList = booksDAO.getBooksByGenreID(Long.valueOf(attributes.get("genre_id")));
        else if(attributes.containsKey("search_line") && attributes.containsKey("search_type")){
            if(attributes.get("search_type").equals(SearchType.AUTHOR.toString())){
                booksList = booksDAO.getBooksByAuthor(attributes.get("search_line"));
            }
            if(attributes.get("search_type").equals(SearchType.TITLE.toString())){
                booksList = booksDAO.getBooksByBookName(attributes.get("search_line"));
            }
        }
        else if(attributes.containsKey("letter")){
            booksList = booksDAO.getBooksByLetter(attributes.get("letter"));
        }
        
        if (booksList != null)return booksList;
        else return Collections.EMPTY_LIST;
    }    
}
