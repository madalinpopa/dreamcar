/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.PoOrderDao;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;
import model.PoOrder;

/**
 *
 * @author pmadalin
 */
@Named(value = "order")
@RequestScoped
public class OrderService {

    private PoOrder order;

    @Inject
    private PoOrderDao poOrderDao;

    @Resource
    private UserTransaction utx;

    @PostConstruct
    public void init() {
        this.order = new PoOrder();
    }

    public void addOrder() {
        try {
            this.order.setStatus(Boolean.TRUE);
            utx.begin();
            this.poOrderDao.addOrder(this.order);
            utx.commit();

            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/admin/home.xhtml?faces-redirect=true");
            
        } catch (Exception e) {
            throw new RuntimeException("Samething wrong happen with addOrder() transaction");
        }

    }

    public PoOrder getOrder() {
        return order;
    }

    public void setOrder(PoOrder order) {
        this.order = order;
    }

}
