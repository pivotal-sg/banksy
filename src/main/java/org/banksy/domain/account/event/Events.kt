package org.banksy.domain.account.event

import org.banksy.domain.account.command.CreateAccount
import java.time.LocalDate
import java.util.*

// Use the naming convetion of noun with past tense verb for Events

data class AccountCreated(val accountNumber: String)

data class AccountCredited(val accountNumber: String, val amount: Long)

data class AccountDebited(val accountNumber: String, val amount: Long)
