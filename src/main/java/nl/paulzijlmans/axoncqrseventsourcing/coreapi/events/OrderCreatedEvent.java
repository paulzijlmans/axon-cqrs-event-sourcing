package nl.paulzijlmans.axoncqrseventsourcing.coreapi.events;

public record OrderCreatedEvent(String orderId, String productId) {
}
