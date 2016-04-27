package org.banksy.domain.account.command.response

/** CommandResponse holds the infrastructure for determining
 * if a Command Succeeded, and embeds the value object/DTO of the response
 *
 */
class CommandResponse<T>(var body: T? = null, val success:Boolean = false) { }
