package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddNewItemUseCaseTest {
    private ToiletRepository toiletRepository;

    @BeforeEach
    void setUp() {
        toiletRepository = mock(ToiletRepository.class);
    }

    @Test
    void shouldNotAddItemWhenAToiletWasNotCreatedYet() {
        final var toiletId = ToiletId.newId();
        when(toiletRepository.findById(toiletId)).thenReturn(Optional.empty());
        // given
        final var useCase = new AddNewItemUseCase();

        // when
        Assertions.assertThrows(ToiletNotFoundException.class, () -> useCase.execute(toiletRepository::findById, toiletRepository::update)
                .apply(new AddNewItemUseCase.Input("Soap", toiletId.value())));

        // then
        verify(toiletRepository, never()).update(any());
    }
    @Test
    void shouldAddANewItemWhenToiletIsCreatedByFunctional() {
        final var capture = ArgumentCaptor.forClass(Toilet.class);
        final var toiletId = ToiletId.newId();
        final var toiletCreated = new Toilet(toiletId, "Toilet", new Geolocation(0.0, 0.0), Collections.emptySet());

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toiletCreated));

        final var useCase = new AddNewItemUseCase();
        useCase.execute(toiletRepository::findById, toiletRepository::update)
                .apply(new AddNewItemUseCase.Input("Soap", toiletId.value()));

        verify(toiletRepository).update(capture.capture());

        final var toilet = capture.getValue();

        assertThat(toilet.items()).hasSize(1);
        assertThat(toilet.items()).extracting("description").containsExactly("Soap");
        assertThat(toilet.id()).isEqualTo(toiletId);
        assertThat(toilet.name()).isEqualTo(toiletCreated.name());
        assertThat(toilet.geolocation()).isEqualTo(toiletCreated.geolocation());
    }

    @Test
    void shouldReturnOutputWithToiletIdAndItemIdWhenTheItemIsCreated() {
        final var capture = ArgumentCaptor.forClass(Toilet.class);
        final var toiletId = ToiletId.newId();
        final var toiletCreated = new Toilet(toiletId, "Toilet", new Geolocation(0.0, 0.0), Collections.emptySet());

        when(toiletRepository.findById(toiletId)).thenReturn(Optional.of(toiletCreated));

        final var useCase = new AddNewItemUseCase();
        final var output = useCase.execute(toiletRepository::findById, toiletRepository::update)
                .apply(new AddNewItemUseCase.Input("Soap", toiletId.value()));

        verify(toiletRepository).update(capture.capture());

        final var toilet = capture.getValue();

        assertThat(toilet.items()).hasSize(1);
        assertThat(toilet.items()).extracting("description").containsExactly("Soap");
        assertThat(toilet.id()).isEqualTo(toiletId);
        assertThat(toilet.name()).isEqualTo(toiletCreated.name());
        assertThat(toilet.geolocation()).isEqualTo(toiletCreated.geolocation());

        assertThat(output.toiletId()).isEqualTo(toiletId);
        assertThat(output.itemId()).isEqualTo(toilet.items().stream().findFirst().get().id());
    }
}