package org.banksy.domain.account.command

import java.util.*

fun createAccountCommandFactory(): CreateAccount {
    return CreateAccount(UUID.randomUUID().toString())
}
