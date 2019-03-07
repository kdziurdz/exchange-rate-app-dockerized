package pl.parser.nbp.domain.exchangerate.summary

import spock.lang.Specification

class ExchangeRateSummaryFactoryTest extends Specification {

    private ExchangeRateSummaryFactory exchangeRateSummaryFactory
    void setup() {
        exchangeRateSummaryFactory = new ExchangeRateSummaryFactory()
    }

    def "Create"() {
        given:
        BigDecimal givenBuyingArithmeticAverage = Mock()
        BigDecimal givenSellingStandardDeviation = Mock()

        when:
        ExchangeRateSummary result = exchangeRateSummaryFactory.create(givenBuyingArithmeticAverage, givenSellingStandardDeviation)

        then:
        with(result) {
            buyingArithmeticAverage == givenBuyingArithmeticAverage
            sellingStandardDeviation == givenSellingStandardDeviation
        }
    }
}
