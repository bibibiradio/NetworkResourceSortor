#resource_id 资源id
#resource_create_time 资源创建时间
#resource_type 资源类型（图片，音乐，视频）
#resource_source 资源来源（acfun,bili,youku,tudou）
#resource_author 资源作者
#resource_duration 资源时长（可选）
#resource_pv 资源点击量
#resource_comment 资源评论量
#resource_danmu 资源弹幕数（可选）
#resource_collect 资源收藏量
#resource_tags 资源tag集合
#resource_coin 资源硬币数（可选）
#resource_url 资源的URL
#resource_score 资源的分数
#resource_valid 资源是否有效
#resource_other_detail 其他
#resource_json_content 所有可获取数据json格式
#resource_insert_time 该数据项插入时间

CREATE TABLE `resources` (
  `r_id` BIGINT NOT NULL AUTO_INCREMENT,
  `r_type` INT NOT NULL,
  `r_source` INT NOT NULL,
  `author` VARCHAR(128) NULL DEFAULT '',
  `r_duration` BIGINT NULL DEFAULT -1,
  `r_pv` BIGINT NULL DEFAULT -1,
  `r_comment` BIGINT NULL DEFAULT -1,
  `r_danmu` BIGINT NULL DEFAULT -1,
  `r_collect` BIGINT NULL DEFAULT -1,
  `r_tags` VARCHAR(1024) NULL DEFAULT '',
  `r_coin` BIGINT NULL DEFAULT -1,
  `is_valid` INT NOT NULL DEFAULT 0,
  `other_detail` VARCHAR(256) NULL DEFAULT '',
  `r_gmt_create` DATETIME NOT NULL,
  `item_gmt_insert` DATETIME NOT NULL,
  PRIMARY KEY (`r_id`),
  UNIQUE INDEX `r_id_UNIQUE` (`r_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Resource Collection';

ALTER TABLE `resources` 
ADD COLUMN `r_url` VARCHAR(1024) NOT NULL AFTER `other_detail`,
ADD COLUMN `r_show_url` VARCHAR(1024) NULL DEFAULT '' AFTER `r_url`;

ALTER TABLE `resources` 
ADD COLUMN `r_title` VARCHAR(512) NULL DEFAULT '' AFTER `r_source`,
ADD COLUMN `r_score` BIGINT NULL DEFAULT -1 AFTER `r_show_url`;

ALTER TABLE `resources` 
COLLATE = utf8_general_ci ;

