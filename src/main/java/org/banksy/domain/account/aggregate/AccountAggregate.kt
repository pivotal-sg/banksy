package org.banksy.domain.account.aggregate

import org.banksy.domain.account.command.AccountDebitedDetails
import org.banksy.domain.account.command.AccountOverdraftLimitSetDetails
import org.banksy.domain.account.command.DebitAccount
import org.banksy.domain.account.command.SetAccountOverdraftLimit
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.*
import java.util.*

class AccountAggregate {

    var balance = 0L
    var overdraftLimit = 0L

    fun apply(accountEvent: AccountEvent) {
        when (accountEvent) {
            is AccountCreated -> apply(accountEvent)
            is AccountCredited -> apply(accountEvent)
            is AccountDebited -> apply(accountEvent)
            is AccountOverdraftLimitSet -> apply(accountEvent)
        }
    }

    private fun apply(accountCreated: AccountCreated) {
        overdraftLimit = accountCreated.overdraftLimit
    }

    private fun apply(accountCredited: AccountCredited) {
        balance += accountCredited.amount
    }

    private fun apply(accountDebited: AccountDebited) {
        balance = accountDebited.afterBalance
    }

    private fun apply(accountOverdraftLimitSet: AccountOverdraftLimitSet) {
        overdraftLimit = accountOverdraftLimitSet.overdraftLimit
    }

    fun validateAndGenerateEvents(command: DebitAccount): Pair<CommandResponse<AccountDebitedDetails>, List<AccountEvent>> {
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
                    listOf("Cannot exceed overdraft limit")),
                    events)
        }

        events.add(AccountDebited(accountNumber, amount, balance, balance - amount))
        return Pair(CommandResponse(accountDebitedDetails, true), events)
    }

    fun validateAndGenerateEvents(command: SetAccountOverdraftLimit): Pair<CommandResponse<AccountOverdraftLimitSetDetails>, List<AccountEvent>> {
        val (accountNumber, overdraftLimit) = command
        val accountOverdraftLimitSetDetails = AccountOverdraftLimitSetDetails(accountNumber, overdraftLimit)

        val events = ArrayList<AccountEvent>()
        events.add(AccountOverdraftLimitSet(accountNumber, overdraftLimit))
        return Pair(CommandResponse(accountOverdraftLimitSetDetails, true), events)
    }
}


