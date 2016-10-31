CREATE TABLE `post_config` (
  `post_conf_id` BIGINT NOT NULL AUTO_INCREMENT,
  `u_id` BIGINT NOT NULL,
  `post_type` TINYINT NOT NULL,
  `post_site` INT NOT NULL DEFAULT 0,
  `post_way` TINYINT NOT NULL DEFAULT 0,
  `post_address` VARCHAR(1024) NOT NULL,
  `post_feq` BIGINT NOT NULL,
  `last_post` DATETIME NOT NULL,
  `post_num` INT NOT NULL DEFAULT 0,
  `post_category` VARCHAR(64) NOT NULL DEFAULT '',
  `post_tags` VARCHAR(64) NOT NULL DEFAULT '',
  `post_limit_day` INT NOT NULL DEFAULT 7,
  `item_gmt_create` DATETIME NOT NULL,
  `item_gmt_update` DATETIME NOT NULL,
  PRIMARY KEY (`post_conf_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Post Config';