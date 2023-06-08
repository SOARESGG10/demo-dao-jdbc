<div align="center">
    <img src="https://miro.medium.com/v2/resize:fit:720/format:webp/1*PpTSlj9PSgB4VPEx4zEReQ.png" width="300">
</div>

```sql
CREATE DATABASE cursojdbc;

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` smallint NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `seller`;
CREATE TABLE `seller` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  `email` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `salary` decimal(8,2) NOT NULL,
  `department_id` smallint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
