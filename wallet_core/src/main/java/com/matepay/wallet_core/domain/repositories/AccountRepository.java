package com.matepay.wallet_core.domain.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;

import java.util.UUID;

public interface AccountRepository {
    Account get(UUID uuid) throws Exceptions.AccountNotFound;
    Account save(Account account);
}
