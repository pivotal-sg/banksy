package org.banksy.domain.account.query

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.banksy.domain.account.event.AccountCreated
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

    fun accountInfo(accountNumber: String): AccountInfo? {
        return repo.get(accountNumber)
    }
}

