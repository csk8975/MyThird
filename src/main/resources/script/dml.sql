SELECT a.`code`, a.`level`,(a.check_num - a.unqualified_num) / a.check_num 
    FROM check_item_result_statis a 
    WHERE a.report_id = 4;

CREATE TABLE `check_item_report_result` (
    `report_id` int(11) NOT NULL DEFAULT '0',
    `score` double(4,2) DEFAULT NULL,
    `report_level` varchar(64) DEFAULT NULL,
    PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
