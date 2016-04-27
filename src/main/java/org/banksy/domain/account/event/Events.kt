package org.banksy.domain.account.event

import org.banksy.domain.account.command.Create
import java.time.LocalDate
import java.util.*

data class AccountCreated(val accountNumber: String, val createdAt: LocalDate = LocalDate.now(), val eventID: String = UUID.randomUUID().toString())

