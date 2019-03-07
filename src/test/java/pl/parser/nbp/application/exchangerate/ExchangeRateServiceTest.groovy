package pl.parser.nbp.application.exchangerate

import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryDTO
import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryMapper
import pl.parser.nbp.domain.exchangerate.ExchangeRateDataProvider
import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary
import spock.lang.Specification

import java.time.LocalDate

class ExchangeRateServiceTest extends Specification {

    private ExchangeRateDataProvider exchangeRateDataProvider
    private ExchangeRateSummaryMapper exchangeRateSummaryMapper

    private ExchangeRateService exchangeRateService


    void setup() {
        exchangeRateDataProvider = Mock()
        exchangeRateSummaryMapper = Mock()

        exchangeRateService = new ExchangeRateService(exchangeRateDataProvider, exchangeRateSummaryMapper)

    }

    def "getSummary() - should call domain service and return data"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now()
        LocalDate dateTo = LocalDate.now()

        and:
        ExchangeRateSummary returnedData = Mock()
        exchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo) >> returnedData

        and:
        ExchangeRateSummaryDTO returnedMappedData = Mock()
        exchangeRateSummaryMapper.asDTO(returnedData) >> returnedMappedData

        when:
        ExchangeRateSummaryDTO result = exchangeRateService
                .getSummary(currencyCode, dateFrom, dateTo)

        then:
        result == returnedMappedData
    }

    def "getSummary() - should throw if date from is after date to"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now().plusDays(1)
        LocalDate dateTo = LocalDate.now()

        when:
        exchangeRateService
                .getSummary(currencyCode, dateFrom, dateTo)

        then:
        thrown(IllegalArgumentException)
    }
}
