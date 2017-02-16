--2014-8-22 增加工作票更多办理时的序列
-- Create sequence 
create sequence SEQ_PTW_MORE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Drop columns 
alter table PTW_EXT drop column extappltime;

--2014-8-23 修改命名错误
-- Add/modify columns 
alter table PTW_REMARKTASK rename column reamrksigntime to REMARKSIGNTIME;

--2014-8-26
-- Add/modify columns 
alter table PTW_PROCESS modify processid varchar2(64);