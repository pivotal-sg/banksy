package org.banksy.acceptance.account

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.banksy.domain.account.command.CreateAccount
import org.banksy.domain.account.command.CreditAccount
import org.banksy.domain.account.command.PayInterestForAccount
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(JUnitKSpecRunner::class)
class PayInterestForAccountSpec : KSpec() {
    override fun spec() {

        describe("Pay interest on an account with positive balance") {
            val bus = EventBus()
            val eventLog = EventLog(bus)
            val accountRepo = AccountRepository()
            val accountService = AccountService(accountRepo, eventLog)
            val accountQueryService = AccountQueryService(bus)
            val accountInTheBlack = "123"

            it("performs interest charge") {
                accountService.handle(CreateAccount(accountInTheBlack, 0))

                accountService.handle(CreditAccount(accountInTheBlack, 10))
                accountService.handle(PayInterestForAccount(accountInTheBlack, BigDecimal(0.1)))

                val chargedAccount = accountQueryService.accountInfo(accountInTheBlack)!!
                assertThat(chargedAccount.balance).isEqualTo(11)
            }
        }
    }
}




