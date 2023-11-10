package dev.mcabsan.katas.eventsourcing

import java.time.Instant

sealed interface BaseEvent {
    val id: String
    val occurredAt: Instant
}

sealed interface DomainEvent : BaseEvent
sealed interface DeprecatedEvent : BaseEvent {
    fun toLatestVersion(): DomainEvent
}

data class AuctionCreated(
    override val id: String,
    override val occurredAt: Instant,
    val itemDescription: String,
    val initialPrice: Int
) : DomainEvent

data class AuctionNewBid(
    override val id: String,
    override val occurredAt: Instant,
    val amount: Int
) : DeprecatedEvent {
    override fun toLatestVersion(): DomainEvent = AuctionNewBidV2(id, occurredAt, amount, "unknown")
}

data class AuctionNewBidV2(
    override val id: String,
    override val occurredAt: Instant,
    val amount: Int,
    val bidder: String
) : DomainEvent

data class AuctionClosed(
    override val id: String,
    override val occurredAt: Instant
) : DomainEvent