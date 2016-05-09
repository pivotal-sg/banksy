package org.banksy.domain.account.aggregate

import org.banksy.domain.account.command.*
import org.banksy.domain.account.command.response.CommandResponse
import org.banksy.domain.account.event.*
import java.math.BigDecimal
import java.math.MathContext
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
            is AccountInterestCharged -> apply(accountEvent)
        }
    }

    private fun apply(accountCreated: AccountCreated) {
        overdraftLimit = accountCreated.overdraftLimit
    }

    private fun apply(accountCredited: AccountCredited) {
        balance = accountCredited.afterBalance
    }

    private fun apply(accountDebited: AccountDebited) {
        balance = accountDebited.afterBalance
    }

    private fun apply(accountOverdraftLimitSet: AccountOverdraftLimitSet) {
        overdraftLimit = accountOverdraftLimitSet.overdraftLimit
    }

    private fun apply(accountInterestCharged: AccountInterestCharged) {
        balance = accountInterestCharged.afterBalance
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

    fun validateAndGenerateEvents(command: ChargeInterestOnAccount): Pair<CommandResponse<AccountInterestChargedDetails>, List<AccountEvent>> {
        val (accountNumber, interestPercent) = command
        val afterBalance = computeAfterBalanceWithInterest(interestPercent)

        val accountInterestChargedDetails = AccountInterestChargedDetails(accountNumber, interestPercent)
        val events = ArrayList<AccountEvent>()

        if (interestPercent <= BigDecimal.ZERO) {
            return Pair(CommandResponse(
                    accountInterestChargedDetails,
                    false,
                    listOf("Can only charge a positive interest percent amount")),
                    events)
        }

        events.add(AccountInterestCharged(accountNumber, interestPercent, afterBalance))
        return Pair(CommandResponse(accountInterestChargedDetails, true), events)
    }

    private fun computeAfterBalanceWithInterest(interestPercent: BigDecimal): Long {
        val afterBalancePercent = interestPercent.add(BigDecimal.ONE)
        val afterBalance = afterBalancePercent * BigDecimal(balance)
        return afterBalance.round(MathContext.DECIMAL64).toLong()
    }

    fun validateAndGenerateEvents(command: PayInterestForAccount): Pair<CommandResponse<AccountInterestPaidDetails>, List<AccountEvent>> {
        val (accountNumber, interestRate) = command
        val afterBalance = computeAfterBalanceWithInterest(interestRate)

        val accountInterestPaidDetails = AccountInterestPaidDetails(accountNumber, interestRate)
        val events = ArrayList<AccountEvent>()

        if (interestRate <= BigDecimal.ZERO) {
            return Pair(CommandResponse(
                    accountInterestPaidDetails,
                    false,
                    listOf("Can only pay a positive interest percent amount")),
                    events)
        }

        if (balance <= 0) {
            return Pair(CommandResponse(
                    accountInterestPaidDetails,
                    false,
                    listOf("Can only pay for accounts with positive balance")),
                    events)
        }

        events.add(AccountInterestPaid(accountNumber, interestRate, afterBalance))
        return Pair(CommandResponse(accountInterestPaidDetails, true), events)
    }
}


