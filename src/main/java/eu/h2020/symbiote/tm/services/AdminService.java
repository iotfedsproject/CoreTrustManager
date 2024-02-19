package eu.h2020.symbiote.tm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.h2020.symbiote.tm.dtos.ProductRatingDto;
import eu.h2020.symbiote.tm.interfaces.rest.AuthManager;
import eu.h2020.symbiote.tm.models.ProductRating;
import eu.h2020.symbiote.tm.models.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Value("${symbIoTe.core.aams.url}")
    private String aamsUrl;

    @Value("${baas.base.url}")
    private String baasUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private AuthManager authManager;

    public void updateInterval(String interval) {
        logger.info("Retrieving available base urls...");
        ResponseEntity<String> resp = restTemplate.exchange(aamsUrl, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<String>() {
                });
        try {
            JsonNode rootNode = objectMapper.readTree(resp.getBody());
            JsonNode aams = rootNode.path("availableAAMs");
            Iterator<JsonNode> it = aams.elements();
            it.next();
            while(it.hasNext()){
                String temp = it.next().get("aamAddress").toString();
                String url = temp.substring(1, temp.length()-4);
                logger.info("Sending interval update to: {}", url);
                ResponseEntity<String> resp1 = restTemplate.exchange(url+"trust-manager/qos/updateInterval/"+interval, HttpMethod.POST, new HttpEntity<>(new HttpHeaders()),
                        new ParameterizedTypeReference<String>() {
                        });
                logger.info("Request to {}: {}",url, resp1.getStatusCode().toString());
            }

        }
        catch(Exception e){
            logger.info(e.getMessage());
        }

    }

    public String receiveProductRating(ProductRatingDto ratingDto){
        logger.info("MARKETPLACE: Received new product rating.");
        String url = baasUrl + "/products/rate_product";
        ProductRating rating = new ProductRating(
                ratingDto.getProductId(),
                ratingDto.getUserId(),
                new Rating(ratingDto.getRating().getEaseOfUse(),
                        ratingDto.getRating().getValueForMoney(),
                        ratingDto.getRating().getBusinessEnablement(),
                        ratingDto.getRating().getCorrectness(),
                        ratingDto.getRating().getCompleteness(),
                        ratingDto.getRating().getRelevance(),
                        ratingDto.getRating().getResponseTime(),
                        ratingDto.getRating().getPrecision())
        );
        try {
            logger.info("Sending new rating to :{}", url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(rating, new HttpHeaders()),
                    new ParameterizedTypeReference<String>() {
                    });
            if(HttpStatus.OK == response.getStatusCode()) {
                logger.info("Subjective Score update for product ID: {} was successful", ratingDto.getProductId());
                return response.getBody();
            } else {
                logger.info("Subjective Score update for product ID: {} was unsuccessful.Retrying...", ratingDto.getProductId());
                ResponseEntity<String> retryResponse = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(objectMapper.writeValueAsString(rating), new HttpHeaders()),
                        new ParameterizedTypeReference<String>() {
                        });
                logger.info("Retry result: {}", retryResponse.getStatusCode().toString());
                if(HttpStatus.OK == retryResponse.getStatusCode()) {
                    return retryResponse.getBody();
                } else {
                    throw new RuntimeException(retryResponse.getBody());
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
