package org.banksy.domain.account.aggregate

import org.banksy.domain.account.command.AccountDebitedDetails
import org.banksy.domain.account.command.DebitAccount
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.AccountCreated
import org.banksy.domain.account.event.AccountCredited
import org.banksy.domain.account.event.AccountDebited
import org.banksy.domain.account.event.AccountEvent
import java.util.*

class AccountAggregate {

    var balance = 0L
    var overdraftLimit = 0L

    fun apply(accountEvent: AccountEvent) {
        when (accountEvent) {
            is AccountCreated -> apply(accountEvent)
            is AccountCredited -> apply(accountEvent)
            is AccountDebited -> apply(accountEvent)
        }
    }

    fun apply(accountCreated: AccountCreated) {
        // TODO...
    }

    fun apply(accountCredited: AccountCredited) {
        balance += accountCredited.amount
    }

    fun apply(accountDebited: AccountDebited) {
        balance -= accountDebited.amount
    }

    fun validate(command: DebitAccount): Pair<CommandResponse<AccountDebitedDetails>, List<AccountEvent>> {
        val accountNumber = command.accountNumber
        val amount = command.amount
        val accountDebitedDetails = AccountDebitedDetails(accountNumber, amount)
        val events = ArrayList<AccountEvent>()

        if (amount <= 0) {
            return Pair(CommandResponse(
                    accountDebitedDetails,
                    false,
                    listOf("Can only debit a positive amount")),
                    events)
        }

        if (balance - amount < overdraftLimit) {
            return Pair(CommandResponse(
                    accountDebitedDetails,
                    false,
                    listOf("Overdraft Limit Exceeded")),
                    events)
        }

        events.add(AccountDebited(accountNumber, amount))
        return Pair(CommandResponse(accountDebitedDetails, true), events)
    }
}


