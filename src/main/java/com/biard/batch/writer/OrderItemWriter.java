package com.biard.batch.writer;

import com.biard.batch.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Writer for Order
 */
public class OrderItemWriter implements ItemWriter<Order> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemWriter.class);
    private static final String INSERT_ORDER = "insert into `order` ( `ORDERNUM`, `CUST_ID`, `COUNTRY` ) values(?,?,?)";
    private JdbcTemplate jdbcTemplate;


    /**
     * @param ds
     */
    public OrderItemWriter(DataSource ds) {
        LOGGER.info("DataSource={}", ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void write(List<? extends Order> orders) throws Exception {

        for (final Order order : orders) {
            LOGGER.info("File {}", order.getResource());
            if (order.getResource() != null) {
                LOGGER.info("File Name {}", order.getResource().getFile().getName());
                jdbcTemplate.update(INSERT_ORDER, order.getOrderNum(),
                        order.getCustId(), order.getCountry());
            }
            LOGGER.info("Order{OrderNum={}, CustId={}, country={}}", order.getOrderNum(),
                    order.getCustId(), order.getCountry());
        }

    }

}
