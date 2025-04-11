package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.TransactionRepository;
import com.matepay.wallet_core.infra.kafka.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateTransactionUsecaseTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    CreateTransactionUsecase makeSut() throws Exceptions {
        final var accountFrom = makeAccount(makeClient());
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var transaction = makeTransaction(accountFrom, accountTo, amount);
        mockSuccessSaveTransaction(transaction);

        return new CreateTransactionUsecase(
                accountRepository,
                transactionRepository,
                kafkaProducer
        );
    }

    CreateTransactionUsecase.Input makeInput(Account from, Account to, BigDecimal amount) {
        return new CreateTransactionUsecase.Input(from.getUuid(), to.getUuid(), amount);
    }

    Client makeClient() {
        return new Client("John Doe", "john.doe@mocks.com");
    }

    Account makeAccount(Client client) {
        return new Account(client);
    }

    Transaction makeTransaction(Account from, Account to, BigDecimal amount) throws Exceptions {
        return new Transaction(from, to, amount);
    }

    void mockSuccessGetAccount(Account account) throws Exceptions {
        when(accountRepository.get(account.getUuid())).thenReturn(account);
    }

    void mockFailureGetAccount(Account account) throws Exceptions {
        when(accountRepository.get(account.getUuid())).thenThrow(new Exceptions.AccountNotFound());
    }

    void mockSuccessSaveTransaction(Transaction transaction) throws Exceptions {
        when(transactionRepository.save(any())).thenReturn(transaction);
    }

    @Test
    void shouldCallAccountRepositoryToGetFromAndToAccounts() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = makeAccount(makeClient());
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        final var transaction = makeTransaction(accountFrom, accountTo, amount);
        mockSuccessGetAccount(accountFrom);
        mockSuccessGetAccount(accountTo);
        mockSuccessSaveTransaction(transaction);
        accountFrom.credit(BigDecimal.valueOf(300.0));

        usecase.execute(input);

        verify(accountRepository, times(1)).get(accountFrom.getUuid());
        verify(accountRepository, times(1)).get(accountTo.getUuid());
    }

    @Test
    void shouldThrowIfCannotFindAccountFrom() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = makeAccount(makeClient());
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        mockFailureGetAccount(accountFrom);
        mockSuccessGetAccount(accountTo);

        assertThrows(Exceptions.AccountNotFound.class, () -> usecase.execute(input));
    }

    @Test
    void shouldThrowIfCannotFindAccountTo() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = makeAccount(makeClient());
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        mockSuccessGetAccount(accountFrom);
        mockFailureGetAccount(accountTo);

        assertThrows(Exceptions.AccountNotFound.class, () -> usecase.execute(input));
    }

    @Test
    void shouldThrowIfAccountFromHasNotEnoughBalance() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = makeAccount(makeClient());
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        accountFrom.credit(BigDecimal.valueOf(100));
        mockSuccessGetAccount(accountFrom);
        mockSuccessGetAccount(accountTo);

        assertThrows(Exceptions.NotEnoughBalance.class, () -> usecase.execute(input));
    }

    @Test
    void shouldCallTransactionRepositoryAndAccountRepositoryToAdjustBalances() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = spy(makeAccount(makeClient()));
        final var accountTo = spy(makeAccount(makeClient()));
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        accountFrom.credit(BigDecimal.valueOf(300.0));
        mockSuccessGetAccount(accountFrom);
        mockSuccessGetAccount(accountTo);

        usecase.execute(input);

        verify(transactionRepository, times(1)).save(any());
        verify(accountFrom, times(1)).debit(input.amount());
        verify(accountTo, times(1)).credit(input.amount());
        verify(accountRepository, times(1)).updateBalance(accountFrom);
        verify(accountRepository, times(1)).updateBalance(accountTo);
    }

    @Test
    void shouldReturnCorrectTransactionOnTransactionRepositorySuccess() throws Exceptions {
        final var usecase = makeSut();
        final var accountFrom = makeAccount(makeClient());
        accountFrom.credit(BigDecimal.valueOf(300.0));
        final var accountTo = makeAccount(makeClient());
        final var amount = BigDecimal.valueOf(200.0);
        final var input = makeInput(accountFrom, accountTo, amount);
        final var transaction = makeTransaction(accountFrom, accountTo, amount);
        mockSuccessGetAccount(accountFrom);
        mockSuccessGetAccount(accountTo);
        mockSuccessSaveTransaction(transaction);

        final var result = usecase.execute(input);

        assertEquals(transaction, result);
    }
}