package pl.parser.nbp.infrastructure.exchangerate;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Component
public class Calculator {

    private final static int PRECISION = 4;

    public BigDecimal getArithmeticalAverage(List<BigDecimal> decimals){
        return decimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(decimals.size()), ROUND_HALF_UP);
    }

    public BigDecimal getStandardDeviation(List<BigDecimal> decimals){

        final BigDecimal arithmeticalAverage = getArithmeticalAverage(decimals);

        BigDecimal sumOfSquaredDiffs = decimals.stream().reduce(BigDecimal.ZERO, (accumulator, nextDecimal) -> accumulator.add(nextDecimal.subtract(arithmeticalAverage).pow(2)));

        return sqrt(sumOfSquaredDiffs.divide(BigDecimal.valueOf(decimals.size()),PRECISION, RoundingMode.HALF_UP));
    }

    private BigDecimal sqrt(BigDecimal bigDecimal) {
        BigDecimal x0 = new BigDecimal(0);
        BigDecimal x1 = new BigDecimal(Math.sqrt(bigDecimal.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = bigDecimal.divide(x0, PRECISION, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(BigDecimal.valueOf(2), PRECISION, ROUND_HALF_UP);
        }
        return x1;
    }
}
