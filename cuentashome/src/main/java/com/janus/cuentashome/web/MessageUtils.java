package com.janus.cuentashome.web;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class MessageUtils {
    private static final String BUNDLE_NAME = "bundle.web"; // Your message bundle file name without the extension

    public static void addInfoMessage(String key) {
        addMessage(key, FacesContext.getCurrentInstance().getViewRoot().getLocale(), FacesMessage.SEVERITY_INFO);
    }
    
    public static void addErrorMessage(String key) {
        addMessage(key, FacesContext.getCurrentInstance().getViewRoot().getLocale(), FacesMessage.SEVERITY_ERROR);
    }

    public static void addErrorMessage(Throwable e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
    }

    public static void addMessage(String key, Locale locale, Severity severity) {
        String message = getMessage(key, locale);
        FacesMessage facesMessage = new FacesMessage(severity, message, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
    }

    // You can add more utility methods here as needed
    private static String getMessage(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        return bundle.getString(key);
    }
    
}
