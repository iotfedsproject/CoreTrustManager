package eu.h2020.symbiote.tm.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRatingDto {

    @NotNull
    private String productId;

    @NotNull
    private String userId;

    @NotNull
    private RatingDto rating;
}

