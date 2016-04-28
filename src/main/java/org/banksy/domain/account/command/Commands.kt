package org.banksy.domain.account.command

data class CreateAccount(val accountNumber: String)

data class CreditAccount(val accountNumber: String, val amount: Long)

data class DebitAccount(val accountNumber: String, val amount: Long)
