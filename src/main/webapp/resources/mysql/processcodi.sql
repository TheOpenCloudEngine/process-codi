SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `uengine` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `uengine` ;

-- -----------------------------------------------------
-- Table `uengine`.`bpm_knol`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_knol` (
  `id` VARCHAR(20) NOT NULL ,
  `name` VARCHAR(1000) NULL DEFAULT NULL ,
  `linkedInstId` INT(11) NULL DEFAULT NULL ,
  `parentId` VARCHAR(20) NOT NULL ,
  `no` INT(11) NULL DEFAULT NULL ,
  `authorid` VARCHAR(100) NULL DEFAULT NULL ,
  `type` CHAR(10) NULL DEFAULT NULL ,
  `vistype` CHAR(10) NULL DEFAULT NULL ,
  `CONNTYPE` CHAR(20) NULL DEFAULT NULL ,
  `URL` VARCHAR(200) NULL DEFAULT NULL ,
  `THUMBNAIL` VARCHAR(200) NULL DEFAULT NULL ,
  `SECUOPT` CHAR(1) NULL DEFAULT '0' ,
  `companyId` VARCHAR(20) NULL DEFAULT NULL ,
  `refId` VARCHAR(20) NULL DEFAULT NULL ,
  `budget` INT(11) NULL DEFAULT NULL ,
  `effort` INT(11) NULL DEFAULT NULL ,
  `benefit` INT(11) NULL DEFAULT NULL ,
  `penalty` INT(11) NULL DEFAULT NULL ,
  `startdate` DATE NULL DEFAULT NULL ,
  `enddate` DATE NULL DEFAULT NULL ,
  `description` VARCHAR(1000) NULL DEFAULT NULL ,
  `ext` VARCHAR(3000) NULL DEFAULT NULL ,
  `isreleased` TINYINT(1) NULL DEFAULT NULL ,
  `isdistributed` TINYINT(1) NULL DEFAULT NULL ,
  `projectalias` VARCHAR(1000) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `parentId` (`parentId` ASC, `no` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_noti`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_noti` (
  `notiId` MEDIUMTEXT NULL DEFAULT NULL ,
  `userId` CHAR(100) NULL DEFAULT NULL ,
  `actorId` CHAR(100) NULL DEFAULT NULL ,
  `instId` INT(11) NULL DEFAULT NULL ,
  `actAbstract` VARCHAR(300) NULL DEFAULT NULL ,
  `taskId` INT(11) NULL DEFAULT NULL ,
  `type` INT(11) NULL DEFAULT NULL ,
  `inputdate` DATETIME NULL DEFAULT NULL ,
  `confirm` INT(11) NULL DEFAULT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_procinst`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_procinst` (
  `INSTID` INT(11) NOT NULL ,
  `DEFVERID` VARCHAR(100) NULL DEFAULT NULL ,
  `DEFID` VARCHAR(100) NULL DEFAULT NULL ,
  `DEFNAME` VARCHAR(255) NULL DEFAULT NULL ,
  `DEFPATH` VARCHAR(255) NULL DEFAULT NULL ,
  `DEFMODDATE` DATETIME NULL DEFAULT NULL ,
  `STARTEDDATE` DATETIME NULL DEFAULT NULL ,
  `FINISHEDDATE` DATETIME NULL DEFAULT NULL ,
  `DUEDATE` DATETIME NULL DEFAULT NULL ,
  `STATUS` VARCHAR(20) NULL DEFAULT NULL ,
  `INFO` VARCHAR(255) NULL DEFAULT NULL ,
  `NAME` VARCHAR(255) NULL DEFAULT NULL ,
  `ISDELETED` INT(11) NULL DEFAULT '0' ,
  `ISADHOC` INT(11) NULL DEFAULT '0' ,
  `ISARCHIVE` INT(11) NULL DEFAULT '0' ,
  `ISSUBPROCESS` INT(11) NULL DEFAULT '0' ,
  `ISEVENTHANDLER` INT(11) NULL DEFAULT '0' ,
  `ROOTINSTID` INT(11) NULL DEFAULT NULL ,
  `MAININSTID` INT(11) NULL DEFAULT NULL ,
  `MAINDEFVERID` INT(11) NULL DEFAULT NULL ,
  `MAINACTTRCTAG` VARCHAR(20) NULL DEFAULT NULL ,
  `MAINEXECSCOPE` VARCHAR(20) NULL DEFAULT NULL ,
  `ABSTRCPATH` VARCHAR(200) NULL DEFAULT NULL ,
  `DONTRETURN` INT(11) NULL DEFAULT NULL ,
  `MODDATE` DATETIME NULL DEFAULT NULL ,
  `EXT1` VARCHAR(100) NULL DEFAULT NULL ,
  `INITEP` VARCHAR(100) NULL DEFAULT NULL ,
  `INITRSNM` VARCHAR(100) NULL DEFAULT NULL ,
  `CURREP` VARCHAR(100) NULL DEFAULT NULL ,
  `CURRRSNM` VARCHAR(100) NULL DEFAULT NULL ,
  `STRATEGYID` INT(11) NULL DEFAULT NULL ,
  `PREVCURREP` VARCHAR(100) NULL DEFAULT NULL ,
  `PREVCURRRSNM` VARCHAR(100) NULL DEFAULT NULL ,
  `STARCNT` INT(11) NULL DEFAULT NULL ,
  `STARRSNM` VARCHAR(100) NULL DEFAULT NULL ,
  `STARFLAG` INT(11) NULL DEFAULT NULL ,
  `ABSTRACTINST` TEXT NULL DEFAULT NULL ,
  `CURRTRCTAG` INT(11) NULL DEFAULT NULL ,
  `PREVTRCTAG` INT(11) NULL DEFAULT NULL ,
  `INITCOMCD` VARCHAR(20) NULL DEFAULT NULL ,
  `SECUOPT` CHAR(1) NULL DEFAULT '0' ,
  `lastcmnt` VARCHAR(200) NULL DEFAULT NULL ,
  `initcmpl` INT(1) NULL DEFAULT NULL ,
  `topicId` CHAR(20) NULL DEFAULT NULL ,
  `progress` VARCHAR(10) NULL DEFAULT NULL ,
  `lastcmnt2` VARCHAR(200) NULL DEFAULT NULL ,
  `BENEFIT` INT(6) NULL DEFAULT NULL ,
  `PENALTY` INT(6) NULL DEFAULT NULL ,
  `EFFORT` INT(6) NULL DEFAULT NULL ,
  `lastCmntEp` VARCHAR(100) NULL DEFAULT NULL ,
  `lastCmntRsnm` VARCHAR(100) NULL DEFAULT NULL ,
  `lastCmnt2Ep` VARCHAR(100) NULL DEFAULT NULL ,
  `lastCmnt2Rsnm` VARCHAR(100) NULL DEFAULT NULL ,
  `isdocument` INT(11) NULL DEFAULT '0' ,
  `isfileadded` INT(11) NULL DEFAULT '0' ,
  `lastcmntTaskId` INT(11) NULL DEFAULT NULL ,
  `lastcmnt2TaskId` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`INSTID`) ,
  INDEX `FKF57F151C46F158C1` (`DEFID` ASC) ,
  INDEX `FKF57F151C78EB68E6` (`ROOTINSTID` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_procvar`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_procvar` (
  `VARID` INT(11) NOT NULL AUTO_INCREMENT ,
  `INSTID` INT(11) NULL DEFAULT NULL ,
  `DATATYPE` INT(11) NULL DEFAULT NULL ,
  `VALUESTRING` VARCHAR(4000) NULL DEFAULT NULL ,
  `VALUELONG` INT(11) NULL DEFAULT NULL ,
  `VALUEBOOLEAN` INT(11) NULL DEFAULT NULL ,
  `VALUEDATE` DATETIME NULL DEFAULT NULL ,
  `VARINDEX` INT(11) NULL DEFAULT NULL ,
  `TRCTAG` VARCHAR(20) NULL DEFAULT NULL ,
  `ISPROPERTY` INT(11) NULL DEFAULT NULL ,
  `MODDATE` DATETIME NULL DEFAULT NULL ,
  `KEYNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `KEYSTRING` VARCHAR(200) NULL DEFAULT NULL ,
  `FILECONTENT` TEXT NULL DEFAULT NULL ,
  `HTMLFILEPATH` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`VARID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 17678
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_roledef`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_roledef` (
  `roledefid` VARCHAR(100) NULL DEFAULT NULL ,
  `defId` VARCHAR(50) NULL DEFAULT NULL ,
  `roleName` CHAR(20) NULL DEFAULT NULL ,
  `mappedUserId` VARCHAR(50) NULL DEFAULT NULL ,
  `comCode` VARCHAR(50) NULL DEFAULT NULL ,
  `mappedRoleCode` VARCHAR(100) NULL DEFAULT NULL ,
  `roleDefType` VARCHAR(10) NULL DEFAULT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_rolemapping`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_rolemapping` (
  `ROLEMAPPINGID` INT(11) NOT NULL AUTO_INCREMENT ,
  `INSTID` INT(11) NULL DEFAULT NULL ,
  `ROOTINSTID` INT(11) NULL DEFAULT NULL ,
  `ROLENAME` VARCHAR(255) NULL DEFAULT NULL ,
  `ENDPOINT` VARCHAR(255) NULL DEFAULT NULL ,
  `VALUE` VARCHAR(4000) NULL DEFAULT NULL ,
  `RESNAME` VARCHAR(200) NULL DEFAULT NULL ,
  `ASSIGNTYPE` INT(11) NULL DEFAULT NULL ,
  `ASSIGNPARAM1` VARCHAR(100) NULL DEFAULT NULL ,
  `DISPATCHOPTION` INT(11) NULL DEFAULT NULL ,
  `DISPATCHPARAM1` VARCHAR(100) NULL DEFAULT NULL ,
  `GROUPID` VARCHAR(30) NULL DEFAULT NULL ,
  `ISREFERENCER` INT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`ROLEMAPPINGID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 2519
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_seq`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_seq` (
  `SEQ` INT(11) NULL DEFAULT NULL ,
  `TBNAME` VARCHAR(255) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `MODDATE` DATETIME NULL DEFAULT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_topicmapping`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_topicmapping` (
  `TOPICMAPPINGID` INT(11) NOT NULL ,
  `TOPICID` VARCHAR(20) NOT NULL ,
  `USERID` VARCHAR(255) NULL DEFAULT NULL ,
  `USERNAME` VARCHAR(1000) NULL DEFAULT NULL ,
  `ASSIGNTYPE` INT(11) NULL DEFAULT '0' ,
  PRIMARY KEY (`TOPICMAPPINGID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`bpm_worklist`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`bpm_worklist` (
  `TASKID` INT(11) NOT NULL ,
  `title` VARCHAR(3000) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(500) NULL DEFAULT NULL ,
  `ENDPOINT` VARCHAR(200) NULL DEFAULT NULL ,
  `STATUS` VARCHAR(100) NULL DEFAULT '' ,
  `PRIORITY` INT(11) NULL DEFAULT NULL ,
  `STARTDATE` DATETIME NULL DEFAULT NULL ,
  `ENDDATE` DATETIME NULL DEFAULT NULL ,
  `DUEDATE` DATETIME NULL DEFAULT NULL ,
  `INSTID` INT(11) NULL DEFAULT NULL ,
  `DEFID` VARCHAR(100) NULL DEFAULT NULL ,
  `DEFNAME` VARCHAR(200) NULL DEFAULT NULL ,
  `TRCTAG` VARCHAR(100) NULL DEFAULT NULL ,
  `TOOL` VARCHAR(200) NULL DEFAULT NULL ,
  `PARAMETER` VARCHAR(4000) NULL DEFAULT NULL ,
  `GROUPID` INT(11) NULL DEFAULT NULL ,
  `GROUPNAME` INT(11) NULL DEFAULT NULL ,
  `EXT1` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT2` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT3` VARCHAR(200) NULL DEFAULT NULL ,
  `ISURGENT` INT(11) NULL DEFAULT NULL ,
  `HASATTACHFILE` INT(11) NULL DEFAULT NULL ,
  `HASCOMMENT` INT(11) NULL DEFAULT NULL ,
  `DOCUMENTCATEGORY` VARCHAR(200) NULL DEFAULT NULL ,
  `ISDELETED` INT(11) NULL DEFAULT '0' ,
  `ROOTINSTID` INT(11) NULL DEFAULT NULL ,
  `DISPATCHOPTION` INT(11) NULL DEFAULT NULL ,
  `DISPATCHPARAM1` VARCHAR(100) NULL DEFAULT NULL ,
  `ROLENAME` VARCHAR(100) NULL DEFAULT NULL ,
  `RESNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `REFROLENAME` VARCHAR(100) NULL DEFAULT NULL ,
  `EXECSCOPE` VARCHAR(5) NULL DEFAULT NULL ,
  `SAVEDATE` DATETIME NULL DEFAULT NULL ,
  `ABSTRACT` TEXT NULL DEFAULT NULL ,
  `type` CHAR(10) NULL DEFAULT NULL ,
  `content` MEDIUMTEXT NULL DEFAULT NULL ,
  `extfile` VARCHAR(200) NULL DEFAULT NULL ,
  `prevver` INT(11) NULL DEFAULT NULL ,
  `nextver` INT(11) NULL DEFAULT NULL ,
  `EXT4` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT5` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT6` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT7` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT8` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT9` VARCHAR(200) NULL DEFAULT NULL ,
  `EXT10` VARCHAR(200) NULL DEFAULT NULL ,
  `prtTskId` INT(11) NULL DEFAULT NULL ,
  `MAJORVER` INT(5) NULL DEFAULT NULL ,
  `MINORVER` INT(5) NULL DEFAULT NULL ,
  `grpTaskId` INT(11) NULL DEFAULT NULL ,
  `folderId` VARCHAR(100) NULL DEFAULT NULL ,
  `folderName` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`TASKID`) ,
  INDEX `FK33852DAFF5139497` (`DEFID` ASC) ,
  INDEX `FK33852DAFE10386FC` (`ENDPOINT` ASC) ,
  INDEX `FK33852DAF63959984` (`INSTID` ASC) ,
  INDEX `FK33852DAF78EB68E6` (`ROOTINSTID` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`comtable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`comtable` (
  `COMCODE` VARCHAR(20) NOT NULL DEFAULT '' ,
  `COMNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `ISDELETED` VARCHAR(1) NULL DEFAULT '0' ,
  `repmail` VARCHAR(100) NULL DEFAULT NULL ,
  `repMlHst` VARCHAR(100) NULL DEFAULT NULL ,
  `repMlPwd` VARCHAR(100) NULL DEFAULT NULL ,
  `alias` VARCHAR(100) NULL DEFAULT NULL ,
  `logoPath` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`COMCODE`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`contact`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`contact` (
  `userId` VARCHAR(100) NULL DEFAULT NULL ,
  `friendId` VARCHAR(100) NULL DEFAULT NULL ,
  `friendName` VARCHAR(20) NULL DEFAULT NULL ,
  `network` CHAR(10) NULL DEFAULT NULL ,
  `mood` VARCHAR(100) NULL DEFAULT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`emp_prop_table`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`emp_prop_table` (
  `PROPID` INT(11) NOT NULL ,
  `COMCODE` VARCHAR(20) NOT NULL ,
  `EMPCODE` VARCHAR(20) NOT NULL ,
  `PROPKEY` VARCHAR(40) NOT NULL ,
  `PROPVALUE` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`PROPID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`emptable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`emptable` (
  `empcode` VARCHAR(100) NOT NULL DEFAULT '' ,
  `EMPNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `PASSWORD` VARCHAR(20) NULL DEFAULT NULL ,
  `ISADMIN` INT(11) NULL DEFAULT NULL ,
  `JIKNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `EMAIL` VARCHAR(100) NULL DEFAULT NULL ,
  `PARTCODE` VARCHAR(20) NULL DEFAULT NULL ,
  `GLOBALCOM` VARCHAR(20) NULL DEFAULT NULL ,
  `ISDELETED` VARCHAR(1) NULL DEFAULT '0' ,
  `locale` VARCHAR(10) NULL DEFAULT NULL ,
  `NATEON` VARCHAR(50) NULL DEFAULT NULL ,
  `MSN` VARCHAR(50) NULL DEFAULT NULL ,
  `MOBILECMP` VARCHAR(50) NULL DEFAULT NULL ,
  `MOBILENO` VARCHAR(50) NULL DEFAULT NULL ,
  `FACEBOOKID` VARCHAR(100) NULL DEFAULT NULL ,
  `FACEBOOKPASSWORD` VARCHAR(100) NULL DEFAULT NULL ,
  `preferux` CHAR(10) NULL DEFAULT NULL ,
  `prefermob` CHAR(10) NULL DEFAULT NULL ,
  `mood` VARCHAR(100) NULL DEFAULT NULL ,
  `APPKEY` VARCHAR(100) NULL DEFAULT NULL ,
  `approved` INT(1) NULL DEFAULT NULL ,
  `guest` INT(1) NULL DEFAULT NULL ,
  `inviteUser` VARCHAR(100) NULL DEFAULT NULL ,
  `MailNoti` INT(1) NULL DEFAULT '0' ,
  `notiEmail` INT(1) NULL DEFAULT '0' ,
  `authkey` VARCHAR(100) NULL DEFAULT NULL ,
  `roletype` VARCHAR(10) NULL DEFAULT NULL ,
  PRIMARY KEY (`empcode`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`inst_emp_perf`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`inst_emp_perf` (
  `INSTID` INT(11) NOT NULL ,
  `EMPCODE` VARCHAR(100) NOT NULL DEFAULT '' ,
  `BUSINESSVALUE` INT(10) NULL DEFAULT NULL ,
  PRIMARY KEY (`INSTID`, `EMPCODE`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`logtable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`logtable` (
  `id` INT(11) NOT NULL ,
  `type` VARCHAR(20) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `empcode` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `comcode` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  `ip` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  `date` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `uengine`.`notisetting`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`notisetting` (
  `id` INT(11) NOT NULL ,
  `userId` VARCHAR(20) NOT NULL ,
  `notiAdvice` INT(11) NULL DEFAULT '0' ,
  `modiUser` INT(11) NULL DEFAULT '0' ,
  `modiTopic` INT(11) NULL DEFAULT '0' ,
  `modiOrgan` INT(11) NULL DEFAULT '0' ,
  `writeBookmark` INT(11) NULL DEFAULT '0' ,
  `writeTopic` INT(11) NULL DEFAULT '0' ,
  `writeOrgan` INT(11) NULL DEFAULT '0' ,
  `writeInstance` INT(11) NULL DEFAULT '0' ,
  `inviteTopic` INT(11) NULL DEFAULT '0' ,
  `inviteOrgan` INT(11) NULL DEFAULT '0' ,
  `addFriend` INT(11) NULL DEFAULT '0' ,
  `beforehandNoti` INT(11) NULL DEFAULT '0' ,
  `notiTime` INT(11) NULL DEFAULT '0' ,
  `defaultNotiTime` VARCHAR(20) NULL DEFAULT NULL ,
  `notiEmail` INT(11) NULL DEFAULT '0' ,
  `checkLogin` INT(11) NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`parttable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`parttable` (
  `GLOBALCOM` VARCHAR(20) NULL DEFAULT NULL ,
  `PARTCODE` VARCHAR(20) NOT NULL ,
  `PARTNAME` VARCHAR(30) NULL DEFAULT NULL ,
  `PARENT_PARTCODE` VARCHAR(20) NULL DEFAULT NULL ,
  `ISDELETED` VARCHAR(1) NULL DEFAULT '0' ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `COMCODE` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`PARTCODE`) ,
  INDEX `FK3B63679B4506931C` (`COMCODE` ASC) ,
  CONSTRAINT `FK3B63679B4506931C`
    FOREIGN KEY (`COMCODE` )
    REFERENCES `uengine`.`comtable` (`COMCODE` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`processmap`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`processmap` (
  `mapId` VARCHAR(100) NOT NULL DEFAULT '' ,
  `defId` VARCHAR(50) NOT NULL DEFAULT '' ,
  `name` VARCHAR(50) NULL DEFAULT NULL ,
  `iconPath` VARCHAR(255) NULL DEFAULT NULL ,
  `color` VARCHAR(10) NULL DEFAULT NULL ,
  `comcode` VARCHAR(20) NULL DEFAULT NULL ,
  `no` INT(11) NULL DEFAULT NULL ,
  `cmphrase` CHAR(200) NULL DEFAULT NULL ,
  `cmtrgr` VARCHAR(20) NULL DEFAULT NULL ,
  `isScheduled` INT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`mapId`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`processtopicmapping`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`processtopicmapping` (
  `processName` VARCHAR(200) NOT NULL ,
  `processPath` VARCHAR(200) NOT NULL ,
  `topicId` VARCHAR(50) NOT NULL ,
  `type` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`topicId`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`recentitem`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`recentitem` (
  `empcode` VARCHAR(100) NOT NULL ,
  `itemType` VARCHAR(10) NOT NULL ,
  `itemId` VARCHAR(100) NOT NULL ,
  `updateDate` DATETIME NULL DEFAULT NULL ,
  `clickedCount` VARCHAR(4) NULL DEFAULT NULL ,
  PRIMARY KEY (`empcode`, `itemType`, `itemId`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`roletable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`roletable` (
  `ROLECODE` VARCHAR(20) NOT NULL ,
  `COMCODE` VARCHAR(20) NULL DEFAULT NULL ,
  `DESCR` VARCHAR(255) NULL DEFAULT NULL ,
  `ISDELETED` VARCHAR(1) NULL DEFAULT '0' ,
  `URL` VARCHAR(200) NULL DEFAULT NULL ,
  `THUMBNAIL` VARCHAR(200) NULL DEFAULT NULL ,
  `roleName` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ROLECODE`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `uengine`.`roleusertable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uengine`.`roleusertable` (
  `ROLECODE` VARCHAR(20) NOT NULL ,
  `EMPCODE` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`ROLECODE`, `EMPCODE`) ,
  INDEX `FK8CEEB30D7C65B1A` (`ROLECODE` ASC) ,
  CONSTRAINT `FK8CEEB30D7C65B1A`
    FOREIGN KEY (`ROLECODE` )
    REFERENCES `uengine`.`roletable` (`ROLECODE` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



USE `uengine` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- INSERT INTO `comtable` (`COMCODE`, `COMNAME`) VALUES ('1', 'uEngine');
-- INSERT INTO `emptable` (`empcode`, `EMPNAME`, `PASSWORD`, `ISADMIN`, `EMAIL`,`GLOBALCOM`, `ISDELETED`, `LOCALE`, `approved`, `guest`) VALUES ('1', 'Tester', 'test', '1', 'test@uengine.org', '1', '0', 'ko', '1', '0');



alter table bpm_worklist add column hasChild int;


alter table processmap modify column mapid varchar(200); -- generation logic of map id should be changed to reduce the string size
alter table processmap modify column defid varchar(200); -- generation logic of map id should be changed to reduce the string size