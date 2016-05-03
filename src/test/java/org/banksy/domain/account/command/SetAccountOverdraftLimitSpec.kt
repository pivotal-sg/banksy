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
import org.banksy.domain.account.event.AccountOverdraftLimitSet
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class SetAccountOverdraftLimitSpec : KSpec(){
    override fun spec()
    {
        describe("Setting overdraft limit") {

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

            it("set overdraft limit") {
                val command = SetAccountOverdraftLimit(accountNumber, -100)
                val response = accountService.handle(command)

                assertThat(response.success).isTrue()

                val accountAggregate = accountRepo.find(accountNumber)!!
                assertThat(accountAggregate.overdraftLimit).isEqualTo(-100)

                val lastEvent = eventLog.latest()
                if (lastEvent is AccountOverdraftLimitSet) {
                    assertThat(lastEvent.accountNumber).isEqualTo("123")
                    assertThat(lastEvent.overdraftLimit).isEqualTo(-100)
                } else {
                    fail("lastEvent wasn't an `AccountOverdraftLimitSet`")
                }
            }
        }
    }
}
