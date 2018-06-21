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
@ManagedBean(name = "booksController")
@RequestScoped
public class BooksController {

    private static BooksDAO booksDAO = new BooksDAO();
    private Map<String, String> attributes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

    private final int BOOKS_ON_PAGE = 3;
    private int booksFound = determineBooksQuantity();
    private List<Integer> listOfPages;
    private int currentPage;
    private String inputValue;

    public List<Book> returnBooksList() {
        List<Book> booksList = null;
        int position = 0;
        
        if (attributes.containsKey("page_number")) {
            int page = Integer.valueOf(attributes.get("page_number"));
            if (page > 1) {
                position = (page - 1) * BOOKS_ON_PAGE;
            }
        }
        
        if (attributes.containsKey("genre_id")) {
            checkAndFill(booksFound);
            booksList = booksDAO.getBooksByGenreID(Long.valueOf(attributes.get("genre_id")), position, BOOKS_ON_PAGE);
        }
        
        else if (attributes.containsKey("search_line") && attributes.containsKey("search_type")) {
            if (attributes.get("search_type").equals(SearchType.AUTHOR.toString())) {
                checkAndFill(booksFound);
                booksList = booksDAO.getBooksByAuthor(attributes.get("search_line"), position, BOOKS_ON_PAGE);
            }
            if (attributes.get("search_type").equals(SearchType.TITLE.toString())) {
                checkAndFill(booksFound);
                booksList = booksDAO.getBooksByBookName(attributes.get("search_line"), position, BOOKS_ON_PAGE);
            }
        }
        
        else if (attributes.containsKey("letter")) {
            checkAndFill(booksFound);
            booksList = booksDAO.getBooksByLetter(attributes.get("letter"), position, BOOKS_ON_PAGE);
        }

        if (booksList != null) {
            return booksList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private int determineBooksQuantity() {
        if (attributes.containsKey("genre_id")) {
            return booksDAO.prefetchBooksByGenreID(Long.valueOf(attributes.get("genre_id")));
        } else if (attributes.containsKey("search_line") && attributes.containsKey("search_type")) {
            if (attributes.get("search_type").equals(SearchType.AUTHOR.toString())) {
                return booksDAO.prefetchBooksByAuthor(attributes.get("search_line"));
            }
            if (attributes.get("search_type").equals(SearchType.TITLE.toString())) {
                return booksDAO.prefetchBooksByBookName(attributes.get("search_line"));
            }
        } else if (attributes.containsKey("letter")) {
            return booksDAO.prefetchBooksByLetter(attributes.get("letter"));
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
