package org.banksy.eventlog

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.assertj.core.api.Assertions.*

@RunWith(JUnitKSpecRunner::class)
class EventLogSpec: KSpec() {
    override fun spec() {

        val mockedEventBus = mock(EventBus::class.java)
        var eventLog = EventLog(mockedEventBus)

        beforeEach {
            reset(mockedEventBus)
            eventLog = EventLog(mockedEventBus)
        }

        describe("save") {
            it("Publishes events that are saved") {
                var eventUnderTest = AnEvent()
                eventLog.save(eventUnderTest)
                verify(mockedEventBus).post(eventUnderTest)
            }
        }

        describe("getting events") {
            it("can get the last event") {
                var firstEvent = AnEvent()
                var lastEvent = AnEvent()
                eventLog.save(firstEvent)
                eventLog.save(lastEvent)
                assertThat(eventLog.latest()).isEqualTo(lastEvent)
            }
        }
    }

    private class AnEvent {}

}
