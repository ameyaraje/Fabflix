CREATE TABLE IF NOT EXISTS employees (
  email    VARCHAR(50) PRIMARY KEY,
  password VARCHAR(20) NOT NULL,
  fullname VARCHAR(100)
);

INSERT INTO employees (email, password, fullname)
SELECT "classta@course.edu", "classta", "TA CS122B"
FROM dual WHERE NOT EXISTS (
  SELECT 1 FROM employees
  WHERE email = "classta@course.edu"
    AND password = "classta"
    AND fullname = "TA CS122B"
);

INSERT INTO employees (email, password, fullname)
SELECT "root@example.com", "root", "root"
FROM dual WHERE NOT EXISTS (
  SELECT 1 FROM employees
  WHERE email = "root@example.com"
    AND password = "root"
    AND fullname = "root"
);