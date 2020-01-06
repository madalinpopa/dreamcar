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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
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

    private UIComponent component;
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

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("renderMessage", true);
            session.setAttribute("message", " The order has been added!");

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

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("renderMessage", true);
            session.setAttribute("message", " The order has been closed!");

            // Reload the home page
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/admin/home.xhtml?faces-redirect=true");

        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
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

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("renderMessage", true);
            session.setAttribute("message", " The order has been opened!");

            // Reload the home page
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            nav.performNavigation("/admin/home.xhtml?faces-redirect=true");

        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int id) {

        try {

            utx.begin();
            this.poOrderDao.deleteOrder(id);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            e.printStackTrace();
        }

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("renderMessage", true);
        session.setAttribute("message", " The order has been deleted!");

        // Reload the home page
        FacesContext context = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
        nav.performNavigation("/admin/home.xhtml?faces-redirect=true");
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

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

}
