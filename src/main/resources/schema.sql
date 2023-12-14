CREATE TABLE `employees` (
  `id` bigint(20) NOT NULL,
  `employee_id` varchar(40) DEFAULT NULL,
  `seat_id` varchar(40) DEFAULT NULL,
  `work_type` enum('OFFICE','REMOTE','VACATION') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ovvvp79dq21byf7svnuekb6iw` (`employee_id`),
  UNIQUE KEY `UK_f4hxjh29y6x0n36jd2uqt8t96` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `seats` (
  `id` bigint(20) NOT NULL,
  `number` int(11) NOT NULL,
  `seat_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jx4fqndhprb1xnm6ttta5rpa3` (`number`),
  UNIQUE KEY `UK_6p58mrhqk0wui2bhnvtemx4yy` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;