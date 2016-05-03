package org.banksy.domain.account.command

// TODO/FIXME: Fix the Account Creation and Crediting Details to include Error list.

data class AccountCreationDetails (val accountNumber: String, val errors: List<String> = listOf())

data class AccountCreditedDetails (val accountNumber: String, val amount: Long, val errors: List<String> = listOf())

data class AccountDebitedDetails (val accountNumber: String, val amount: Long, val errors: List<String> = listOf())

data class AccountOverdraftLimitSetDetails (val accountNumber: String, val overdraftLimit: Long, val errors: List<String> = listOf())
