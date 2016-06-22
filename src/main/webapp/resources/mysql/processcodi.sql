set @old_unique_checks=@@unique_checks, unique_checks=0;
set @old_foreign_key_checks=@@foreign_key_checks, foreign_key_checks=0;
set @old_sql_mode=@@sql_mode, sql_mode='traditional,allow_invalid_dates';

create schema if not exists `uengine` default character set utf8 collate utf8_unicode_ci ;
use `uengine` ;

-- -----------------------------------------------------
-- table `uengine`.`bpm_knol`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_knol` (
  `id` varchar(20) not null ,
  `name` varchar(1000) null default null ,
  `linkedinstid` int(11) null default null ,
  `parentid` varchar(20) not null ,
  `no` int(11) null default null ,
  `authorid` varchar(100) null default null ,
  `type` char(10) null default null ,
  `vistype` char(10) null default null ,
  `conntype` char(20) null default null ,
  `url` varchar(200) null default null ,
  `thumbnail` varchar(200) null default null ,
  `secuopt` char(1) null default '0' ,
  `companyid` varchar(20) null default null ,
  `refid` varchar(20) null default null ,
  `budget` int(11) null default null ,
  `effort` int(11) null default null ,
  `benefit` int(11) null default null ,
  `penalty` int(11) null default null ,
  `startdate` date null default null ,
  `enddate` date null default null ,
  `description` varchar(1000) null default null ,
  `ext` varchar(3000) null default null ,
  `isreleased` tinyint(1) null default null ,
  `isdistributed` tinyint(1) null default null ,
  `projectalias` varchar(1000) null default null ,
  index `parentid` (`parentid` asc, `no` asc) )
engine = myisam
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_noti`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_noti` (
  `notiid` mediumtext null default null ,
  `userid` char(100) null default null ,
  `actorid` char(100) null default null ,
  `instid` int(11) null default null ,
  `actabstract` varchar(300) null default null ,
  `taskid` int(11) null default null ,
  `type` int(11) null default null ,
  `inputdate` datetime null default null ,
  `confirm` int(11) null default null )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_procinst`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_procinst` (
  `instid` int(11) not null ,
  `defverid` varchar(100) null default null ,
  `defid` varchar(100) null default null ,
  `defname` varchar(255) null default null ,
  `defpath` varchar(255) null default null ,
  `defmoddate` datetime null default null ,
  `starteddate` datetime null default null ,
  `finisheddate` datetime null default null ,
  `duedate` datetime null default null ,
  `status` varchar(20) null default null ,
  `info` varchar(255) null default null ,
  `name` varchar(255) null default null ,
  `isdeleted` int(11) null default '0' ,
  `isadhoc` int(11) null default '0' ,
  `isarchive` int(11) null default '0' ,
  `issubprocess` int(11) null default '0' ,
  `iseventhandler` int(11) null default '0' ,
  `rootinstid` int(11) null default null ,
  `maininstid` int(11) null default null ,
  `maindefverid` int(11) null default null ,
  `mainacttrctag` varchar(20) null default null ,
  `mainexecscope` varchar(20) null default null ,
  `abstrcpath` varchar(200) null default null ,
  `dontreturn` int(11) null default null ,
  `moddate` datetime null default null ,
  `ext1` varchar(100) null default null ,
  `initep` varchar(100) null default null ,
  `initrsnm` varchar(100) null default null ,
  `currep` varchar(100) null default null ,
  `currrsnm` varchar(100) null default null ,
  `strategyid` int(11) null default null ,
  `prevcurrep` varchar(100) null default null ,
  `prevcurrrsnm` varchar(100) null default null ,
  `starcnt` int(11) null default null ,
  `starrsnm` varchar(100) null default null ,
  `starflag` int(11) null default null ,
  `abstractinst` text null default null ,
  `currtrctag` int(11) null default null ,
  `prevtrctag` int(11) null default null ,
  `initcomcd` varchar(20) null default null ,
  `secuopt` char(1) null default '0' ,
  `lastcmnt` varchar(200) null default null ,
  `initcmpl` int(1) null default null ,
  `topicid` char(20) null default null ,
  `progress` varchar(10) null default null ,
  `lastcmnt2` varchar(200) null default null ,
  `benefit` int(6) null default null ,
  `penalty` int(6) null default null ,
  `effort` int(6) null default null ,
  `lastcmntep` varchar(100) null default null ,
  `lastcmntrsnm` varchar(100) null default null ,
  `lastcmnt2ep` varchar(100) null default null ,
  `lastcmnt2rsnm` varchar(100) null default null ,
  `isdocument` int(11) null default '0' ,
  `isfileadded` int(11) null default '0' ,
  `lastcmnttaskid` int(11) null default null ,
  `lastcmnt2taskid` int(11) null default null ,
  primary key (`instid`) ,
  index `fkf57f151c46f158c1` (`defid` asc) ,
  index `fkf57f151c78eb68e6` (`rootinstid` asc) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_procvar`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_procvar` (
  `varid` int(11) not null auto_increment ,
  `instid` int(11) null default null ,
  `datatype` int(11) null default null ,
  `valuestring` varchar(4000) null default null ,
  `valuelong` int(11) null default null ,
  `valueboolean` int(11) null default null ,
  `valuedate` datetime null default null ,
  `varindex` int(11) null default null ,
  `trctag` varchar(20) null default null ,
  `isproperty` int(11) null default null ,
  `moddate` datetime null default null ,
  `keyname` varchar(100) null default null ,
  `keystring` varchar(200) null default null ,
  `filecontent` text null default null ,
  `htmlfilepath` varchar(255) null default null ,
  primary key (`varid`) )
engine = innodb
auto_increment = 17678
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_roledef`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_roledef` (
  `roledefid` varchar(100) null default null ,
  `defid` varchar(50) null default null ,
  `rolename` char(20) null default null ,
  `mappeduserid` varchar(50) null default null ,
  `comcode` varchar(50) null default null ,
  `mappedrolecode` varchar(100) null default null ,
  `roledeftype` varchar(10) null default null )
engine = myisam
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_rolemapping`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_rolemapping` (
  `rolemappingid` int(11) not null auto_increment ,
  `instid` int(11) null default null ,
  `rootinstid` int(11) null default null ,
  `rolename` varchar(255) null default null ,
  `endpoint` varchar(255) null default null ,
  `value` varchar(4000) null default null ,
  `resname` varchar(200) null default null ,
  `assigntype` int(11) null default null ,
  `assignparam1` varchar(100) null default null ,
  `dispatchoption` int(11) null default null ,
  `dispatchparam1` varchar(100) null default null ,
  `groupid` varchar(30) null default null ,
  `isreferencer` int(1) null default '0' ,
  primary key (`rolemappingid`) )
engine = innodb
auto_increment = 2519
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_seq`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_seq` (
  `seq` int(11) null default null ,
  `tbname` varchar(255) null default null ,
  `description` varchar(255) null default null ,
  `moddate` datetime null default null )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_topicmapping`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_topicmapping` (
  `topicmappingid` int(11) not null ,
  `topicid` varchar(20) not null ,
  `userid` varchar(255) null default null ,
  `username` varchar(1000) null default null ,
  `assigntype` int(11) null default '0' ,
  primary key (`topicmappingid`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`bpm_worklist`
-- -----------------------------------------------------
create  table if not exists `uengine`.`bpm_worklist` (
  `taskid` int(11) not null ,
  `title` varchar(3000) null default null ,
  `description` varchar(500) null default null ,
  `endpoint` varchar(200) null default null ,
  `status` varchar(100) null default '' ,
  `priority` int(11) null default null ,
  `startdate` datetime null default null ,
  `enddate` datetime null default null ,
  `duedate` datetime null default null ,
  `instid` int(11) null default null ,
  `defid` varchar(100) null default null ,
  `defname` varchar(200) null default null ,
  `trctag` varchar(100) null default null ,
  `tool` varchar(200) null default null ,
  `parameter` varchar(4000) null default null ,
  `groupid` int(11) null default null ,
  `groupname` int(11) null default null ,
  `ext1` varchar(200) null default null ,
  `ext2` varchar(200) null default null ,
  `ext3` varchar(200) null default null ,
  `isurgent` int(11) null default null ,
  `hasattachfile` int(11) null default null ,
  `hascomment` int(11) null default null ,
  `documentcategory` varchar(200) null default null ,
  `isdeleted` int(11) null default '0' ,
  `rootinstid` int(11) null default null ,
  `dispatchoption` int(11) null default null ,
  `dispatchparam1` varchar(100) null default null ,
  `rolename` varchar(100) null default null ,
  `resname` varchar(100) null default null ,
  `refrolename` varchar(100) null default null ,
  `execscope` varchar(5) null default null ,
  `savedate` datetime null default null ,
  `abstract` text null default null ,
  `type` char(10) null default null ,
  `content` mediumtext null default null ,
  `extfile` varchar(200) null default null ,
  `prevver` int(11) null default null ,
  `nextver` int(11) null default null ,
  `ext4` varchar(200) null default null ,
  `ext5` varchar(200) null default null ,
  `ext6` varchar(200) null default null ,
  `ext7` varchar(200) null default null ,
  `ext8` varchar(200) null default null ,
  `ext9` varchar(200) null default null ,
  `ext10` varchar(200) null default null ,
  `prttskid` int(11) null default null ,
  `majorver` int(5) null default null ,
  `minorver` int(5) null default null ,
  `grptaskid` int(11) null default null ,
  `folderid` varchar(100) null default null ,
  `foldername` varchar(100) null default null ,
  primary key (`taskid`) ,
  index `fk33852daff5139497` (`defid` asc) ,
  index `fk33852dafe10386fc` (`endpoint` asc) ,
  index `fk33852daf63959984` (`instid` asc) ,
  index `fk33852daf78eb68e6` (`rootinstid` asc) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`comtable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`comtable` (
  `comcode` varchar(20) not null default '' ,
  `comname` varchar(100) null default null ,
  `description` varchar(255) null default null ,
  `isdeleted` varchar(1) null default '0' ,
  `repmail` varchar(100) null default null ,
  `repmlhst` varchar(100) null default null ,
  `repmlpwd` varchar(100) null default null ,
  `alias` varchar(100) null default null ,
  `logopath` varchar(100) null default null ,
  `killbillAccount` varchar(100) DEFAULT NULL,
  `killbillSubscription` varchar(100) DEFAULT NULL,
  primary key (`comcode`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`contact`
-- -----------------------------------------------------
create  table if not exists `uengine`.`contact` (
  `userid` varchar(100) null default null ,
  `friendid` varchar(100) null default null ,
  `friendname` varchar(20) null default null ,
  `network` char(10) null default null ,
  `mood` varchar(100) null default null )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`emp_prop_table`
-- -----------------------------------------------------
create  table if not exists `uengine`.`emp_prop_table` (
  `propid` int(11) not null ,
  `comcode` varchar(20) not null ,
  `empcode` varchar(20) not null ,
  `propkey` varchar(40) not null ,
  `propvalue` varchar(100) not null ,
  primary key (`propid`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`emptable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`emptable` (
  `empcode` varchar(100) not null default '' ,
  `empname` varchar(100) null default null ,
  `password` varchar(20) null default null ,
  `isadmin` int(11) null default null ,
  `jikname` varchar(100) null default null ,
  `email` varchar(100) null default null ,
  `partcode` varchar(20) null default null ,
  `globalcom` varchar(20) null default null ,
  `isdeleted` varchar(1) null default '0' ,
  `locale` varchar(10) null default null ,
  `nateon` varchar(50) null default null ,
  `msn` varchar(50) null default null ,
  `mobilecmp` varchar(50) null default null ,
  `mobileno` varchar(50) null default null ,
  `facebookid` varchar(100) null default null ,
  `facebookpassword` varchar(100) null default null ,
  `preferux` char(10) null default null ,
  `prefermob` char(10) null default null ,
  `mood` varchar(100) null default null ,
  `appkey` varchar(100) null default null ,
  `approved` int(1) null default null ,
  `guest` int(1) null default null ,
  `inviteuser` varchar(100) null default null ,
  `mailnoti` int(1) null default '0' ,
  `notiemail` int(1) null default '0' ,
  `authkey` varchar(100) null default null ,
  `roletype` varchar(10) null default null ,
  primary key (`empcode`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`inst_emp_perf`
-- -----------------------------------------------------
create  table if not exists `uengine`.`inst_emp_perf` (
  `instid` int(11) not null ,
  `empcode` varchar(100) not null default '' ,
  `businessvalue` int(10) null default null ,
  primary key (`instid`, `empcode`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`logtable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`logtable` (
  `id` int(11) not null ,
  `type` varchar(20) character set 'utf8' collate 'utf8_unicode_ci' not null ,
  `empcode` varchar(100) character set 'utf8' collate 'utf8_unicode_ci' not null ,
  `comcode` varchar(100) character set 'utf8' collate 'utf8_unicode_ci' null default null ,
  `ip` varchar(50) character set 'utf8' collate 'utf8_unicode_ci' null default null ,
  `date` varchar(50) character set 'utf8' collate 'utf8_unicode_ci' null default null ,
  primary key (`id`) )
engine = innodb
default character set = utf8
collate = utf8_unicode_ci;


-- -----------------------------------------------------
-- table `uengine`.`notisetting`
-- -----------------------------------------------------
create  table if not exists `uengine`.`notisetting` (
  `id` int(11) not null ,
  `userid` varchar(20) not null ,
  `notiadvice` int(11) null default '0' ,
  `modiuser` int(11) null default '0' ,
  `moditopic` int(11) null default '0' ,
  `modiorgan` int(11) null default '0' ,
  `writebookmark` int(11) null default '0' ,
  `writetopic` int(11) null default '0' ,
  `writeorgan` int(11) null default '0' ,
  `writeinstance` int(11) null default '0' ,
  `invitetopic` int(11) null default '0' ,
  `inviteorgan` int(11) null default '0' ,
  `addfriend` int(11) null default '0' ,
  `beforehandnoti` int(11) null default '0' ,
  `notitime` int(11) null default '0' ,
  `defaultnotitime` varchar(20) null default null ,
  `notiemail` int(11) null default '0' ,
  `checklogin` int(11) null default '0' ,
  primary key (`id`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`parttable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`parttable` (
  `globalcom` varchar(20) null default null ,
  `partcode` varchar(20) not null ,
  `partname` varchar(30) null default null ,
  `parent_partcode` varchar(20) null default null ,
  `isdeleted` varchar(1) null default '0' ,
  `description` varchar(255) null default null ,
  `comcode` varchar(255) null default null ,
  primary key (`partcode`) ,
  index `fk3b63679b4506931c` (`comcode` asc) ,
  constraint `fk3b63679b4506931c`
    foreign key (`comcode` )
    references `uengine`.`comtable` (`comcode` ))
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`processmap`
-- -----------------------------------------------------
create  table if not exists `uengine`.`processmap` (
  `mapid` varchar(100) not null default '' ,
  `defid` varchar(50) not null default '' ,
  `name` varchar(50) null default null ,
  `iconpath` varchar(255) null default null ,
  `color` varchar(10) null default null ,
  `comcode` varchar(20) null default null ,
  `no` int(11) null default null ,
  `cmphrase` char(200) null default null ,
  `cmtrgr` varchar(20) null default null ,
  `isscheduled` int(1) null default '0' ,
  primary key (`mapid`) )
engine = myisam
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`processtopicmapping`
-- -----------------------------------------------------
create  table if not exists `uengine`.`processtopicmapping` (
  `processname` varchar(200) not null ,
  `processpath` varchar(200) not null ,
  `topicid` varchar(50) not null ,
  `type` varchar(20) not null ,
  primary key (`topicid`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`recentitem`
-- -----------------------------------------------------
create  table if not exists `uengine`.`recentitem` (
  `empcode` varchar(100) not null ,
  `itemtype` varchar(10) not null ,
  `itemid` varchar(100) not null ,
  `updatedate` datetime null default null ,
  `clickedcount` varchar(4) null default null ,
  primary key (`empcode`, `itemtype`, `itemid`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`roletable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`roletable` (
  `rolecode` varchar(20) not null ,
  `comcode` varchar(20) null default null ,
  `descr` varchar(255) null default null ,
  `isdeleted` varchar(1) null default '0' ,
  `url` varchar(200) null default null ,
  `thumbnail` varchar(200) null default null ,
  `rolename` varchar(100) null default null ,
  primary key (`rolecode`) )
engine = innodb
default character set = utf8;


-- -----------------------------------------------------
-- table `uengine`.`roleusertable`
-- -----------------------------------------------------
create  table if not exists `uengine`.`roleusertable` (
  `rolecode` varchar(20) not null ,
  `empcode` varchar(20) not null ,
  primary key (`rolecode`, `empcode`) ,
  index `fk8ceeb30d7c65b1a` (`rolecode` asc) ,
  constraint `fk8ceeb30d7c65b1a`
    foreign key (`rolecode` )
    references `uengine`.`roletable` (`rolecode` ))
engine = innodb
default character set = utf8;



use `uengine` ;


set sql_mode=@old_sql_mode;
set foreign_key_checks=@old_foreign_key_checks;
set unique_checks=@old_unique_checks;

-- insert into `comtable` (`comcode`, `comname`) values ('1', 'uengine');
-- insert into `emptable` (`empcode`, `empname`, `password`, `isadmin`, `email`,`globalcom`, `isdeleted`, `locale`, `approved`, `guest`) values ('1', 'tester', 'test', '1', 'test@uengine.org', '1', '0', 'ko', '1', '0');



alter table bpm_worklist add column haschild int;


alter table processmap modify column mapid varchar(200); -- generation logic of map id should be changed to reduce the string size
alter table processmap modify column defid varchar(200); -- generation logic of map id should be changed to reduce the string size

alter table emptable modify column password varchar(100);