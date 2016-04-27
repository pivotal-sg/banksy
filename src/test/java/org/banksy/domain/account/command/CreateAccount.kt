package org.banksy.domain.account.command

import io.damo.kspec.Spec
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog

class CreateAccountSpec: Spec({
    describe("#create") {
        test("Successfully create an Account") {
            var eventLog = EventLog()
            var accountRepo = AccountRepository()
            var accountService = AccountService(accountRepo, eventLog)

            val command = Create("123")
            val response = accountService.handle(command)

            assertThat(response).isInstanceOf(CommandResponse::class.java)
            assertThat(response.success).isTrue()
        }
    }
})
