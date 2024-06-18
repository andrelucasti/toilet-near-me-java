package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.toilet.business.GeolocationInvalidException;
import io.andrelucas.toiletnearme.toilet.business.ItemId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventType;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.AddNewItemUseCase;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import io.andrelucas.toiletnearme.toilet.infrastructure.controllers.requests.AddNewItemRequest;
import io.andrelucas.toiletnearme.toilet.infrastructure.controllers.requests.CreateToiletRequest;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.SemanticAttributes;
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
    private final OpenTelemetry openTelemetry;


    public ToiletController(final RegisterToiletUseCase registerToiletUseCase,
                            final AddNewItemUseCase addNewItemUseCase,
                            final ToiletRepository toiletRepository, OpenTelemetry openTelemetry) {
        this.registerToiletUseCase = registerToiletUseCase;
        this.addNewItemUseCase = addNewItemUseCase;
        this.toiletRepository = toiletRepository;
        this.openTelemetry = openTelemetry;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final CreateToiletRequest request) {
        final var span = openTelemetry.getTracer("toilet-near-me-spring")
                .spanBuilder("/toilet")
                .startSpan()
                .setAttribute("http.request.method", "POST")
                .setAttribute("customerId", request.customerId())
                .setAttribute("latitude", request.latitude())
                .setAttribute("longitude", request.longitude());

        try(final var scope = span.makeCurrent()) {
            final var input = new RegisterToiletUseCase
                    .Input(request.description(), request.latitude(), request.longitude(), request.customerId());

            final var output = registerToiletUseCase.execute(input);

            final var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(output.toiletId())
                    .toUri();

            span.setAttribute("http.status_code", HttpStatus.CREATED.value());
            return ResponseEntity.created(uri).build();

        } catch (GeolocationInvalidException e) {
            logger.error("Invalid geolocation for request: {}", request, e);
            span.recordException(e);
            span.setAttribute("http.status_code", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (CustomerNotFound e) {
            logger.error("Customer not found for request: {}", request, e);
            span.recordException(e);
            span.setAttribute("http.status_code", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Internal server error occurred while creating a toilet: {}", request, e);
            span.recordException(e);
            span.setAttribute("http.status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            span.end();
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
