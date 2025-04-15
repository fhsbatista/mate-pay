package com.matepay.balances.domain.repositories;

import java.math.BigDecimal;
import java.util.UUID;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;

public interface AccountRepository {
    Account get(UUID uuid) throws Exceptions.AccountNotFound;
    Account registerBalanceUpdate(UUID accountId, BigDecimal updatedBalance) throws Exceptions.AccountNotFound;
    Account create(Account account);
}
