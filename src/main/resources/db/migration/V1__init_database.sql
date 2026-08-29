CREATE SEQUENCE branch_seq       START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE category_seq     START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE customer_seq     START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE inventory_seq    START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE order_item_seq   START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE orders_seq       START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE product_seq      START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE refund_seq       START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE shift_report_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE store_seq        START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE users_seq        START WITH 1 INCREMENT BY 50;



CREATE TABLE store (
    id             BIGINT       NOT NULL,
    status         SMALLINT     CHECK (status BETWEEN 0 AND 2),
    store_admin_id BIGINT       UNIQUE,
    brand          VARCHAR(255) NOT NULL,
    description    VARCHAR(255),
    address        VARCHAR(255),
    email          VARCHAR(255),
    phone          VARCHAR(255),
    store_type     VARCHAR(255),
    created_at     TIMESTAMP(6),
    updated_at     TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE users (
    id          BIGINT       NOT NULL,
    role        SMALLINT     NOT NULL CHECK (role BETWEEN 0 AND 4),
    branch_id   BIGINT,
    store_id    BIGINT,
    email       VARCHAR(255) NOT NULL UNIQUE,
    full_name   VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(255),
    last_login  TIMESTAMP(6),
    created_at  TIMESTAMP(6),
    updated_at  TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE branch (
    id         BIGINT       NOT NULL,
    store_id   BIGINT,
    manager_id BIGINT       UNIQUE,
    name       VARCHAR(255),
    address    VARCHAR(255),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    open_time  TIME(0),
    close_time TIME(0),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE branch_working_days (
    branch_id    BIGINT NOT NULL,
    working_days VARCHAR(255)
);

CREATE TABLE category (
    id       BIGINT NOT NULL,
    store_id BIGINT,
    name     VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE customer (
    id         BIGINT       NOT NULL,
    email      VARCHAR(255),
    full_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE product (
    id            BIGINT           NOT NULL,
    category_id   BIGINT,
    store_id      BIGINT,
    name          VARCHAR(255)     NOT NULL,
    sku           VARCHAR(255)     NOT NULL UNIQUE,
    brand         VARCHAR(255),
    description   VARCHAR(255),
    image         VARCHAR(255),
    mrp           DOUBLE PRECISION,
    selling_price DOUBLE PRECISION,
    created_at    TIMESTAMP(6),
    updated_at    TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE inventory (
    id          BIGINT  NOT NULL,
    branch_id   BIGINT,
    product_id  BIGINT,
    quantity    INTEGER NOT NULL,
    last_update TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE orders (
    id           BIGINT           NOT NULL,
    branch_id    BIGINT,
    cashier_id   BIGINT,
    customer_id  BIGINT,
    payment_type SMALLINT         CHECK (payment_type BETWEEN 0 AND 2),
    total_amount DOUBLE PRECISION,
    created_at   TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE order_item (
    id         BIGINT           NOT NULL,
    order_id   BIGINT,
    product_id BIGINT,
    price      DOUBLE PRECISION,
    quantity   INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE orders_items (
    order_id BIGINT NOT NULL,
    items_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE shift_report (
    id            BIGINT           NOT NULL,
    branch_id     BIGINT,
    cashier_id    BIGINT,
    total_orders  INTEGER          NOT NULL,
    total_sales   DOUBLE PRECISION,
    total_refunds DOUBLE PRECISION,
    net_sale      DOUBLE PRECISION,
    shift_start   TIMESTAMP(6),
    shift_end     TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE shift_report_recent_orders (
    shift_report_id  BIGINT NOT NULL,
    recent_orders_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE shift_report_top_selling_products (
    shift_report_id         BIGINT NOT NULL,
    top_selling_products_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE refund (
    id              BIGINT           NOT NULL,
    branch_id       BIGINT,
    cashier_id      BIGINT,
    order_id        BIGINT,
    shift_report_id BIGINT,
    amount          DOUBLE PRECISION,
    payment_type    SMALLINT         CHECK (payment_type BETWEEN 0 AND 2),
    reason          VARCHAR(255),
    created_at      TIMESTAMP(6),
    PRIMARY KEY (id)
);



ALTER TABLE branch
    ADD CONSTRAINT fk_branch_manager FOREIGN KEY (manager_id) REFERENCES users (id);
ALTER TABLE branch
    ADD CONSTRAINT fk_branch_store FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE branch_working_days
    ADD CONSTRAINT fk_branch_working_days_branch FOREIGN KEY (branch_id) REFERENCES branch (id);

ALTER TABLE category
    ADD CONSTRAINT fk_category_store FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_branch FOREIGN KEY (branch_id) REFERENCES branch (id);
ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_item
    ADD CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE order_item
    ADD CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_branch FOREIGN KEY (branch_id) REFERENCES branch (id);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_cashier FOREIGN KEY (cashier_id) REFERENCES users (id);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE orders_items
    ADD CONSTRAINT fk_orders_items_item FOREIGN KEY (items_id) REFERENCES order_item (id);
ALTER TABLE orders_items
    ADD CONSTRAINT fk_orders_items_order FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE product
    ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category (id);
ALTER TABLE product
    ADD CONSTRAINT fk_product_store FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE refund
    ADD CONSTRAINT fk_refund_branch FOREIGN KEY (branch_id) REFERENCES branch (id);
ALTER TABLE refund
    ADD CONSTRAINT fk_refund_cashier FOREIGN KEY (cashier_id) REFERENCES users (id);
ALTER TABLE refund
    ADD CONSTRAINT fk_refund_order FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE refund
    ADD CONSTRAINT fk_refund_shift_report FOREIGN KEY (shift_report_id) REFERENCES shift_report (id);

ALTER TABLE shift_report
    ADD CONSTRAINT fk_shift_report_branch FOREIGN KEY (branch_id) REFERENCES branch (id);
ALTER TABLE shift_report
    ADD CONSTRAINT fk_shift_report_cashier FOREIGN KEY (cashier_id) REFERENCES users (id);

ALTER TABLE shift_report_recent_orders
    ADD CONSTRAINT fk_sr_recent_orders_order FOREIGN KEY (recent_orders_id) REFERENCES orders (id);
ALTER TABLE shift_report_recent_orders
    ADD CONSTRAINT fk_sr_recent_orders_report FOREIGN KEY (shift_report_id) REFERENCES shift_report (id);

ALTER TABLE shift_report_top_selling_products
    ADD CONSTRAINT fk_sr_top_products_product FOREIGN KEY (top_selling_products_id) REFERENCES product (id);
ALTER TABLE shift_report_top_selling_products
    ADD CONSTRAINT fk_sr_top_products_report FOREIGN KEY (shift_report_id) REFERENCES shift_report (id);

ALTER TABLE store
    ADD CONSTRAINT fk_store_admin FOREIGN KEY (store_admin_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT fk_users_branch FOREIGN KEY (branch_id) REFERENCES branch (id);
ALTER TABLE users
    ADD CONSTRAINT fk_users_store FOREIGN KEY (store_id) REFERENCES store (id);



CREATE INDEX idx_branch_store_id ON branch (store_id);
CREATE INDEX idx_branch_working_days_branch_id ON branch_working_days (branch_id);
CREATE INDEX idx_category_store_id ON category (store_id);
CREATE INDEX idx_inventory_branch_id ON inventory (branch_id);
CREATE INDEX idx_inventory_product_id ON inventory (product_id);
CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_order_item_product_id ON order_item (product_id);
CREATE INDEX idx_orders_branch_id ON orders (branch_id);
CREATE INDEX idx_orders_cashier_id ON orders (cashier_id);
CREATE INDEX idx_orders_customer_id ON orders (customer_id);
CREATE INDEX idx_orders_items_order_id ON orders_items (order_id);
CREATE INDEX idx_product_category_id ON product (category_id);
CREATE INDEX idx_product_store_id ON product (store_id);
CREATE INDEX idx_refund_branch_id ON refund (branch_id);
CREATE INDEX idx_refund_cashier_id ON refund (cashier_id);
CREATE INDEX idx_refund_order_id ON refund (order_id);
CREATE INDEX idx_refund_shift_report_id ON refund (shift_report_id);
CREATE INDEX idx_shift_report_branch_id ON shift_report (branch_id);
CREATE INDEX idx_shift_report_cashier_id ON shift_report (cashier_id);
CREATE INDEX idx_sr_recent_orders_report_id ON shift_report_recent_orders (shift_report_id);
CREATE INDEX idx_sr_top_products_report_id ON shift_report_top_selling_products (shift_report_id);
CREATE INDEX idx_users_branch_id ON users (branch_id);
CREATE INDEX idx_users_store_id ON users (store_id);