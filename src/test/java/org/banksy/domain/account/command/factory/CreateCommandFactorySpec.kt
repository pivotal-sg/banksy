package org.banksy.domain.account.command.factory

import com.google.common.eventbus.EventBus
import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith
import org.assertj.core.api.Assertions.*
import org.banksy.domain.account.command.createAccountFactory
import java.util.*

@RunWith(JUnitKSpecRunner::class)
class CreateCommandFactorySpec : KSpec(){
    override fun spec() {
        describe("Account Create Command Factory") {
            it("generates account numbers and builds account create commands") {
                /*
                 * [ Controller ] -> [Factory] -> [builds command]
                 *                             <- [returns command]
                 *                -> [send command to account service]
                 *                <- [returns result]
                 * <- [Returns response to requestor]
                 */
                val createCommand = createAccountFactory()

                try {
                    UUID.fromString(createCommand.accountNumber)
                } catch (e:IllegalArgumentException) {
                    fail("Account Number was not a valid UUID")
                }

            }
        }
    }
}
