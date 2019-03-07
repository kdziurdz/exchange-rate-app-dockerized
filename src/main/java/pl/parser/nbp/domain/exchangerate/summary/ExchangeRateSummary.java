package pl.parser.nbp.domain.exchangerate.summary;

import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.parser.nbp.domain.support.annotation.ValueObject;

import java.math.BigDecimal;

@ValueObject
public class ExchangeRateSummary {

    ExchangeRateSummary(BigDecimal buyingArithmeticAverage, BigDecimal sellingStandardDeviation) {
        this.buyingArithmeticAverage = buyingArithmeticAverage;
        this.sellingStandardDeviation = sellingStandardDeviation;
    }

    private BigDecimal buyingArithmeticAverage;
    private BigDecimal sellingStandardDeviation;

    public BigDecimal getBuyingArithmeticAverage() {
        return buyingArithmeticAverage;
    }

    public BigDecimal getSellingStandardDeviation() {
        return sellingStandardDeviation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("buyingArithmeticAverage", buyingArithmeticAverage)
                .append("sellingStandardDeviation", sellingStandardDeviation)
                .toString();
    }
}
