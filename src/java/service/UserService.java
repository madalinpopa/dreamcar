/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CompanyDao;
import dao.ProfileDao;
import dao.UserDao;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Company;
import model.Profile;
import model.User;

/**
 *
 * @author pmadalin
 */
@Named(value = "user")
@RequestScoped
public class UserService {

    private User user;
    private Profile profile;
    private Company company;
    private List<User> vendors;

    @Inject
    private UserDao userDao;

    @Inject
    private ProfileDao profileDao;

    @Inject
    private CompanyDao compdanyDao;

    @Inject
    private AuthService authServ;

    @Resource
    UserTransaction utx;

    @PostConstruct
    public void init() {
        this.user = new User();
        this.company = new Company();
        this.profile = new Profile();
        this.vendors = this.userDao.getAllVendors("vendor");
    }

    public void register() {

        // persist the user into database
        try {
            this.utx.begin();
            // First, add the user
            this.user.setRole("vendor");
            User user = this.userDao.addUser(this.user);

            // Second, add the company
            Company comp = this.compdanyDao.addCompany(this.company);

            // Third, add the user profile 
            this.profile.setCompany(comp);
            this.profile.setUserId(user);
            this.profileDao.addProfile(this.profile);

            this.utx.commit();
            this.profileDao.getEm().getEntityManagerFactory().getCache().evictAll();

            // set the message for success
            String message = "The user has been registeres!";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(message));
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            System.out.println("Register user failed to persist");
            throw new RuntimeException();
        }

    }

    public void updateUserProfile() {

        User updated_user = this.authServ.getUser();
        Profile updated_user_profile = updated_user.getProfile();
        Company updated_user_company = updated_user_profile.getCompany();

        try {
            this.utx.begin();
            this.userDao.getEm().merge(updated_user);
            this.userDao.getEm().flush();
            this.profileDao.getEm().merge(updated_user_profile);
            this.profileDao.getEm().flush();
            this.compdanyDao.getEm().merge(updated_user_company);
            this.compdanyDao.getEm().flush();
            this.utx.commit();

            // Reload the profile page
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/vendor/profile.xhtml?faces-redirect=true");

        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<User> getVendors() {
        return vendors;
    }

    public void setVendors(List<User> vendors) {
        this.vendors = vendors;
    }

}
