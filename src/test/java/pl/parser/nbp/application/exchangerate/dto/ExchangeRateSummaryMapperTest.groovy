package pl.parser.nbp.application.exchangerate.dto

import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary
import spock.lang.Specification

class ExchangeRateSummaryMapperTest extends Specification {

    private ExchangeRateSummaryMapper exchangeRateSummaryMapper

    void setup() {
        exchangeRateSummaryMapper = new ExchangeRateSummaryMapper()
    }

    def "asDTO() should map ExchangeRateSummary to dto"() {
        given:
        ExchangeRateSummary vo = new ExchangeRateSummary(BigDecimal.ONE, BigDecimal.TEN)

        when:
        ExchangeRateSummaryDTO result = exchangeRateSummaryMapper.asDTO(vo)

        then:
        with(result) {
            buyingArithmeticAverage == vo.buyingArithmeticAverage
            sellingStandardDeviation == vo.sellingStandardDeviation
        }
    }
}
