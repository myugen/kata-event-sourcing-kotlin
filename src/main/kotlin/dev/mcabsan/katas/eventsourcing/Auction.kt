package dev.mcabsan.katas.eventsourcing

import java.time.Instant
import java.util.*

class Auction private constructor(
    var id: String,
    var itemDescription: String,
    var initialPrice: Int,
    var currentBid: Int = 0,
    val changes: MutableList<BaseEvent> = mutableListOf()
) {
    fun makeBid(amount: Int) {
        val event = AuctionNewBid(id, Instant.now(), amount)
        changes.add(event)

        applyEvent(event)
    }

    private fun applyEvent(event: BaseEvent) {
        when (event) {
            is AuctionCreated -> apply(event)
            is AuctionNewBid -> apply(event)
        }
    }

    private fun apply(event: AuctionCreated) {
        this.id = event.id
        this.itemDescription = event.itemDescription
        this.initialPrice = event.initialPrice
    }

    private fun apply(event: AuctionNewBid) {
        this.currentBid = event.amount
    }

    companion object {
        private fun empty(): Auction = Auction("", "", 0)
        fun loadFromHistory(events: List<BaseEvent>): Auction = empty()
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
