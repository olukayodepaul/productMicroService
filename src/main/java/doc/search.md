-- 1. Products Table
-- Stores information about products available for sale.
-- Admin: Manages product details.
CREATE TABLE products (
id SERIAL PRIMARY KEY,                              -- Unique identifier for each product
organisation_id UUID NOT NULL,                      -- ID of the organisation that owns the product
brand_id INTEGER REFERENCES brands(id),             -- Foreign key reference to the brands table
name VARCHAR(255) NOT NULL,                         -- Name of the product
description TEXT,                                   -- Description of the product
price DECIMAL(10, 2) NOT NULL,                      -- Price of the product
discount DECIMAL(10, 2) DEFAULT 0.00,               -- Discount on the product
category_id INTEGER,                                -- ID of the category the product belongs to
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,     -- Timestamp when the product was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,     -- Timestamp when the product was last updated
is_active BOOLEAN DEFAULT TRUE                       -- Status of the product (active or inactive)
);
-- Relationship: One-to-Many with product_media, product_specifications, product_policies, product_reviews, product_comments, product_feedback, related_products, special_offers, product_tags, product_wishlists


-- 2. Search Queries Table
-- Logs each search query entered by users.
CREATE TABLE search_queries (
id SERIAL PRIMARY KEY,                    -- Unique identifier for each search query
user_id INT REFERENCES users(id),         -- References the user who made the search (optional, nullable for anonymous users)
query_text VARCHAR(255) NOT NULL,         -- The search term or phrase entered by the user
created_at TIMESTAMP DEFAULT NOW(),       -- Timestamp of when the search was made
results_count INT NOT NULL,               -- Number of products returned for this search query
organization_id INT REFERENCES organizations(id) -- (Optional) ID of the organization to filter searches by specific brand or group if needed
);


-- 3. Clicks Table
-- Logs each click event related to search queries.
CREATE TABLE clicks (
id SERIAL PRIMARY KEY,                      -- Unique identifier for each click event
search_query_id INT REFERENCES search_queries(id) ON DELETE CASCADE, -- Reference to the search query associated with this click
product_id INT REFERENCES products(id),     -- References the product that was clicked
clicked_at TIMESTAMP DEFAULT NOW(),         -- Timestamp of when the product was clicked
user_id INT REFERENCES users(id),           -- References the user who clicked the product (nullable for anonymous users)
organization_id INT REFERENCES organizations(id) -- (Optional) Tracks the organization context in multi-tenant applications
);


-- 4. Indexing Queue Table
-- Manages indexing tasks for newly added or updated products.
CREATE TABLE indexing_queue (
id SERIAL PRIMARY KEY,                          -- Unique identifier for each indexing task
product_id INT REFERENCES products(id) ON DELETE CASCADE, -- ID of the product to be indexed
status VARCHAR(50) DEFAULT 'pending',           -- Status of the indexing task (e.g., pending, processing, completed)
created_at TIMESTAMP DEFAULT NOW(),             -- Timestamp when the indexing task was created
processed_at TIMESTAMP,                         -- Timestamp when the indexing task was processed
organization_id INT REFERENCES organizations(id) -- (Optional) Organization-specific indexing task in multi-tenant setups
);


-- 5. Search Filters Table
-- Stores available filters for search (dynamic filters like category, brand, etc.)
CREATE TABLE search_filters (
id SERIAL PRIMARY KEY,                          -- Unique identifier for each filter
filter_type VARCHAR(50) NOT NULL,               -- Type of filter (e.g., 'category', 'brand', 'price_range')
filter_value VARCHAR(255) NOT NULL,             -- Value for the filter (e.g., 'Electronics', 'Nike', 'Under $50')
is_active BOOLEAN DEFAULT TRUE,                 -- Status of the filter (active or inactive)
created_at TIMESTAMP DEFAULT NOW(),             -- Timestamp of filter creation
updated_at TIMESTAMP DEFAULT NOW()              -- Timestamp of last update
);


-- 6. Search Suggestions Table
-- Stores search suggestions based on trending or popular keywords.
CREATE TABLE search_suggestions (
id SERIAL PRIMARY KEY,                          -- Unique identifier for each suggestion
suggestion_text VARCHAR(255) NOT NULL,          -- Suggested keyword or phrase for searches
popularity_score INT DEFAULT 0,                 -- Score based on popularity to rank suggestions
created_at TIMESTAMP DEFAULT NOW(),             -- Timestamp when the suggestion was added
updated_at TIMESTAMP DEFAULT NOW()              -- Timestamp when the suggestion was last updated
);


-- Indexes for optimizing search, indexing, and suggestion performance
CREATE INDEX idx_search_queries_query_text ON search_queries(query_text); -- Index on query_text for optimized full-text search
CREATE INDEX idx_clicks_product_id ON clicks(product_id);                 -- Index on product_id to track product popularity
CREATE INDEX idx_clicks_user_id ON clicks(user_id);                       -- Index on user_id for user-specific click behavior
CREATE INDEX idx_search_queries_user_id ON search_queries(user_id);       -- Index on user_id to speed up user-specific search queries
CREATE INDEX idx_indexing_queue_status ON indexing_queue(status);          -- Index on status to efficiently filter indexing tasks
CREATE INDEX idx_search_filters_type_value ON search_filters(filter_type, filter_value); -- Composite index on filter type and value for efficient retrieval
CREATE INDEX idx_search_suggestions_popularity ON search_suggestions(popularity_score); -- Index to quickly fetch popular suggestions


/*
Documentation:

1. `products` Table:
    - Contains detailed information about each product, including:
        - `id`: Unique identifier for each product.
        - `organisation_id`: ID of the organisation that owns the product.
        - `brand_id`: References the brands table to indicate the brand of the product.
        - `name`: Name of the product.
        - `description`: Detailed description of the product.
        - `price`: Price of the product.
        - `discount`: Discount applied to the product.
        - `category_id`: ID of the category the product belongs to.
        - `created_at`: Timestamp of when the product was created.
        - `updated_at`: Timestamp of the last update to the product.
        - `is_active`: Status indicating whether the product is active.

2. `search_queries` Table:
    - Records each search query initiated by users, storing details such as:
        - `id`: Unique identifier for each search entry.
        - `query_text`: The text entered by the user for searching products.
        - `user_id`: The user who initiated the search (nullable for anonymous users).
        - `created_at`: Automatically sets the timestamp of when the search was made.
        - `results_count`: Records the number of results found for each query.
        - `organization_id`: (Optional) Links the query to a specific organization, useful in multi-tenant environments.

3. `clicks` Table:
    - Logs each click on a product following a search. Each entry includes:
        - `id`: Unique identifier for each click event.
        - `search_query_id`: Links each click to a specific search query, allowing analysis of product clicks within specific searches.
        - `product_id`: ID of the clicked product, allowing tracking of product popularity.
        - `clicked_at`: Automatically records the timestamp of the click.
        - `user_id`: References the user who clicked the product (nullable for anonymous users).
        - `organization_id`: (Optional) Links the click event to an organization for multi-tenant setups.

4. `indexing_queue` Table:
    - Purpose: Manages indexing tasks, especially useful when new products are added or existing ones are updated, ensuring products are searchable.
    - Columns:
        - `id`: Unique identifier for each task.
        - `product_id`: References the product that needs indexing, ensuring products can be processed individually.
        - `status`: Tracks the task's status (e.g., 'pending', 'processing', 'completed').
        - `created_at`: Timestamp of task creation.
        - `processed_at`: Timestamp of task completion.
        - `organization_id`: (Optional) Links to an organization in multi-tenant setups.
    - Use Case: Provides a reliable way to manage and track indexing requests, improving search indexing efficiency.

5. `search_filters` Table:
    - Purpose: Stores available filters (e.g., category, brand) to enhance search flexibility and precision.
    - Columns:
        - `id`: Unique identifier for each filter.
        - `filter_type`: Type of filter (e.g., category, brand).
        - `filter_value`: Value of the filter.
        - `is_active`: Indicates if the filter is active, allowing dynamic filter control.
        - `created_at`: Timestamp of creation.
        - `updated_at`: Timestamp of last update, useful for managing dynamic filters.

6. `search_suggestions` Table:
    - Purpose: Stores trending or frequently used search terms to assist with real-time search suggestions.
    - Columns:
        - `id`: Unique identifier for each suggestion.
        - `suggestion_text`: Suggested keyword or phrase based on search trends or manual curation.
        - `popularity_score`: Tracks the popularity to prioritize suggestions.
        - `created_at`: Timestamp of suggestion creation.
        - `updated_at`: Timestamp of last update, allowing suggestion refresh based on trends.
          */
