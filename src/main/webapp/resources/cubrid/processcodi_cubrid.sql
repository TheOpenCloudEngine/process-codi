CREATE TABLE "app"(
"appid" integer AUTO_INCREMENT(1,1) NOT NULL,
"appname" character varying(100),
"simpleoverview" character varying(1000),
"fulloverview" character varying(5000),
"pricing" character varying(200),
"createdate" datetime,
"updatedate" datetime,
"version" character varying(5),
"extfilename" character varying(200),
"filecontent" character varying(1000),
"logofilename" character varying(200),
"logocontent" character varying(1000),
"status" character varying(100),
"comcode" character varying(20),
"categoryid" integer DEFAULT 0,
"installcnt" integer DEFAULT 0,
"isdeleted" integer DEFAULT 0,
"url" character varying(200),
"projectid" character varying(20),
"comname" character varying(20)
);

ALTER SERIAL "app_ai_appid"  START WITH 2;
CREATE TABLE "appkey_attributes"(
"appkey" character varying(108),
"attributekey" character varying(90),
"attributevalue" character varying(300)
);

CREATE TABLE "appkeys"(
"appkey" character varying(108) NOT NULL,
"creationtime" datetime,
"lastaccessedtime" datetime
);

CREATE TABLE "appmapping"(
"appid" integer AUTO_INCREMENT(1,1) NOT NULL,
"comcode" character varying(20) NOT NULL,
"appname" character varying(100),
"isdeleted" integer DEFAULT 0
);

ALTER SERIAL "appmapping_ai_appid"  START WITH 1;
CREATE TABLE "bizrate"(
"empcode" character varying(60) DEFAULT '' NOT NULL,
"yearmm" character varying(18) DEFAULT '' NOT NULL,
"bizday" integer,
"workedday" integer,
"latedday" integer
);

CREATE TABLE "bpm_acltable"(
"acltableid" integer DEFAULT 0 NOT NULL,
"defid" integer,
"comcode" character varying(60),
"partcode" character varying(60),
"empcode" character varying(60),
"rolecode" character varying(60),
"defaultuser" character varying(3),
"permission" character varying(3)
);

CREATE TABLE "bpm_deadln_dim"(
"deadln_id" integer NOT NULL,
"deadln_desc" character varying(765)
);

CREATE TABLE "bpm_knol"(
"id" character varying(60) NOT NULL,
"name" character varying(3000),
"linkedinstid" integer,
"parentid" character varying(60) NOT NULL,
"no" integer,
"authorid" character varying(300),
"type" character varying(30),
"vistype" character varying(30),
"conntype" character varying(60),
"url" character varying(600),
"thumbnail" character varying(600),
"secuopt" character varying(3) DEFAULT '0  ',
"companyid" character varying(60),
"refid" character varying(60),
"budget" integer,
"effort" integer,
"benefit" integer,
"penalty" integer,
"startdate" date,
"enddate" date,
"description" character varying(1000)
);

CREATE TABLE "bpm_noti"(
"notiid" character varying(48831645),
"userid" character varying(300),
"actorid" character varying(300),
"instid" integer,
"actabstract" character varying(900),
"taskid" integer,
"type" integer,
"inputdate" timestamp DEFAULT TIMESTAMP'2013-03-19 14:22:45' NOT NULL,
"confirm" integer
);

CREATE TABLE "bpm_prfm_fact_2006"(
"inst_id" integer,
"rootinst_id" integer,
"rsrc_id" character varying(150),
"time_id" integer,
"act_id" character varying(300),
"act_name" character varying(300),
"def_id" integer,
"def_name" character varying(600),
"prsngtime" integer,
"trnrndtime" integer,
"idletime" integer,
"chovrtime" integer,
"sspndtime" integer,
"cost" integer,
"sla_stftry" integer,
"modtime" date,
"deadlnht" integer
);

CREATE TABLE "bpm_procdef"(
"defid" integer AUTO_INCREMENT(1,1) NOT NULL,
"description" character varying(765),
"parentfolder" integer,
"isfolder" integer DEFAULT 0,
"isadhoc" integer DEFAULT 0,
"objtype" character varying(30),
"prodver" integer DEFAULT 0,
"prodverid" integer,
"name" character varying(765),
"isdeleted" integer DEFAULT 0,
"moddate" datetime,
"alias" character varying(300),
"comcode" character varying(60),
"isvisible" integer,
"author" character varying(300)
);

ALTER SERIAL "bpm_procdef_ai_defid"  START WITH 1;
CREATE TABLE "bpm_procdefver"(
"defverid" integer DEFAULT 0 NOT NULL,
"filepath" character varying(765),
"ver" integer,
"defid" integer,
"defname" character varying(765),
"moddate" datetime,
"prodstartdate" datetime,
"isdeleted" integer DEFAULT 0
);

CREATE TABLE "bpm_procinst"(
"instid" integer AUTO_INCREMENT(1,1) NOT NULL,
"defverid" character varying(300),
"defid" character varying(300),
"defname" character varying(765),
"defpath" character varying(765),
"defmoddate" datetime,
"starteddate" datetime,
"finisheddate" datetime,
"duedate" datetime,
"status" character varying(60),
"info" character varying(765),
"name" character varying(765),
"isdeleted" integer DEFAULT 0,
"isadhoc" integer DEFAULT 0,
"isarchive" integer DEFAULT 0,
"issubprocess" integer DEFAULT 0,
"iseventhandler" integer DEFAULT 0,
"rootinstid" integer,
"maininstid" integer,
"maindefverid" integer,
"mainacttrctag" character varying(60),
"mainexecscope" character varying(60),
"abstrcpath" character varying(600),
"dontreturn" integer,
"moddate" datetime,
"ext1" character varying(300),
"initep" character varying(300),
"initrsnm" character varying(300),
"currep" character varying(300),
"currrsnm" character varying(300),
"strategyid" integer,
"prevcurrep" character varying(300),
"prevcurrrsnm" character varying(300),
"starcnt" integer,
"starrsnm" character varying(300),
"starflag" integer,
"abstractinst" character varying(196605),
"currtrctag" integer,
"prevtrctag" integer,
"initcomcd" character varying(60),
"secuopt" character varying(3) DEFAULT '0  ',
"lastcmnt" character varying(600),
"initcmpl" integer,
"topicid" character varying(60),
"progress" character varying(30),
"lastcmnt2" character varying(600),
"bvbenefit" integer,
"bvpenalty" integer,
"effort" integer,
"lastcmntep" character varying(300),
"lastcmntrsnm" character varying(300),
"lastcmnt2ep" character varying(300),
"lastcmnt2rsnm" character varying(300)
);

ALTER SERIAL "bpm_procinst_ai_instid"  START WITH 1;
CREATE TABLE "bpm_procvar"(
"varid" integer AUTO_INCREMENT(1,1) NOT NULL,
"instid" integer,
"datatype" integer,
"valuestring" character varying(12000),
"valuelong" integer,
"valueboolean" integer,
"valuedate" datetime,
"varindex" integer,
"trctag" character varying(60),
"isproperty" integer,
"moddate" datetime,
"keyname" character varying(300),
"keystring" character varying(600),
"filecontent" character varying(196605),
"htmlfilepath" character varying(765)
);

ALTER SERIAL "bpm_procvar_ai_varid"  START WITH 2092;
CREATE TABLE "bpm_roledef"(
"roledefid" character varying(300),
"defid" character varying(150),
"rolename" character varying(60),
"mappeduserid" character varying(150),
"comcode" character varying(150)
);

CREATE TABLE "bpm_rolemapping"(
"rolemappingid" integer AUTO_INCREMENT(1,1) NOT NULL,
"instid" integer,
"rootinstid" integer,
"rolename" character varying(765),
"endpoint" character varying(765),
"value" character varying(12000),
"resname" character varying(600),
"assigntype" integer,
"assignparam1" character varying(300),
"dispatchoption" integer,
"dispatchparam1" character varying(300),
"groupid" character varying(90),
"isreferencer" integer DEFAULT 0
);

ALTER SERIAL "bpm_rolemapping_ai_rolemappingid"  START WITH 66;
CREATE TABLE "bpm_seq"(
"seq" integer,
"tbname" character varying(765),
"description" character varying(765),
"moddate" datetime
);

CREATE TABLE "bpm_strinst"(
"instid" integer,
"strtgid" integer,
"isdeleted" character varying(3) DEFAULT '0'
);

CREATE TABLE "bpm_strtg"(
"strtgid" integer DEFAULT 0 NOT NULL,
"strtgnm" character varying(300),
"type" character varying(90),
"isdeleted" character varying(3) DEFAULT '0',
"status" character varying(30),
"instcnt" integer,
"cmpltinstcnt" integer,
"description" character varying(765),
"comcode" character varying(60),
"startdate" date,
"enddate" date,
"partcode" character varying(60),
"partname" character varying(300)
);

CREATE TABLE "bpm_strtg_parentmapping"(
"strtgid" integer,
"parentid" integer
);

CREATE TABLE "bpm_tag"(
"tagid" integer NOT NULL,
"name" character varying(765) NOT NULL,
"comcode" character varying(60) NOT NULL
);

CREATE TABLE "bpm_topicmapping"(
"topicmappingid" integer NOT NULL,
"topicid" character varying(20) NOT NULL,
"userid" character varying(255),
"username" character varying(1000),
"assigntype" integer DEFAULT 0
);

CREATE TABLE "bpm_worklist"(
"taskid" integer AUTO_INCREMENT(1,1) NOT NULL,
"title" character varying(3000),
"description" character varying(500),
"endpoint" character varying(200),
"status" character varying(100) DEFAULT '',
"priority" integer,
"startdate" datetime,
"enddate" datetime,
"duedate" datetime,
"instid" integer,
"defid" character varying(100),
"defname" character varying(200),
"trctag" character varying(100),
"tool" character varying(200),
"parameter" character varying(4000),
"groupid" integer,
"groupname" integer,
"ext1" character varying(200),
"ext2" character varying(200),
"ext3" character varying(200),
"isurgent" integer,
"hasattachfile" integer,
"hascomment" integer,
"documentcategory" character varying(200),
"isdeleted" integer DEFAULT 0,
"rootinstid" integer,
"dispatchoption" integer,
"dispatchparam1" character varying(100),
"rolename" character varying(100),
"resname" character varying(100),
"refrolename" character varying(100),
"execscope" character varying(5),
"savedate" datetime,
"abstract" character varying(1073741823),
"type" character varying(10),
"content" character varying(3000),
"extfile" character varying(200),
"prevver" integer,
"nextver" integer,
"ext5" character varying(200),
"ext6" character varying(200),
"ext7" character varying(200),
"ext8" character varying(200),
"ext9" character varying(200),
"ext10" character varying(200),
"prttskid" integer,
"grptaskid" integer,
"majorver" integer,
"minorver" integer,
"ext4" character varying(200)
);

ALTER SERIAL "bpm_worklist_ai_taskid"  START WITH 1;
CREATE TABLE "category"(
"categoryid" integer NOT NULL,
"categoryname" character varying(255),
"parentcategoryid" integer,
"moddate" datetime,
"deleted" integer DEFAULT 0
);

CREATE TABLE "comtable"(
"comcode" character varying(20) DEFAULT '' NOT NULL,
"comname" character varying(30),
"description" character varying(255),
"isdeleted" character varying(1) DEFAULT '0',
"repmail" character varying(100),
"repmlhst" character varying(100),
"repmlpwd" character varying(100)
);

CREATE TABLE "contact"(
"userid" character varying(100),
"friendid" character varying(100),
"friendname" character varying(20),
"network" character varying(10),
"mood" character varying(100)
);

CREATE TABLE "emptable"(
"empcode" character varying(100) DEFAULT '' NOT NULL,
"empname" character varying(100),
"password" character varying(20),
"isadmin" integer,
"jikname" character varying(100),
"email" character varying(100),
"partcode" character varying(20),
"globalcom" character varying(20),
"isdeleted" character varying(1) DEFAULT '0',
"locale" character varying(10),
"nateon" character varying(50),
"msn" character varying(50),
"mobilecmp" character varying(50),
"mobileno" character varying(50),
"facebookid" character varying(100),
"facebookpassword" character varying(100),
"preferux" character varying(10),
"prefermob" character varying(10),
"mood" character varying(100),
"appkey" character varying(100),
"approved" integer
);

CREATE TABLE "oauth_token"(
"user_id" character varying(300) NOT NULL,
"access_token" character varying(300)
);

CREATE TABLE "parttable"(
"globalcom" character varying(20),
"partcode" character varying(20) NOT NULL,
"partname" character varying(30),
"parent_partcode" character varying(20),
"isdeleted" character varying(1) DEFAULT '0',
"description" character varying(255),
"comcode" character varying(255)
);

CREATE TABLE "processmap"(
"mapid" character varying(100) DEFAULT '' NOT NULL,
"defid" character varying(50) DEFAULT '' NOT NULL,
"name" character varying(50),
"iconpath" character varying(255),
"color" character varying(10),
"comcode" character varying(20),
"no" integer,
"cmphrase" character varying(200),
"cmtrgr" character varying(20)
);

CREATE TABLE "roletable"(
"rolecode" character varying(20) NOT NULL,
"comcode" character varying(20),
"descr" character varying(255),
"isdeleted" character varying(1) DEFAULT '0'
);

CREATE TABLE "roleusertable"(
"rolecode" character varying(20) NOT NULL,
"empcode" character varying(20) NOT NULL,
"comcode" character varying(20) NOT NULL
);

CREATE TABLE "schedule_table"(
"sche_idx" integer,
"instid" integer,
"trctag" character varying(100),
"startdate" datetime
);

create table "INST_EMP_PERF" (
	"INSTID" int(11) NOT NULL,
	"EMPCODE" varchar(20) NOT NULL,
	"BUSINESSVALUE" int(10),
	PRIMARY KEY (INSTID, EMPCODE)
);


-- index
ALTER TABLE "app" ADD PRIMARY KEY("appid");
ALTER TABLE "appkeys" ADD PRIMARY KEY("appkey");
ALTER TABLE "appmapping" ADD PRIMARY KEY("appid","comcode");
ALTER TABLE "bizrate" ADD PRIMARY KEY("empcode","yearmm");
ALTER TABLE "bpm_acltable" ADD PRIMARY KEY("acltableid");
ALTER TABLE "bpm_deadln_dim" ADD PRIMARY KEY("deadln_id");
ALTER TABLE "bpm_knol" ADD PRIMARY KEY("id");
ALTER TABLE "bpm_procdef" ADD PRIMARY KEY("defid");
ALTER TABLE "bpm_procdefver" ADD PRIMARY KEY("defverid");
ALTER TABLE "bpm_procinst" ADD PRIMARY KEY("instid");
ALTER TABLE "bpm_procvar" ADD PRIMARY KEY("varid");
ALTER TABLE "bpm_rolemapping" ADD PRIMARY KEY("rolemappingid");
ALTER TABLE "bpm_strtg" ADD PRIMARY KEY("strtgid");
ALTER TABLE "bpm_tag" ADD PRIMARY KEY("tagid");
ALTER TABLE "bpm_topicmapping" ADD PRIMARY KEY("topicmappingid");
ALTER TABLE "bpm_worklist" ADD PRIMARY KEY("taskid");
ALTER TABLE "category" ADD PRIMARY KEY("categoryid");
ALTER TABLE "comtable" ADD PRIMARY KEY("comcode");
ALTER TABLE "emptable" ADD PRIMARY KEY("empcode");
ALTER TABLE "oauth_token" ADD PRIMARY KEY("user_id");
ALTER TABLE "parttable" ADD PRIMARY KEY("partcode");
ALTER TABLE "processmap" ADD PRIMARY KEY("mapid");
ALTER TABLE "roletable" ADD PRIMARY KEY("rolecode");
ALTER TABLE "roleusertable" ADD PRIMARY KEY("rolecode","empcode","comcode");

CREATE INDEX "parentid" ON "bpm_knol"("parentid","no");
CREATE INDEX "fkf57f151c46f158c1" ON "bpm_procinst"("defid");

CREATE INDEX "fkf57f151c78eb68e6" ON "bpm_procinst"("rootinstid");

CREATE INDEX "test1" ON "bpm_procinst"("instid","rootinstid","secuopt");
CREATE INDEX "test1" ON "bpm_rolemapping"("rootinstid","assigntype","endpoint");

CREATE INDEX "test2" ON "bpm_rolemapping"("instid","rolename");

CREATE INDEX "test3" ON "bpm_rolemapping"("rolename");
CREATE INDEX "test1" ON "bpm_worklist"("instid","rolename");

CREATE INDEX "test2" ON "bpm_worklist"("instid","refrolename");

ALTER TABLE "bpm_procdefver" ADD  CONSTRAINT  "fk_defid" FOREIGN KEY ("defid") REFERENCES "bpm_procdef"("defid") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "parttable" ADD  CONSTRAINT  "fk_comcode" FOREIGN KEY ("comcode") REFERENCES "comtable"("comcode") ON DELETE RESTRICT ON UPDATE RESTRICT;


-- marketplace category data add
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (1, '인사/급여', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (2, '영업 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (3, '생산 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (4, '설비 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (5, '구매 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (6, '수출입 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (7, '재고 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (8, '품질 관리', -1, NULL, 0);
INSERT INTO "category" ("categoryid", "categoryname", "parentcategoryid", "moddate", "deleted") VALUES (9, '기술 정보 관리', -1, NULL, 0);


-- paasManager and sample tester data add
INSERT INTO "comtable" ("comcode", "comname", "description", "isdeleted", "repmail", "repmlhst", "repmlpwd") VALUES ('uengine.org', 'uengine.org', NULL, NULL, NULL, NULL, NULL);
INSERT INTO "comtable" ("comcode", "comname", "description", "isdeleted", "repmail", "repmlhst", "repmlpwd") VALUES ('CloudManager', 'CloudManager', '', '', '', '', '');
INSERT INTO "comtable" ("comcode", "comname", "description", "isdeleted", "repmail", "repmlhst", "repmlpwd") VALUES ('uenginecloud.com', 'uenginecloud.com', '', '', '', '', '');

INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('test', 'test', 'test', 1, NULL, 'test@uengine.org', NULL, 'uengine.org', '0', 'ko', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('processCodi.uengine.org', 'CODI', NULL, 0, NULL, NULL, NULL, 'uengine.org', NULL, 'ko', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('paasManager', 'paasManager', 'admin', 1, '', 'paasManager@cloud.Manager', NULL, 'CloudManager', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('pm@uenginecloud.com', 'pm', 'test', 1, '', 'pm@uenginecloud.com', NULL, 'uenginecloud.com', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('processCodi.uenginecloud.com', 'CODI', NULL, 0, NULL, NULL, NULL, 'uenginecloud.com', NULL, 'ko', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('tenantAdmin@uenginecloud.com', 'tenantAdmin', 'test', 1, '', 'tenantAdmin@uenginecloud.com', NULL, 'uenginecloud.com', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('dev01@uenginecloud.com', '개발자1', 'test', 0, 'eee', 'dev01@uenginecloud.com', NULL, 'uenginecloud.com', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'wave      ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('somehow', '개발자2', 'test', 0, NULL, 'dev02@uenginecloud.com', NULL, 'uenginecloud.com', '0', 'ko', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('pm@uengine.org', 'pm', 'test', 1, '', 'pm@uengine.org', NULL, 'uengine.org', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('tenantAdmin@uengine.org', 'tenantManager', 'test', 1, '', 'tenantManager@uengine.org', NULL, 'uengine.org', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);
INSERT INTO "emptable" ("empcode", "empname", "password", "isadmin", "jikname", "email", "partcode", "globalcom", "isdeleted", "locale", "nateon", "msn", "mobilecmp", "mobileno", "facebookid", "facebookpassword", "preferux", "prefermob", "mood", "appkey", "approved") VALUES ('dev01@uengine.org', '개발자1', 'test', 0, '', 'dev01@uengine.org', NULL, 'uengine.org', '0', 'ko', NULL, NULL, NULL, '', '', NULL, 'tw        ', 'auto      ', NULL, NULL, 1);

INSERT INTO "roletable" ("rolecode", "comcode", "descr", "isdeleted") VALUES ('paasManager', 'CloudManager', 'PaaS관리자', '0');

INSERT INTO "roleusertable" ("rolecode", "empcode", "comcode") VALUES ('paasManager', 'paasManager', 'CloudManager');


alter table emptable add guest int(1);
ALTER TABLE `bpm_procinst`  CHANGE COLUMN `BVBENEFIT` `BENEFIT` INT(6) NULL DEFAULT NULL AFTER `lastcmnt2`,  CHANGE COLUMN `BVPENALTY` `PENALTY` INT(6) NULL DEFAULT NULL AFTER `BENEFIT`;