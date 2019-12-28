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
import model.Company;

/**
 *
 * @author pmadalin
 */
@Named(value = "companyDao")
@RequestScoped
public class CompanyDao {

    @PersistenceContext
    private EntityManager em;

    public Company addCompany(Company comp) {
        this.em.persist(comp);
        this.em.flush();
        return comp;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
