package org.banksy.domain.account.command

import java.util.*

fun createAccountFactory(): AccountCreate {
    return AccountCreate(UUID.randomUUID().toString())
}
