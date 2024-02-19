package eu.h2020.symbiote.tm.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingDto {

    @NotNull
    private double easeOfUse;

    @NotNull
    private double valueForMoney;

    @NotNull
    private double businessEnablement;

    @NotNull
    private double correctness;

    @NotNull
    private double completeness;

    @NotNull
    private double relevance;

    @NotNull
    private double responseTime;

    @NotNull
    private double precision;
}
