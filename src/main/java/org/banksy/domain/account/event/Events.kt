package org.banksy.domain.account.event

import org.banksy.domain.account.command.AccountCreate
import java.time.LocalDate
import java.util.*

data class AccountCreated(val accountNumber: String)

