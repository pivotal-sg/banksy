package org.banksy.domain.account.command

import java.math.BigDecimal

// TODO/FIXME: Generalize/Inherit a base AccountDetails object which has account number and errors list

data class AccountCreationDetails (val accountNumber: String, val errors: List<String> = listOf())

data class AccountCreditedDetails (val accountNumber: String, val amount: Long, val errors: List<String> = listOf())

data class AccountDebitedDetails (val accountNumber: String, val amount: Long, val errors: List<String> = listOf())

data class AccountOverdraftLimitSetDetails (val accountNumber: String, val overdraftLimit: Long, val errors: List<String> = listOf())

data class AccountInterestChargedDetails(val accountNumber: String, val interestPercent: BigDecimal, val errors: List<String> = listOf())

data class AccountInterestPaidDetails(val accountNumber: String, val interestRate: BigDecimal, val errors: List<String> = listOf())
