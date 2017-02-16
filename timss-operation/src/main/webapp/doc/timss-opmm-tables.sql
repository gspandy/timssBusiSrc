alter table "timss_op_rules_detail"
   drop constraint FK_OP_RULES_DETAIL_REFERENCE_OP_DUTY;

alter table "timss_op_rules_detail"
   drop constraint FK_OP_RULES_DETAIL_REFERENCE_OP_CLASS;

alter table "timss_op_rules_detail"
   drop constraint FK_OP_RULES_DETAIL_REFERENCE_OP_RULES;

alter table "timss_op_rules_op_classes"
   drop constraint FK_OP_RULES_OP_CLASSES_REFERENCE_OP_RULES;

alter table "timss_op_rules_op_classes"
   drop constraint FK_OP_RULES_OP_CLASSES_REFERENCE_OP_CLASS;

alter table "timss_op_rules_op_duty"
   drop constraint FK_OP_RULES_OP_DUTY_REFERENCE_OP_DUTY;

alter table "timss_op_rules_op_duty"
   drop constraint FK_OP_RULES_OP_DUTY_REFERENCE_OP_RULES;

alter table "timss_op_schedule"
   drop constraint FK_OP_SCHED_REFERENCE_OP_RULES_DETAIL;

drop table "timss_op_classes" cascade constraints;

drop table "timss_op_duty" cascade constraints;

drop table "timss_op_rules" cascade constraints;

drop table "timss_op_rules_detail" cascade constraints;

drop table "timss_op_rules_op_classes" cascade constraints;

drop table "timss_op_rules_op_duty" cascade constraints;

drop table "timss_op_schedule" cascade constraints;

/*==============================================================*/
/* Table: "timss_op_classes"                                    */
/*==============================================================*/
create table "timss_op_classes"  (
   "id"                 NUMBER                          not null,
   "num"                VARCHAR(255),
   "name"               VARCHAR(255),
   "start_time"         DATE,
   "end_time"           DATE,
   "sort"               INTEGER,
   "abb_name"           VARCHAR(255),
   constraint PK_TIMSS_OP_CLASSES primary key ("id")
);

comment on table "timss_op_classes" is
'班次表';

comment on column "timss_op_classes"."num" is
'班次编码';

comment on column "timss_op_classes"."name" is
'班次名称';

comment on column "timss_op_classes"."start_time" is
'开始时间';

comment on column "timss_op_classes"."end_time" is
'结束时间';

comment on column "timss_op_classes"."sort" is
'排序';

comment on column "timss_op_classes"."abb_name" is
'别名';

/*==============================================================*/
/* Table: "timss_op_duty"                                       */
/*==============================================================*/
create table "timss_op_duty"  (
   "id"                 INTEGER                         not null,
   "num"                VARCHAR2(255 BYTE),
   "name"               VARCHAR(255),
   "sort"               INTEGER,
   "dept_id"            INTEGER,
   constraint PK_TIMSS_OP_DUTY primary key ("id")
);

comment on table "timss_op_duty" is
'值别表';

comment on column "timss_op_duty"."num" is
'编码';

comment on column "timss_op_duty"."name" is
'名称';

comment on column "timss_op_duty"."sort" is
'排序';

comment on column "timss_op_duty"."dept_id" is
'部门';

/*==============================================================*/
/* Table: "timss_op_rules"                                      */
/*==============================================================*/
create table "timss_op_rules"  (
   "id"                 INTEGER                         not null,
   "num"                UROWID(255),
   "name"               UROWID(255),
   "period"             INTEGER,
   constraint PK_TIMSS_OP_RULES primary key ("id")
);

comment on table "timss_op_rules" is
'排班规则表';

comment on column "timss_op_rules"."num" is
'编码';

comment on column "timss_op_rules"."name" is
'名称';

comment on column "timss_op_rules"."period" is
'周期';

/*==============================================================*/
/* Table: "timss_op_rules_detail"                               */
/*==============================================================*/
create table "timss_op_rules_detail"  (
   "id"                 INTEGER                         not null,
   "duty_id"            INTEGER,
   "day_time"           INTEGER,
   "classes_id"         INTEGER,
   "rules_id"           INTEGER,
   constraint PK_TIMSS_OP_RULES_DETAIL primary key ("id")
);

comment on table "timss_op_rules_detail" is
'规则详细表';

comment on column "timss_op_rules_detail"."duty_id" is
'值别';

comment on column "timss_op_rules_detail"."day_time" is
'天次';

comment on column "timss_op_rules_detail"."classes_id" is
'班次';

comment on column "timss_op_rules_detail"."rules_id" is
'排班规则';

/*==============================================================*/
/* Table: "timss_op_rules_op_classes"                           */
/*==============================================================*/
create table "timss_op_rules_op_classes"  (
   "id"                 INTEGER                         not null,
   "rules_id"           INTEGER,
   "classes_id"         INTEGER,
   constraint PK_TIMSS_OP_RULES_OP_CLASSES primary key ("id")
);

comment on table "timss_op_rules_op_classes" is
'排班规则_班次中间表';

comment on column "timss_op_rules_op_classes"."rules_id" is
'排班规则';

comment on column "timss_op_rules_op_classes"."classes_id" is
'班次';

/*==============================================================*/
/* Table: "timss_op_rules_op_duty"                              */
/*==============================================================*/
create table "timss_op_rules_op_duty"  (
   "id"                 INTEGER                         not null,
   "rules_id"           INTEGER,
   "duty_id"            INTEGER,
   constraint PK_TIMSS_OP_RULES_OP_DUTY primary key ("id")
);

comment on table "timss_op_rules_op_duty" is
'排班规则_值别中间表';

comment on column "timss_op_rules_op_duty"."rules_id" is
'排班规则';

comment on column "timss_op_rules_op_duty"."duty_id" is
'值别';

/*==============================================================*/
/* Table: "timss_op_schedule"                                   */
/*==============================================================*/
create table "timss_op_schedule"  (
   "id"                 INTEGER                         not null,
   "date_time"          DATE,
   "rules_detail_id"    INTEGER,
   constraint PK_TIMSS_OP_SCHEDULE primary key ("id")
);

comment on table "timss_op_schedule" is
'排班详情表';

comment on column "timss_op_schedule"."date_time" is
'时间';

comment on column "timss_op_schedule"."rules_detail_id" is
'规则详细';

alter table "timss_op_rules_detail"
   add constraint FK_OP_RULES_DETAIL_REFERENCE_OP_DUTY foreign key ("duty_id")
      references "timss_op_duty" ("id");

alter table "timss_op_rules_detail"
   add constraint FK_OP_RULES_DETAIL_REFERENCE_OP_CLASS foreign key ("classes_id")
      references "timss_op_classes" ("id");

alter table "timss_op_rules_detail"
   add constraint FK_OP_RULES_DETAIL_REFERENCE_OP_RULES foreign key ("rules_id")
      references "timss_op_rules" ("id");

alter table "timss_op_rules_op_classes"
   add constraint FK_OP_RULES_OP_CLASSES_REFERENCE_OP_RULES foreign key ("rules_id")
      references "timss_op_rules" ("id");

alter table "timss_op_rules_op_classes"
   add constraint FK_OP_RULES_OP_CLASSES_REFERENCE_OP_CLASS foreign key ("classes_id")
      references "timss_op_classes" ("id");

alter table "timss_op_rules_op_duty"
   add constraint FK_OP_RULES_OP_DUTY_REFERENCE_OP_DUTY foreign key ("duty_id")
      references "timss_op_duty" ("id");

alter table "timss_op_rules_op_duty"
   add constraint FK_OP_RULES_OP_DUTY_REFERENCE_OP_RULES foreign key ("rules_id")
      references "timss_op_rules" ("id");

alter table "timss_op_schedule"
   add constraint FK_OP_SCHED_REFERENCE_OP_RULES_DETAIL foreign key ("rules_detail_id")
      references "timss_op_rules_detail" ("id");
