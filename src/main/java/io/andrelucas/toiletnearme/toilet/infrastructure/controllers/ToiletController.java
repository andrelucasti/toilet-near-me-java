package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.toilet.business.GeolocationInvalidException;
import io.andrelucas.toiletnearme.toilet.business.ItemId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.AddNewItemUseCase;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import io.andrelucas.toiletnearme.toilet.infrastructure.controllers.requests.AddNewItemRequest;
import io.andrelucas.toiletnearme.toilet.infrastructure.controllers.requests.CreateToiletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/toilet")
public class ToiletController {
    private static final Logger logger = LoggerFactory.getLogger(ToiletController.class);

    private final RegisterToiletUseCase registerToiletUseCase;
    private final AddNewItemUseCase addNewItemUseCase;
    private final ToiletRepository toiletRepository;


    public ToiletController(final RegisterToiletUseCase registerToiletUseCase,
                            final AddNewItemUseCase addNewItemUseCase,
                            final ToiletRepository toiletRepository) {
        this.registerToiletUseCase = registerToiletUseCase;
        this.addNewItemUseCase = addNewItemUseCase;
        this.toiletRepository = toiletRepository;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final CreateToiletRequest request) {
        try {
            final var input = new RegisterToiletUseCase
                    .Input(request.description(), request.latitude(), request.longitude(), request.customerId());
            final var output = registerToiletUseCase.execute(input);

            final var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(output.toiletId())
                    .toUri();

            return ResponseEntity.created(uri).build();

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

    @PostMapping("/{toiletId}/item")
    public ResponseEntity<ItemId> addNewItem(@RequestBody final AddNewItemRequest request,
                                             @PathVariable("toiletId") final String toiletId){
        try {
            final var input = new AddNewItemUseCase.Input(request.description(), toiletId);
            final var output = addNewItemUseCase.execute(toiletRepository::findById, toiletRepository::update)
                    .apply(input);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(output.itemId());

        } catch (ToiletNotFoundException e) {
            logger.error("Toilet not found for request: {}", request, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Internal server error occurred while adding a new item to toilet: {}", request, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
