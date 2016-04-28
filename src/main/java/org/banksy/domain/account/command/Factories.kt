package org.banksy.domain.account.command

import java.util.*

fun createAccountCommandFactory(): AccountCreate {
    return AccountCreate(UUID.randomUUID().toString())
}
