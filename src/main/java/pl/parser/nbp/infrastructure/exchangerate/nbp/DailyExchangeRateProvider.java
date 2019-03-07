package pl.parser.nbp.infrastructure.exchangerate.nbp;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRatesApiDTO;
import pl.parser.nbp.infrastructure.support.exception.ExternalApiException;

@Component
class DailyExchangeRateProvider {

    private static final String NBP_URL = "http://www.nbp.pl/kursy/xml/%s.xml";

    private final RestTemplate restTemplate;

    DailyExchangeRateProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    DailyExchangeRatesApiDTO get(String fileName) {
        try {
            return restTemplate
                    .getForObject(String.format(NBP_URL, fileName), DailyExchangeRatesApiDTO.class);
        } catch (RestClientException ex) {
            throw new ExternalApiException(ex.getMessage(), ExternalApiException.ErrorCode.EXTERNAL_API_EXCEPTION);
        }
    }
}
