package org.banksy.domain.account.command

data class CreateAccount(val accountNumber: String, val overdraftLimit: Long)

data class CreditAccount(val accountNumber: String, val amount: Long)

data class DebitAccount(val accountNumber: String, val amount: Long)

data class SetAccountOverdraftLimit(val accountNumber: String, val overdraftLimit: Long)
