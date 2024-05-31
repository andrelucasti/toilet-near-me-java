package io.andrelucas.toiletnearme.customer.infrastructure.controller;

import io.andrelucas.toiletnearme.customer.business.usecases.CreateCustomerUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final CreateCustomerRequest request) {

        final var output = createCustomerUseCase.execute(new CreateCustomerUseCase.Input(request.name(), request.email()));
        final var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(output.customerId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    public record CreateCustomerRequest(String name, String email){}
}
