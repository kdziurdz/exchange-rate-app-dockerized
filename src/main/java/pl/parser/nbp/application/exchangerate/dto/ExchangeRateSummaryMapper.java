package pl.parser.nbp.application.exchangerate.dto;

import org.springframework.stereotype.Component;
import pl.parser.nbp.domain.exchangerate.summary.ExchangeRateSummary;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class ExchangeRateSummaryMapper {
    public ExchangeRateSummaryDTO asDTO(ExchangeRateSummary vo) {
        return new ExchangeRateSummaryDTO(vo.getBuyingArithmeticAverage(), vo.getSellingStandardDeviation());
    }
}
