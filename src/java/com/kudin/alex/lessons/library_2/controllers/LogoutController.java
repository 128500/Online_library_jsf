package com.kudin.alex.lessons.library_2.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since Jun 30, 2018
 */
@ManagedBean
@SessionScoped
public class LogoutController implements Serializable {

    /**
     * Logouts user invalidating the current session
     *
     * @return part of URI for the authorization page
     */
    public String userLogout() {

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }
}
