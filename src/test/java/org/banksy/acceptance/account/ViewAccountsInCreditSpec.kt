package org.banksy.acceptance.account

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.banksy.domain.account.command.CreateAccount
import org.banksy.domain.account.command.CreditAccount
import org.banksy.domain.account.command.DebitAccount
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class ViewAccountsInCreditSpec : KSpec() {
    override fun spec() {

        describe("View accounts in credit from the account info query service") {
            val bus = EventBus()
            val eventLog = EventLog(bus)
            val accountRepo = AccountRepository()
            val accountService = AccountService(accountRepo, eventLog)

            it("lists accounts that have a positive, non-nil balance") {
                val accountQueryService = AccountQueryService(bus)

                val inCreditAccountNumber = "123"
                accountService.handle(CreateAccount(inCreditAccountNumber, 0))
                accountService.handle(CreditAccount(inCreditAccountNumber, 300))

                val anotherInCreditAccountNumber = "777"
                accountService.handle(CreateAccount(anotherInCreditAccountNumber, 0))
                accountService.handle(CreditAccount(anotherInCreditAccountNumber, 100))

                val inDebitAccountNumber = "888"
                accountService.handle(CreateAccount(inDebitAccountNumber, -100))
                accountService.handle(DebitAccount(inDebitAccountNumber, 20))

                val zeroBalanceAccount = "000"
                accountService.handle(CreateAccount(zeroBalanceAccount, 0))

                val accountInfos = accountQueryService.inCreditAccounts()
                val accountNumbers = accountInfos.map { a -> a.accountNumber }
                assertThat(accountNumbers).containsOnly(anotherInCreditAccountNumber, inCreditAccountNumber)
            }
        }
    }
}
