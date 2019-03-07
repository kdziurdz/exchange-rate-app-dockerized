package pl.parser.nbp.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ExchangeRateTableNotFoundException extends RuntimeException {
    private final Object date;

    public ExchangeRateTableNotFoundException(LocalDate date) {
        super(String.format("Cannot find exchange rate data table for date: %s", date.toString()));
        this.date = date;
    }

    public Object getDate() {
        return this.date;
    }
}
