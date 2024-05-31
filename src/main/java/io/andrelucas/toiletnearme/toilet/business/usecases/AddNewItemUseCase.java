package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;

public class AddNewItemUseCase {

    private final ToiletRepository toiletRepository;

    public AddNewItemUseCase(ToiletRepository toiletRepository) {
        this.toiletRepository = toiletRepository;
    }

    public Output execute(final Input input) {
        final var toiletId = ToiletId.with(input.value());
        final var toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet does not exist"));

        return new Output();
    }

    public record Input(String soap, String value){}
    public record Output(){}
}
