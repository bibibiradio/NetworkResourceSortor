CREATE TABLE `viewer_score` (
  `viewer_id` BIGINT NOT NULL AUTO_INCREMENT,
  `viewer_name` VARCHAR(256) NOT NULL,
  `score` BIGINT NOT NULL,
  `viewer_type` VARCHAR(64) NULL,
  `view_cnt` BIGINT NULL,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`viewer_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'View Collection';

ALTER TABLE `viewer_score` 
ADD COLUMN `view_resource_num` BIGINT NULL AFTER `view_cnt`;

ALTER TABLE `viewer_score` 
CHANGE COLUMN `viewer_type` `viewer_type` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
CHANGE COLUMN `view_cnt` `view_cnt` BIGINT(20) NOT NULL ,
CHANGE COLUMN `view_resource_num` `view_resource_num` BIGINT(20) NOT NULL ,
ADD COLUMN `viewer_category` VARCHAR(128) NULL AFTER `viewer_type`;

ALTER TABLE `viewer_score` 
CHANGE COLUMN `viewer_category` `viewer_category` VARCHAR(128) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ;