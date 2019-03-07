package pl.parser.nbp.infrastructure.exchangerate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@XmlRootElement(name="tabela_kursow")
@XmlAccessorType(XmlAccessType.NONE)
public class DailyExchangeRatesApiDTO {

    @XmlAttribute(name="typ")
    private String type;

    @XmlElement(name="data_notowania")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate quotationDate;

    @XmlElement(name="data_publikacji")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate publishDate;

    @XmlElement(name="pozycja")
    private List<DailyExchangeRateApiDTO> allCurrenciesExchangeRate = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public List<DailyExchangeRateApiDTO> getAllCurrenciesExchangeRate() {
        return allCurrenciesExchangeRate;
    }

    public void setAllCurrenciesExchangeRate(List<DailyExchangeRateApiDTO> allCurrenciesExchangeRate) {
        this.allCurrenciesExchangeRate = allCurrenciesExchangeRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("quotationDate", quotationDate)
                .append("publishDate", publishDate)
//                .append("allCurrenciesExchangeRate", allCurrenciesExchangeRate)
                .toString();
    }
}
