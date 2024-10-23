## productMicro-Service

### Product features
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
<br>

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
<br>

###### product deletion
You can use the following `curl` command to test the product deletion API:
- Product is deleted with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
```bash
curl -X DELETE " /api/products/{id}" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" 
```
<br>

###### get all product
You can use the following `curl` command to test the product fetch API:
- The product is fetch with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
- get all the product associated with the organisation_id
```bash
curl -X GET " /api/products" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" 
```
<br>

###### get product by id
You can use the following `curl` command to test all the individual product fetch API:
- The individual products are fetch with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
- get individual product associated with the organisation_id
```bash
curl -X GET " /api/products/{id}" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <Role-admin>" 
```

<br><br>

### Product Media features
###### add Media data. image or video with primary and secondary listing
You can use the following `curl` command to test all the individual product fetch API:
- Product Media creation. The product media is created with the organisation_id to maintain ownership.
- the token `role*` should be `admin` for the claim
```bash
curl -X POST "/api/product-media" \
-H "Content-Type: multipart/form-data" \
-F 'data={"product_id": 1}' \
-F "file=@/path/to/your/image.jpg"
```

###### Update file media


###### Update primary listing

