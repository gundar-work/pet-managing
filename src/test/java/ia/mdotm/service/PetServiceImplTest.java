package ia.mdotm.service;

import ia.mdotm.model.Pet;
import ia.mdotm.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

  @Mock
  private PetRepository petRepository;

  @InjectMocks
  private PetServiceImpl sut;

  @Test
  void create_throwsWhenNull() {

    assertThatThrownBy(() -> sut.create(null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("must not be null");

    verifyNoInteractions(petRepository);
  }

  @Test
  void create_savesPet() {

    Pet input = new Pet(null, "Fido", "dog", 3, "Ana");
    Pet saved = new Pet(1L, "Fido", "dog", 3, "Ana");
    when(petRepository.save(input)).thenReturn(saved);

    Pet result = sut.create(input);

    assertThat(result).isEqualTo(saved);
    verify(petRepository).save(input);
  }

  @Test
  void getById_delegatesToRepository() {

    Pet pet = new Pet(5L, "Moon", "cat", 2, "Martha");
    when(petRepository.findById(5L)).thenReturn(Optional.of(pet));

    Optional<Pet> result = sut.getById(5L);

    assertThat(result).contains(pet);
    verify(petRepository).findById(5L);
  }

  @Test
  void list_delegatesToRepository() {

    List<Pet> pets = List.of(
      new Pet(1L, "A", "cat", 1, "B"),
      new Pet(2L, "C", "dog", 2, "D")
    );
    when(petRepository.findAll()).thenReturn(pets);

    List<Pet> result = sut.list();

    assertThat(result).containsExactlyElementsOf(pets);
    verify(petRepository).findAll();
  }

  @Test
  void update_throwsWhenNull() {

    assertThatThrownBy(() -> sut.update(null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("must not be null");

    verifyNoInteractions(petRepository);
  }

  @Test
  void update_throwsWhenPetDoesNotExist() {

    Pet input = new Pet(9L, "Mike", "bird", 1, "John");
    when(petRepository.existsById(9L)).thenReturn(false);

    assertThatThrownBy(() -> sut.update(input))
      .isInstanceOf(PetNotFoundException.class)
      .hasMessageContaining("Pet not found for id = [9]");

    verify(petRepository).existsById(9L);
  }

  @Test
  void update_savesWhenPetExists() {

    Pet input = new Pet(9L, "Toby", "bird", 1, "Michael");
    when(petRepository.existsById(9L)).thenReturn(true);
    when(petRepository.save(input)).thenReturn(input);

    Pet result = sut.update(input);

    assertThat(result).isEqualTo(input);
    verify(petRepository).existsById(9L);
    verify(petRepository).save(input);
  }

  @Test
  void delete_delegatesToRepository() {

    when(petRepository.deleteById(4L)).thenReturn(true);

    boolean result = sut.delete(4L);

    assertThat(result).isTrue();
    verify(petRepository).deleteById(4L);
  }
}
