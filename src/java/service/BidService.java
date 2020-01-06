/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.BidDao;
import dao.PoOrderDao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import model.Bid;
import model.PoOrder;

/**
 *
 * @author pmadalin
 */
@Named(value = "bidService")
@RequestScoped
public class BidService {

    private UIComponent component;
    private Bid bid;
    private PoOrder order;
    private List<Bid> bids;

    @Inject
    private BidDao bidDao;

    @Inject
    private PoOrderDao orderDao;

    @Resource
    private UserTransaction utx;

    @PostConstruct
    public void init() {
        this.bid = new Bid();
    }

    public void addBid() {

        // Get the param send from new-offer.xhtml page
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

        // Get the order from database
        this.order = this.orderDao.findOrderById(Integer.parseInt(params.get("orderId")));

        // Get the current date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String offerDate = formatter.format(date);

        try {

            if (this.order != null) {
                // Create the offer
                this.utx.begin();
                this.bid.setPoNumber(order);
                this.bid.setBidDate(offerDate);
                this.bidDao.addBid(this.bid);
                this.utx.commit();

                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                session.setAttribute("renderMessage", true);
                session.setAttribute("message", " Your offer has been added to the order!");

                ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
                nav.performNavigation("/vendor/home.xhtml?faces-redirect=true");

            }

        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            e.printStackTrace();

            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("/vendor/home.xhtml?faces-redirect=true");
        }
    }
    
    public void getAllBidsForAnOrder(int orderId){
    
        // get the order
        PoOrder selected_order = this.orderDao.findOrderById(orderId);
        
        if(selected_order != null){
            this.bids = selected_order.getBidList();
        }
    }
    
    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public PoOrder getOrder() {
        return order;
    }

    public void setOrder(PoOrder order) {
        this.order = order;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

}
