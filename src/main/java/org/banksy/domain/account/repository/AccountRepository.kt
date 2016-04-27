package org.banksy.domain.account.repository

import org.banksy.domain.account.aggregate.AccountAggregate

class AccountRepository {
    fun findOrCreate(accountNumber: String): AccountAggregate {
        return AccountAggregate()
    }
}
