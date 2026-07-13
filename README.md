# Evaluación Plataformas Especiales 2026 — Solución

Proyecto de dos microservicios Spring Boot + frontend en HTML/JS puro, resolviendo
el ejercicio solicitado.

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

## Decisiones de diseño (para poder explicarlas en la entrevista)

1. **Por qué dos microservicios separados y no uno solo**: así se respeta
   exactamente el flujo pedido (API 1 recibe/valida/descifra → reenvía a API 2
   que persiste), y se practica comunicación entre servicios con `RestTemplate`.
2. **Cifrado AES-256 del campo `secreto`**: en el front se usa `CryptoJS.AES.encrypt`
   con una passphrase compartida. CryptoJS genera un string en formato compatible
   con OpenSSL (`Salted__` + salt + datos), por lo que en Java se reconstruye la
   derivación de llave/IV (EVP_BytesToKey con MD5) en `AesEncryptionUtil.java`
   para poder descifrarlo. Una vez descifrado, se guarda en texto plano en la
   base de datos, tal como pide el enunciado.
3. **Validaciones**: se usan anotaciones de Bean Validation (`@NotBlank`,
   `@Pattern`) sobre el DTO de entrada, y un `@RestControllerAdvice`
   (`GlobalExceptionHandler`) centraliza el manejo de errores de validación y
   de errores al llamar a la API 2.
4. **Password de login con BCrypt**: se usa `BCryptPasswordEncoder` de
   `spring-security-crypto` (sin habilitar todo el módulo de Spring Security,
   ya que el ejercicio no pide seguridad de endpoints, solo el hash del
   password). El usuario demo se inserta automáticamente vía `CommandLineRunner`.
5. **Referencia y estatus**: se generan en la API 2 con `SecureRandom`
   (6 dígitos) al momento de guardar, y el estatus inicial siempre es "Aprobada".
6. **PATCH con `@Query`**: se actualiza el estatus a "Cancelada" solo si
   coinciden `id` y `referencia`, usando una consulta JPQL con `@Modifying`.
7. **Paginación**: se implementa con `Pageable`/`PageRequest` de Spring Data
   JPA, permitiendo indicar página, tamaño y campo/dirección de ordenamiento
   por query params.

## Posibles mejoras (si preguntan "qué le faltaría")

- Mover la passphrase AES y demás configuración sensible a variables de entorno.
- Agregar Spring Security completo con JWT para proteger los endpoints tras el login.
- Agregar pruebas unitarias (JUnit/Mockito) para servicios y controllers.
- Usar OpenFeign en vez de RestTemplate (que ya está deprecado a futuro) para
  la comunicación entre microservicios.
- Manejo de errores más granular (por ejemplo, tiempo de espera/circuit breaker
  con Resilience4j al llamar a la API 2).
