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
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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

    private final int BOOKS_ON_PAGE = 3;
    private int booksFound;
    private List<Integer> listOfPages;
    private int currentPage;
    private String inputValue;

    public int getBOOKS_ON_PAGE() {
        return BOOKS_ON_PAGE;
    }

    public int getBooksFound() {
        return booksFound;
    }

    public List<Book> returnBooksList() {
        List<Book> booksList = null;

        Map<String, String> attributes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        if (attributes.containsKey("genre_id")) {
            booksFound = booksDAO.prefetchBooksByGenreID(Long.valueOf(attributes.get("genre_id")));
            checkAndFill(booksFound);
            booksList = booksDAO.getBooksByGenreID(Long.valueOf(attributes.get("genre_id")));
        } else if (attributes.containsKey("search_line") && attributes.containsKey("search_type")) {
            if (attributes.get("search_type").equals(SearchType.AUTHOR.toString())) {
                booksList = booksDAO.getBooksByAuthor(attributes.get("search_line"));
            }
            if (attributes.get("search_type").equals(SearchType.TITLE.toString())) {
                booksList = booksDAO.getBooksByBookName(attributes.get("search_line"));
            }
        } else if (attributes.containsKey("letter")) {
            booksList = booksDAO.getBooksByLetter(attributes.get("letter"));
        }

        if (booksList != null) {
            return booksList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public List<Integer> getListOfPages() {
        return listOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
    
    private void checkAndFill(int booksFound){
        if(booksFound > BOOKS_ON_PAGE){
            listOfPages = new ArrayList<>();
            int i = booksFound;
            Integer k = 1;
            while(i >= 0){
                listOfPages.add(k);
                i -= BOOKS_ON_PAGE;
                k += 1;
            }
        }
    }

    public String getInputValue() {
        return inputValue;
    }
    
    public void setInputValue(String value){
        this.inputValue = value;
    }
}
