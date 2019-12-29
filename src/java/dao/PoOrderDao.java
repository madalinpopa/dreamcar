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
import model.PoOrder;

/**
 *
 * @author pmadalin
 */
@Named(value = "poOrderDao")
@RequestScoped
public class PoOrderDao {

    @PersistenceContext
    private EntityManager em;

    public PoOrder addOrder(PoOrder po) {
        this.em.persist(po);
        this.em.flush();
        return po;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
