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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    public boolean deleteOrder(int id){
        
        Query query = this.em.createQuery("DELETE FROM PoOrder p WHERE p.orderId = :id");
        query.setParameter("id", id);
        int order = query.executeUpdate();
        return order > 0;
    }
    
    public List<PoOrder> getAllOrders(){
        List<PoOrder> orders = this.em.createNamedQuery("PoOrder.findAll").getResultList();
        return orders;
    }
    
    public PoOrder findOrderById(int id){
        PoOrder order = this.em.find(PoOrder.class, id);
        return order;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
