
package com.kudin.alex.lessons.library_2.entities;

/**
 *
 * @author homeuser
 */
public class Author {
    
    private int id;   
    
    private String name;
    
    Author(){};
    
    public Author (String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
