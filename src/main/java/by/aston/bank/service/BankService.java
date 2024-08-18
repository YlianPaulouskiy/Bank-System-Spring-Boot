package by.aston.bank.service;

import by.aston.bank.repository.BankRepository;
import by.aston.bank.service.dto.BankReadDto;
import by.aston.bank.service.dto.BankUserDto;
import by.aston.bank.service.dto.BankWriteDto;
import by.aston.bank.service.mapper.BankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Transactional(readOnly = true)
    public Optional<BankReadDto> findById(Long id) {
        return Optional.ofNullable(
                bankMapper.toReadDto(
                        bankRepository.findById(id)
                                .orElse(null)));
    }

    @Transactional(readOnly = true)
    public List<BankReadDto> findAll() {
        return bankRepository.findAll()
                .stream()
                .map(bankMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BankUserDto> create(BankWriteDto writeDto) {
        return Optional.ofNullable(
                bankMapper.toUserDto(
                        bankRepository.save(bankMapper.toEntity(writeDto))));
    }

    @Transactional
    public Optional<BankReadDto> update(Long id, BankWriteDto writeDto) {
        var bank = bankMapper.toEntity(writeDto);
        bank.setId(id);
        return Optional.ofNullable(
                bankMapper.toReadDto(
                        bankRepository.saveAndFlush(bank)));
    }

    @Transactional
    public boolean delete(Long id) {
        bankRepository.deleteById(id);
        return !bankRepository.existsById(id);
    }

}
