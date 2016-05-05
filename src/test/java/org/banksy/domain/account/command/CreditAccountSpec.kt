package org.banksy.domain.account.command

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreditAccountSpec : KSpec(){
    override fun spec()
    {
        describe("Crediting an account") {

            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber, 0L)
            val bus = EventBus()

            var accountRepo = AccountRepository()
            var eventLog = EventLog(bus)
            var accountService = AccountService(accountRepo, eventLog)

            beforeEach {
                accountService.handle(createAccountCommand)
            }

            afterEach {
                accountRepo = AccountRepository()
                eventLog = EventLog(bus)
                accountService = AccountService(accountRepo, eventLog)
            }

            it("results in a AccountCredited event being persisted") {

                val command = CreditAccount(accountNumber, 100)
                val response = accountService.handle(command)

                assertThat(response.success).isTrue()
                val lastEvent = eventLog.latest()
                if (lastEvent is AccountCredited) {
                    assertThat(lastEvent.accountNumber).isEqualTo("123")
                    assertThat(lastEvent.amount).isEqualTo(100)
                } else {
                    System.out.print("BOO: " + lastEvent.javaClass)
                    fail("lastEvent wasn't an `AccountCredited`")
                }
            }

            it("don't accept no negative credit amounts") {
                val command = CreditAccount(accountNumber, -1)
                var (T, success) = accountService.handle(command)

                assertThat(success).isFalse()
            }

            it("don't accept no zero credit amounts") {
                val command = CreditAccount(accountNumber, 0)
                var (T, success) = accountService.handle(command)

                assertThat(success).isFalse()
            }

        }
    }
}
