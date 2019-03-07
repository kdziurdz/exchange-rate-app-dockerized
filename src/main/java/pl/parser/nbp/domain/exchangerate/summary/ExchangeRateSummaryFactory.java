package pl.parser.nbp.domain.exchangerate.summary;

import pl.parser.nbp.domain.support.annotation.DomainFactory;

import java.math.BigDecimal;

@DomainFactory
public class ExchangeRateSummaryFactory {
    public ExchangeRateSummary create(BigDecimal buyingArithmeticAverage, BigDecimal sellingStandardDeviation){
        return new ExchangeRateSummary(buyingArithmeticAverage, sellingStandardDeviation);
    }
}
