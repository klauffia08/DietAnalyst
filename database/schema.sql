-- Dokumentacyjny schemat MySQL zgodny z encjami JPA.
-- Aplikacja tworzy/aktualizuje tabele automatycznie przez Hibernate (ddl-auto=update).

CREATE DATABASE IF NOT EXISTS dietanalyst CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci;
USE dietanalyst;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  age INT NULL,
  goal VARCHAR(160) NULL,
  activity VARCHAR(100) NULL,
  calories_target INT NOT NULL DEFAULT 2000,
  notes TEXT NULL,
  assigned_dietitian_id BIGINT NULL,
  CONSTRAINT fk_user_dietitian FOREIGN KEY (assigned_dietitian_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(140) NOT NULL UNIQUE,
  category VARCHAR(100),
  kcal DOUBLE NOT NULL,
  protein DOUBLE NOT NULL,
  carbs DOUBLE NOT NULL,
  fat DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS meals (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meal_date DATE NOT NULL,
  meal_type VARCHAR(30) NOT NULL,
  grams DOUBLE NOT NULL,
  kcal DOUBLE NOT NULL,
  protein DOUBLE NOT NULL,
  carbs DOUBLE NOT NULL,
  fat DOUBLE NOT NULL,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  CONSTRAINT fk_meal_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_meal_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS diet_plans (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(180) NOT NULL,
  status VARCHAR(40) NOT NULL,
  created_at DATETIME NOT NULL,
  content TEXT NOT NULL,
  dietitian_note TEXT NULL,
  user_id BIGINT NOT NULL,
  reviewed_by_id BIGINT NULL,
  CONSTRAINT fk_plan_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_plan_reviewer FOREIGN KEY (reviewed_by_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_time DATETIME NOT NULL,
  actor_email VARCHAR(160),
  action VARCHAR(255) NOT NULL,
  details TEXT NULL
);
