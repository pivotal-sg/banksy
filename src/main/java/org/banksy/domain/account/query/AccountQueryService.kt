package org.banksy.domain.account.query

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountDebited
import java.util.*


class AccountQueryService {

    constructor(bus: EventBus) {
        bus.register(this)
    }

    val repo = HashMap<String, AccountInfo>()

    @Subscribe
    fun onAccountCreated(event: AccountCreated) {
        val accountNumber = event.accountNumber
        repo.set(accountNumber, AccountInfo(accountNumber, 0))
    }

    @Subscribe
    fun onAccountCredit(event: AccountCredited) {
        val accountNumber = event.accountNumber
        val account : AccountInfo? = repo.get(accountNumber)
        val newAccount = AccountInfo(account!!.accountNumber, account!!.balance + event.amount)
        repo.set(accountNumber, newAccount)
    }

    @Subscribe
    fun onAccountDebit(event: AccountDebited) {
        val accountNumber = event.accountNumber
        val account : AccountInfo? = repo.get(accountNumber)
        val newAccount = AccountInfo(account!!.accountNumber, account!!.balance - event.amount)
        repo.set(accountNumber, newAccount)
    }

    fun accountInfo(accountNumber: String): AccountInfo? {
        return repo.get(accountNumber)
    }
}
