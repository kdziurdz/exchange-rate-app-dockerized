package pl.parser.nbp.application.exchangerate.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class ExchangeRateSummaryDTO {

    public ExchangeRateSummaryDTO(BigDecimal buyingArithmeticAverage, BigDecimal sellingStandardDeviation) {
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
