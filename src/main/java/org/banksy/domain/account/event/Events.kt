package org.banksy.domain.account.event

// Use the naming convention of noun with past tense verb for Events

interface AccountEvent

data class AccountCreated(val accountNumber: String, val overdraftLimit: Long) : AccountEvent

data class AccountCredited(val accountNumber: String, val amount: Long) : AccountEvent

data class AccountDebited(val accountNumber: String, val amount: Long) : AccountEvent

data class AccountOverdraftLimitSet(val accountNumber: String, val overdraftLimit: Long) : AccountEvent
