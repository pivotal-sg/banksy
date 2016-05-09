package org.banksy.domain.account.command

import java.math.BigDecimal

data class CreateAccount(val accountNumber: String, val overdraftLimit: Long)

data class CreditAccount(val accountNumber: String, val amount: Long)

data class DebitAccount(val accountNumber: String, val amount: Long)

data class SetAccountOverdraftLimit(val accountNumber: String, val overdraftLimit: Long)

data class ChargeInterestOnAccount(val accountNumber: String, val overdraftInterestRate: BigDecimal)

data class PayInterestForAccount(val accountNumber: String, val interestRate: BigDecimal)
