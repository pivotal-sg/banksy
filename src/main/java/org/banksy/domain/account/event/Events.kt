package org.banksy.domain.account.event

import java.math.BigDecimal
import java.util.*

// Use the naming convention of noun with past tense verb for Events
open class AccountEvent(val createdAt : Date = Date(), val eventId : UUID = UUID.randomUUID())

class AccountCreated(val accountNumber: String, val overdraftLimit: Long) : AccountEvent()

class AccountCredited(val accountNumber: String, val amount: Long, val openingBalance: Long, val closingBalance: Long) : AccountEvent()

class AccountDebited(val accountNumber: String, val amount: Long, val openingBalance: Long, val closingBalance: Long) : AccountEvent()

class AccountOverdraftLimitSet(val accountNumber: String, val overdraftLimit: Long) : AccountEvent()

class AccountInterestCharged(val accountNumber: String, val overdraftInterestRate: BigDecimal, val closingBalance: Long) : AccountEvent()

class AccountInterestPaid(val accountNumber: String, val interestRate: BigDecimal, val closingBalance: Long) : AccountEvent()
