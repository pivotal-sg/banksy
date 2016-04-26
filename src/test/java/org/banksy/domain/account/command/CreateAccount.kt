package org.banksy.domain.account.command

import io.damo.kspec.*

class CreateAccountSpec: Spec({
    describe("#create") {
        test {
            val command = Create("123")
            verify(response).isSuccessType()
        }
    }
})
