-- begin CCSM_PRODUCT
create table CCSM_PRODUCT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end CCSM_PRODUCT
-- begin CCSM_CUSTOMER
create table CCSM_CUSTOMER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    FIRST_NAME varchar(255),
    EXTERNAL_ID varchar(255) not null,
    --
    primary key (ID)
)^
-- end CCSM_CUSTOMER
-- begin CCSM_SUBSCRIPTION
create table CCSM_SUBSCRIPTION (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    STATUS varchar(50) not null,
    CUSTOMER_ID varchar(36) not null,
    PLAN_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end CCSM_SUBSCRIPTION
-- begin CCSM_PLAN
create table CCSM_PLAN (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    EXTERNAL_ID varchar(255) not null,
    NAME varchar(255) not null,
    PRODUCT_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end CCSM_PLAN
