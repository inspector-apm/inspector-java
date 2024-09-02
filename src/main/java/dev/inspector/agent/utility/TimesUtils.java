package dev.inspector.agent.utility;

import java.math.BigDecimal;
import java.time.Instant;

public class TimesUtils {

    public static BigDecimal getTimestamp(){
        Instant now = Instant.now();
        return BigDecimal.valueOf(now.getEpochSecond())
                .add(BigDecimal.valueOf(now.getNano(), 9));
    }

}
