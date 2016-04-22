CREATE TABLE `resources_score` (
  `r_id` BIGINT NOT NULL,
  `r_type` VARCHAR(64) NOT NULL,
  `r_category` VARCHAR(64) NOT NULL,
  `score` BIGINT NOT NULL,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`r_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'resources_score';