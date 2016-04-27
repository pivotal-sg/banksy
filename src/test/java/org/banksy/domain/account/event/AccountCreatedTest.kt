package org.banksy.domain.account.event

import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreateAccountSpec: KSpec() {
    override fun spec() {
        describe("#accountCreated")
        {
            it("Constructor defaults are sane") {
                val accountCreated1 = AccountCreated("123")
                val accountCreated2 = AccountCreated("321")

                assertThat(accountCreated1.eventID).isNotEqualTo(accountCreated2.eventID)
            }
       }
    }
}
