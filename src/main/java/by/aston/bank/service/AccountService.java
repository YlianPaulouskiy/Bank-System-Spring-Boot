package by.aston.bank.service;


import by.aston.bank.model.Account;
import by.aston.bank.repository.AccountRepository;
import by.aston.bank.repository.BankRepository;
import by.aston.bank.repository.UserRepository;
import by.aston.bank.service.dto.AccountReadDto;
import by.aston.bank.service.dto.AccountWriteDto;
import by.aston.bank.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public Optional<AccountReadDto> findById(Long id) {
        return Optional.ofNullable(
                accountMapper.toReadDto(
                        accountRepository.findById(id).orElse(null)));
    }

    @Transactional(readOnly = true)
    public List<AccountReadDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<AccountReadDto> create(AccountWriteDto writeDto) {
        var optAccount = createdUpdatedAccount(writeDto);
        return optAccount.map(account -> accountMapper.toReadDto(
                accountRepository.save(account)));
    }

    @Transactional
    public Optional<AccountReadDto> update(Long id, AccountWriteDto writeDto) {
        var optAccount = createdUpdatedAccount(writeDto);
        return optAccount.map(account -> {
            account.setId(id);
            return accountMapper.toReadDto(
                    accountRepository.saveAndFlush(account));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        accountRepository.deleteById(id);
        return !accountRepository.existsById(id);
    }

    private Optional<Account> createdUpdatedAccount(AccountWriteDto writeDto) {
        var account = accountMapper.toEntity(writeDto);
        var optionalBank = bankRepository.findById(writeDto.bankId());
        var optionalUser = userRepository.findById(writeDto.userId());
        if (optionalBank.isEmpty() || optionalUser.isEmpty()) {
            return Optional.empty();
        }
        account.setBank(optionalBank.get());
        account.setUser(optionalUser.get());
        return Optional.of(account);
    }

}
