ALTER TABLE `resources` 
CHANGE COLUMN `r_type` `r_type` VARCHAR(64) NOT NULL ;
ALTER TABLE `resources` 
ADD COLUMN `r_category` VARCHAR(64) NOT NULL AFTER `r_source`;