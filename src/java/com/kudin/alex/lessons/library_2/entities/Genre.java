
package com.kudin.alex.lessons.library_2.entities;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since Apr 26, 2018
 */
public class Genre {

    private long id;
    
    private String name;
    
    Genre(){}
    
    public Genre(String name){
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
