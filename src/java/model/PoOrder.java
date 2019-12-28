/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pmadalin
 */
@Entity
@Table(name = "ORDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoOrder.findAll", query = "SELECT p FROM PoOrder p")
    , @NamedQuery(name = "PoOrder.findByOrderId", query = "SELECT p FROM PoOrder p WHERE p.orderId = :orderId")
    , @NamedQuery(name = "PoOrder.findByPoNumber", query = "SELECT p FROM PoOrder p WHERE p.poNumber = :poNumber")
    , @NamedQuery(name = "PoOrder.findByStatus", query = "SELECT p FROM PoOrder p WHERE p.status = :status")
    , @NamedQuery(name = "PoOrder.findByOrderDate", query = "SELECT p FROM PoOrder p WHERE p.orderDate = :orderDate")
    , @NamedQuery(name = "PoOrder.findByComponent", query = "SELECT p FROM PoOrder p WHERE p.component = :component")
    , @NamedQuery(name = "PoOrder.findByDueDate", query = "SELECT p FROM PoOrder p WHERE p.dueDate = :dueDate")
    , @NamedQuery(name = "PoOrder.findByQt", query = "SELECT p FROM PoOrder p WHERE p.qt = :qt")})
public class PoOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Integer orderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PO_NUMBER")
    private int poNumber;
    @Column(name = "STATUS")
    private Boolean status;
    @Size(max = 30)
    @Column(name = "ORDER_DATE")
    private String orderDate;
    @Size(max = 50)
    @Column(name = "COMPONENT")
    private String component;
    @Size(max = 30)
    @Column(name = "DUE_DATE")
    private String dueDate;
    @Column(name = "QT")
    private Integer qt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poNumber", fetch = FetchType.EAGER)
    private List<Bid> bidList;

    public PoOrder() {
    }

    public PoOrder(Integer orderId) {
        this.orderId = orderId;
    }

    public PoOrder(Integer orderId, int poNumber) {
        this.orderId = orderId;
        this.poNumber = poNumber;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public int getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(int poNumber) {
        this.poNumber = poNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getQt() {
        return qt;
    }

    public void setQt(Integer qt) {
        this.qt = qt;
    }

    @XmlTransient
    public List<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoOrder)) {
            return false;
        }
        PoOrder other = (PoOrder) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PoOrder[ orderId=" + orderId + " ]";
    }

}
