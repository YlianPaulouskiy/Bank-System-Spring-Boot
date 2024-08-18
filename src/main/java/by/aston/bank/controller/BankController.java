package by.aston.bank.controller;

import by.aston.bank.service.BankService;
import by.aston.bank.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/{id}")
    public ResponseEntity<BankReadDto> findById(@PathVariable Long id) {
        return bankService.findById(id)
                .map(bankReadDto -> new ResponseEntity<>(bankReadDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<List<BankReadDto>> findAll() {
        return new ResponseEntity<>(bankService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BankUserDto> create(@RequestBody BankWriteDto writeDto) {
        return bankService.create(writeDto)
                .map(bankUserDto -> new ResponseEntity<>(bankUserDto, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    public ResponseEntity<BankReadDto> update(@RequestParam Long id,
                                                 @RequestBody BankWriteDto writeDto) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return bankService.update(id, writeDto)
                .map(bankReadDto -> new ResponseEntity<>(bankReadDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping
    protected ResponseEntity delete(@RequestParam Long id) {
        return bankService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}
