package nl.paulzijlmans.axoncqrseventsourcing.commandmodel.order;

import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.CreateOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands.ShipOrderCommand;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderConfirmedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderCreatedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.events.OrderShippedEvent;
import nl.paulzijlmans.axoncqrseventsourcing.coreapi.exceptions.UnconfirmedOrderException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderAggregateTest {

    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_ID = UUID.randomUUID().toString();

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void givenNoPriorActivity_whenCreateOrderCommand_thenShouldPublishOrderCreatedEvent() {
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(ORDER_ID, PRODUCT_ID))
                .expectEvents(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderCreatedEvent_whenShipOrderCommand_thenShouldThrowUnconfirmedOrderException() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID))
                .when(new ShipOrderCommand(ORDER_ID))
                .expectException(UnconfirmedOrderException.class);
    }

    @Test
    void givenOrderCreatedEventAndOrderConfirmedEvent_whenShipOrderCommand_thenShouldPublishOrderShippedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID), new OrderConfirmedEvent(ORDER_ID))
                .when(new ShipOrderCommand(ORDER_ID))
                .expectEvents(new OrderShippedEvent(ORDER_ID));
    }
}