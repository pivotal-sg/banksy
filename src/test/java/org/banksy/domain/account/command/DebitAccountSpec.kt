package org.banksy.domain.account.command

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.aggregate.AccountAggregate
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.command.CreditAccount
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountDebited
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class DebitAccountSpec : KSpec(){
    override fun spec()
    {
        describe("debiting an account") {

            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber)
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

            it("results in a AccountDebited event being persisted") {
                val credit = CreditAccount(accountNumber, 100)
                accountService.handle(credit)

                val command = DebitAccount(accountNumber, 100)
                val response = accountService.handle(command)

                assertThat(response.success).isTrue()
                val lastEvent = eventLog.latest()
                if (lastEvent is AccountDebited) {
                    assertThat(lastEvent.accountNumber).isEqualTo("123")
                    assertThat(lastEvent.amount).isEqualTo(100)
                } else {
                    fail("lastEvent wasn't an `AccountDebited`")
                }
            }

            it("don't accept no negative debit amounts") {
                val command = DebitAccount(accountNumber, -1)
                var (T, success) = accountService.handle(command)

                assertThat(success).isFalse()
            }

            it("don't accept no zero debit amounts") {
                val command = DebitAccount(accountNumber, 0)
                var (T, success) = accountService.handle(command)

                assertThat(success).isFalse()
            }

            it("respects the overdraft limits") {
                val amount = 1L
                val command = DebitAccount(accountNumber, amount)

                var (content, success, errors) = accountService.handle(command)

                assertThat(success).isFalse()
                assertThat(errors).contains("Overdraft Limit Exceeded")
            }

        }
    }
}
