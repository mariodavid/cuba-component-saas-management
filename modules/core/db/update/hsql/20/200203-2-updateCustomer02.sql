-- alter table CCSM_CUSTOMER add column TENANT_ID varchar(36) ^
-- update CCSM_CUSTOMER set TENANT_ID = <default_value> ;
-- alter table CCSM_CUSTOMER alter column TENANT_ID set not null ;
alter table CCSM_CUSTOMER add column TENANT_ID varchar(36) not null ;
