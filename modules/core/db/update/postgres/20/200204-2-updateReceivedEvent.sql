alter table CCSM_RECEIVED_EVENT rename column type_ to type___u36037 ;
alter table CCSM_RECEIVED_EVENT alter column type___u36037 drop not null ;
alter table CCSM_RECEIVED_EVENT add column TYPE_ varchar(255) ^
update CCSM_RECEIVED_EVENT set TYPE_ = '' where TYPE_ is null ;
alter table CCSM_RECEIVED_EVENT alter column TYPE_ set not null ;
alter table CCSM_RECEIVED_EVENT alter column CONTENT set data type text ;
