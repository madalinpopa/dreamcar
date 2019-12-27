/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.UserDao;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author pmadalin
 */
@Named(value = "auth")
@SessionScoped
public class AuthService implements Serializable {

    private String username;
    private String password;
    private User user;

    @Inject
    UserDao userDao;

    public String login() {

        // Get the user
        this.user = userDao.getUser(this.username, this.password);

        // Check if the result is null
        if (this.user != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            // If the user is not null, check the role
            if (this.user.getUser_role().equals("vendor")) {
                session.setAttribute("user", "vendor");
                return "/vendor/home.xhtml?faces-redirect=true";
            } else if (this.user.getUser_role().equals("admin")) {
                session.setAttribute("user", "admin");
                return "/admin/home.xhtml?faces-redirect=true";
            }
        } else {

            // set the message
            String message = "The user doesn't exist!";

            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot view = context.getViewRoot();
            UIComponent component = view.findComponent("loginMessage");
            component.getAttributes().put("rendered", true);
            component.getAttributes().put("value", message);

            System.out.println("Utilizatorul nu exista");
        }

        // Redirect again to login page.
        return "/public/login.xhtml?redirect=true";

    }

    public String logout() {

        FacesContext instance = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) instance.getExternalContext().getSession(false);
        session.invalidate();
        return "/index.xhtml?faces-redirect=true";
    }

    public void isAdmin(ComponentSystemEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Object isLogged = session.getAttribute("user");

        if (isLogged == null) {
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/public/login.xhtml?faces-redirect=true");

        } else if ("vendor".equals(isLogged)) {
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/public/login.xhtml?faces-redirect=true");
        }

    }

    public void isVendor(ComponentSystemEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Object isLogged = session.getAttribute("user");

        if (isLogged == null) {
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/public/login.xhtml?faces-redirect=true");

        } else if ("admin".equals(isLogged)) {
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/public/login.xhtml?faces-redirect=true");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
