package org.banksy.domain.account.event

import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class AccountCreatedSpec: KSpec() {
    override fun spec() {
        describe("the default constructor")
        {
            it("assigns a unique Account number") {
                val accountCreated1 = AccountCreated("123", 0L)
                val accountCreated2 = AccountCreated("321", 0L)

                assertThat(accountCreated1.accountNumber).isNotEqualTo(accountCreated2.accountNumber)
            }
       }
    }
}
