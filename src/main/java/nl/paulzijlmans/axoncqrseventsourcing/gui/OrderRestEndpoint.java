package nl.paulzijlmans.axoncqrseventsourcing.gui;

import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.ConfirmOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.CreateOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.ShipOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.queries.FindAllOrderedProductsQuery;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.queries.Order;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class OrderRestEndpoint {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderRestEndpoint(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/ship-order")
    public CompletableFuture<Void> shipOrder() {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(orderId, "Deluxe Chair"))
                .thenCompose(result -> commandGateway.send(new ConfirmOrderCommand(orderId)))
                .thenCompose(result -> commandGateway.send(new ShipOrderCommand(orderId)));
    }

    @GetMapping("/all-orders")
    public CompletableFuture<List<Order>> findAllOrders() {
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(Order.class));
    }
}
