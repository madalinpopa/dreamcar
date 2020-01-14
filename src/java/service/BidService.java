/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.BidDao;
import dao.PoOrderDao;
import dao.UserDao;
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
import model.User;

/**
 *
 * @author pmadalin
 */
@Named(value = "bidService")
@RequestScoped
public class BidService {

    private int orderId;
    private UIComponent component;
    private Bid bid;
    private PoOrder order;
    private List<Bid> bids;
    private List<Bid> userBids;
    private User user;

    @Inject
    private UserDao userDao;

    @Inject
    private BidDao bidDao;

    @Inject
    private PoOrderDao orderDao;

    @Inject
    private AuthService auth;

    @Resource
    private UserTransaction utx;

    @PostConstruct
    public void init() {
        this.bid = new Bid();
        User dbUser = this.userDao.getUserById(this.auth.getUser().getUserId());
        this.userBids = dbUser.getBidList();
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
                this.bid.setUserId(auth.getUser());
                this.bid.setPoNumber(order);
                this.bid.setBidDate(offerDate);
                this.bid.setStatus(Boolean.FALSE);
                this.bidDao.addBid(this.bid);
                this.utx.commit();

                this.bidDao.getEm().getEntityManagerFactory().getCache().evictAll();

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

    public void getAllBidsForAnOrder() {

        // get the order
        PoOrder selected_order = this.orderDao.findOrderById(this.orderId);

        if (selected_order != null) {

            this.bids = selected_order.getBidList();
            this.order = selected_order;
        }
    }

    public String acceptBid() {

        // Get the param send from new-offer.xhtml page
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

        this.orderId = Integer.parseInt(params.get("orderId"));
        this.order = this.orderDao.findOrderById(this.orderId);
        String bidId = params.get("bidId");

        if (bidId != null) {
            this.bid = this.bidDao.findBidById(Integer.parseInt(bidId));
            try {
                this.utx.begin();
                this.bid.setStatus(Boolean.TRUE);
                this.bidDao.getEm().merge(this.bid);
                this.bidDao.getEm().flush();
                this.utx.commit();

            } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
                e.printStackTrace();
            }
        }

        return "/admin/offers.xhtml?faces-redirect=true&orderId=" + this.orderId;

    }

    public String declineBid() {

        // Get the param send from new-offer.xhtml page
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

        this.orderId = Integer.parseInt(params.get("orderId"));
        this.order = this.orderDao.findOrderById(this.orderId);
        String bidId = params.get("bidId");

        if (bidId != null) {
            this.bid = this.bidDao.findBidById(Integer.parseInt(bidId));
            try {
                this.utx.begin();
                this.bid.setStatus(Boolean.FALSE);
                this.bidDao.getEm().merge(this.bid);
                this.bidDao.getEm().flush();
                this.utx.commit();

            } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
                e.printStackTrace();
            }
        }

        return "/admin/offers.xhtml?faces-redirect=true&orderId=" + this.orderId;

    }

    public String deleteBid(int id) {

        try {

            this.utx.begin();
            this.bidDao.deleteBid(id);
            this.utx.commit();

            User dbUser = this.userDao.getUserById(this.auth.getUser().getUserId());
            this.userBids = dbUser.getBidList();
            this.bidDao.getEm().getEntityManagerFactory().getCache().evictAll();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/vendor/profile.xhtml?faces-redirect=true";
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<Bid> getUserBids() {
        return userBids;
    }

    public void setUserBids(List<Bid> userBids) {
        this.userBids = userBids;
    }

}
