alter table CCSM_ANIMAL add constraint FK_CCSM_ANIMAL_ON_IMAGE foreign key (IMAGE_ID) references SYS_FILE(ID);
create index IDX_CCSM_ANIMAL_ON_IMAGE on CCSM_ANIMAL (IMAGE_ID);
