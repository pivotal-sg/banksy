package org.banksy.domain.account.command

import java.math.BigDecimal

data class CreateAccount(val accountNumber: String, val overdraftLimit: Long)

data class CreditAccount(val accountNumber: String, val amount: Long)

data class DebitAccount(val accountNumber: String, val amount: Long)

data class SetAccountOverdraftLimit(val accountNumber: String, val overdraftLimit: Long)

// TODO: interest percent should become float and also an application level configured value
data class ChargeInterestOnAccount(val accountNumber: String, val interestPercent: BigDecimal)
