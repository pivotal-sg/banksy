package org.banksy.domain.account.command

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.event.AccountInterestPaid
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(JUnitKSpecRunner::class)
class PayInterestForAccountSpec : KSpec() {
    override fun spec() {
        describe("pay interest on account") {
            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber, -100)
            val bus = EventBus()
            var accountRepo = AccountRepository()
            var eventLog = EventLog(bus)
            var accountService = AccountService(accountRepo, eventLog)
            var creditAccountCommand = CreditAccount(accountNumber, 10)

            beforeEach {
                accountService.handle(createAccountCommand)
                accountService.handle(creditAccountCommand)
            }

            afterEach {
                accountRepo = AccountRepository()
                eventLog = EventLog(bus)
                accountService = AccountService(accountRepo, eventLog)
            }

            it("results in an AccountInterestPaid event being persisted") {
                val interestPaid = PayInterestForAccount(accountNumber, BigDecimal(0.1))
                accountService.handle(interestPaid)

                val lastEvent = eventLog.latest()

                if (lastEvent is AccountInterestPaid) {
                    assertThat(lastEvent.accountNumber).isEqualTo(accountNumber)
                    assertThat(lastEvent.interestRate).isEqualTo(BigDecimal(0.1))
                    assertThat(lastEvent.afterBalance).isEqualTo(11)
                } else {
                    fail("lastEvent wasn't `AccountInterestCharged`")
                }
            }

            it("doesn't accept zero interest percent amounts") {
                val command = PayInterestForAccount(accountNumber, BigDecimal.ZERO)

                val response = accountService.handle(command)
                assertThat(response.success).isFalse()
                assertThat(response.errors).containsOnly("Can only pay a positive interest percent amount")
            }

            it("doesn't accept negative interest percent amounts") {
                val command = PayInterestForAccount(accountNumber, -BigDecimal.ONE)

                val response = accountService.handle(command)
                assertThat(response.success).isFalse()
                assertThat(response.errors).containsOnly("Can only pay a positive interest percent amount")
            }

            it("doesn't pay accounts with zero balance") {
                val debitToNothing = DebitAccount(accountNumber, 10)
                val response = accountService.handle(debitToNothing)
                assertThat(response.success).isTrue()

                val interestPercent = BigDecimal(0.01)
                val command = PayInterestForAccount(accountNumber, interestPercent)

                var (details, success, errors) = accountService.handle(command)
                assertThat(success).isFalse()
                assertThat(details?.accountNumber).isEqualTo(accountNumber)
                assertThat(details?.interestRate).isCloseTo(interestPercent, within(BigDecimal(0.00001)))
                assertThat(errors).containsOnly("Can only pay for accounts with positive balance")
            }

            it("doesn't pay accounts with negative balance") {
                val debitToNegativeBalance = DebitAccount(accountNumber, 11)
                val response = accountService.handle(debitToNegativeBalance)
                assertThat(response.success).isTrue()

                val interestPercent = BigDecimal(0.01)
                val command = PayInterestForAccount(accountNumber, interestPercent)

                var (details, success, errors) = accountService.handle(command)
                assertThat(success).isFalse()
                assertThat(details?.accountNumber).isEqualTo(accountNumber)
                assertThat(details?.interestRate).isCloseTo(interestPercent, within(BigDecimal(0.00001)))
                assertThat(errors).containsOnly("Can only pay for accounts with positive balance")
            }
        }
    }
}

