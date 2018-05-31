
package com.kudin.alex.lessons.library_2.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 17, 2018
 */

@ManagedBean(name="log_commander")
@RequestScoped
public class LoginCommander {

    public LoginCommander(){}
    
    public String login(){
        return "library_template";
    }
}
