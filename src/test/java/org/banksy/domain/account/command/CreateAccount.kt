package org.banksy.domain.account.command

import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreateAccountSpec: KSpec() {
    override fun spec()
    {
        describe("#create") {
            var eventLog = EventLog()
            var accountRepo = AccountRepository()
            var accountService = AccountService(accountRepo, eventLog)

            afterEach {
                // Reset the storages/service.
                eventLog = EventLog()
                accountRepo = AccountRepository()
                accountService = AccountService(accountRepo, eventLog)
            }

            it("Successfully create an Account") {
                val command = AccountCreate("123")
                val response = accountService.handle(command)

                assertThat(response).isInstanceOf(CommandResponse::class.java)
                assertThat(response.success).isTrue()
            }

            it("Fail to create an account with a blank accountNumber") {
                val command = AccountCreate("")
                val response = accountService.handle(command)

                assertThat(response).isInstanceOf(CommandResponse::class.java)
                assertThat(response.success).isFalse()
            }

            it("create results in a AccountCreated event being persisted") {
                val command = AccountCreate("123")
                accountService.handle(command)

                val lastEvent = eventLog.latest()
                if (lastEvent is AccountCreated) {
                    assertThat(lastEvent.accountNumber).isEqualTo("123")
                } else {
                    fail("lastEvent wasn't an `AccountCreated`")
                }
            }
        }
    }
}
