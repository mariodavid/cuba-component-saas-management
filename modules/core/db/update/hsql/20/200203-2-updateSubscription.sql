-- alter table CCSM_SUBSCRIPTION add column CUSTOMER_ID varchar(36) ^
-- update CCSM_SUBSCRIPTION set CUSTOMER_ID = <default_value> ;
-- alter table CCSM_SUBSCRIPTION alter column CUSTOMER_ID set not null ;
alter table CCSM_SUBSCRIPTION add column CUSTOMER_ID varchar(36) not null ;
-- alter table CCSM_SUBSCRIPTION add column PLAN_ID varchar(36) ^
-- update CCSM_SUBSCRIPTION set PLAN_ID = <default_value> ;
-- alter table CCSM_SUBSCRIPTION alter column PLAN_ID set not null ;
alter table CCSM_SUBSCRIPTION add column PLAN_ID varchar(36) not null ;
