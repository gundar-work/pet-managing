package ia.mdotm.repository;

import ia.mdotm.model.Pet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

class MockPetRepositoryImplTest {

    @Test
    void save_assignsIdWhenNull() {

        Map<Long, Pet> store = new ConcurrentHashMap<>();
        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(store, new AtomicLong(0));

        Pet created = sut.save(new Pet(null, "Fido", "dog", 3, "Ana"));

        assertThat(created.id()).isEqualTo(1L);
        assertThat(store).containsKey(1L);
    }

    @Test
    void save_keepsExistingId() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));
        Pet input = new Pet(42L, "Mishi", "cat", 2, "Alfred");

        Pet saved = sut.save(input);

        assertThat(saved.id()).isEqualTo(42L);
        assertThat(sut.findById(42L)).contains(input);
    }

    @Test
    void findById_returnsEmptyWhenMissing() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));

        assertThat(sut.findById(99L)).isEmpty();
    }

    @Test
    void findAll_returnsSortedById() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));
        sut.save(new Pet(5L, "B", "dog", 1, "A"));
        sut.save(new Pet(2L, "A", "cat", 2, "B"));
        sut.save(new Pet(9L, "C", "bird", 3, "C"));

        List<Pet> all = sut.findAll();

        assertThat(all).extracting(Pet::id).containsExactly(2L, 5L, 9L);
    }

    @Test
    void deleteById_returnsTrueWhenDeleted() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));
        sut.save(new Pet(10L, "Toby", "dog", 4, "Sara"));

        boolean deleted = sut.deleteById(10L);

        assertThat(deleted).isTrue();
        assertThat(sut.existsById(10L)).isFalse();
    }

    @Test
    void existsById_returnsTrueWhenPresent() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));
        sut.save(new Pet(7L, "Moon", "cat", 2, "Martha"));

        assertThat(sut.existsById(7L)).isTrue();
    }

    @Test
    void deleteById_returnsFalseWhenMissing() {

        MockPetRepositoryImpl sut = new MockPetRepositoryImpl(new ConcurrentHashMap<>(), new AtomicLong(0));

        assertThat(sut.deleteById(55L)).isFalse();
    }
}
