package pl.parser.nbp.infrastructure.exchangerate

import spock.lang.Specification

class CalculatorTest extends Specification {

    private Calculator calculator

    void setup() {
        calculator = new Calculator()
    }

    def "getArithmeticalAverage should return correct result"() {
        when:
        BigDecimal result = calculator.getArithmeticalAverage(decimals)

        then:
        result == correctResult

        where:
        decimals << [[BigDecimal.valueOf(5), BigDecimal.valueOf(15)],
                     [BigDecimal.valueOf(-13.1421), BigDecimal.valueOf(15.512312)]]
        correctResult << [10, 1.185106]
    }

    def "getStandardDeviation should return correct result"() {
        when:
        BigDecimal result = calculator.getStandardDeviation(decimals)

        then:
        result == correctResult

        where:
        decimals << [[BigDecimal.valueOf(5), BigDecimal.valueOf(15)],
                     [BigDecimal.valueOf(-13.1421), BigDecimal.valueOf(15.512312)]]
        correctResult << [5, 14.3272]
    }
}
