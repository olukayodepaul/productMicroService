-- 1. Products Table
-- Stores information about products available for sale.
-- Admin: Manages product details.
CREATE TABLE products (
id SERIAL PRIMARY KEY,                -- Unique identifier for each product
organisation_id UUID NOT NULL,        -- ID of the organisation that owns the product
name VARCHAR(255) NOT NULL,           -- Name of the product
description TEXT,                     -- Description of the product
price DECIMAL(10, 2) NOT NULL,        -- Price of the product
discount DECIMAL(10, 2) DEFAULT 0.00, -- Discount on the product
category_id INTEGER,                  -- ID of the category the product belongs to
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the product was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the product was last updated
is_active BOOLEAN DEFAULT TRUE         -- Status of the product (active or inactive)
);
-- Relationship: One-to-Many with product_media, product_specifications, product_policies, product_reviews, product_comments, product_feedback, related_products, special_offers, product_tags, product_wishlists

-- 2. Product Media Table
-- Stores media (images, videos) associated with products.
-- Admin: Uploads and manages media for products.
CREATE TABLE product_media (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each media entry
product_id INTEGER NOT NULL,             -- ID of the product associated with the media
organisation_id UUID NOT NULL,        -- ID of the organisation that owns the product media
media_type VARCHAR(10) CHECK (media_type IN ('image', 'video')), -- Type of media
media_url VARCHAR(255) NOT NULL,         -- URL of the media
is_primary BOOLEAN DEFAULT FALSE,        -- Indicates if this media is the primary image/video
is_active BOOLEAN DEFAULT TRUE,          -- Status of the product (active or inactive)
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the product was last updated
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp when media was added
);

-- 3. Product Specifications Table
-- Stores detailed specifications of products.
-- Admin: Inputs and manages product specifications.
CREATE TABLE product_specifications (
id SERIAL PRIMARY KEY,                                          -- Unique identifier for each specification entry
product_id INTEGER,                             				-- ID of the product associated with the specifications
organisation_id UUID ,                                  		-- ID of the organisation that owns the product specifications
length DECIMAL(10, 2),                                          -- Length of the product
width DECIMAL(10, 2),                                           -- Width of the product
height DECIMAL(10, 2),                                          -- Height of the product
weight DECIMAL(10, 2),                                          -- Weight of the product
material_description TEXT,                                      -- Description of the material used
certification_description TEXT,                                 -- Certification details of the product
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                 -- Timestamp when specifications were added
FOREIGN KEY (product_id) REFERENCES products(id)                -- Relationship: One-to-One (a product has unique specifications)
);
-- 4. Updated Shipping Details Table (Cross-border shipping support)
-- Stores shipping information for products, including domestic and international options.
CREATE TABLE shipping_details (
id SERIAL PRIMARY KEY,                                   -- Unique identifier for each shipping entry
product_id INTEGER NOT NULL,                             -- ID of the product associated with the shipping details
organisation_id UUID NOT NULL,                           -- ID of the organisation that owns the shipping details
shipping_method VARCHAR(100),                            -- Method of shipping (e.g., standard, express, freight)
shipping_cost DECIMAL(10, 2),                            -- Cost of shipping
estimated_delivery_time VARCHAR(50),                     -- Estimated delivery time (e.g., 3-5 days)
country_code VARCHAR(3) DEFAULT 'ALL',                   -- ISO country code (e.g., 'US', 'NG', 'ALL' for global shipping)
region VARCHAR(100),                                     -- Region or specific area (e.g., Europe, West Africa, etc.)
customs_fees DECIMAL(10, 2) DEFAULT 0.00,                -- Customs or import fees if applicable
handling_time VARCHAR(50),                               -- Time required to process the order before shipping (e.g., 1-2 days)
cross_border BOOLEAN DEFAULT FALSE,                      -- Indicates if this is a cross-border/international shipping method
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Timestamp when shipping details were added
FOREIGN KEY (product_id) REFERENCES products(id)         -- Relationship: One-to-Many (a product can have multiple shipping options)
);

-- 5. Product Policies Table
-- Stores warranty and return policy information for products.
-- Admin: Defines policies for products.
CREATE TABLE product_policies (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each policy entry
product_id INTEGER NOT NULL UNIQUE,      -- ID of the product associated with the policies
organisation_id UUID NOT NULL,           -- ID of the organisation that owns the product policies
warranty_description TEXT,               -- Description of the warranty
warranty_period VARCHAR(50),             -- Period of the warranty
return_policy_description TEXT,          -- Description of the return policy
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when policies were added
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-One (a product has unique policies)
FOREIGN KEY (organisation_id) REFERENCES organisations(id) -- Relationship to organisations
);

-- 6. Product Reviews Table
-- Stores customer reviews for products.
-- Customers: Submit reviews based on their experiences.
CREATE TABLE product_reviews (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each review entry
product_id INTEGER NOT NULL,             -- ID of the product being reviewed
organisation_id UUID NOT NULL,           -- ID of the organisation that owns the product reviews
uuid_id INTEGER NOT NULL,                -- ID of the user who wrote the review
rating INTEGER CHECK (rating BETWEEN 1 AND 5), -- Rating given to the product
review_text TEXT,                        -- Text of the review
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the review was created
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-Many (a product can have multiple reviews)
FOREIGN KEY (organisation_id) REFERENCES organisations(id) -- Relationship to organisations
);

-- 7. Related Products Table
-- Stores relationships between products for cross-selling or upselling.
-- Admin: Manages relationships between products.
CREATE TABLE related_products (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each related product entry
product_id INTEGER NOT NULL,             -- ID of the product
related_product_id INTEGER NOT NULL,     -- ID of the related product
organisation_id UUID NOT NULL,           -- ID of the organisation that owns the related products
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the relationship was created
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-Many (a product can have multiple related products)
FOREIGN KEY (related_product_id) REFERENCES products(id), -- Relationship to related products
FOREIGN KEY (organisation_id) REFERENCES organisations(id) -- Relationship to organisations
);

-- 8. Special Offers Table
-- Stores special offers and discounts for products.
-- Admin: Creates and manages special offers.
CREATE TABLE special_offers (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each offer entry
product_id INTEGER NOT NULL,             -- ID of the product associated with the offer
organisation_id UUID NOT NULL,           -- ID of the organisation that owns the special offer
offer_description TEXT,                  -- Description of the special offer
discount_percentage DECIMAL(5, 2),       -- Percentage discount
start_date TIMESTAMP,                    -- Start date of the offer
end_date TIMESTAMP,                      -- End date of the offer
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the offer was created
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-Many (a product can have multiple offers)
FOREIGN KEY (organisation_id) REFERENCES organisations(id) -- Relationship to organisations
);

-- 9. Product Tags Table
-- Stores tags associated with products for categorization.
-- Admin: Manages tags for product categorization.
CREATE TABLE product_tags (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each tag entry
product_id INTEGER NOT NULL,             -- ID of the product associated with the tag
organisation_id UUID NOT NULL,           -- ID of the organisation that owns the product tags
tag VARCHAR(50),                         -- Tag for the product
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the tag was created
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-Many (a product can have multiple tags)
FOREIGN KEY (organisation_id) REFERENCES organisations(id) -- Relationship to organisations
);

-- 10. Product Comments Table
-- Stores customer comments for products separately from ratings.
-- Customers: Leave comments regarding their experiences.
CREATE TABLE product_comments (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each comment entry
product_id INTEGER NOT NULL,             -- ID of the product being commented on
user_id INTEGER NOT NULL,                -- ID of the user who wrote the comment
comment_text TEXT NOT NULL,              -- Text of the comment
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the comment was created
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship: One-to-Many (a product can have multiple comments)
FOREIGN KEY (user_id) REFERENCES users(id) -- Relationship to users
);

-- 11. Product Feedback Table
-- Stores user feedback (like or dislike) for products.
-- Customers: Provide feedback on products.
CREATE TABLE product_feedback (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each feedback entry
product_id INTEGER NOT NULL,             -- ID of the product being liked or disliked
user_id INTEGER NOT NULL,                -- ID of the user giving the feedback
feedback_type VARCHAR(10) CHECK (feedback_type IN ('like', 'dislike')), -- Type of feedback (like or dislike)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the feedback was given
FOREIGN KEY (product_id) REFERENCES products(id), -- Relationship
