package pl.parser.nbp.application.exchangerate;

import org.springframework.stereotype.Service;
import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryDTO;
import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryMapper;
import pl.parser.nbp.domain.exchangerate.ExchangeRateDataProvider;
import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies;
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Service
public class ExchangeRateService {

    private final ExchangeRateDataProvider exchangeRateDataProvider;
    private final ExchangeRateSummaryMapper exchangeRateSummaryMapper;

    public ExchangeRateService(ExchangeRateDataProvider exchangeRateDataProvider, ExchangeRateSummaryMapper exchangeRateSummaryMapper) {
        this.exchangeRateDataProvider = exchangeRateDataProvider;
        this.exchangeRateSummaryMapper = exchangeRateSummaryMapper;
    }

    public ExchangeRateSummaryDTO getSummary(@NotNull ExchangeRateSupportedCurrencies currencyCode, @NotNull LocalDate dateFrom,
                                             @NotNull LocalDate dateTo) {

        if(dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("DateFrom cannot be after DateTo");
        }

        final ExchangeRateSummary summary = exchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo);
        return exchangeRateSummaryMapper.asDTO(summary);
    }
}
