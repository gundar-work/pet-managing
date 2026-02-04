package ia.mdotm.model;

public record Pet(Long id, String name, String species, Integer age, String ownerName) {
  public Pet withId(Long id) {
    return new Pet(id, name, species, age, ownerName);
  }
}
