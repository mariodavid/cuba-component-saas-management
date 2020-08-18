-- begin CCSM_PRODUCT
create table CCSM_PRODUCT (
    ID uuid,
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
    ID uuid,
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
    TENANT_ID uuid not null,
    --
    primary key (ID)
)^
-- end CCSM_CUSTOMER
-- begin CCSM_SUBSCRIPTION
create table CCSM_SUBSCRIPTION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    EXTERNAL_ID varchar(255),
    STATUS varchar(50) not null,
    CUSTOMER_ID uuid not null,
    PLAN_ID uuid not null,
    --
    primary key (ID)
)^
-- end CCSM_SUBSCRIPTION
-- begin CCSM_LIMIT
create table CCSM_LIMIT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    CODE varchar(255) not null,
    --
    primary key (ID)
)^
-- end CCSM_LIMIT
-- begin CCSM_PLAN
create table CCSM_PLAN (
    ID uuid,
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
    PRODUCT_ID uuid not null,
    --
    primary key (ID)
)^
-- end CCSM_PLAN
-- begin CCSM_PLAN_LIMIT
create table CCSM_PLAN_LIMIT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PLAN_ID uuid not null,
    LIMIT_ID uuid not null,
    VALUE_ integer not null,
    --
    primary key (ID)
)^
-- end CCSM_PLAN_LIMIT
-- begin CCSM_RECEIVED_EVENT
create table CCSM_RECEIVED_EVENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TYPE_ varchar(255) not null,
    CONTENT text not null,
    EVENT_ID varchar(255) not null,
    SOURCE varchar(255),
    API_VERSION varchar(255),
    COMMENT_ varchar(4000),
    ACKNOWLEDGED boolean,
    --
    primary key (ID)
)^
-- end CCSM_RECEIVED_EVENT
-- begin CCSM_WAITINGLIST
create table CCSM_WAITINGLIST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    ANIMAL_ID uuid not null,
    POSITION_ integer,
    CONTACT_ID uuid not null,
    --
    primary key (ID)
)^
-- end CCSM_WAITINGLIST
-- begin CCSM_CONTACT
create table CCSM_CONTACT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end CCSM_CONTACT
-- begin CCSM_ANIMAL
create table CCSM_ANIMAL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    NAME varchar(255),
    IMAGE_ID uuid,
    --
    primary key (ID)
)^
-- end CCSM_ANIMAL
-- begin CCSM_PLAN_ROLE_LINK
create table CCSM_PLAN_ROLE_LINK (
    PLAN_ID uuid,
    ROLE_ID uuid,
    primary key (PLAN_ID, ROLE_ID)
)^
-- end CCSM_PLAN_ROLE_LINK
