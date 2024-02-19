package eu.h2020.symbiote.tm.controllers;

import eu.h2020.symbiote.tm.dtos.ProductRatingDto;
import eu.h2020.symbiote.tm.services.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "Update Interval")
    @RequestMapping(path = "/updateInterval/{newInterval}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateInterval(
            @ApiParam(value = "New Interval") @PathVariable String newInterval
    ){

        adminService.updateInterval(newInterval);
        return new ResponseEntity<>("Interval successfully updated", HttpStatus.OK);
    }
    @ApiOperation(value = "Submit product rating")
    @RequestMapping(path = "/submit-rating", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitRating(
            @ApiParam(value = "Product Rating") @RequestBody ProductRatingDto productRatingDto
            ){

        String response = adminService.receiveProductRating(productRatingDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
