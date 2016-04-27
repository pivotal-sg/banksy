package org.banksy.domain.account.event

import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreateAccountSpec: KSpec() {
    override fun spec() {
        describe("the default constructor")
        {
            it("assigns a unique Account number") {
                val accountCreated1 = AccountCreated("123")
                val accountCreated2 = AccountCreated("321")

                assertThat(accountCreated1.accountNumber).isNotEqualTo(accountCreated2.accountNumber)
            }
       }
    }
}
