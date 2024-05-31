package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.toilet.business.GeolocationInvalidException;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toilet")
public class ToiletController {
    private static final Logger logger = LoggerFactory.getLogger(ToiletController.class);

    private final RegisterToiletUseCase registerToiletUseCase;

    public ToiletController(RegisterToiletUseCase registerToiletUseCase) {
        this.registerToiletUseCase = registerToiletUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final CreateToiletRequest request) {
        try {
            final var input = new RegisterToiletUseCase
                    .Input(request.description(), request.latitude(), request.longitude(), request.customerId());
            registerToiletUseCase.execute(input);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (GeolocationInvalidException e) {
            logger.error("Invalid geolocation for request: {}", request, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (CustomerNotFound e) {
            logger.error("Customer not found for request: {}", request, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Internal server error occurred while creating a toilet: {}", request, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public record CreateToiletRequest(String description,
                                      double latitude,
                                      double longitude,
                                      String customerId){}
}
