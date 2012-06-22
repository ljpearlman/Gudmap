package gmerg.beans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import gmerg.entities.*;
import gmerg.assemblers.*;

import gmerg.utils.CookieOperations;

public class AuthenticationBean  {
    private boolean debug = false;

    private String userName;
    private String password;
    
    private UIInput nameInput;
    private UIInput passInput;

    public AuthenticationBean(){
        	    if (debug)
		System.out.println("AuthenticationBean.constructor");

    }
    
    public UIInput getNameInput() {
        return nameInput;
    }
    
    public void setNameInput(UIInput value){
        nameInput = value;
    }
    
    public UIInput getPassInput() {
        return passInput;
    }
    
    public void setPassInput(UIInput value){
        passInput = value;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String value) {
        userName = value;
    }
    
    public void setPassword(String value) {
        password = value;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void validateLogin(FacesContext context, UIComponent component, Object value) {
    
        System.out.println("made it into method!!");
        
        String nme = (String) nameInput.getLocalValue();
        String pass = (String) passInput.getLocalValue();
        
        
        System.out.println("made it into method!!"+ pass);
        System.out.println("made it into method!!"+nme);

        SecurityAssembler assembler = new SecurityAssembler();
        
        User newUser = assembler.getData(nme, pass);
        
        System.out.println("just initialised user");
        
        if(newUser == null) {
            //CookieOperations.setValueInCookie("loggedIn", "", -1, true, context);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid username and password. ", "Please enter a valid username and password. ");
            throw new ValidatorException(message);
        }
        else {
            CookieOperations.setValueInCookie("loggedIn", "passwordAccepted", -1, true);
        }
    }
    
    public String login() {
        return "success";
    }
}