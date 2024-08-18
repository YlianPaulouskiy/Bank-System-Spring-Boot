package by.aston.bank.controller;

import by.aston.bank.service.UserService;
import by.aston.bank.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserWriteDto writeDto) {
        return userService.create(writeDto)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestParam Long id,
                                              @RequestBody UserWriteDto writeDto) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return userService.update(id, writeDto)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping
    protected ResponseEntity delete(@RequestParam Long id) {
        return userService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}
