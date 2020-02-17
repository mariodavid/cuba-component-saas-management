alter table CCSM_PLAN_LIMIT add constraint FK_CCSM_PLAN_LIMIT_ON_PLAN foreign key (PLAN_ID) references CCSM_PLAN(ID);
alter table CCSM_PLAN_LIMIT add constraint FK_CCSM_PLAN_LIMIT_ON_LIMIT foreign key (LIMIT_ID) references CCSM_LIMIT(ID);
create index IDX_CCSM_PLAN_LIMIT_ON_PLAN on CCSM_PLAN_LIMIT (PLAN_ID);
create index IDX_CCSM_PLAN_LIMIT_ON_LIMIT on CCSM_PLAN_LIMIT (LIMIT_ID);
