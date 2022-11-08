package nl.paulzijlmans.axoncqrseventsourcing.commandmodel.order;

import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.ConfirmOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.CreateOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.ShipOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderConfirmedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderCreatedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderShippedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.exceptions.UnconfirmedOrderException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.orderId(), command.productId()));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        if (orderConfirmed) {
            return;
        }
        apply(new OrderConfirmedEvent(orderId));
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (!orderConfirmed) {
            throw new UnconfirmedOrderException();
        }
        apply(new OrderShippedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.orderId();
        orderConfirmed = false;
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        orderConfirmed = true;
    }

    protected OrderAggregate() { }
}
