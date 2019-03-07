package pl.parser.nbp.infrastructure.exchangerate.nbp;

import org.springframework.stereotype.Component;
import pl.parser.nbp.domain.exchangerate.ExchangeRateDataProvider;
import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies;
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary;
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummaryFactory;
import pl.parser.nbp.infrastructure.exchangerate.Calculator;
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRateApiDTO;
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRatesApiDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NBPExchangeRateDataProvider implements ExchangeRateDataProvider {

    private final ExchangeRateTableFileNameProvider exchangeRateTableFileNameProvider;
    private final DailyExchangeRateProvider dailyExchangeRateProvider;
    private final ExchangeRateSummaryFactory exchangeRateSummaryFactory;
    private final Calculator calculator;

    public NBPExchangeRateDataProvider(ExchangeRateTableFileNameProvider exchangeRateTableFileNameProvider, DailyExchangeRateProvider dailyExchangeRateProvider, ExchangeRateSummaryFactory exchangeRateSummaryFactory, Calculator calculator) {
        this.exchangeRateTableFileNameProvider = exchangeRateTableFileNameProvider;
        this.dailyExchangeRateProvider = dailyExchangeRateProvider;
        this.exchangeRateSummaryFactory = exchangeRateSummaryFactory;
        this.calculator = calculator;
    }


    @Override
    public ExchangeRateSummary getSummary(@NotNull ExchangeRateSupportedCurrencies currencyCode, @NotNull LocalDate dateFrom, @NotNull LocalDate dateTo) {

        if(dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("DateFrom cannot be after DateTo");
        }

        if (LocalDate.now().isBefore(dateTo)) {
            throw new IllegalArgumentException("Cannot predict future. Sorry!");
        }

        final List<DailyExchangeRatesApiDTO> dataRange = new ArrayList<>();

        for (LocalDate date = dateFrom; date.isBefore(dateTo) || date.isEqual(dateTo); date = date.plusDays(1)) {
            final Optional<String> optionalTableName = exchangeRateTableFileNameProvider.resolve(date);
            optionalTableName
                    .ifPresent(tableName -> dataRange.add(dailyExchangeRateProvider.get(tableName)));
        }


        return calculateSummary(dataRange, currencyCode.toString());
    }

    private ExchangeRateSummary calculateSummary(List<DailyExchangeRatesApiDTO> data, String currencyCode) {
        List<DailyExchangeRateApiDTO> dailyRates = new ArrayList<>();

        data.forEach(dailyExchangeRatesApiDTO -> dailyExchangeRatesApiDTO.getAllCurrenciesExchangeRate()
                .stream()
                .filter(dailyRate -> dailyRate.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .ifPresent(dailyRates::add));

        if (dailyRates.size() == 0) {
            return exchangeRateSummaryFactory.create(BigDecimal.ZERO, BigDecimal.ZERO);

        } else if (dailyRates.size() == 1) {
            return exchangeRateSummaryFactory.create(dailyRates.get(0).getBuyingRate(), BigDecimal.ZERO);
        } else {
            final BigDecimal buyingArithmeticAverage = calculator.getArithmeticalAverage(dailyRates.stream()
                    .map(DailyExchangeRateApiDTO::getBuyingRate).collect(Collectors.toList()));

            final BigDecimal sellingArithmeticAverage = calculator.getStandardDeviation(dailyRates.stream()
                    .map(DailyExchangeRateApiDTO::getSellingRate).collect(Collectors.toList()));
            return exchangeRateSummaryFactory.create(buyingArithmeticAverage, sellingArithmeticAverage);
        }
    }


}
