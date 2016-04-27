package org.banksy.eventlog

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(JUnitKSpecRunner::class)
class EventLogSpec: KSpec() {
    override fun spec() {
        describe("save") {
            var eventLog = EventLog()

            // reset eventlog
            afterEach {
                eventLog = EventLog()
            }

            it("Publishes events that are saved") {
                class AnEvent {}

                var mockedEventBus = mock(EventBus::class.java)
                eventLog = EventLog(mockedEventBus)

                var eventUnderTest = AnEvent()
                eventLog.save(eventUnderTest)
                verify(mockedEventBus).post(eventUnderTest)

            }
        }
    }
}


