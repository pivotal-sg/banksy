package org.banksy.domain.account.service

import org.banksy.domain.account.aggregate.AccountAggregate
import org.banksy.domain.account.command.AccountCreationDetails
import org.banksy.domain.account.command.Create
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.repository.AccountRepository
import org.banksy.eventlog.EventLog

/**
 * Handles Account commands and events, ensuring that AccountAggregates are up to date.
 */
class AccountService (var accountRepo: AccountRepository, var eventLog: EventLog){

    /**
     * When passed a Create command, persist the event to the event log,  apply it to
     * the newly created AccountAggregate (via AccountRepository)
     *
     * @param create Create account command
     */
    fun handle(create: Create): CommandResponse<AccountCreationDetails> {
        val accountNumber = create.accountNumber

        val newAccount = accountRepo.findOrCreate(accountNumber)
        val createdAccount = AccountCreated(accountNumber)
        eventLog.save(createdAccount)
        newAccount.apply(createdAccount)

        return CommandResponse<AccountCreationDetails>(AccountCreationDetails(accountNumber) ,true)
    }
}
