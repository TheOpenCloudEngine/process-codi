SET @old_unique_checks = @@unique_checks, unique_checks = 0;
SET @old_foreign_key_checks = @@foreign_key_checks, foreign_key_checks = 0;
SET @old_sql_mode = @@sql_mode, sql_mode = 'traditional,allow_invalid_dates';

CREATE SCHEMA IF NOT EXISTS `uengine`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_unicode_ci;
USE `uengine`;

-- -----------------------------------------------------
-- table `uengine`.`bpm_knol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_knol` (
  `id`            VARCHAR(20)   NOT NULL,
  `name`          VARCHAR(1000) NULL DEFAULT NULL,
  `linkedinstid`  INT(11)       NULL DEFAULT NULL,
  `parentid`      VARCHAR(20)   NOT NULL,
  `no`            INT(11)       NULL DEFAULT NULL,
  `authorid`      VARCHAR(100)  NULL DEFAULT NULL,
  `type`          CHAR(10)      NULL DEFAULT NULL,
  `vistype`       CHAR(10)      NULL DEFAULT NULL,
  `conntype`      CHAR(20)      NULL DEFAULT NULL,
  `url`           VARCHAR(200)  NULL DEFAULT NULL,
  `thumbnail`     VARCHAR(200)  NULL DEFAULT NULL,
  `secuopt`       CHAR(1)       NULL DEFAULT '0',
  `companyid`     VARCHAR(20)   NULL DEFAULT NULL,
  `refid`         VARCHAR(20)   NULL DEFAULT NULL,
  `budget`        INT(11)       NULL DEFAULT NULL,
  `effort`        INT(11)       NULL DEFAULT NULL,
  `benefit`       INT(11)       NULL DEFAULT NULL,
  `penalty`       INT(11)       NULL DEFAULT NULL,
  `startdate`     DATE          NULL DEFAULT NULL,
  `enddate`       DATE          NULL DEFAULT NULL,
  `description`   VARCHAR(1000) NULL DEFAULT NULL,
  `ext`           VARCHAR(3000) NULL DEFAULT NULL,
  `isreleased` TINYINT(1) NULL DEFAULT NULL,
  `isdistributed` TINYINT(1)    NULL DEFAULT NULL,
  `projectalias`  VARCHAR(1000) NULL DEFAULT NULL,
  INDEX `parentid` (`parentid` ASC, `no` ASC)
)
  ENGINE = myisam
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_noti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_noti` (
  `notiid`      MEDIUMTEXT   NULL DEFAULT NULL,
  `userid`      CHAR(100)    NULL DEFAULT NULL,
  `actorid`     CHAR(100)    NULL DEFAULT NULL,
  `instid`      INT(11)      NULL DEFAULT NULL,
  `actabstract` VARCHAR(300) NULL DEFAULT NULL,
  `taskid`      INT(11)      NULL DEFAULT NULL,
  `type`        INT(11)      NULL DEFAULT NULL,
  `inputdate`   DATETIME     NULL DEFAULT NULL,
  `confirm`     INT(11)      NULL DEFAULT NULL
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_procinst`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_procinst` (
  `instid`          INT(11)      NOT NULL,
  `defverid`        VARCHAR(100) NULL DEFAULT NULL,
  `defid`           VARCHAR(100) NULL DEFAULT NULL,
  `defname`         VARCHAR(255) NULL DEFAULT NULL,
  `defpath`         VARCHAR(255) NULL DEFAULT NULL,
  `defmoddate`      DATETIME     NULL DEFAULT NULL,
  `starteddate`     DATETIME     NULL DEFAULT NULL,
  `finisheddate`    DATETIME     NULL DEFAULT NULL,
  `duedate`         DATETIME     NULL DEFAULT NULL,
  `status`          VARCHAR(20)  NULL DEFAULT NULL,
  `info`            VARCHAR(255) NULL DEFAULT NULL,
  `name`            VARCHAR(255) NULL DEFAULT NULL,
  `isdeleted`       INT(11)      NULL DEFAULT '0',
  `isadhoc`         INT(11)      NULL DEFAULT '0',
  `isarchive`       INT(11)      NULL DEFAULT '0',
  `issubprocess`    INT(11)      NULL DEFAULT '0',
  `iseventhandler`  INT(11)      NULL DEFAULT '0',
  `rootinstid`      INT(11)      NULL DEFAULT NULL,
  `maininstid`      INT(11)      NULL DEFAULT NULL,
  `maindefverid`    INT(11)      NULL DEFAULT NULL,
  `mainacttrctag`   VARCHAR(20)  NULL DEFAULT NULL,
  `mainexecscope`   VARCHAR(20)  NULL DEFAULT NULL,
  `abstrcpath`      VARCHAR(200) NULL DEFAULT NULL,
  `dontreturn`      INT(11)      NULL DEFAULT NULL,
  `moddate`         DATETIME     NULL DEFAULT NULL,
  `ext1`            VARCHAR(100) NULL DEFAULT NULL,
  `initep`          VARCHAR(100) NULL DEFAULT NULL,
  `initrsnm`        VARCHAR(100) NULL DEFAULT NULL,
  `currep`          VARCHAR(100) NULL DEFAULT NULL,
  `currrsnm`        VARCHAR(100) NULL DEFAULT NULL,
  `strategyid`      INT(11)      NULL DEFAULT NULL,
  `prevcurrep`      VARCHAR(100) NULL DEFAULT NULL,
  `prevcurrrsnm`    VARCHAR(100) NULL DEFAULT NULL,
  `starcnt`         INT(11)      NULL DEFAULT NULL,
  `starrsnm`        VARCHAR(100) NULL DEFAULT NULL,
  `starflag`        INT(11)      NULL DEFAULT NULL,
  `abstractinst`    TEXT         NULL DEFAULT NULL,
  `currtrctag`      INT(11)      NULL DEFAULT NULL,
  `prevtrctag`      INT(11)      NULL DEFAULT NULL,
  `initcomcd`       VARCHAR(20)  NULL DEFAULT NULL,
  `secuopt`         CHAR(1)      NULL DEFAULT '0',
  `lastcmnt`        VARCHAR(200) NULL DEFAULT NULL,
  `initcmpl`        INT(1)       NULL DEFAULT NULL,
  `topicid`         CHAR(20)     NULL DEFAULT NULL,
  `progress`        VARCHAR(10)  NULL DEFAULT NULL,
  `lastcmnt2`       VARCHAR(200) NULL DEFAULT NULL,
  `benefit`         INT(6)       NULL DEFAULT NULL,
  `penalty`         INT(6)       NULL DEFAULT NULL,
  `effort`          INT(6)       NULL DEFAULT NULL,
  `lastcmntep`      VARCHAR(100) NULL DEFAULT NULL,
  `lastcmntrsnm`    VARCHAR(100) NULL DEFAULT NULL,
  `lastcmnt2ep`     VARCHAR(100) NULL DEFAULT NULL,
  `lastcmnt2rsnm`   VARCHAR(100) NULL DEFAULT NULL,
  `isdocument`      INT(11)      NULL DEFAULT '0',
  `isfileadded`     INT(11)      NULL DEFAULT '0',
  `lastcmnttaskid`  INT(11)      NULL DEFAULT NULL,
  `lastcmnt2taskid` INT(11)      NULL DEFAULT NULL,
  PRIMARY KEY (`instid`),
  INDEX `fkf57f151c46f158c1` (`defid` ASC),
  INDEX `fkf57f151c78eb68e6` (`rootinstid` ASC)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_procvar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_procvar` (
  `varid`        INT(11)       NOT NULL AUTO_INCREMENT,
  `instid`       INT(11)       NULL     DEFAULT NULL,
  `datatype`     INT(11)       NULL     DEFAULT NULL,
  `valuestring`  VARCHAR(4000) NULL     DEFAULT NULL,
  `valuelong`    INT(11)       NULL     DEFAULT NULL,
  `valueboolean` INT(11)       NULL     DEFAULT NULL,
  `valuedate`    DATETIME      NULL     DEFAULT NULL,
  `varindex`     INT(11)       NULL     DEFAULT NULL,
  `trctag`       VARCHAR(20)   NULL     DEFAULT NULL,
  `isproperty`   INT(11)       NULL     DEFAULT NULL,
  `moddate`      DATETIME      NULL     DEFAULT NULL,
  `keyname`      VARCHAR(100)  NULL     DEFAULT NULL,
  `keystring`    VARCHAR(200)  NULL     DEFAULT NULL,
  `filecontent`  TEXT          NULL     DEFAULT NULL,
  `htmlfilepath` VARCHAR(255)  NULL     DEFAULT NULL,
  PRIMARY KEY (`varid`)
)
  ENGINE = innodb
  AUTO_INCREMENT = 17678
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_roledef`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_roledef` (
  `roledefid`      VARCHAR(100) NULL DEFAULT NULL,
  `defid`          VARCHAR(50)  NULL DEFAULT NULL,
  `rolename`       CHAR(20)     NULL DEFAULT NULL,
  `mappeduserid`   VARCHAR(50)  NULL DEFAULT NULL,
  `comcode`        VARCHAR(50)  NULL DEFAULT NULL,
  `mappedrolecode` VARCHAR(100) NULL DEFAULT NULL,
  `roledeftype`    VARCHAR(10)  NULL DEFAULT NULL
)
  ENGINE = myisam
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_rolemapping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_rolemapping` (
  `rolemappingid`  INT(11)       NOT NULL AUTO_INCREMENT,
  `instid`         INT(11)       NULL     DEFAULT NULL,
  `rootinstid`     INT(11)       NULL     DEFAULT NULL,
  `rolename`       VARCHAR(255)  NULL     DEFAULT NULL,
  `endpoint`       VARCHAR(255)  NULL     DEFAULT NULL,
  `value`          VARCHAR(4000) NULL     DEFAULT NULL,
  `resname`        VARCHAR(200)  NULL     DEFAULT NULL,
  `assigntype`     INT(11)       NULL     DEFAULT NULL,
  `assignparam1`   VARCHAR(100)  NULL     DEFAULT NULL,
  `dispatchoption` INT(11)       NULL     DEFAULT NULL,
  `dispatchparam1` VARCHAR(100)  NULL     DEFAULT NULL,
  `groupid`        VARCHAR(30)   NULL     DEFAULT NULL,
  `isreferencer`   INT(1)        NULL     DEFAULT '0',
  PRIMARY KEY (`rolemappingid`)
)
  ENGINE = innodb
  AUTO_INCREMENT = 2519
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_seq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_seq` (
  `seq`         INT(11)      NULL DEFAULT NULL,
  `tbname`      VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `moddate`     DATETIME     NULL DEFAULT NULL
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_topicmapping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_topicmapping` (
  `topicmappingid` INT(11)       NOT NULL,
  `topicid`        VARCHAR(20)   NOT NULL,
  `userid`         VARCHAR(255)  NULL DEFAULT NULL,
  `username`       VARCHAR(1000) NULL DEFAULT NULL,
  `assigntype`     INT(11)       NULL DEFAULT '0',
  PRIMARY KEY (`topicmappingid`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_worklist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`bpm_worklist` (
  `taskid`           INT(11)       NOT NULL,
  `title`            VARCHAR(3000) NULL DEFAULT NULL,
  `description`      VARCHAR(500)  NULL DEFAULT NULL,
  `endpoint`         VARCHAR(200)  NULL DEFAULT NULL,
  `status`           VARCHAR(100)  NULL DEFAULT '',
  `priority`         INT(11)       NULL DEFAULT NULL,
  `startdate`        DATETIME      NULL DEFAULT NULL,
  `enddate`          DATETIME      NULL DEFAULT NULL,
  `duedate`          DATETIME      NULL DEFAULT NULL,
  `instid`           INT(11)       NULL DEFAULT NULL,
  `defid`            VARCHAR(100)  NULL DEFAULT NULL,
  `defname`          VARCHAR(200)  NULL DEFAULT NULL,
  `trctag`           VARCHAR(100)  NULL DEFAULT NULL,
  `tool`             VARCHAR(200)  NULL DEFAULT NULL,
  `parameter`        VARCHAR(4000) NULL DEFAULT NULL,
  `groupid`          INT(11)       NULL DEFAULT NULL,
  `groupname`        INT(11)       NULL DEFAULT NULL,
  `ext1`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext2`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext3`             VARCHAR(200)  NULL DEFAULT NULL,
  `isurgent`         INT(11)       NULL DEFAULT NULL,
  `hasattachfile`    INT(11)       NULL DEFAULT NULL,
  `hascomment`       INT(11)       NULL DEFAULT NULL,
  `documentcategory` VARCHAR(200)  NULL DEFAULT NULL,
  `isdeleted`        INT(11)       NULL DEFAULT '0',
  `rootinstid`       INT(11)       NULL DEFAULT NULL,
  `dispatchoption`   INT(11)       NULL DEFAULT NULL,
  `dispatchparam1`   VARCHAR(100)  NULL DEFAULT NULL,
  `rolename`         VARCHAR(100)  NULL DEFAULT NULL,
  `resname`          VARCHAR(100)  NULL DEFAULT NULL,
  `refrolename`      VARCHAR(100)  NULL DEFAULT NULL,
  `execscope`        VARCHAR(5)    NULL DEFAULT NULL,
  `savedate`         DATETIME      NULL DEFAULT NULL,
  `abstract`         TEXT          NULL DEFAULT NULL,
  `type`             CHAR(10)      NULL DEFAULT NULL,
  `content`          MEDIUMTEXT    NULL DEFAULT NULL,
  `extfile`          VARCHAR(200)  NULL DEFAULT NULL,
  `prevver`          INT(11)       NULL DEFAULT NULL,
  `nextver`          INT(11)       NULL DEFAULT NULL,
  `ext4`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext5`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext6`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext7`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext8`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext9`             VARCHAR(200)  NULL DEFAULT NULL,
  `ext10`            VARCHAR(200)  NULL DEFAULT NULL,
  `prttskid`         INT(11)       NULL DEFAULT NULL,
  `majorver`         INT(5)        NULL DEFAULT NULL,
  `minorver`         INT(5)        NULL DEFAULT NULL,
  `grptaskid`        INT(11)       NULL DEFAULT NULL,
  `folderid`         VARCHAR(100)  NULL DEFAULT NULL,
  `foldername`       VARCHAR(100)  NULL DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  INDEX `fk33852daff5139497` (`defid` ASC),
  INDEX `fk33852dafe10386fc` (`endpoint` ASC),
  INDEX `fk33852daf63959984` (`instid` ASC),
  INDEX `fk33852daf78eb68e6` (`rootinstid` ASC)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`comtable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`comtable` (
  `comcode`     VARCHAR(20)  NOT NULL DEFAULT '',
  `comname`     VARCHAR(100) NULL     DEFAULT NULL,
  `description` VARCHAR(255) NULL     DEFAULT NULL,
  `isdeleted`   VARCHAR(1)   NULL     DEFAULT '0',
  `repmail`     VARCHAR(100) NULL     DEFAULT NULL,
  `repmlhst`    VARCHAR(100) NULL     DEFAULT NULL,
  `repmlpwd`    VARCHAR(100) NULL     DEFAULT NULL,
  `alias`       VARCHAR(100) NULL     DEFAULT NULL,
  `logopath`    VARCHAR(100) NULL     DEFAULT NULL,
  `billaccnt` VARCHAR(100)          DEFAULT NULL,
  `billsbscr`   VARCHAR(100),
  PRIMARY KEY (`comcode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`contact` (
  `userid`     VARCHAR(100) NULL DEFAULT NULL,
  `friendid`   VARCHAR(100) NULL DEFAULT NULL,
  `friendname` VARCHAR(20)  NULL DEFAULT NULL,
  `network`    CHAR(10)     NULL DEFAULT NULL,
  `mood`       VARCHAR(100) NULL DEFAULT NULL
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`emp_prop_table`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`emp_prop_table` (
  `propid`    INT(11)      NOT NULL,
  `comcode`   VARCHAR(20)  NOT NULL,
  `empcode`   VARCHAR(20)  NOT NULL,
  `propkey`   VARCHAR(40)  NOT NULL,
  `propvalue` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`propid`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`emptable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`emptable` (
  `empcode`          VARCHAR(100) NOT NULL DEFAULT '',
  `empname`          VARCHAR(100) NULL     DEFAULT NULL,
  `password`         VARCHAR(20)  NULL     DEFAULT NULL,
  `isadmin`          INT(11)      NULL     DEFAULT NULL,
  `jikname`          VARCHAR(100) NULL     DEFAULT NULL,
  `email`            VARCHAR(100) NULL     DEFAULT NULL,
  `partcode`         VARCHAR(20)  NULL     DEFAULT NULL,
  `globalcom`        VARCHAR(20)  NULL     DEFAULT NULL,
  `isdeleted`        VARCHAR(1)   NULL     DEFAULT '0',
  `locale`           VARCHAR(10)  NULL     DEFAULT NULL,
  `nateon`           VARCHAR(50)  NULL     DEFAULT NULL,
  `msn`              VARCHAR(50)  NULL     DEFAULT NULL,
  `mobilecmp`        VARCHAR(50)  NULL     DEFAULT NULL,
  `mobileno`         VARCHAR(50)  NULL     DEFAULT NULL,
  `facebookid`       VARCHAR(100) NULL     DEFAULT NULL,
  `facebookpassword` VARCHAR(100) NULL     DEFAULT NULL,
  `preferux`         CHAR(10)     NULL     DEFAULT NULL,
  `prefermob`        CHAR(10)     NULL     DEFAULT NULL,
  `mood`             VARCHAR(100) NULL     DEFAULT NULL,
  `appkey`           VARCHAR(100) NULL     DEFAULT NULL,
  `approved`         INT(1)       NULL     DEFAULT NULL,
  `guest`            INT(1)       NULL     DEFAULT NULL,
  `inviteuser`       VARCHAR(100) NULL     DEFAULT NULL,
  `mailnoti`         INT(1)       NULL     DEFAULT '0',
  `notiemail`        INT(1)       NULL     DEFAULT '0',
  `authkey`          VARCHAR(100) NULL     DEFAULT NULL,
  `roletype`         VARCHAR(10)  NULL     DEFAULT NULL,
  PRIMARY KEY (`empcode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`inst_emp_perf`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`inst_emp_perf` (
  `instid`        INT(11)      NOT NULL,
  `empcode`       VARCHAR(100) NOT NULL DEFAULT '',
  `businessvalue` INT(10)      NULL     DEFAULT NULL,
  PRIMARY KEY (`instid`, `empcode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`logtable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`logtable` (
  `id`      INT(11)                   NOT NULL,
  `type`    VARCHAR(20)
            CHARACTER SET 'utf8'
            COLLATE 'utf8_unicode_ci' NOT NULL,
  `empcode` VARCHAR(100)
            CHARACTER SET 'utf8'
            COLLATE 'utf8_unicode_ci' NOT NULL,
  `comcode` VARCHAR(100)
            CHARACTER SET 'utf8'
            COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL,
  `ip`      VARCHAR(50)
            CHARACTER SET 'utf8'
            COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL,
  `date`    VARCHAR(50)
            CHARACTER SET 'utf8'
            COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- table `uengine`.`notisetting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`notisetting` (
  `id`              INT(11)     NOT NULL,
  `userid`          VARCHAR(20) NOT NULL,
  `notiadvice`      INT(11)     NULL DEFAULT '0',
  `modiuser`        INT(11)     NULL DEFAULT '0',
  `moditopic`       INT(11)     NULL DEFAULT '0',
  `modiorgan`       INT(11)     NULL DEFAULT '0',
  `writebookmark`   INT(11)     NULL DEFAULT '0',
  `writetopic`      INT(11)     NULL DEFAULT '0',
  `writeorgan`      INT(11)     NULL DEFAULT '0',
  `writeinstance`   INT(11)     NULL DEFAULT '0',
  `invitetopic`     INT(11)     NULL DEFAULT '0',
  `inviteorgan`     INT(11)     NULL DEFAULT '0',
  `addfriend`       INT(11)     NULL DEFAULT '0',
  `beforehandnoti`  INT(11)     NULL DEFAULT '0',
  `notitime`        INT(11)     NULL DEFAULT '0',
  `defaultnotitime` VARCHAR(20) NULL DEFAULT NULL,
  `notiemail`       INT(11)     NULL DEFAULT '0',
  `checklogin`      INT(11)     NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`parttable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`parttable` (
  `globalcom`       VARCHAR(20)  NULL DEFAULT NULL,
  `partcode`        VARCHAR(20)  NOT NULL,
  `partname`        VARCHAR(30)  NULL DEFAULT NULL,
  `parent_partcode` VARCHAR(20)  NULL DEFAULT NULL,
  `isdeleted`       VARCHAR(1)   NULL DEFAULT '0',
  `description`     VARCHAR(255) NULL DEFAULT NULL,
  `comcode`         VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`partcode`),
  INDEX `fk3b63679b4506931c` (`comcode` ASC),
  CONSTRAINT `fk3b63679b4506931c`
  FOREIGN KEY (`comcode`)
  REFERENCES `uengine`.`comtable` (`comcode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`processmap`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`processmap` (
  `mapid`       VARCHAR(100) NOT NULL DEFAULT '',
  `defid`       VARCHAR(50)  NOT NULL DEFAULT '',
  `name`        VARCHAR(50)  NULL     DEFAULT NULL,
  `iconpath`    VARCHAR(255) NULL     DEFAULT NULL,
  `color`       VARCHAR(10)  NULL     DEFAULT NULL,
  `comcode`     VARCHAR(20)  NULL     DEFAULT NULL,
  `no`          INT(11)      NULL     DEFAULT NULL,
  `cmphrase`    CHAR(200)    NULL     DEFAULT NULL,
  `cmtrgr`      VARCHAR(20)  NULL     DEFAULT NULL,
  `isscheduled` INT(1)       NULL     DEFAULT '0',
  PRIMARY KEY (`mapid`)
)
  ENGINE = myisam
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`processtopicmapping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`processtopicmapping` (
  `processname` VARCHAR(200) NOT NULL,
  `processpath` VARCHAR(200) NOT NULL,
  `topicid`     VARCHAR(50)  NOT NULL,
  `type`        VARCHAR(20)  NOT NULL,
  PRIMARY KEY (`topicid`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`recentitem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`recentitem` (
  `empcode`      VARCHAR(100) NOT NULL,
  `itemtype`     VARCHAR(10)  NOT NULL,
  `itemid`       VARCHAR(100) NOT NULL,
  `updatedate`   DATETIME     NULL DEFAULT NULL,
  `clickedcount` VARCHAR(4)   NULL DEFAULT NULL,
  PRIMARY KEY (`empcode`, `itemtype`, `itemid`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`roletable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`roletable` (
  `rolecode`  VARCHAR(20)  NOT NULL,
  `comcode`   VARCHAR(20)  NULL DEFAULT NULL,
  `descr`     VARCHAR(255) NULL DEFAULT NULL,
  `isdeleted` VARCHAR(1)   NULL DEFAULT '0',
  `url`       VARCHAR(200) NULL DEFAULT NULL,
  `thumbnail` VARCHAR(200) NULL DEFAULT NULL,
  `rolename`  VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`rolecode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- table `uengine`.`roleusertable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uengine`.`roleusertable` (
  `rolecode` VARCHAR(20) NOT NULL,
  `empcode`  VARCHAR(20) NOT NULL,
  PRIMARY KEY (`rolecode`, `empcode`),
  INDEX `fk8ceeb30d7c65b1a` (`rolecode` ASC),
  CONSTRAINT `fk8ceeb30d7c65b1a`
  FOREIGN KEY (`rolecode`)
  REFERENCES `uengine`.`roletable` (`rolecode`)
)
  ENGINE = innodb
  DEFAULT CHARACTER SET = utf8;


USE `uengine`;


SET sql_mode = @old_sql_mode;
SET foreign_key_checks = @old_foreign_key_checks;
SET unique_checks = @old_unique_checks;

-- insert into `comtable` (`comcode`, `comname`) values ('1', 'uengine');
-- insert into `emptable` (`empcode`, `empname`, `password`, `isadmin`, `email`,`globalcom`, `isdeleted`, `locale`, `approved`, `guest`) values ('1', 'tester', 'test', '1', 'test@uengine.org', '1', '0', 'ko', '1', '0');



ALTER TABLE bpm_worklist ADD COLUMN haschild INT;


ALTER TABLE processmap MODIFY COLUMN mapid VARCHAR(200);
-- generation logic of map id should be changed to reduce the string size
ALTER TABLE processmap MODIFY COLUMN defid VARCHAR(200);
-- generation logic of map id should be changed to reduce the string size

ALTER TABLE emptable MODIFY COLUMN password VARCHAR(100);


ALTER TABLE comtable MODIFY COLUMN `billaccnt` VARCHAR(100) DEFAULT NULL;
ALTER TABLE comtable MODIFY COLUMN `billsbscr` VARCHAR(100) DEFAULT NULL;

ALTER TABLE bpm_procinst ADD COLUMN issim INT(1) DEFAULT 0; -- to identify simulation instance or not

alter table bpm_procinst modify column defverid varchar(255);
alter table bpm_roledef modify column rolename varchar(255);