package ru.highloadcup.api;

import java.io.Serializable;
import java.math.BigDecimal;

public class AverageVisitMarkDto implements Serializable {

    private BigDecimal avg;

    public AverageVisitMarkDto() {
    }

    public AverageVisitMarkDto(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }
}
