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
import javax.persistence.Query;
import model.Bid;

/**
 *
 * @author pmadalin
 */
@Named(value = "bidDao")
@RequestScoped
public class BidDao {

    @PersistenceContext
    private EntityManager em;

    public Bid addBid(Bid b) {
        this.em.persist(b);
        this.em.flush();
        return b;
    }
    
    public boolean deleteBid(int id){
        Query query = this.em.createQuery("DELETE FROM Bid b WHERE b.bidId = :id");
        query.setParameter("id", id);
        int result = query.executeUpdate();
        return result > 0;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    

}
