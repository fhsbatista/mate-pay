package com.matepay.wallet_core.domain.repositories;

import com.matepay.wallet_core.domain.entities.Account;

import java.util.UUID;

public interface AccountRepository {
    Account get(UUID uuid);
    Account save(Account account);
}
