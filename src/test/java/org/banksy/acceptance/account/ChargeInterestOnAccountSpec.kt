package org.banksy.acceptance.account

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.banksy.domain.account.command.ChargeInterestOnAccount
import org.banksy.domain.account.command.CreateAccount
import org.banksy.domain.account.command.DebitAccount
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(JUnitKSpecRunner::class)
class ChargeInterestOnAccountSpec : KSpec() {
    override fun spec() {

        describe("Charge interest (20%/pa) on an overdrawn account") {
            val bus = EventBus()
            val eventLog = EventLog(bus)
            val accountRepo = AccountRepository()
            val accountService = AccountService(accountRepo, eventLog)
            val accountQueryService = AccountQueryService(bus)
            val overdrawnAccountNumber = "123"

            it("performs interest charge") {
                accountService.handle(CreateAccount(overdrawnAccountNumber, -100))

                // NOTE we do not want to cover exceeding overdraft limit yet. TODO
                accountService.handle(DebitAccount(overdrawnAccountNumber, 10))
                accountService.handle(ChargeInterestOnAccount(overdrawnAccountNumber, BigDecimal(0.1))) // TODO setting for Overdraft interest

                val chargedAccount = accountQueryService.accountInfo(overdrawnAccountNumber)!!
                assertThat(chargedAccount.balance).isEqualTo(-11)
            }
        }
    }
}



