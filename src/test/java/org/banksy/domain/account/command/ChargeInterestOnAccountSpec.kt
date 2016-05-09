package org.banksy.domain.account.command

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.event.AccountInterestCharged
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(JUnitKSpecRunner::class)
class ChargeInterestOnAccountSpec : KSpec(){
    override fun spec()
    {
        describe("charge interest on account") {
            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber, -100)
            val bus = EventBus()
            var accountRepo = AccountRepository()
            var eventLog = EventLog(bus)
            var accountService = AccountService(accountRepo, eventLog)
            var debitAccountCommand = DebitAccount(accountNumber, 10) // account at -10

            beforeEach {
                accountService.handle(createAccountCommand)
                accountService.handle(debitAccountCommand)
            }

            afterEach {
                accountRepo = AccountRepository()
                eventLog = EventLog(bus)
                accountService = AccountService(accountRepo, eventLog)
            }

            it("results in an InterestCharged event being persisted") {
                val interestCharge = ChargeInterestOnAccount(accountNumber, BigDecimal(0.1))
                accountService.handle(interestCharge)

                val lastEvent = eventLog.latest()

                if (lastEvent is AccountInterestCharged) {
                    assertThat(lastEvent.accountNumber).isEqualTo(accountNumber)
                    assertThat(lastEvent.overdraftInterestRate).isEqualTo(BigDecimal(0.1))
                    assertThat(lastEvent.closingBalance).isEqualTo(-11)
                } else {
                    fail("lastEvent wasn't `AccountInterestCharged`")
                }
            }

            it("don't accept zero interest percent amounts") {
                val command = ChargeInterestOnAccount(accountNumber, BigDecimal.ZERO)

                val response = accountService.handle(command)
                assertThat(response.success).isFalse()
                assertThat(response.errors).containsOnly("Can only charge a positive interest percent amount")
            }

            it("isn't affected by overdraft limits") {
                val debitToOverdraftLimit = DebitAccount(accountNumber, 90)
                val response = accountService.handle(debitToOverdraftLimit)
                assertThat(response.success).isTrue()

                val overdraftInterestRate = BigDecimal(0.01)
                val command = ChargeInterestOnAccount(accountNumber, overdraftInterestRate)

                var (details, success, errors) = accountService.handle(command)
                assertThat(success).isTrue()
                assertThat(details?.accountNumber).isEqualTo(accountNumber)
                assertThat(details?.overdraftInterestRate).isCloseTo(overdraftInterestRate, within(BigDecimal(0.00001)))
                assertThat(errors).isEmpty()
            }
       }
    }
}
