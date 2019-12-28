/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Profile;

/**
 *
 * @author pmadalin
 */
@Named(value = "profileDao")
@RequestScoped
public class ProfileDao {

    @PersistenceContext
    private EntityManager em;

    public Profile addProfile(Profile p) {
        this.em.persist(p);
        this.em.flush();
        return p;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
