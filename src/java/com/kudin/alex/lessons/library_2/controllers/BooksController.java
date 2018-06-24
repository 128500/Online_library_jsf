package com.kudin.alex.lessons.library_2.controllers;

import com.kudin.alex.lessons.library_2.daos.BooksDAO;
import com.kudin.alex.lessons.library_2.entities.Book;
import java.util.ArrayList;
import java.util.HashMap;
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
@ManagedBean(name = "booksController")
@RequestScoped
public class BooksController {

    private static BooksDAO booksDAO = new BooksDAO();
    private Map<String, String> attributes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private Map<String,String> params = new HashMap<>(attributes);
    
    private final int BOOKS_ON_PAGE = 3;
    private int booksFound = determineBooksQuantity();
    private List<Integer> listOfPages;
    private int currentPage;
    private String inputValue;

    public List<Book> returnBooksList() {
        
        checkAndFill(booksFound);
        int position = 0;
        
        params.put("quantity", String.valueOf(BOOKS_ON_PAGE));
        
        if (params.containsKey("page_number")) {
            int page = Integer.valueOf(params.get("page_number"));
            if (page > 1) {
                position = (page - 1) * BOOKS_ON_PAGE;
            }
        }
        
        params.put("position", String.valueOf(position));
        
        return booksDAO.fetchBooks(params);
    }

    private int determineBooksQuantity() {
        if(!params.isEmpty())return booksDAO.prefetchQuantity(params);
        return 0;
    }

    /**
     * Checks if the quantity of found books more than parameter BOOKS_ON_PAGE
     * and if so creates ArrayList Integers (look field 'listOfPages') to view
     * it on the page
     *
     * @param booksFound quantity of books found with the pre-fetch from
     * database
     */
    private void checkAndFill(int booksFound) {
        if (booksFound > BOOKS_ON_PAGE) {
            listOfPages = new ArrayList<>();
            int i = booksFound;
            Integer k = 1;
            while (i >= 0) {
                listOfPages.add(k);
                i -= BOOKS_ON_PAGE;
                k += 1;
            }
        }
    }

    /**
     * Logouts user invalidating the current session
     *
     * @return part of URI for the authorization page
     */
    public String userLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index";
    }

    /*Getters and setters*/
    public List<Integer> getListOfPages() {
        return listOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getBOOKS_ON_PAGE() {
        return BOOKS_ON_PAGE;
    }

    public int getBooksFound() {
        return booksFound;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String value) {
        this.inputValue = value;
    }
}
