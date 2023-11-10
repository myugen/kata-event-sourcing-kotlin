package dev.mcabsan.katas.eventsourcing

import java.time.Instant
import java.util.*

class Auction private constructor(
    var id: String,
    var itemDescription: String,
    var initialPrice: Int,
    var currentBid: Int = 0,
    var currentBidder: String = "",
    var closed: Boolean = false,
    val changes: MutableList<BaseEvent> = mutableListOf()
) {
    fun makeBid(amount: Int, bidder: String) {
        val event = AuctionNewBidV2(id, Instant.now(), amount, bidder)
        changes.add(event)

        applyEvent(event)
    }

    fun close() {
        val event = AuctionClosed(id, Instant.now())
        changes.add(event)

        applyEvent(event)
    }

    private fun applyEvent(event: DomainEvent) {
        when (event) {
            is AuctionCreated -> apply(event)
            is AuctionNewBidV2 -> apply(event)
            is AuctionClosed -> apply(event)
        }
    }

    private fun apply(event: AuctionCreated) {
        this.id = event.id
        this.itemDescription = event.itemDescription
        this.initialPrice = event.initialPrice
    }

    private fun apply(event: AuctionNewBidV2) {
        this.currentBid = event.amount
        this.currentBidder = event.bidder
    }

    private fun apply(event: AuctionClosed) {
        this.closed = true
    }

    companion object {
        private fun empty(): Auction = Auction("", "", 0)
        fun loadFromHistory(events: List<DomainEvent>): Auction = empty()
            .apply { events.forEach(this::applyEvent) }

        fun create(itemDescription: String, initialPrice: Int): Auction {
            val id = UUID.randomUUID().toString()
            val eventOccurredAt = Instant.now()
            val auctionCreated = AuctionCreated(id, eventOccurredAt, itemDescription, initialPrice)
            return Auction(id, itemDescription, initialPrice)
                .apply { changes.add(auctionCreated) }
        }
    }
}
