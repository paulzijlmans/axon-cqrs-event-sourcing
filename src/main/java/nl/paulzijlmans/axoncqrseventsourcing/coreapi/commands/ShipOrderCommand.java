package nl.paulzijlmans.axoncqrseventsourcing.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ShipOrderCommand(@TargetAggregateIdentifier String orderId) {
}
