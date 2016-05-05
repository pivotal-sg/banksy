package org.banksy.acceptance.account

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.assertj.core.api.Assertions.assertThat
import org.banksy.domain.account.command.CreateAccount
import org.banksy.domain.account.query.AccountInfo
import org.banksy.domain.account.query.AccountQueryService
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.domain.account.service.AccountService
import org.banksy.eventlog.EventLog
import org.junit.runner.RunWith

@RunWith(JUnitKSpecRunner::class)
class CreateAndViewSpec : KSpec() {
    override fun spec() {

        describe("creating an account and then viewing it") {
            val bus = EventBus()
            val accountRepo = AccountRepository()

            var eventLog = EventLog(bus)
            var accountService = AccountService(accountRepo, eventLog)

            afterEach {
                eventLog = EventLog(bus)
                accountService = AccountService(accountRepo, eventLog)
            }

            it("creates a viewable account") {
                val accountCreateCommand = CreateAccount("123", 0L)
                val accountQueryService = AccountQueryService(bus)

                accountService.handle(accountCreateCommand)

                val accountInfo: AccountInfo? = accountQueryService.accountInfo("123")
                assertThat(accountInfo?.balance).isEqualTo(0)
            }
        }
    }
}

