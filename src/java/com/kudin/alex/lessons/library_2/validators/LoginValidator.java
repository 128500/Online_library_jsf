
package com.kudin.alex.lessons.library_2.validators;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 15, 2018
 */
@FacesValidator("com.kudin.alex.lessons.library_2.validators.LoginValidator")
public class LoginValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        StringBuilder sb = new StringBuilder();
        ResourceBundle bundle = ResourceBundle.getBundle("com.kudin.alex.lessons.library_2.nls.message", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String name = String.valueOf(value);
        
        if(name.length() < 3){
            sb.append(bundle.getString("not_enough_letters"));
        }
        
        if(Character.isLowerCase(name.charAt(0))){
            if(!sb.toString().equals("")) sb.append("\n");
            sb.append(bundle.getString("lower_case"));
        }
        
        if(name.equals("username") || name.equals("login") ){
            if(!sb.toString().equals("")) sb.append("\n");
            sb.append(bundle.getString("username"));
        }
        
        if(!sb.toString().equals("")) {
            FacesMessage message = new FacesMessage(sb.toString());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
