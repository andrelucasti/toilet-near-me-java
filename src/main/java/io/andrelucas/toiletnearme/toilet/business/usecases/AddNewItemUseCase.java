package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.toilet.business.ItemId;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AddNewItemUseCase {

    public Function<Input, Output> execute(final Function<ToiletId, Optional<Toilet>> findToiletById,
                                           final Consumer<Toilet> updateToilet) {

        return (input) -> {
            final var toiletId = ToiletId.with(input.value());
            final var toilet = findToiletById.apply(toiletId)
                    .orElseThrow(() -> new ToiletNotFoundException("Toilet does not exist"));

            final var tuple = toilet.addItem(input.soap());
            updateToilet.accept(tuple._1());

            return new Output(tuple._1().id(), tuple._2().id());
        };
    }

    public record Input(String soap, String value){}
    public record Output(ToiletId toiletId, ItemId itemId){}
}
