
-- MySQL v5.x

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


create database IF NOT EXISTS mahmood;

CREATE TABLE IF NOT EXISTS `question_deatil` (
  `id` bigint(20) NOT NULL auto_increment,
  `quest` varchar(100) collate utf8_unicode_ci default NULL,
  `QA` varchar(100) collate utf8_unicode_ci default NULL,
  `QB` varchar(100) collate utf8_unicode_ci default NULL,
  `QC` varchar(100) collate utf8_unicode_ci default NULL,
  `QD` varchar(100) collate utf8_unicode_ci default NULL,
  `correctAns` varchar(100) collate utf8_unicode_ci default NULL,
  `image` varbinary(20000) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;


truncate table `question_deatil` ;
