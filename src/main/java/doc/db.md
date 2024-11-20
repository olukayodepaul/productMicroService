

CREATE TABLE brands (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each brand
organisation_id UUID NOT NULL,                              -- ID of the organisation that owns the brand
name VARCHAR(255) NOT NULL,                                 -- Name of the brand
logo_url VARCHAR(255),                                      -- URL of the brand logo
description TEXT,                                           -- Description of the brand
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Timestamp when the brand was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Timestamp when the brand was last updated
FOREIGN KEY (organisation_id) REFERENCES organisations(id)  -- Foreign key to the organisations table
);


-- 1. Products Table
-- Stores information about products available for sale.
-- Admin: Manages product details.
CREATE TABLE products (
    id SERIAL PRIMARY KEY,                                          -- Unique identifier for each product
    organisation_id UUID NOT NULL,                                  -- ID of the organisation that owns the product
    brand_id INTEGER REFERENCES brands(id),                         -- Foreign key reference to the brands table
    name VARCHAR(255) NOT NULL,                                     -- Name of the product
    description TEXT,                                               -- Description of the product
    price DECIMAL(10, 2) NOT NULL,                                  -- Price of the product
    currency VARCHAR(50) NOT NULL,                                  -- Specify currency
    discount DECIMAL(10, 2) DEFAULT 0.00,                           -- Discount on the product
    category_id INTEGER,                                            -- ID of the category the product belongs to
    created_by UUID NOT NULL,                                       -- New field for tracking who created the record
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                 -- Timestamp when the product was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                 -- Timestamp when the product was last updated
    is_active BOOLEAN DEFAULT TRUE,                                 -- Status of the product (active or inactive)
    warranty_policy_id INTEGER REFERENCES warranty_policies(id),    -- Foreign key to warranty policies
    return_policy_id INTEGER REFERENCES return_policies(id)         -- Foreign key to return policies,
);
-- Relationship: One-to-Many with product_media, product_specifications, product_policies, product_reviews, product_comments, product_feedback, related_products, special_offers, product_tags, product_wishlists

CREATE TABLE products_log_trail (
    id SERIAL PRIMARY KEY,                                          -- Unique identifier for each product
    products_id INTEGER REFERENCES products(id),
    change_type VARCHAR(50) NOT NULL,                               -- Type of change: 'update', 'delete', created, etc.
    organisation_id UUID NOT NULL,                                  -- ID of the organisation that owns the product
    brand_id INTEGER REFERENCES brands(id),                         -- Foreign key reference to the brands table
    name VARCHAR(255) NOT NULL,                                     -- Name of the product
    description TEXT,                                               -- Description of the product
    price DECIMAL(10, 2) NOT NULL,                                  -- Price of the product
    currency VARCHAR(50) NOT NULL,                                  -- Specify currency
    discount DECIMAL(10, 2) DEFAULT 0.00,                           -- Discount on the product
    category_id INTEGER,                                            -- ID of the category the product belongs to
    created_updated_deleted_by UUID NOT NULL,                       -- New field for tracking who?
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                 -- Timestamp when the product was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                 -- Timestamp when the product was last updated
    is_active BOOLEAN DEFAULT TRUE,                                 -- Status of the product (active or inactive)
    warranty_policy_id INTEGER REFERENCES warranty_policies(id),    -- Foreign key to warranty policies
    return_policy_id INTEGER REFERENCES return_policies(id) ,       -- Foreign key to return policies,
    consolidated UUID NOT NULL,                                     -- map old and new change_type together,
    old_new_change VARCHAR(5) NOT NULL                              -- specify which is old which is new change
);



 //----------------------------------------------------------------------------------------------//
-- 2. Product Comments Table
-- Stores customer comments for products separately from ratings.
-- Customers: Leave comments regarding their experiences.
CREATE TABLE product_comments (
id SERIAL PRIMARY KEY,                                  -- Unique identifier for each comment entry
product_id INTEGER NOT NULL,                            -- ID of the product being commented on
user_id UUID NOT NULL,                                  -- ID of the user who wrote the comment
organisation_id UUID NOT NULL,                          -- ID of the organisation that owns the product
comment_text TEXT NOT NULL,                             -- Text of the comment
is_active BOOLEAN DEFAULT TRUE,                         -- Status of the comment (active or inactive)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         -- Timestamp when the comment was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         -- Timestamp when the comment was last updated
FOREIGN KEY (product_id) REFERENCES products(id),       -- Relationship: One-to-Many (a product can have multiple comments)

    -- Unique constraint for one comment per user per product (if desired)
    UNIQUE (user_id, product_id)
);

-- Optional indexes to improve query performance on product_id and user_id
CREATE INDEX idx_product_comments_product_id ON product_comments(product_id);
CREATE INDEX idx_product_comments_user_id ON product_comments(user_id);



//----------------------------------------------------------------------------------------------//
-- 3. Product Feedback Table
-- Ensure one feedback per user per product
-- Stores user feedback (like or dislike or neutral) for products.
-- Customers: Provide feedback on products.
CREATE TABLE product_feedback (
id SERIAL PRIMARY KEY,                    -- Unique identifier for each feedback entry
organisation_id UUID NOT NULL,            -- ID of the organisation that owns the product
product_id INTEGER NOT NULL,              -- ID of the product receiving feedback
user_id UUID NOT NULL,                    -- ID of the user giving the feedback
feedback_type VARCHAR(10) CHECK (feedback_type IN ('like', 'dislike', 'neutral')), -- Type of feedback
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp when the feedback was last updated
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp when the feedback was given
FOREIGN KEY (product_id) REFERENCES products(id) -- Relationship: One-to-Many (a product can have multiple feedback entries)
);
-- Optional index to improve query performance on product_id
CREATE INDEX idx_product_feedback_product_id ON product_feedback(product_id);



//----------------------------------------------------------------------------------------------//

-- 4. Product Media Table
-- Stores media (images, videos) associated with products.
-- Relationship: One-to-Many (a product can have multiple media type entries)
-- Admin: Uploads and manages media for products.

CREATE TABLE product_media (
id SERIAL PRIMARY KEY,                   		-- Unique identifier for each media entry
product_id INTEGER NOT NULL,             		-- ID of the product associated with the media
organisation_id UUID NOT NULL,        			-- ID of the organisation that owns the product media
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 	-- Timestamp when media was added
);

CREATE TABLE product_media_content (
id SERIAL PRIMARY KEY,                   							-- Unique identifier for each media entry
product_media_id INTEGER REFERENCES product_media(id),
product_id INTEGER NOT NULL,             							-- ID of the product associated with the media
organisation_id UUID NOT NULL,        								-- ID of the organisation that owns the product media
created_by UUID NOT NULL,                                           -- New field for tracking who created the record
media_type VARCHAR(10) CHECK (media_type IN ('image', 'video')), 	-- Type of media
media_url VARCHAR(255) NOT NULL,         							-- URL of the media
is_primary BOOLEAN DEFAULT FALSE,        							-- Indicates if this media is the primary image/video
is_active BOOLEAN DEFAULT TRUE,          							-- Status of the product (active or inactive)
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 					-- Timestamp when the product was last updated
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 						-- Timestamp when media was added
);



//----------------------------------------------------------------------------------------------//
-- 5. Product Policies Table
-- Stores warranty and return policy information for products.
-- one product policy/warranty per product
-- Admin: Defines policies for products.
CREATE TABLE warranty_policies (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each warranty policy
product_id INTEGER NOT NULL,                                 -- ID of the product associated with the warranty
organisation_id UUID NOT NULL,                               -- ID of the organisation that owns the product warranty
created_by UUID NOT NULL,                                    -- ID of the user who created the policy
warranty_description TEXT,                                   -- Description of the warranty
warranty_period VARCHAR(50),                                 -- Duration of the warranty
is_active BOOLEAN DEFAULT TRUE,                              -- Status of the warranty (active or inactive)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the warranty was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the warranty was last updated
FOREIGN KEY (product_id) REFERENCES products(id),            -- Relationship to the products table
FOREIGN KEY (organisation_id) REFERENCES organisations(id)   -- Relationship to the organisations table
);

CREATE INDEX idx_warranty_product_id ON warranty_policies(product_id);
CREATE INDEX idx_warranty_organisation_id ON warranty_policies(organisation_id);


CREATE TABLE warranty_policy_trail (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each change record
warranty_policy_id INTEGER NOT NULL,                         -- References the warranty policy being changed
product_id INTEGER NOT NULL,                                 -- ID of the product associated with the policy
organisation_id UUID NOT NULL,                               -- ID of the organisation associated with the policy
changed_by UUID NOT NULL,                                    -- ID of the user who made the change
change_type VARCHAR(50) NOT NULL,                             -- Type of change: 'update', 'delete', etc.
old_warranty_description TEXT,                               -- Previous warranty description before the change
old_warranty_period VARCHAR(50),                             -- Previous warranty period before the change
new_warranty_description TEXT,                               -- Updated warranty description
new_warranty_period VARCHAR(50),                             -- Updated warranty period
changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the change was made
FOREIGN KEY (warranty_policy_id) REFERENCES warranty_policies(id),  -- Relationship to the warranty policies table
FOREIGN KEY (product_id) REFERENCES products(id),            -- Relationship to the products table
FOREIGN KEY (organisation_id) REFERENCES organisations(id)   -- Relationship to the organisations table
);



CREATE TABLE return_policies (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each return policy
product_id INTEGER NOT NULL,                                 -- ID of the product associated with the return policy
organisation_id UUID NOT NULL,                               -- ID of the organisation that owns the return policy
created_by UUID NOT NULL,                                    -- ID of the user who created the policy
return_policy_description TEXT,                              -- Description of the return policy
is_active BOOLEAN DEFAULT TRUE,                              -- Status of the return policy (active or inactive)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the return policy was created
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the return policy was last updated
FOREIGN KEY (product_id) REFERENCES products(id),            -- Relationship to the products table
FOREIGN KEY (organisation_id) REFERENCES organisations(id)   -- Relationship to the organisations table
);

CREATE INDEX idx_return_product_id ON return_policies(product_id);
CREATE INDEX idx_return_organisation_id ON return_policies(organisation_id);


CREATE TABLE return_policy_trail (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each change record
return_policy_id INTEGER NOT NULL,                           -- References the return policy being changed
product_id INTEGER NOT NULL,                                 -- ID of the product associated with the policy
organisation_id UUID NOT NULL,                               -- ID of the organisation associated with the policy
changed_by UUID NOT NULL,                                    -- ID of the user who made the change
change_type VARCHAR(50) NOT NULL,                             -- Type of change: 'update', 'delete', etc.
old_return_policy_description TEXT,                          -- Previous return policy description before the change
new_return_policy_description TEXT,                          -- Updated return policy description
changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the change was made
FOREIGN KEY (return_policy_id) REFERENCES return_policies(id), -- Relationship to the return policies table
FOREIGN KEY (product_id) REFERENCES products(id),            -- Relationship to the products table
FOREIGN KEY (organisation_id) REFERENCES organisations(id)   -- Relationship to the organisations table
);





//----------------------------------------------------------------------------------------------//




[//]: # ()
[//]: # (-- 5. Product Policies Table)

[//]: # (-- Stores warranty and return policy information for products.)

[//]: # (-- one product policy/warranty per product)

[//]: # (-- Admin: Defines policies for products.)

[//]: # (CREATE TABLE product_policies &#40;)

[//]: # (    id SERIAL PRIMARY KEY,                                      -- Unique identifier for each policy entry)

[//]: # (    product_id INTEGER NOT NULL UNIQUE,                         -- ID of the product associated with the policies)

[//]: # (    organisation_id UUID NOT NULL,                              -- ID of the organisation that owns the product policies)

[//]: # (    created_by UUID NOT NULL,                                   -- New field for tracking who created the record)

[//]: # (    warranty_description TEXT,                                  -- Description of the warranty)

[//]: # (    warranty_period VARCHAR&#40;50&#41;,                                -- Period of the warranty)

[//]: # (    return_policy_description TEXT,                             -- Description of the return policy)

[//]: # (    is_active BOOLEAN DEFAULT TRUE,                             -- Status of the product &#40;active or inactive&#41;)

[//]: # (    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Timestamp when the product was last updated)

[//]: # (    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Timestamp when policies were added)

[//]: # (    FOREIGN KEY &#40;product_id&#41; REFERENCES products&#40;id&#41;,           -- Relationship: One-to-One &#40;a product has unique policies&#41;)

[//]: # (    FOREIGN KEY &#40;organisation_id&#41; REFERENCES organisations&#40;id&#41;  -- Relationship to organisations)

[//]: # (&#41;;)

[//]: # (CREATE INDEX idx_product_id ON product_policies&#40;product_id&#41;;)

[//]: # (CREATE INDEX idx_organisation_id ON product_policies&#40;organisation_id&#41;;)

[//]: # ()
[//]: # ()
[//]: # (-- Logs every change made to the product policies for legal tracking.)

[//]: # (-- Keeps a record of the product policy history, who changed it, and when it was changed.)

[//]: # ()
[//]: # (CREATE TABLE policy_trail &#40;)

[//]: # (    id SERIAL PRIMARY KEY,                                       -- Unique identifier for each policy change)

[//]: # (    product_policy_id INTEGER NOT NULL,                          -- References the policy entry being changed)

[//]: # (    product_id INTEGER NOT NULL,                                 -- ID of the product associated with the policy)

[//]: # (    organisation_id UUID NOT NULL,                               -- ID of the organisation associated with the policy)

[//]: # (    updated_by UUID NOT NULL,                                    -- New field for tracking who created the record)

[//]: # (    changed_by UUID NOT NULL,                                    -- ID of the admin or user who made the change)

[//]: # (    change_type VARCHAR&#40;50&#41; NOT NULL,                             -- Type of change: 'update', 'delete')

[//]: # (    old_warranty_description TEXT,                               -- The previous warranty description before the change)

[//]: # (    old_warranty_period VARCHAR&#40;50&#41;,                             -- The previous warranty period before the change)

[//]: # (    old_return_policy_description TEXT,                          -- The previous return policy description before the change)

[//]: # (    new_warranty_description TEXT,                               -- The updated warranty description)

[//]: # (    new_warranty_period VARCHAR&#40;50&#41;,                             -- The updated warranty period)

[//]: # (    new_return_policy_description TEXT,                          -- The updated return policy description)

[//]: # (    action VARCHAR&#40;10&#41; CHECK &#40;action IN &#40;'update', 'delete'&#41;&#41;,    -- Action performed &#40;update or delete&#41;)

[//]: # (    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- Timestamp when the change was made)

[//]: # (    FOREIGN KEY &#40;product_policy_id&#41; REFERENCES product_policies&#40;id&#41;, -- Relationship to the product policies table)

[//]: # (    FOREIGN KEY &#40;product_id&#41; REFERENCES products&#40;id&#41;,            -- Relationship to products)

[//]: # (    FOREIGN KEY &#40;organisation_id&#41; REFERENCES organisations&#40;id&#41;   -- Relationship to organisations)

[//]: # (&#41;;)













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
is_active BOOLEAN DEFAULT TRUE,                                 -- Status of the product (active or inactive)
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                  -- Timestamp when the product was last updated
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
cross_border VARCHAR(30),                                -- Indicates if this is a cross-border/international/local shipping method
is_active BOOLEAN DEFAULT TRUE,                          -- Status of the product (active or inactive)
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Timestamp when the product was last updated
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Timestamp when shipping details were added
FOREIGN KEY (product_id) REFERENCES products(id)         -- Relationship: One-to-Many (a product can have multiple shipping options)
);


-- 6. Product Reviews Table
-- Stores customer reviews for products.
-- Customers: Submit reviews based on their experiences.
CREATE TABLE product_reviews (
id SERIAL PRIMARY KEY,                                      -- Unique identifier for each review entry
product_id INTEGER NOT NULL,                                -- ID of the product being reviewed
organisation_id UUID NOT NULL,                              -- ID of the organisation that owns the product reviews
user_id INTEGER NOT NULL,                              -- ID of the user who wrote the review
rating INTEGER CHECK (rating BETWEEN 1 AND 5),              -- Rating given to the product
review_text TEXT,                                           -- Text of the review
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Timestamp when the review was created
FOREIGN KEY (product_id) REFERENCES products(id),           -- Relationship: One-to-Many (a product can have multiple reviews)
FOREIGN KEY (organisation_id) REFERENCES organisations(id)  -- Relationship to organisations
);


-- 7. Related Products Table
-- Stores relationships between products for cross-selling or upselling.
-- Admin: Manages relationships between products.
CREATE TABLE related_products (aUUU
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





//implement the wishlist
-- 12. Product Wishlists Table
-- Stores user wishlists for products they want to purchase later.
-- Customers: Create wishlists for future purchases.
CREATE TABLE product_wishlists (
id SERIAL PRIMARY KEY,                   -- Unique identifier for each wishlist entry
user_id INTEGER NOT NULL,                -- ID of the user who created the wishlist
product_id INTEGER NOT NULL,             -- ID of the product added to the wishlist
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the product was added to the wishlist
FOREIGN KEY (user_id) REFERENCES users(id), -- Relationship to users
FOREIGN KEY (product_id) REFERENCES products(id) -- Relationship to products
);