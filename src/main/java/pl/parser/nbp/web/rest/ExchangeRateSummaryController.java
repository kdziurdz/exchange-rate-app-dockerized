package pl.parser.nbp.web.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.parser.nbp.application.exchangerate.ExchangeRateService;
import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryDTO;
import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController()
@RequestMapping("api/1/exchange-rate-summary")
public class ExchangeRateSummaryController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateSummaryController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/")
    public ResponseEntity<ExchangeRateSummaryDTO> get(@RequestParam ExchangeRateSupportedCurrencies currencyCode,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo) {
        final ExchangeRateSummaryDTO summary = exchangeRateService.getSummary(currencyCode, dateFrom, dateTo);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}
