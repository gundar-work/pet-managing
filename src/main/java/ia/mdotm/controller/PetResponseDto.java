package ia.mdotm.controller;

public record PetResponseDto(
  long id,
  String name,
  String species,
  Integer age,
  String ownerName
) {
}