CREATE TABLE template
(
    id           BIGINT       NOT NULL,
    exchange     VARCHAR(255) NOT NULL,
    routing_key  VARCHAR(255) NOT NULL,
    version      VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    template     BLOB         NOT NULL,
    CONSTRAINT pk_template PRIMARY KEY (id)
);