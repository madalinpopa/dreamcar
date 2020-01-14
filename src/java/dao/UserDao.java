/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Bid;
import model.User;

/**
 *
 * @author pmadalin
 */
@Named(value = "userDao")
@RequestScoped
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User addUser(User u) {
        this.em.persist(u);
        this.em.flush();
        return u;
    }

    public User getUser(String username, String password) {

        // Set the query
        String query = "SELECT u from User u where u.username = :user and u.password = :pass";

        // Interogate the database
        try {
            Query q = this.em.createQuery(query);

            q.setParameter("user", username);
            q.setParameter("pass", password);

            User user = (User) q.getSingleResult();

            // Return the user
            return user;

        } catch (NoResultException e) {

            // If no result, then return null
            return null;
        }
    }
    
    public List<User> getAllVendors(String role){
        
        Query query = this.em.createNamedQuery("User.findByRole");
        query.setParameter("role", role);
        List<User> vendors = query.getResultList();
        return vendors;
    }
    
    public User getUserById(int id){
        Query query = this.em.createNamedQuery("User.findByUserId");
        query.setParameter("userId", id);
        User user = (User) query.getSingleResult();
        return user;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
