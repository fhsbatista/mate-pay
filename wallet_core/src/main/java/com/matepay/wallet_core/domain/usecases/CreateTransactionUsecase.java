package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import com.matepay.wallet_core.domain.repositories.TransactionRepository;
import com.matepay.wallet_core.infra.kafka.KafkaProducer;
import com.matepay.wallet_core.messaging.events.TransactionCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private final KafkaProducer kafkaProducer;

    public CreateTransactionUsecase(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            KafkaProducer kafkaProducer
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional(rollbackFor = Exceptions.class)
    public Transaction execute(Input input) throws Exceptions {
        final Account from = accountRepository.get(input.accountFromId);
        final Account to = accountRepository.get(input.accountToId);
        if (!hasBalance(from, input.amount)) throw new Exceptions.NotEnoughBalance();

        from.debit(input.amount);
        to.credit(input.amount);

        accountRepository.updateBalance(from);
        accountRepository.updateBalance(to);

        final Transaction persistedTransaction = transactionRepository.save(new Transaction(from, to, input.amount));
        final TransactionCreated event = new TransactionCreated(persistedTransaction);

        kafkaProducer.publish("transactions", event);

        return persistedTransaction;
    }

    private boolean hasBalance(Account from, BigDecimal amount) {
        return from.getBalance().compareTo(amount) >= 0;
    }
}
