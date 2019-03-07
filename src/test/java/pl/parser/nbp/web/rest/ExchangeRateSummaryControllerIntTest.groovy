package pl.parser.nbp.web.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateSummaryControllerIntTest extends Specification {

    @Autowired
    MockMvc mockMvc

    void setup() {
    }

    def "Get should return OK"() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/api/1/exchange-rate-summary/?currencyCode=USD&dateFrom=2018-01-01&dateTo=2018-01-10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }
}
