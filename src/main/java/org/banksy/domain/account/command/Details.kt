package org.banksy.domain.account.command

data class AccountCreationDetails (val accountNumber: String)

data class AccountCreditedDetails (val accountNumber: String, val amount: Long)

data class AccountDebitedDetails (val accountNumber: String, val amount: Long)
