package org.banksy.domain.account.repository

import org.banksy.domain.account.aggregate.AccountAggregate
import org.banksy.domain.account.command.SetAccountOverdraftLimit
import java.util.*

class AccountRepository {
    val repo = HashMap<String, AccountAggregate>()

    fun add(accountNumber: String): AccountAggregate {
        val aggregate = AccountAggregate()
        repo.put(accountNumber, aggregate)
        return aggregate
    }

    fun find(accountNumber: String): AccountAggregate? {
        return repo[accountNumber]
    }

    fun save(accountNumber: String, accountAggregate: AccountAggregate) {
        repo.put(accountNumber, accountAggregate)
    }
}
