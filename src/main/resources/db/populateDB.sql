DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
  (100000, '2020-02-23 10:00', 'завтрак', 1000),
  (100000, '2020-02-23 14:00', 'обед', 500),
  (100000, '2020-02-23 18:00', 'ужин', 500),
  (100000, '2020-02-23 20:00', 'Еда на граничное значение', 100),
  (100000, '2020-02-24 10:00', 'завтрак', 1000),
  (100000, '2020-02-24 13:00', 'обед', 300),
  (100000, '2020-02-24 18:00', 'ужин', 700),
  (100001, '2020-02-24 10:00', 'завтрак admin', 1000),
  (100001, '2020-02-24 13:00', 'обед admin', 300),
  (100001, '2020-02-24 18:00', 'ужин admin', 700);
