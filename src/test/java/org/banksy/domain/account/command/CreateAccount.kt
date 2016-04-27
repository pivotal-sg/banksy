package org.banksy.domain.account.command

import io.damo.kspec.Spec
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.service.AccountService

class CreateAccountSpec: Spec({
    describe("#create") {
        test {
            val command = Create("123")
            var accountService = AccountService()

            val response = accountService.handle(command)

            assertThat(response).isInstanceOf(CommandResponse::class.java)
            assertThat(response.success).isTrue()
        }
    }
})
