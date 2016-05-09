package org.banksy.domain.account.query

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountDebited
import org.banksy.domain.account.event.AccountInterestCharged
import java.math.BigDecimal
import java.util.*


class AccountQueryService {

    constructor(bus: EventBus) {
        bus.register(this)
    }

    val accountRepo = HashMap<String, AccountInfo>()

    @Subscribe
    fun onAccountCreated(event: AccountCreated) {
        val accountNumber = event.accountNumber
        val overdraftLimit = event.overdraftLimit
        accountRepo[accountNumber] = AccountInfo(accountNumber, 0, overdraftLimit)
    }

    @Subscribe
    fun onAccountCredit(event: AccountCredited) {
        val accountNumber = event.accountNumber
        val account: AccountInfo = accountRepo[accountNumber]!!
        val newAccount = AccountInfo(account.accountNumber, event.afterBalance, account.overdraftLimit)
        accountRepo[accountNumber] = newAccount
    }

    @Subscribe
    fun onAccountDebit(event: AccountDebited) {
        val accountNumber = event.accountNumber
        val account: AccountInfo = accountRepo[accountNumber]!!
        val newAccount = AccountInfo(account.accountNumber, event.afterBalance, account.overdraftLimit)
        accountRepo[accountNumber] = newAccount
    }

    @Subscribe
    fun onAccountInterestCharged(event: AccountInterestCharged) {
        val accountNumber = event.accountNumber
        val account: AccountInfo = accountRepo[accountNumber]!!
        val newAccount = AccountInfo(account.accountNumber, event.interestCharged.add(BigDecimal(account.balance)).toLong(), account.overdraftLimit)
        accountRepo[accountNumber] = newAccount
    }

    fun accountInfo(accountNumber: String): AccountInfo? {
        return accountRepo[accountNumber]
    }

    fun overdrawnAccounts(): Collection<AccountInfo> {
        return accountRepo
                .filterValues { accountInfo -> accountInfo.balance < 0 }
                .values
    }

    fun inCreditAccounts(): Collection<AccountInfo> {
        return accountRepo
                .filterValues { accountInfo -> accountInfo.balance > 0 }
                .values
    }
}
