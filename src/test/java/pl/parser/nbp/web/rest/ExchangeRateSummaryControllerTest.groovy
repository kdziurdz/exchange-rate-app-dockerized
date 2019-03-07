package pl.parser.nbp.web.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.parser.nbp.application.exchangerate.ExchangeRateService
import pl.parser.nbp.application.exchangerate.dto.ExchangeRateSummaryDTO
import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies
import spock.lang.Specification

import java.time.LocalDate

class ExchangeRateSummaryControllerTest extends Specification {

    private ExchangeRateService exchangeRateService

    private ExchangeRateSummaryController exchangeRateSummaryController


    void setup() {
        exchangeRateService = Mock()

        exchangeRateSummaryController = new ExchangeRateSummaryController(exchangeRateService)

    }

    def "get() - should call service and return data"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now()
        LocalDate dateTo = LocalDate.now()

        and:
        ExchangeRateSummaryDTO returnedData = Mock()
        exchangeRateService.getSummary(currencyCode, dateFrom, dateTo) >> returnedData

        when:
        ResponseEntity<ExchangeRateSummaryDTO> result = exchangeRateSummaryController
                .get(currencyCode, dateFrom, dateTo)

        then:
        result.body == returnedData
        result.statusCode == HttpStatus.OK
    }
}
