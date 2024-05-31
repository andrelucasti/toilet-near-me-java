package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AddNewItemUseCaseTest {

    private ToiletRepository toiletRepository;

    @BeforeEach
    void setUp() {
        toiletRepository = mock(ToiletRepository.class);
    }

    @Test
    void shouldNotAddItemWhenAToiletNotWasCreatedYet() {
        final var toiletId = ToiletId.newId();
        when(toiletRepository.findById(toiletId)).thenReturn(Optional.empty());
        // given
        final var useCase = new AddNewItemUseCase(toiletRepository);

        // when
        Assertions.assertThrows(ToiletNotFoundException.class, () -> useCase.execute(new AddNewItemUseCase.Input("Soap", toiletId.value())));

        // then
        verify(toiletRepository, never()).save(any());
    }
}