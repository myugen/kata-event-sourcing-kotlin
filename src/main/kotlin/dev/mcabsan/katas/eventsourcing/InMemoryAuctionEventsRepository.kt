package dev.mcabsan.katas.eventsourcing

class InMemoryAuctionEventsRepository(
    private val eventsStore: MutableMap<String, MutableList<BaseEvent>> = mutableMapOf()
) {
    fun save(auction: Auction) {
        eventsStore[auction.id] = auction.changes
    }

    fun getById(id: String): Auction {
        val events = eventsStore[id] ?: emptyList()
        return Auction.loadFromHistory(events)
    }
}