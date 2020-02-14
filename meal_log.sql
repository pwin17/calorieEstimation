CREATE TABLE meal_log(
	meal_id serial,
	user_id integer NOT NULL,
	food CHAR(50) NOT NULL,
	calorie int NOT NULL,
	time_eaten TIME,
	date_eat DATE
);