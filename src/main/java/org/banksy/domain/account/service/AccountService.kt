package org.banksy.domain.account.service

import org.banksy.domain.account.aggregate.AccountAggregate
import org.banksy.domain.account.command.AccountCreationDetails
import org.banksy.domain.account.command.AccountCreate
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
     * @param accountCreateCommand Create account command
     */
    fun handle(accountCreateCommand: AccountCreate): CommandResponse<AccountCreationDetails> {
        val accountNumber = accountCreateCommand.accountNumber
        if (accountNumber.isBlank()) {
            return CommandResponse<AccountCreationDetails>(null, false)
        }
        val newAccount = accountRepo.findOrCreate(accountNumber)
        val createdAccount = AccountCreated(accountNumber)
        eventLog.save(createdAccount)
        newAccount.apply(createdAccount)

        return CommandResponse<AccountCreationDetails>(AccountCreationDetails(accountNumber) ,true)
    }
}
