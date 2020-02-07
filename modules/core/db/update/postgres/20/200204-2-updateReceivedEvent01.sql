alter table CCSM_RECEIVED_EVENT add column API_VERSION varchar(255) ;
alter table CCSM_RECEIVED_EVENT add column EVENT_ID varchar(255) ^
update CCSM_RECEIVED_EVENT set EVENT_ID = '' where EVENT_ID is null ;
alter table CCSM_RECEIVED_EVENT alter column EVENT_ID set not null ;
alter table CCSM_RECEIVED_EVENT add column WEBHOOK_STATUS varchar(255) ;
alter table CCSM_RECEIVED_EVENT add column SOURCE varchar(255) ;
