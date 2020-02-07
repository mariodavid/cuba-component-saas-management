create table CCSM_WAITINGLIST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ANIMAL_ID uuid not null,
    POSITION_ integer,
    CONTACT_ID uuid not null,
    --
    primary key (ID)
);