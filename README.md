## Arquitectura

```
Front (index.html)  --login/venta-->  API 1 (api-gateway, puerto 8081)  --POST-->  API 2 (api-backend, puerto 8082)  --JPA-->  H2
```

- **api-gateway (API 1)**: recibe el JSON del front, valida los campos con Bean
  Validation (`@Valid`, `@Pattern`, `@NotBlank`), descifra el campo `secreto`
  (AES-256, enviado desde el front) y reenvía la transacción a la API 2 usando
  `RestTemplate`. También expone el login (usuario/password validado con BCrypt
  contra una tabla H2).
- **api-backend (API 2)**: guarda la transacción con `JpaRepository` en H2,
  genera una referencia aleatoria de 6 dígitos, marca el estatus como
  "Aprobada", expone el `PATCH` para cancelar (usando `@Query` + `@Modifying`)
  y el `GET` paginado (`Pageable`/`PageRequest`).
- **frontend**: un solo `index.html` con tres pantallas (login, registrar
  operación, listar con paginación). Cifra el campo `secreto` con
  `CryptoJS.AES.encrypt(texto, passphrase)` antes de enviarlo, igual formato
  que espera `AesEncryptionUtil.java` en el backend.

## Cómo correrlo

Requiere Java 17 y Maven (con acceso a Maven Central para descargar
dependencias la primera vez).

```bash
# Terminal 1
cd api-backend
mvn spring-boot:run

# Terminal 2
cd api-gateway
mvn spring-boot:run
```

Luego abre `frontend/index.html` directamente en el navegador (doble clic
basta, no necesita servidor porque hace `fetch` a `localhost:8081`).

Usuario de prueba (se crea automáticamente al levantar `api-gateway`):
- usuario: `admin`
- password: `admin123`

## Endpoints principales

| Método | URL | Descripción |
|---|---|---|
| POST | `http://localhost:8081/api/gateway/login` | Login (usuario/password BCrypt) |
| POST | `http://localhost:8081/api/gateway/venta` | Recibe la transacción, valida y reenvía a la API 2 |
| POST | `http://localhost:8082/api/backend/transacciones` | (uso interno, la llama la API 1) Guarda la transacción |
| PATCH | `http://localhost:8082/api/backend/transacciones` | Cancela una transacción: `{ "id": 1, "referencia": "262737", "estatus": "cancelar" }` |
| GET | `http://localhost:8082/api/backend/transacciones?page=0&size=10&sortBy=cliente&direction=asc` | Consulta paginada |

Consolas H2 (para ver los datos guardados):
- Gateway (tabla usuarios): `http://localhost:8081/h2-console` — JDBC URL `jdbc:h2:mem:gatewaydb`
- Backend (tabla transacciones): `http://localhost:8082/h2-console` — JDBC URL `jdbc:h2:mem:backenddb`

## Prueba con Postman (PATCH)

```
PATCH http://localhost:8082/api/backend/transacciones
Content-Type: application/json

{
  "id": 1,
  "referencia": "262737",
  "estatus": "cancelar"
}
```
