package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toilet")
public class ToiletController {

    private final RegisterToiletUseCase registerToiletUseCase;

    public ToiletController(RegisterToiletUseCase registerToiletUseCase) {
        this.registerToiletUseCase = registerToiletUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final CreateToiletRequest request) {

        final var input = new RegisterToiletUseCase
                .Input(request.description(), request.latitude(), request.longitude(), request.customerId());
        registerToiletUseCase.execute(input);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public record CreateToiletRequest(String description, double latitude, double longitude, String customerId){}
}
