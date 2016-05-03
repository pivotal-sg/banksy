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

@RunWith(JUnitKSpecRunner::class)
class DebitAndViewSpec : KSpec() {
    override fun spec() {

        describe("View correct balance from the account info query service") {
            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber)

            val bus = EventBus()
            var eventLog = EventLog(bus)
            var accountRepo = AccountRepository()
            var accountService = AccountService(accountRepo, eventLog)

            it("handles credits and debits") {
                val accountQueryService = AccountQueryService(bus)

                accountService.handle(createAccountCommand)
                accountService.handle(CreditAccount(accountNumber, 300))

                val debits = longArrayOf(100, 100, 100)
                debits.forEach { accountService.handle(DebitAccount(accountNumber, it)) }

                val accountInfo: AccountInfo? = accountQueryService.accountInfo(accountNumber)
                assertThat(accountInfo?.balance).isEqualTo(0)
            }
        }
    }
}
