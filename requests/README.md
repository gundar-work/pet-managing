# Bruno requests

Collection: `pet-managing`

## Environment
- Use `requests/environments/local.bru` with:
  - `baseUrl` (default `http://localhost:8080`)

## Recommended order
1. Create Pet
2. Get Pet By Id (update `id` if the created one is different)
3. Update Pet
4. List Pets
5. Delete Pet

## Error and validation samples
- Create Pet - Validation Error
- Update Pet - Validation Error
- Create Pet - Malformed JSON
- Update Pet - Not Found (uses `missingPetId`)
- Delete Pet - Not Found (uses `missingPetId`)
