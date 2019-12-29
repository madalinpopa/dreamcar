/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.PoOrderDao;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
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
    private List<PoOrder> orders;

    @Inject
    private PoOrderDao poOrderDao;

    @Resource
    private UserTransaction utx;

    @PostConstruct
    public void init() {
        this.order = new PoOrder();
        this.orders = this.poOrderDao.getAllOrders();
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

    public void closeOrder(int id) {

        PoOrder order_db = this.poOrderDao.findOrderById(id);

        try {

            // Update the status
            utx.begin();
            order_db.setStatus(Boolean.FALSE);
            this.poOrderDao.getEm().merge(order_db);
            this.poOrderDao.getEm().flush();
            utx.commit();

            // Reload the home page
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/admin/home.xhtml?faces-redirect=true");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openOrder(int id) {

        PoOrder order_db = this.poOrderDao.findOrderById(id);

        try {

            // Update the status
            utx.begin();
            order_db.setStatus(Boolean.TRUE);
            this.poOrderDao.getEm().merge(order_db);
            this.poOrderDao.getEm().flush();
            utx.commit();

            // Reload the home page
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/admin/home.xhtml?faces-redirect=true");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PoOrder getOrder() {
        return order;
    }

    public void setOrder(PoOrder order) {
        this.order = order;
    }

    public List<PoOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PoOrder> orders) {
        this.orders = orders;
    }

}