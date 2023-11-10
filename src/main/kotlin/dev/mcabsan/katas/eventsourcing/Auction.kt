package dev.mcabsan.katas.eventsourcing

import java.time.Instant
import java.util.*

class Auction private constructor(
    var id: String,
    var itemDescription: String,
    var initialPrice: Int,
    val changes: MutableList<BaseEvent> = mutableListOf()
) {
    private fun apply(event: BaseEvent) {
        when (event) {
            is AuctionCreated -> apply(event)
        }
    }

    private fun apply(event: AuctionCreated) {
        this.id = event.id
        this.itemDescription = event.itemDescription
        this.initialPrice = event.initialPrice
    }

    companion object {
        private fun empty(): Auction {
            return Auction("", "", 0)
        }

        fun loadFromHistory(events: List<BaseEvent>): Auction {
            val auction = empty()
            events.forEach { auction.apply(it) }
            return auction
        }

        fun create(itemDescription: String, initialPrice: Int): Auction {
            val id = UUID.randomUUID().toString()
            val eventOccurredAt = Instant.now()
            val auctionCreated = AuctionCreated(id, eventOccurredAt, itemDescription, initialPrice)
            return Auction(id, itemDescription, initialPrice)
                .apply { changes.add(auctionCreated) }
        }
    }
}
