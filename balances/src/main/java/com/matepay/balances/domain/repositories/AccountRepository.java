package com.matepay.balances.domain.repositories;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;

import java.util.UUID;

public interface AccountRepository {
    Account get(UUID uuid) throws Exceptions.AccountNotFound;
}
