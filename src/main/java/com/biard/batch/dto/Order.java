package com.biard.batch.dto;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "order")
public class Order implements Serializable, ResourceAware {
    /**
     * Data spring batch
     */
    private transient Resource resource;
    private String custId;
    private String orderNum;
    private String country;

    /**
     * @return
     */
    @XmlElement(name = "CUST_ID")
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @XmlElement(name = "ORDERNUM")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @XmlElement(name = "COUNTRY")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * <p> Get the field resource. </p>
     *
     * @return org.springframework.core.io.Resource : value of field
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * <p> Set the field resource. </p>
     * Spring batch can set the resource to the Object because {@code }implements ResourceAware}
     *
     * @param pResource the field to set
     */
    @Override
    public void setResource(final Resource pResource) {
        resource = pResource;
    }
}
