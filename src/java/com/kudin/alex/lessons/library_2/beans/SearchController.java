
package com.kudin.alex.lessons.library_2.beans;

import com.kudin.alex.lessons.library_2.enums.SearchType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 20, 2018
 */

@ManagedBean(name="serchController")
@SessionScoped
public class SearchController implements Serializable {

    private SearchType searchType;
    private Map<String, SearchType> searchList = new HashMap<>();
    
    public SearchController(){
        ResourceBundle bundle = ResourceBundle.getBundle("com.kudin.alex.lessons.library_2.nls.books_message", 
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.put(bundle.getString("author_name"), SearchType.AUTHOR);
        searchList.put(bundle.getString("book_name"), SearchType.TITLE);
    }
    
    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}
