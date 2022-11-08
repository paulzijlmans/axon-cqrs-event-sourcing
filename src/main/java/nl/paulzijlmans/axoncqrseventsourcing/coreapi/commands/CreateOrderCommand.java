package nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateOrderCommand(@TargetAggregateIdentifier String orderId, String productId) {
}
