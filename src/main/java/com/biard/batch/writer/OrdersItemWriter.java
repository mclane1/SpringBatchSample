package com.biard.batch.writer;

import com.biard.batch.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class OrdersItemWriter implements ItemWriter<List<Order>> {
    private static final String INSERT_ORDER = "insert into `order` ( `ORDERNUM`, `CUST_ID`, `COUNTRY` ) values(?,?,?)";
    private final Logger logger = LoggerFactory.getLogger(OrdersItemWriter.class);
    private JdbcTemplate jdbcTemplate;


    public OrdersItemWriter(DataSource ds) {
        logger.info("DataSource={}", ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void write(List<? extends List<Order>> orderss) throws Exception {

        for (final List<Order> orders : orderss) {
            for (final Order order : orders) {
                logger.info("File {}", order.getResource());
                if (order.getResource() != null) {
                    logger.info("File Name {}", order.getResource().getFile().getName());
                    jdbcTemplate.update(INSERT_ORDER, order.getOrderNum(),
                            order.getCustId(), order.getCountry());
                }
                logger.info("Order{OrderNum={}, CustId={}, country={}}", order.getOrderNum(),
                        order.getCustId(), order.getCountry());
            }
        }

    }

}
