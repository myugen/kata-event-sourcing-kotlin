package dev.mcabsan.katas.eventsourcing

import java.time.Instant

sealed interface BaseEvent {
    val id: String
    val occurredAt: Instant
}

data class AuctionCreated(
    override val id: String,
    override val occurredAt: Instant,
    val itemDescription: String,
    val initialPrice: Int
) : BaseEvent

data class AuctionNewBid(
    override val id: String,
    override val occurredAt: Instant,
    val amount: Int
) : BaseEvent

data class AuctionNewBidV2(
    override val id: String,
    override val occurredAt: Instant,
    val amount: Int,
    val bidder: String
) : BaseEvent

data class AuctionClosed(
    override val id: String,
    override val occurredAt: Instant
) : BaseEvent