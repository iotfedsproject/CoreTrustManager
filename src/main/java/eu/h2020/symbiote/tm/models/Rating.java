package eu.h2020.symbiote.tm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Rating {
    @NotNull
    @JsonProperty("ease_of_use")
    private double easeOfUse;

    @NotNull
    @JsonProperty("value_for_money")
    private double valueForMoney;

    @NotNull
    @JsonProperty("business_enablement")
    private double businessEnablement;

    @NotNull
    private double correctness;

    @NotNull
    private double completeness;

    @NotNull
    private double relevance;

    @NotNull
    @JsonProperty("response_time")
    private double responseTime;

    @NotNull
    private double precision;
}
