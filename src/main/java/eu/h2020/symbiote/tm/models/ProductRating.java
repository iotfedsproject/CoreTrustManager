package eu.h2020.symbiote.tm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRating implements Serializable {

    @NotNull
    @JsonProperty("product_id")
    private String productId;

    @NotNull
    @JsonProperty("user_id")
    private String userId;

    private Rating rating;
}
