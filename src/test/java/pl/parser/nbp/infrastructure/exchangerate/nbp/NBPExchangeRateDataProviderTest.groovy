package pl.parser.nbp.infrastructure.exchangerate.nbp

import pl.parser.nbp.domain.exchangerate.ExchangeRateSupportedCurrencies
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummaryFactory
import pl.parser.nbp.infrastructure.exchangerate.Calculator
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRateApiDTO
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRatesApiDTO
import spock.lang.Specification

import java.time.LocalDate

class NBPExchangeRateDataProviderTest extends Specification {

    private ExchangeRateTableFileNameProvider exchangeRateTableFileNameProvider
    private DailyExchangeRateProvider dailyExchangeRateProvider
    private ExchangeRateSummaryFactory exchangeRateSummaryFactory
    private Calculator calculator

    private NBPExchangeRateDataProvider nbpExchangeRateDataProvider

    void setup() {
        exchangeRateTableFileNameProvider = Mock()
        dailyExchangeRateProvider = Mock()
        exchangeRateSummaryFactory = new ExchangeRateSummaryFactory()
        calculator = Mock()

        nbpExchangeRateDataProvider = new NBPExchangeRateDataProvider(exchangeRateTableFileNameProvider,
                dailyExchangeRateProvider, exchangeRateSummaryFactory, calculator)
    }

    def "getSummary() should throw if date from is after date to"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now().plusDays(1)
        LocalDate dateTo = LocalDate.now()

        when:
        nbpExchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo)

        then:
        thrown(IllegalArgumentException)
    }

    def "getSummary() should call exchangeRateTableFileNameProvider and dailyExchangeRateProvider and calculate result with more than two results"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now().minusDays(2)
        LocalDate dateTo = LocalDate.now()

        and:
        Optional<String> tb1 = Optional.of("table1")
        Optional<String> tb2 = Optional.empty()
        Optional<String> tb3 = Optional.of("table3")
        exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >>> [tb1, tb2, tb3]

        and:
        DailyExchangeRatesApiDTO day1 = getDailyExchangeRatesApiDTO(currencyCode.toString())
        DailyExchangeRatesApiDTO day3 = getDailyExchangeRatesApiDTO(currencyCode.toString())
        dailyExchangeRateProvider.get("table1") >> day1
        dailyExchangeRateProvider.get("table3") >> day3

        and:
        BigDecimal givenAverage = BigDecimal.ONE
        calculator.getArithmeticalAverage(_ as List) >> givenAverage

        BigDecimal givenDeviation = BigDecimal.TEN
        calculator.getStandardDeviation(_ as List) >> givenDeviation

        when:
        ExchangeRateSummary result = nbpExchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo)

        then:
        with(result) {
            buyingArithmeticAverage == givenAverage
            sellingStandardDeviation == givenDeviation
        }

        and:
        1 * dailyExchangeRateProvider.get("table1") >> day1
        1 * dailyExchangeRateProvider.get("table3") >> day3
        1 * calculator.getArithmeticalAverage(_ as List) >> givenAverage
        1 * calculator.getStandardDeviation(_ as List) >> givenDeviation
        1 * exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >> tb1
        1 * exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >> tb2
        1 * exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >> tb3
    }

    def "getSummary() should call exchangeRateTableFileNameProvider and dailyExchangeRateProvider and calculate result with exactly one result"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now().minusDays(2)
        LocalDate dateTo = LocalDate.now()

        and:
        Optional<String> tb1 = Optional.of("table1")
        Optional<String> tb2 = Optional.empty()
        Optional<String> tb3 = Optional.empty()
        exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >>> [tb1, tb2, tb3]

        and:
        DailyExchangeRatesApiDTO day1 = getDailyExchangeRatesApiDTO(currencyCode.toString())
        dailyExchangeRateProvider.get("table1") >> day1

        when:
        ExchangeRateSummary result = nbpExchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo)

        then:
        with(result) {
            buyingArithmeticAverage == day1.getAllCurrenciesExchangeRate().get(0).getBuyingRate()
            sellingStandardDeviation == BigDecimal.ZERO
        }

        and:
        1 * dailyExchangeRateProvider.get("table1") >> day1
        0 * dailyExchangeRateProvider.get("table3")
        0 * calculator.getArithmeticalAverage(_ as List)
        0 * calculator.getStandardDeviation(_ as List)
    }

    def "getSummary() should call exchangeRateTableFileNameProvider and calculate result with no results"() {
        given:
        ExchangeRateSupportedCurrencies currencyCode = ExchangeRateSupportedCurrencies.CHF
        LocalDate dateFrom = LocalDate.now().minusDays(2)
        LocalDate dateTo = LocalDate.now()

        and:
        Optional<String> tb1 = Optional.empty()
        Optional<String> tb2 = Optional.empty()
        Optional<String> tb3 = Optional.empty()
        exchangeRateTableFileNameProvider.resolve(_ as LocalDate) >>> [tb1, tb2, tb3]

        when:
        ExchangeRateSummary result = nbpExchangeRateDataProvider.getSummary(currencyCode, dateFrom, dateTo)

        then:
        with(result) {
            buyingArithmeticAverage == BigDecimal.ZERO
            sellingStandardDeviation == BigDecimal.ZERO
        }

        and:
        0 * dailyExchangeRateProvider.get("table1")
        0 * dailyExchangeRateProvider.get("table3")
        0 * calculator.getArithmeticalAverage(_ as List)
        0 * calculator.getStandardDeviation(_ as List)
    }

    DailyExchangeRatesApiDTO getDailyExchangeRatesApiDTO(String currency) {
        DailyExchangeRatesApiDTO day = new DailyExchangeRatesApiDTO()

        List<DailyExchangeRateApiDTO> allCurrenciesExchangeRate = new ArrayList<>()

        DailyExchangeRateApiDTO singleDayRate = new DailyExchangeRateApiDTO()
        singleDayRate.setCurrencyCode(currency)
        singleDayRate.setBuyingRate(BigDecimal.TEN)
        singleDayRate.setSellingRate(BigDecimal.ONE)
        allCurrenciesExchangeRate.push(singleDayRate)

        day.setAllCurrenciesExchangeRate(allCurrenciesExchangeRate)
        return day
    }
}
