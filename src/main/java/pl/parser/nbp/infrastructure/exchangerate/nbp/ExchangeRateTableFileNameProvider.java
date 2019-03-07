package pl.parser.nbp.infrastructure.exchangerate.nbp;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.parser.nbp.infrastructure.support.exception.ExternalApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class ExchangeRateTableFileNameProvider {
    private final static String ACTUAL_YEAR_DIRECTORY = "https://www.nbp.pl/kursy/xml/dir.txt";
    private final static String NOT_ACTUAL_YEAR_DIRECTORY = "https://www.nbp.pl/kursy/xml/dir%d.txt";
    private final static String YEAR_FORMAT = "yyMMdd";
    private final static String TABLE_NAME_PATTERN = "c...z%s";

    private final RestTemplate restTemplate;

    public ExchangeRateTableFileNameProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Optional<String> resolve(LocalDate desiredDate) {
        LocalDate now = LocalDate.now();

        String properUrl = getProperUrl(desiredDate, now);

        String tableNames = getTableNames(properUrl);

        return Optional.ofNullable(findTableForDesiredDate(desiredDate, tableNames));
    }

    private String getProperUrl(LocalDate desiredDate, LocalDate now) {
        String properUrl;

        if (now.getYear() == desiredDate.getYear()) {
            properUrl = ACTUAL_YEAR_DIRECTORY;
        } else {
            properUrl = String.format(NOT_ACTUAL_YEAR_DIRECTORY, desiredDate.getYear());
        }
        return properUrl;
    }

    private String getTableNames(String properUrl) {
        try {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            return restTemplate.getForObject(properUrl, String.class);
        } catch (RestClientException ex) {
            throw new ExternalApiException(ex.getMessage(), ExternalApiException.ErrorCode.EXTERNAL_API_EXCEPTION);
        }
    }

    private String findTableForDesiredDate(LocalDate desiredDate, String tableNames) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YEAR_FORMAT);
        String formattedDate = desiredDate.format(formatter);

        Pattern pattern = Pattern.compile(String.format(TABLE_NAME_PATTERN, formattedDate));
        Matcher matcher = pattern.matcher(tableNames);
        boolean found = matcher.find();
        return found ? matcher.group() : null;
    }
}
