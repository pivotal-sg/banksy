package org.banksy.domain.account.query

data class AccountInfo(val accountNumber: String, val balance: Long, val overdraftLimit: Long)
