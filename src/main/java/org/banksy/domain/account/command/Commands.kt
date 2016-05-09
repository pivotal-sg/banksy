package org.banksy.domain.account.command

import java.math.BigDecimal

data class CreateAccount(val accountNumber: String, val overdraftLimit: Long)

data class CreditAccount(val accountNumber: String, val amount: Long)

data class DebitAccount(val accountNumber: String, val amount: Long)

data class SetAccountOverdraftLimit(val accountNumber: String, val overdraftLimit: Long)

// TODO: create application level configured value for interest and rename interestPercent to overdraftInterestRate
//       other passable naming ideas are: overdraftPenaltyRate or penaltyRate
data class ChargeInterestOnAccount(val accountNumber: String, val interestPercent: BigDecimal)

data class PayInterestForAccount(val accountNumber: String, val interestRate: BigDecimal)
