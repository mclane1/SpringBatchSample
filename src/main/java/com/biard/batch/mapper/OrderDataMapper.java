package com.biard.batch.mapper;

import com.biard.batch.dto.Order;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class OrderDataMapper implements FieldSetMapper<Order> {

    @Override
    public Order mapFieldSet(FieldSet fieldSet) throws BindException {

        Order order = new Order();
        order.setCustId(fieldSet.readString(0));
        order.setOrderNum(fieldSet.readString(1));
        order.setCountry(fieldSet.readString(2));
        return order;

    }

}
