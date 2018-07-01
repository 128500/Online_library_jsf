package com.kudin.alex.lessons.library_2.controllers;

import com.kudin.alex.lessons.library_2.daos.BooksDAO;
import com.kudin.alex.lessons.library_2.entities.Book;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BooksController implements Serializable {

    private static BooksDAO booksDAO = new BooksDAO();
    private Map<String, String> attributes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    private Map<String, String> params = new HashMap<>(attributes);

    private final int BOOKS_ON_PAGE = 2;
    private int booksFound;
    private List<Integer> listOfPages;
    private String inputValue;
    private int currentPage;

    public List<Book> returnBooksList() {

        if (attributes.containsKey("search_line") || attributes.containsKey("genre_id") || attributes.containsKey("letter")) {
        
            if(!attributes.containsKey("search_line")) inputValue = "";
            
            imitateLoading();
            
            int quantity = determineBooksQuantity();

            booksFound = quantity;

            checkAndFill(quantity);

            params.put("quantity", String.valueOf(BOOKS_ON_PAGE));
            int position = 0;
            if (attributes.containsKey("page_number")) {
                int page = Integer.valueOf(attributes.get("page_number"));
                currentPage = page;
                if (page > 1) {
                    position = (page - 1) * BOOKS_ON_PAGE;
                }
            }

            params.put("position", String.valueOf(position));

            return booksDAO.fetchBooks(params);
        }
        
        return Collections.EMPTY_LIST;
    }

    /**
     * Determines the total quantity of books that meet the request
     *
     * @return quantity of found books
     */
    public int determineBooksQuantity() {
        if (!attributes.isEmpty() && attributes != null) {
            return booksDAO.prefetchQuantity(attributes);
        }
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
            while (i > 0) {
                listOfPages.add(k);
                i -= BOOKS_ON_PAGE;
                k += 1;
            }
        }
    }

    public boolean makeRender() {
        return determineBooksQuantity() > BOOKS_ON_PAGE;
    }

    
    private void imitateLoading(){
        try{
            Thread.sleep(200);
        } catch(InterruptedException e){
            Logger.getLogger(BooksDAO.class.getName()).log(Level.SEVERE, null, e );
        }
    }

    /*Getters and setters*/
    public List<Integer> getListOfPages() {
        return listOfPages;
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

    public int getCurrentPage() {
        return currentPage;
    }

}
