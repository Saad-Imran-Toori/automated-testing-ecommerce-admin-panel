# Postman – API Test Collection

Postman was used to manually exercise and verify every endpoint of the e‑commerce REST API
(`auth.php` and `products.php`).

## Contents

| File | Description |
|------|-------------|
| `ecommerce_api.postman_collection.json` | Importable Postman collection (v2.1) with a request + assertion for every endpoint. **Auto‑generated to match the documented API** for convenience. |
| `postman work.docx` | The original Postman testing write‑up. |
| `../../screenshots/postman/` | 13 screenshots of the actual Postman requests and responses (`postman_01.png` … `postman_13.png`). |

## How to use the collection

1. Open Postman → **Import** → select `ecommerce_api.postman_collection.json`.
2. Make sure the app is running and, if needed, adjust the `baseUrl` collection variable
   (default `http://localhost/ecommerce_api/backend/api`).
3. Run the requests individually, or use the **Collection Runner** to execute them all.

## Endpoints covered

**Auth (`auth.php`, POST)**
- Sign up a new admin
- Log in an admin

**Products (`products.php`)**
- `GET` all products
- `GET` a single product (`?id=`)
- `POST` create a product
- `PUT` update a product (`?id=`)
- `DELETE` a product (`?id=`)
- `POST` with a negative price → expects a validation error

Each request includes a basic test script asserting the response `status` field.
