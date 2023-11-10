package dev.mcabsan.katas.eventsourcing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EventSourcingKataTest {
    private val repository = InMemoryAuctionEventsRepository()

    @Test
    fun `should create an auction`() {
        val auction = Auction.create("Mario Bros NES", 10000)

        repository.save(auction)

        assertThat(repository.getById(auction.id))
            .usingRecursiveComparison()
            .ignoringFields("changes")
            .isEqualTo(auction)
    }

    @Test
    fun `should make a bid`() {
        val auction = Auction.create("Mario Bros NES", 10000)

        auction.makeBid(20000)
        auction.makeBid(30000)

        repository.save(auction)
        val actual = repository.getById(auction.id)

        assertThat(actual.currentBid).isEqualTo(30000)
    }
}