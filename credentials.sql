CREATE TABLE credentials(
user_id serial PRIMARY KEY,
username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL,
first_name CHAR(50) NOT NULL,
last_name CHAR(50) NOT NULL);