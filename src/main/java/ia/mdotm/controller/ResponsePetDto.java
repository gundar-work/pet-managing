package ia.mdotm.controller;

public record ResponsePetDto(
        long id,
        String name,
        String species,
        Integer age,
        String ownerName
) {
}