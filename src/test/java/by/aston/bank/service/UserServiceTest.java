package by.aston.bank.service;

import by.aston.bank.repository.UserRepository;
import by.aston.bank.service.mapper.UserMapper;
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
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @Test
    void Should_Ok_WhenFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());

        var optReadDto = userService.findById(1L);

        assertTrue(optReadDto.isPresent());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userMapper, times(1)).toUserDto(any());
    }

    @Test
    void Should_ReturnEmpty_WhenNoDataInDB() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userMapper.toUserDto(any())).thenReturn(null);

        var optReadDto = userService.findById(1L);

        assertTrue(optReadDto.isEmpty());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userMapper, times(1)).toUserDto(any());
    }

    @Test
    void Should_Ok_WhenFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(createUser()));
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());

        var list = userService.findAll();

        assertEquals(list.size(), 1);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(list.size())).toUserDto(any());
    }

    @Test
    void Should_Ok_WhenCreateClient() {
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());
        when(userMapper.toClientEntity(any())).thenReturn(createClient());
        when(userRepository.save(any())).thenReturn(createClient());

        var writeDto = createClientWriteDto();
        var optReadDto = userService.create(writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().getId(), 1L);
        assertEquals(optReadDto.get().getName(), writeDto.name());
        assertEquals(optReadDto.get().getLastName(), writeDto.lastName());
        verify(userMapper, times(1)).toUserDto(any());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).toClientEntity(any());
    }

    @Test
    void Should_Ok_WhenCreateManager() {
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());
        when(userMapper.toManagerEntity(any())).thenReturn(createManager());
        when(userRepository.save(any())).thenReturn(createManager());

        var writeDto = createManagerWriteDto();
        var optReadDto = userService.create(writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().getId(), 1L);
        assertEquals(optReadDto.get().getName(), writeDto.name());
        assertEquals(optReadDto.get().getLastName(), writeDto.lastName());
        verify(userMapper, times(1)).toUserDto(any());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).toManagerEntity(any());
    }


    @Test
    void Should_Ok_WhenUpdateClient() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createClient()));
        when(userMapper.toClientEntity(any())).thenReturn(createClient());
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());
        when(userRepository.saveAndFlush(any())).thenReturn(createClient());

        var writeDto = createClientWriteDto();
        var optReadDto = userService.update(1L, writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().getId(), 1L);
        assertEquals(optReadDto.get().getName(), writeDto.name());
        assertEquals(optReadDto.get().getLastName(), writeDto.lastName());
        verify(userMapper, times(1)).toUserDto(any());
        verify(userRepository, times(1)).saveAndFlush(any());
        verify(userMapper, times(1)).toClientEntity(any());
    }

    @Test
    void Should_Ok_WhenUpdateManager() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createManager()));
        when(userMapper.toManagerEntity(any())).thenReturn(createManager());
        when(userMapper.toUserDto(any())).thenReturn(createUserDto());
        when(userRepository.saveAndFlush(any())).thenReturn(createClient());

        var writeDto = createManagerWriteDto();
        var optReadDto = userService.update(1L, writeDto);

        assertTrue(optReadDto.isPresent());
        assertEquals(optReadDto.get().getId(), 1L);
        assertEquals(optReadDto.get().getName(), writeDto.name());
        assertEquals(optReadDto.get().getLastName(), writeDto.lastName());
        verify(userMapper, times(1)).toUserDto(any());
        verify(userRepository, times(1)).saveAndFlush(any());
        verify(userMapper, times(1)).toManagerEntity(any());
    }

    @Test
    void Should_ReturnTrue_WhenDeleteByIdIsOK() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertTrue(userService.delete(anyLong()));
    }

    @Test
    void Should_ReturnFalse_WhenDeleteByIdIsNotComplete() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        assertFalse(userService.delete(anyLong()));
    }

}
