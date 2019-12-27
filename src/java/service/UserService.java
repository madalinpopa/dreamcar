/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.UserDao;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;
import model.User;

/**
 *
 * @author pmadalin
 */
@Named(value = "user")
@RequestScoped
public class UserService {

    private User user;
    private String username;
    private String password;
    private String company;
    private String email;

    @Inject
    private UserDao userDao;

    @Resource
    UserTransaction utx;

    public void register() {

        // define the user
        this.user = new User();
        this.user.setCompany(this.company);
        this.user.setEmail(this.email);
        this.user.setUsername(this.username);
        this.user.setPassword(this.password);
        this.user.setUser_role("vendor");

        // persist the user into database
        try {
            this.utx.begin();
            this.userDao.addUser(this.user);
            this.utx.commit();
            
            // set the message for success
            String message = "The user has been registeres!";
            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(message));
        } catch (Exception e) {
            System.out.println("Register user failed to persist");
            throw new RuntimeException();
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
