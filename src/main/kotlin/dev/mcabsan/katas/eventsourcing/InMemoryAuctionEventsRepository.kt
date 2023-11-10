package dev.mcabsan.katas.eventsourcing

class InMemoryAuctionEventsRepository(
    private val eventsStore: MutableMap<String, MutableList<BaseEvent>> = mutableMapOf()
) {
    fun save(auction: Auction) {
        eventsStore[auction.id] = auction.changes
    }

    fun getById(id: String): Auction = Auction.loadFromHistory(aggregateEvents(id))

    private fun aggregateEvents(id: String): List<DomainEvent> {
        val events = eventsStore[id] ?: emptyList()
        return events.map {
            when (it) {
                is DomainEvent -> it
                is DeprecatedEvent -> it.toLatestVersion()
            }
        }
    }
}