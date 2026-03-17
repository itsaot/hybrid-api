# Hybrid API

A Spring Boot REST API for businesses that sell both services and goods, such as hospitals, hotels, accommodation platforms, and car rentals.

## Features

- Manage providers like hospitals, hotels, and rental companies.
- Manage offerings that can be either `GOODS` or `SERVICE`.
- Create customer transactions that support goods-only purchases, service bookings, or mixed hybrid checkout.
- Protect endpoints with Spring Security using HTTP Basic authentication and role-based access.
- Publish interactive API docs with Swagger UI / OpenAPI.
- Support both in-memory H2 and PostgreSQL through Spring profiles.
- Include test-friendly email, weather, and maps integrations driven by `.env`.
- Seeded demo data with an in-memory H2 database.

## Run

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

## Environment File

Create a file named `.env` in the project root, beside `pom.xml`. You can copy from `.env.example`.

Example:

```env
APP_PORT=8080
SPRING_PROFILES_ACTIVE=default

DB_URL=jdbc:h2:mem:hybriddb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
DB_USERNAME=sa
DB_PASSWORD=
DB_DRIVER=org.h2.Driver

PAYMENT_PROVIDER=stripe
PAYMENT_BASE_URL=https://api.stripe.com
PAYMENT_PUBLIC_KEY=pk_test_replace_me
PAYMENT_SECRET_KEY=sk_test_replace_me
PAYMENT_WEBHOOK_URL=https://your-domain.example.com/api/payments/webhook

EMAIL_PROVIDER=resend
EMAIL_BASE_URL=https://api.resend.com
EMAIL_API_KEY=re_test_replace_me
EMAIL_FROM_ADDRESS=onboarding@resend.dev
EMAIL_ENABLED=false

WEATHER_PROVIDER=open-meteo
WEATHER_BASE_URL=https://api.open-meteo.com
WEATHER_API_KEY=
WEATHER_ENABLED=true

MAPS_PROVIDER=openstreetmap
MAPS_GEOCODE_BASE_URL=https://nominatim.openstreetmap.org
MAPS_TILES_BASE_URL=https://tile.openstreetmap.org/{z}/{x}/{y}.png
MAPS_ROUTE_BASE_URL=https://router.project-osrm.org
MAPS_API_KEY=
MAPS_ENABLED=true
```

Notes:

- `.env` is ignored by git.
- For local testing, keep `SPRING_PROFILES_ACTIVE=default` and use H2.
- For PostgreSQL, set `SPRING_PROFILES_ACTIVE=postgres` and update the database values.

## Authentication

Swagger UI and the H2 console are public. API endpoints require authentication.

Default demo users:

- `admin` / `admin123`
- `provider` / `provider123`
- `customer` / `customer123`

Role access:

- `ADMIN`: full provider creation/update, offerings, and transactions
- `PROVIDER_MANAGER`: create offerings and read API data
- `CUSTOMER`: create transactions and read API data

## API Docs

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Payment config preview: `GET /api/payments/config`
- Email config preview: `GET /api/email/config`
- Weather config preview: `GET /api/integrations/weather/config`
- Maps config preview: `GET /api/integrations/maps/config`

## Useful endpoints

- `GET /api/providers`
- `POST /api/providers`
- `GET /api/offerings?providerId=1&type=SERVICE`
- `POST /api/offerings`
- `GET /api/transactions`
- `POST /api/transactions`
- `GET /h2-console`

## PostgreSQL Profile

Run with PostgreSQL:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Environment variables used by the PostgreSQL profile:

- `DB_URL` default: `jdbc:postgresql://localhost:5432/hybrid_api`
- `DB_USERNAME` default: `postgres`
- `DB_PASSWORD` default: `postgres`

If you do not activate the `postgres` profile, the app uses H2 in-memory storage.

## H2 Console Fix

If the H2 console shows an error like `Database ".../test" not found`, it is using the wrong JDBC URL.

Use these exact H2 console values:

- JDBC URL: `jdbc:h2:mem:hybriddb`
- User Name: `sa`
- Password: leave blank

The application must already be running before you open the H2 console.

## How To Use It

This project is an API-first backend.

- It is primarily API-based: your app, frontend, mobile app, or Postman will call REST endpoints like `/api/providers` and `/api/transactions`.
- Swagger UI gives you a browser link to explore and test the API interactively.
- The H2 console is only a database admin page for local development, not the main product interface.

Step by step:

1. Copy `.env.example` to `.env`.
2. Leave `SPRING_PROFILES_ACTIVE=default` if you want the built-in H2 database for local testing.
3. Run `mvn spring-boot:run`.
4. Open `http://localhost:8080/swagger-ui/index.html`.
5. Click `Authorize` and enter one of the demo users:
   - `admin` / `admin123`
   - `provider` / `provider123`
   - `customer` / `customer123`
6. Start with `GET /api/providers` to confirm the API is working.
7. Use `GET /api/offerings` to view goods and services.
8. Use `POST /api/transactions` to create a booking, purchase, or hybrid checkout.
9. If you want to inspect the local database, open `http://localhost:8080/h2-console` and use the H2 values listed above.
10. If you want a real database, change `.env` to `SPRING_PROFILES_ACTIVE=postgres`, set the PostgreSQL values, and run the app again.

## Payment Settings

The app now reads payment settings from `.env`. Right now this is configuration-ready rather than a full payment gateway integration.

- `PAYMENT_PROVIDER`
- `PAYMENT_BASE_URL`
- `PAYMENT_PUBLIC_KEY`
- `PAYMENT_SECRET_KEY`
- `PAYMENT_WEBHOOK_URL`

You can verify what is loaded, without exposing the full secret, by calling `GET /api/payments/config` as an authenticated user.

## Email, Weather, And Maps

The app is now prewired for simple test providers:

- Email: Resend
- Weather: Open-Meteo
- Maps: OpenStreetMap tiles with Nominatim geocoding

These are all controlled through `.env`, so you can swap providers later without changing your controllers.

Useful endpoints:

- `GET /api/email/config`
- `POST /api/email/test`
- `GET /api/integrations/weather/current?latitude=-26.2041&longitude=28.0473`
- `GET /api/integrations/maps/search?query=Johannesburg`
- `GET /api/integrations/maps/config`
- `GET /api/integrations/maps/providers`
- `GET /api/integrations/maps/route?startLatitude=-26.2041&startLongitude=28.0473&endLatitude=-26.1453&endLongitude=28.0413`
- `GET /api/providers/search?query=Rosebank`

Notes:

- `POST /api/email/test` requires the `admin` user and a valid `EMAIL_API_KEY`.
- Weather and maps work out of the box for testing with the default Open-Meteo and OpenStreetMap settings.
- Maps integration returns search results and also exposes a tile URL template for a frontend map library.
- New transactions will attempt to send a confirmation email when `EMAIL_ENABLED=true` and the email provider is fully configured.
- `GET /api/integrations/maps/providers` is useful for React map markers because it returns provider IDs, names, categories, and coordinates directly.
- `GET /api/integrations/maps/route` returns simple route geometry points a frontend map can draw as a polyline.

## Example transaction payload

```json
{
  "customerName": "Alex Johnson",
  "customerEmail": "alex@example.com",
  "lines": [
    {
      "offeringId": 1,
      "quantity": 1,
      "serviceDate": "2026-03-20",
      "notes": "Morning slot preferred"
    },
    {
      "offeringId": 2,
      "quantity": 2
    }
  ]
}
```
