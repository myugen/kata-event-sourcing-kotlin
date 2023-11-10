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
}