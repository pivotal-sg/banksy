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
import org.banksy.domain.account.query.AccountInfo
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitKSpecRunner::class)
class ViewOverdrawnAccountsSpec : KSpec() {
    override fun spec() {

        describe("View overdrawn accounts from the account info query service") {
            val bus = EventBus()
            val eventLog = EventLog(bus)
            val accountRepo = AccountRepository()
            val accountService = AccountService(accountRepo, eventLog)

            it("lists accounts that have exceeded the overdraft limit") {
                val accountQueryService = AccountQueryService(bus)

                val overdrawnAccountNumber = "123"
                accountService.handle(CreateAccount(overdrawnAccountNumber, -100L))
                accountService.handle(CreditAccount(overdrawnAccountNumber, 300))
                accountService.handle(DebitAccount(overdrawnAccountNumber, 350))

                val accountNumber = "888"
                accountService.handle(CreateAccount(accountNumber, -10L))
                accountService.handle(CreditAccount(accountNumber, 200))

                val accountInfos = accountQueryService.overdrawnAccounts()
                val accountNumbers = accountInfos.map { a -> a.accountNumber }
                assertThat(accountNumbers).containsExactly(overdrawnAccountNumber)
            }
        }
    }
}
