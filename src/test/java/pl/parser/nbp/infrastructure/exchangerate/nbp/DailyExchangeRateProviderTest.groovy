package pl.parser.nbp.infrastructure.exchangerate.nbp

import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import pl.parser.nbp.infrastructure.exchangerate.dto.DailyExchangeRatesApiDTO
import pl.parser.nbp.infrastructure.support.exception.ExternalApiException
import spock.lang.Specification

class DailyExchangeRateProviderTest extends Specification {

    private RestTemplate restTemplate

    private DailyExchangeRateProvider dailyExchangeRateProvider

    void setup() {
        restTemplate = Mock()
        dailyExchangeRateProvider = new DailyExchangeRateProvider(restTemplate)
    }

    def "get should return data"() {
        given:
        String filename = "filename"

        and:
        DailyExchangeRatesApiDTO data = Mock()
        restTemplate.getForObject("http://www.nbp.pl/kursy/xml/filename.xml",
                DailyExchangeRatesApiDTO.class) >> data

        when:
        DailyExchangeRatesApiDTO result = dailyExchangeRateProvider.get(filename)

        then:
        result == data
    }

    def "get should catch exception and throw own"() {
        given:
        String filename = "filename"

        and:
        DailyExchangeRatesApiDTO data = Mock()
        restTemplate.getForObject(_, _) >> {throw new RestClientException("msg")}

        when:
         dailyExchangeRateProvider.get(filename)

        then:
        thrown(ExternalApiException)
    }
}
