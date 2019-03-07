package pl.parser.nbp.infrastructure.exchangerate.nbp

import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import pl.parser.nbp.infrastructure.support.exception.ExternalApiException
import spock.lang.Specification

import java.time.LocalDate

class ExchangeRateTableFileNameProviderTest extends Specification {

    private RestTemplate restTemplate
    private ExchangeRateTableFileNameProvider exchangeRateTableFileNameProvider

    void setup() {
        restTemplate = Mock()

        exchangeRateTableFileNameProvider = new ExchangeRateTableFileNameProvider(restTemplate)
    }

    def "resolve should return filled optional if regex matches for not actual year"() {
        given:
        LocalDate desiredDate = LocalDate.of(2018, 1, 2)

        and:
        restTemplate.getMessageConverters() >> new ArrayList<HttpMessageConverter<?>>()


        and:
        String data = "cxxz180102 cxxxz180103 dxxxz180102 cxxxz180102"
        restTemplate.getForObject("https://www.nbp.pl/kursy/xml/dir2018.txt",
                String.class) >> data

        when:
        Optional<String> result = exchangeRateTableFileNameProvider.resolve(desiredDate)

        then:
        result.isPresent() == true
        result.get() == "cxxxz180102"
    }

    def "resolve should return empty optional if regex matches for actual year"() {
        given:
        LocalDate desiredDate = LocalDate.now()

        and:
        restTemplate.getMessageConverters() >> new ArrayList<HttpMessageConverter<?>>()


        and:
        String data = "cxxz180102 cxxxz180103 dxxxz180102 cxxxz180102"
        restTemplate.getForObject("https://www.nbp.pl/kursy/xml/dir.txt",
                String.class) >> data

        when:
        Optional<String> result = exchangeRateTableFileNameProvider.resolve(desiredDate)

        then:
        result.isPresent() == false
    }

    def "resolve should return empty optional if regex matches for not actual year"() {
        given:
        LocalDate desiredDate = LocalDate.of(2018, 1, 2)

        and:
        restTemplate.getMessageConverters() >> new ArrayList<HttpMessageConverter<?>>()


        and:
        String data = "cxxz180102 cxxxz180103 dxxxz180102 cxxxz180106"
        restTemplate.getForObject("https://www.nbp.pl/kursy/xml/dir2018.txt",
                String.class) >> data

        when:
        Optional<String> result = exchangeRateTableFileNameProvider.resolve(desiredDate)

        then:
        result.isPresent() == false
    }

    def "resolve catch exception and throw own"() {
        given:
        LocalDate desiredDate = LocalDate.of(2018, 1, 2)

        and:
        restTemplate.getMessageConverters() >> new ArrayList<HttpMessageConverter<?>>()


        and:
        restTemplate.getForObject("https://www.nbp.pl/kursy/xml/dir2018.txt",
                String.class) >> {throw new RestClientException("msg")}

        when:
        exchangeRateTableFileNameProvider.resolve(desiredDate)

        then:
        thrown(ExternalApiException)
    }
}
