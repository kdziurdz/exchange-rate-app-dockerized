package pl.parser.nbp.infrastructure.exchangerate.dto;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {
    public BigDecimal unmarshal(String v) throws Exception {

        String dotSeparatedDecimal = v.replaceAll(",","\\.");

        return new BigDecimal(dotSeparatedDecimal);
    }

    public String marshal(BigDecimal v) throws Exception {
        return v.toString();
    }
}
