package com.biard.batch.processor;

import com.biard.batch.dto.Order;
import com.biard.batch.dto.ResourceMap;
import com.biard.batch.listener.ChangeExitStatusExecutionListener;
import com.biard.batch.mapper.OrderDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring-Batch ItemProcessor to transform Json File to Order Object.
 */
public class JSonToOrderItemProcessor implements ItemProcessor<ResourceMap, List<Order>>, ItemStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSonToOrderItemProcessor.class);
    private ExecutionContext exectutionContext;
    private String pattern = null;

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     * provided item should not continue.
     * @throws Exception
     */
    @Override
    public List<Order> process(final ResourceMap item) throws Exception {
        final List<Order> outputs = new ArrayList<Order>();

        for (final Map.Entry entry : item.entrySet()) {
            LOGGER.info("[{}]: {}: {}", item.getResource(), entry.getKey(), entry.getValue());
            if ("orders".equals(entry.getKey())) {
                final List<Map<String, Object>> myOrders = (List<Map<String, Object>>) entry.getValue();
                for (final Map<String, Object> mapOrder : myOrders) {
                    for (final Map.Entry<String, Object> order : mapOrder.entrySet()) {
                        LOGGER.info("{} : {}", order.getKey(), order.getValue());
                        final Order theOrder = getOrder(order.getValue());
                        LOGGER.info("Order{OrderNum={}, CustId={}, country={}}", theOrder.getOrderNum(),
                                theOrder.getCustId(), theOrder.getCountry());
                        theOrder.setResource(item.getResource());
                        outputs.add(theOrder);
                    }

                }

            }
            final String exitStatus = getExitCode(item.getResource());
            if (exitStatus != null) {
                // To change exitStatus of step
                exectutionContext.put(ChangeExitStatusExecutionListener.EXIT_STATUS, exitStatus);
            }
        }

        return outputs;
    }

    private Order getOrder(final Object object) {
        final FieldSetMapper<Order> mapper = new OrderDataMapper();
        Order order = null;
        try {
            order = mapper.mapFieldSet(getFieldSet((Map<String, Object>) object));
        } catch (final BindException e) {
            LOGGER.error("Can't mapFieldSet : ", e);
        }
        return order;
    }

    private FieldSet getFieldSet(final Map<String, Object> input) {
        final String[] tokens = new String[input.size()];
        final String[] names = new String[input.size()];
        int cpt = 0;
        for (final Map.Entry<String, Object> entry : input.entrySet()) {
            tokens[cpt] = toStringFieldSet(entry.getValue());
            names[cpt] = entry.getKey();
            cpt++;
        }
        return new DefaultFieldSet(tokens, names);
    }

    private String toStringFieldSet(final Object obj) {
        final String output;
        if (obj instanceof String) {
            output = (String) obj;
        } else if (obj instanceof Boolean) {
            if ((Boolean) obj) {
                output = "true";
            } else {
                output = "false";
            }
        } else {
            output = obj.toString();
        }
        return output;
    }


    /**
     * @param resource
     * @return
     */
    private String getExitCode(final Resource resource) {
        final String endValue;
        if (resource == null) {
            endValue = null;
        } else if (resource.getFilename() == null) {
            endValue = null;
        } else if (this.pattern == null) {
            endValue = null;
        } else {
            //'(.*?)' in some string with 'the data i want' inside
            final Pattern pPattern = Pattern.compile(this.pattern);
            final Matcher matcher = pPattern.matcher(resource.getFilename());
            if (matcher.find()) {
                endValue = matcher.group(1);
            } else {
                endValue = null;
            }
        }
        return endValue;
    }

    /**
     * <p> Set the field pattern. </p>
     *
     * @param pPattern the field to set
     */
    public void setPattern(final String pPattern) {
        pattern = pPattern;
    }

    /**
     * If any resources are needed for the stream to operate they need to be destroyed here. Once this method has been
     * called all other methods (except open) may throw an exception.
     */
    @Override
    public void close() {
// Nothing to close
    }

    /**
     * Open the stream for the provided {@link ExecutionContext}.
     *
     * @param pExecutionContext
     * @throws IllegalArgumentException if context is null
     */
    @Override
    public void open(final ExecutionContext pExecutionContext) {
        exectutionContext = pExecutionContext;
    }

    /**
     * Indicates that the execution context provided during open is about to be saved. If any state is remaining, but
     * has not been put in the context, it should be added here.
     *
     * @param pExecutionContext to be updated
     * @throws IllegalArgumentException if executionContext is null.
     */
    @Override
    public void update(final ExecutionContext pExecutionContext) {
        exectutionContext = pExecutionContext;
    }


}
