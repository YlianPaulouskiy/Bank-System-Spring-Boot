package by.aston.bank.service;

import by.aston.bank.repository.BankRepository;
import by.aston.bank.service.mapper.BankMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static by.aston.bank.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankRepository bankRepository;
    @Mock
    private BankMapper bankMapper;

    @Test
    void Should_Ok_WhenFindById() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(createBank()));
        when(bankMapper.toReadDto(any())).thenReturn(createBankReadDto());

        var optReadDto = bankService.findById(1L);

        assertTrue(optReadDto.isPresent());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(bankMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_ReturnEmpty_WhenNoDataInDB() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(bankMapper.toReadDto(any())).thenReturn(null);

        var optReadDto = bankService.findById(1L);

        assertTrue(optReadDto.isEmpty());
        verify(bankRepository, times(1)).findById(anyLong());
        verify(bankMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_Ok_WhenFindAll() {
        when(bankRepository.findAll()).thenReturn(List.of(createBank()));
        when(bankMapper.toReadDto(any())).thenReturn(createBankReadDto());

        var list = bankService.findAll();

        assertEquals(list.size(), 1);
        verify(bankRepository, times(1)).findAll();
        verify(bankMapper, times(list.size())).toReadDto(any());
    }

    @Test
    void Should_Ok_WhenCreate() {
        when(bankMapper.toEntity(any())).thenReturn(createBank());
        when(bankRepository.save(any())).thenReturn(createBank());
        when(bankMapper.toUserDto(any())).thenReturn(createBankUserDto());

        var writeDto = createBankWriteDto();
        var optReadDto = bankService.create(writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().id(), 1L);
        assertEquals(optReadDto.get().title(), writeDto.title());
        verify(bankMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).save(any());
        verify(bankMapper, times(1)).toUserDto(any());
    }

    @Test
    void Should_Ok_WhenUpdate() {
        when(bankMapper.toEntity(any())).thenReturn(createBank());
        when(bankRepository.saveAndFlush(any())).thenReturn(createBank());
        when(bankMapper.toReadDto(any())).thenReturn(createBankReadDto());

        var writeDto = createBankWriteDto();
        var optReadDto = bankService.update(1L, writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().id(), 1L);
        assertEquals(optReadDto.get().title(), writeDto.title());
        verify(bankMapper, times(1)).toEntity(any());
        verify(bankRepository, times(1)).saveAndFlush(any());
        verify(bankMapper, times(1)).toReadDto(any());
    }

    @Test
    void Should_ReturnTrue_WhenDeleteByIdIsOK() {
        when(bankRepository.existsById(anyLong())).thenReturn(false);
        assertTrue(bankService.delete(anyLong()));
    }

    @Test
    void Should_ReturnFalse_WhenDeleteByIdIsNotComplete() {
        when(bankRepository.existsById(anyLong())).thenReturn(true);
        assertFalse(bankService.delete(anyLong()));
    }

}
