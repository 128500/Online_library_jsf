
package com.kudin.alex.lessons.library_2.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 14, 2018
 */

@ManagedBean(name="user")
@SessionScoped
public class User implements Serializable{

    private long id;
    private String name;
    private String password;

    public User(){}
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
