package pl.parser.nbp.domain.exchangerate;


import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary;
import pl.parser.nbp.domain.support.annotation.DomainService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@DomainService
public interface ExchangeRateDataProvider {
    ExchangeRateSummary getSummary(@NotNull ExchangeRateSupportedCurrencies currencyCode, @NotNull LocalDate dateFrom,
                                   @NotNull LocalDate dateTo);
}
