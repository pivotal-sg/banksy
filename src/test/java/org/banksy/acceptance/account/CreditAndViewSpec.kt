package org.banksy.acceptance.account

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.banksy.domain.account.command.CreateAccount
import org.banksy.domain.account.command.CreditAccount
import org.banksy.domain.account.query.AccountInfo
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreditAndViewSpec : KSpec() {
    override fun spec() {

        describe("Credit an account and then viewing it") {
            val accountNumber = "123"
            val createAccountCommand = CreateAccount(accountNumber, 0L)

            val bus = EventBus()
            var eventLog = EventLog(bus)
            var accountRepo = AccountRepository()
            var accountService = AccountService(accountRepo, eventLog)

            it("Accumulates multiple credits") {
                val accountQueryService = AccountQueryService(bus)

                accountService.handle(createAccountCommand)

                val credits = longArrayOf(100, 12, 34, 100)
                credits.forEach { accountService.handle(CreditAccount(accountNumber, it)) }

                val accountInfo: AccountInfo? = accountQueryService.accountInfo(accountNumber)
                assertThat(accountInfo?.balance).isEqualTo(246)
            }
        }
    }
}
