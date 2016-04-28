package org.banksy.domain.account.aggregate

import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountDebited

class AccountAggregate {
    fun apply(accountCreated: AccountCreated) {
        // TODO...
    }

    fun apply(accountCredited: AccountCredited) {
        // TODO...
    }

    fun apply(accountDebited: AccountDebited) {
        // TODO...
    }
}
