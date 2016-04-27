package org.banksy.domain.account.service;

import org.banksy.domain.account.command.Create;
import org.banksy.domain.account.command.response.CommandResponse;

public class AccountService {
    public CommandResponse handle(Create create) {
        return new CommandResponse();
    }
}
