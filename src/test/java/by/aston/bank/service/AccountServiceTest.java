package by.aston.bank.service;

import by.aston.bank.repository.AccountRepository;
import by.aston.bank.repository.BankRepository;
import by.aston.bank.repository.UserRepository;
import by.aston.bank.service.mapper.AccountMapper;
import by.aston.bank.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static by.aston.bank.util.CommonTestData.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {


    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BankRepository bankRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountMapper accountMapper;


    @Test
    void Should_Ok_WhenFindById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(CommonTestData.createAccount()));
        when(accountMapper.toReadDto(any())).thenReturn(CommonTestData.createAccountReadDto());

        var optReadDto = accountService.findById(1L);

        assertTrue(optReadDto.isPresent());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_ReturnEmpty_WhenNoDataInDB() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(accountMapper.toReadDto(any())).thenReturn(null);

        var optReadDto = accountService.findById(1L);

        assertTrue(optReadDto.isEmpty());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_Ok_WhenFindAll() {
        when(accountRepository.findAll()).thenReturn(List.of(CommonTestData.createAccount()));
        when(accountMapper.toReadDto(any())).thenReturn(CommonTestData.createAccountReadDto());

        var list = accountService.findAll();

        assertEquals(list.size(), 1);
        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(list.size())).toReadDto(any());
    }

    @Test
    void Should_Ok_WhenCreate() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(createBank()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(accountRepository.save(any())).thenReturn(createAccount());
        when(accountMapper.toReadDto(any())).thenReturn(createAccountReadDto());

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.create(writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().id(), 1L);
        assertEquals(optReadDto.get().number(), writeDto.number());
        assertEquals(optReadDto.get().cash(), writeDto.cash());
        assertEquals(optReadDto.get().user().getId(), 1L);
        assertEquals(optReadDto.get().bank().id(), 1L);

        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any());
        verify(accountMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_ReturnEmpty_WhenCreateAndBankIsNotExists() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.create(writeDto);

        assertTrue(optReadDto.isEmpty());
        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void Should_ReturnEmpty_WhenCreateAndUserIsNotExists() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(createBank()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.create(writeDto);

        assertTrue(optReadDto.isEmpty());
        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void Should_Ok_WhenUpdate() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(createBank()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(accountRepository.saveAndFlush(any())).thenReturn(createAccount());
        when(accountMapper.toReadDto(any())).thenReturn(createAccountReadDto());

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.update(1L, writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().id(), 1L);
        assertEquals(optReadDto.get().number(), writeDto.number());
        assertEquals(optReadDto.get().cash(), writeDto.cash());
        assertEquals(optReadDto.get().user().getId(), 1L);
        assertEquals(optReadDto.get().bank().id(), 1L);

        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).saveAndFlush(any());
        verify(accountMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_ReturnEmpty_WhenUpdateAndBankNotExists() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.update(1L, writeDto);

        assertTrue(optReadDto.isEmpty());
        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void Should_ReturnEmpty_WhenUpdateAndUserNotExists() {
        when(accountMapper.toEntity(any())).thenReturn(createAccount());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(createBank()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        var writeDto = createAccountWriteDto();
        var optReadDto = accountService.update(1L, writeDto);

        assertTrue(optReadDto.isEmpty());
        verify(accountMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void Should_ReturnTrue_WhenDeleteByIdIsOK() {
        when(accountRepository.existsById(anyLong())).thenReturn(false);
        assertTrue(accountService.delete(anyLong()));
    }

    @Test
    void Should_ReturnFalse_WhenDeleteByIdIsNotComplete() {
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        assertFalse(accountService.delete(anyLong()));
    }
}
