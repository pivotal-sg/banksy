package org.banksy.domain.account.event

import io.damo.kspec.Spec
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.service.AccountService

class CreateAccountSpec: Spec({
    describe("#accountCreated") {
        test("Constructor defaults are sane") {
            val accountCreated1 = AccountCreated("123")
            val accountCreated2 = AccountCreated("321")

            assertThat(accountCreated1.eventID).isNotEqualTo(accountCreated2.eventID)
        }
    }
})
