package by.aston.bank.controller;

import by.aston.bank.service.AccountService;
import by.aston.bank.service.dto.AccountReadDto;
import by.aston.bank.service.dto.AccountWriteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountReadDto> findById(@PathVariable Long id) {
        return accountService.findById(id)
                .map(accountReadDto -> new ResponseEntity<>(accountReadDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<List<AccountReadDto>> findAll() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountReadDto> create(@RequestBody AccountWriteDto writeDto) {
        return accountService.create(writeDto)
                .map(accountReadDto -> new ResponseEntity<>(accountReadDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    public ResponseEntity<AccountReadDto> update(@RequestParam Long id,
                                                 @RequestBody AccountWriteDto writeDto) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return accountService.update(id, writeDto)
                .map(accountReadDto -> new ResponseEntity<>(accountReadDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping
    protected ResponseEntity delete(@RequestParam Long id) {
        return accountService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


}
