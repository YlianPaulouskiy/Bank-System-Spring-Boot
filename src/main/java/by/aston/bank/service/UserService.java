package by.aston.bank.service;

import by.aston.bank.repository.UserRepository;
import by.aston.bank.service.dto.UserDto;
import by.aston.bank.service.dto.UserWriteDto;
import by.aston.bank.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return Optional.ofNullable(
                userMapper.toUserDto(
                        userRepository.findById(id).orElse(null)));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserDto> create(UserWriteDto writeDto) {
        Optional<UserDto> optUserDto = Optional.empty();
        if (writeDto.startWork() != null) {
            optUserDto = Optional.ofNullable(
                    userMapper.toUserDto(
                            userRepository.save(userMapper.toManagerEntity(writeDto))));
        } else {
            optUserDto = Optional.ofNullable(
                    userMapper.toUserDto(
                            userRepository.save(userMapper.toClientEntity(writeDto))));
        }
        return optUserDto;
    }

    @Transactional
    public Optional<UserDto> update(Long id, UserWriteDto writeDto) {
        var user = userRepository.findById(id);
        Optional<UserDto> optUserDto = Optional.empty();
        if (user.isPresent()) {
            if (user.get().getUserType().equals('M')) {
                var manager = userMapper.toManagerEntity(writeDto);
                manager.setId(id);
                optUserDto = Optional.ofNullable(
                        userMapper.toUserDto(
                                userRepository.saveAndFlush(manager)));
            } else if (user.get().getUserType().equals('C')) {
                var client = userMapper.toClientEntity(writeDto);
                client.setId(id);
                optUserDto = Optional.ofNullable(
                        userMapper.toUserDto(
                                userRepository.saveAndFlush(client)));
            }
        }
        return optUserDto;
    }

    @Transactional
    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }

}
