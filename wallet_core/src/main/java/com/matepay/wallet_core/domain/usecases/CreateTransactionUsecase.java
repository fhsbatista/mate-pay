package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import com.matepay.wallet_core.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateTransactionUsecase {
    public record Input(
            UUID accountFromId,
            UUID accountToId,
            BigDecimal amount
    ) {
    }

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    public CreateTransactionUsecase(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction execute(Input input) throws Exceptions {
        final Account from = accountRepository.get(input.accountFromId);
        final Account to = accountRepository.get(input.accountToId);
        if (!hasBalance(from, input.amount)) throw new Exceptions.NotEnoughBalance();

        final Transaction transaction = new Transaction(from, to, input.amount);

        return transactionRepository.save(transaction);
    }

    private boolean hasBalance(Account from, BigDecimal amount) {
        return from.getBalance().compareTo(amount) >= 0;
    }
}
