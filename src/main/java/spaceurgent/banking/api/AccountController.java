package spaceurgent.banking.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import spaceurgent.banking.dto.AccountDto;
import spaceurgent.banking.dto.ErrorDto;
import spaceurgent.banking.exception.InvalidAmountException;
import spaceurgent.banking.service.AccountService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccountDto createAccount(@RequestParam(name = "balance", defaultValue = "0.00") BigDecimal balance) {
        return AccountDto.from(accountService.createAccount(balance));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidAmountException.class})
    public ErrorDto handleBadRequestException(Exception exception,
                                              HttpServletRequest request) {
        final var requestPath = ServletUriComponentsBuilder.fromRequest(request)
                .build().getPath();
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), requestPath);
    }
}
