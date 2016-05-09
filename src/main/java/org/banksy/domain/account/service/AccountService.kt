package org.banksy.domain.account.service

import org.banksy.domain.account.aggregate.AccountAggregate
import org.banksy.domain.account.command.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountEvent
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
     * @param command Create account command
     */
    fun handle(command: CreateAccount): CommandResponse<AccountCreationDetails> {
        val accountNumber = command.accountNumber
        if (accountNumber.isBlank()) {
            return CommandResponse<AccountCreationDetails>(null,
                    false,
                    listOf("Account number cannot be blank"))
        }
        val accountAggregate = accountRepo.add(accountNumber)
        val event = AccountCreated(accountNumber, command.overdraftLimit)
        eventLog.save(event)
        accountAggregate.apply(event)
        accountRepo.save(accountNumber, accountAggregate)

        return CommandResponse<AccountCreationDetails>(AccountCreationDetails(accountNumber) ,true)
    }

    /**
     * When passed a Credit command, persist the event to the event log,  apply it to
     * the correct AccountAggregate (via AccountRepository.find)
     *
     * @param command Credit account command
     */
    fun handle(command: CreditAccount): CommandResponse<AccountCreditedDetails> {
        val accountNumber = command.accountNumber
        val amount = command.amount
        if (amount <= 0) {
           return CommandResponse<AccountCreditedDetails>(
                  AccountCreditedDetails(accountNumber, amount),
                  false,
                  listOf("Can only credit a positive value"))
        }

        val accountAggregate = accountRepo.find(accountNumber)!!
        val accountCredited = AccountCredited(accountNumber, amount, accountAggregate.balance, accountAggregate.balance + amount)
        eventLog.save(accountCredited)
        accountAggregate.apply(accountCredited)

        return CommandResponse<AccountCreditedDetails>(AccountCreditedDetails(accountNumber, amount), true)
    }

    /**
     * When passed a Debit command, persist the event to the event log,  apply it to
     * the correct AccountAggregate (via AccountRepository)
     *
     * @param command Debit account command
     */
    fun handle(command: DebitAccount): CommandResponse<AccountDebitedDetails> {
        val accountNumber = command.accountNumber
        val accountAggregate = accountRepo.find(accountNumber)!!
        val (response, events) = accountAggregate.validateAndGenerateEvents(command)

        process(accountAggregate, events)
        accountRepo.save(accountNumber, accountAggregate)

        return response
    }

    fun handle(command: SetAccountOverdraftLimit): CommandResponse<AccountOverdraftLimitSetDetails> {
        val accountNumber = command.accountNumber
        val accountAggregate = accountRepo.find(accountNumber)!!
        val (response, events) = accountAggregate.validateAndGenerateEvents(command)

        process(accountAggregate, events)
        accountRepo.save(accountNumber, accountAggregate)

        return response
    }

    fun handle(command: ChargeInterestOnAccount): CommandResponse<AccountInterestChargedDetails> {
        val accountNumber = command.accountNumber
        val accountAggregate = accountRepo.find(accountNumber)!!
        val (response, events) = accountAggregate.validateAndGenerateEvents(command)

        process(accountAggregate, events)
        accountRepo.save(accountNumber, accountAggregate)

        return response
    }

    fun handle(command: PayInterestForAccount): CommandResponse<AccountInterestPaidDetails> {
        val accountNumber = command.accountNumber
        val accountAggregate = accountRepo.find(accountNumber)!!
        val (response, events) = accountAggregate.validateAndGenerateEvents(command)

        process(accountAggregate, events)
        accountRepo.save(accountNumber, accountAggregate)

        return response
    }

    private fun process(accountAggregate: AccountAggregate, events: List<AccountEvent>) {
        events.forEach { process(accountAggregate, it) }
    }

    private fun process(accountAggregate: AccountAggregate, event: AccountEvent) {
        eventLog.save(event)
        accountAggregate.apply(event)
    }
}


