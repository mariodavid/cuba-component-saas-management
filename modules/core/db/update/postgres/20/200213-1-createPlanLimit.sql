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
);