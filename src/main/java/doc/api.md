#### productMicroService

###### product registration
You can use the following `curl` command to test the product creation API:
- Product creation. The product is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
```bash
curl -X POST "/api/products" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" \
-d '{
    "name": "Product Name",
    "description": "Product description",
    "price": 29.99,
    "discount": 5.00,
    "category_id": 2
}'
```

###### product update
You can use the following `curl` command to test the product update API:
- Product update. The product is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
```bash
curl -X PUT " /api/products/{id}" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" \
-d '{
  "name": "Product1",
  "description": "Product description11",
  "price": 29.91,
  "discount": 55.01,
  "category_id": 11
}'
```

###### product deletion
You can use the following `curl` command to test the product deletion API:
- Product deletion. The product is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
```bash
curl -X DELETE " /api/products/{id}" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" \
-d '{
}'
```

###### get all product
You can use the following `curl` command to test the product fetch API:
- Product deletion. The product is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
- get all the product associated with the organisation_id
```bash
curl -X GET " /api/products" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" \
-d '{
```

###### get product by id
You can use the following `curl` command to test all the individual product fetch API:
- Product deletion. The product is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
- get all the product associated with the organisation_id
```bash
curl -X GET " /api/products/{id}" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" \
-d '{
  "id" 1,
  "organisation_id": 1,
```


