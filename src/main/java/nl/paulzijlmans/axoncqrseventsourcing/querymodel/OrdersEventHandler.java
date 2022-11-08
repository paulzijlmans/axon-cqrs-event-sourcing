package nl.paulzijlmans.axoncqrseventsourcing.querymodel;

import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderConfirmedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderCreatedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderShippedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.queries.FindAllOrderedProductsQuery;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.queries.Order;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdersEventHandler {

    private final Map<String, Order> orders = new HashMap<>();

    @EventHandler
    public void on(OrderCreatedEvent event) {
        String orderId = event.orderId();
        orders.put(orderId, new Order(orderId, event.productId()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orders.computeIfPresent(event.orderId(), (orderId, order) -> {
            order.setOrderConfirmed();
            return order;
        });
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orders.computeIfPresent(event.orderId(), (orderId, order) -> {
            order.setOrderShipped();
            return order;
        });
    }

    @QueryHandler
    public List<Order> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orders.values());
    }
}
