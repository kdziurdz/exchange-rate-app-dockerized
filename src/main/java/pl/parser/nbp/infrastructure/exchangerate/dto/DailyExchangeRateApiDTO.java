package pl.parser.nbp.infrastructure.exchangerate.dto;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

@XmlRootElement(name="pozycja")
@XmlAccessorType(XmlAccessType.NONE)
public class DailyExchangeRateApiDTO {

    @XmlElement(name="nazwa_waluty")
    private String currencyName;

    @XmlElement(name="przelicznik")
    private Integer factor;

    @XmlElement(name="kod_waluty")
    private String currencyCode;

    @XmlElement(name="kurs_kupna")
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    private BigDecimal buyingRate;

    @XmlElement(name="kurs_sprzedazy")
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    private BigDecimal sellingRate;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(BigDecimal buyingRate) {
        this.buyingRate = buyingRate;
    }

    public BigDecimal getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(BigDecimal sellingRate) {
        this.sellingRate = sellingRate;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("currencyName", currencyName)
                .append("factor", factor)
                .append("currencyCode", currencyCode)
                .append("buyingRate", buyingRate)
                .append("sellingRate", sellingRate)
                .toString();
    }
}
