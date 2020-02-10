CREATE DATABASE cars_rental;

USE cars_rental;

create table fuel_types
(
    id            int auto_increment
        primary key,
    name        varchar(10) not null
);

create table transmissions
(
    id            int auto_increment
        primary key,
    name        varchar(10) not null
);

create table cars
(
    id            int auto_increment
        primary key,
    number        varchar(10) not null unique,
    model         varchar(50) not null ,
    mileage       int         not null,
    year_of_issue int         not null,
    price_hour    int         not null,
    transmission_id  int not null,
    fuel_type_id int not null,
	available       tinyint(1) not null,
    FOREIGN KEY (transmission_id) REFERENCES transmissions (id),
    FOREIGN KEY (fuel_type_id) REFERENCES transmissions (id)
);


create table accounts
(
    id int auto_increment primary key,
    login varchar(16) not null unique,
    password varchar(50) not null,
    email varchar(60) not null unique,
    role boolean not null, -- true - администратор, false - клиент
    full_name varchar(30) null,
    birthday_year int null
);

CREATE TABLE orders (
    id int auto_increment not null,
    order_date DATETIME NOT NULL,
    rental_period int not null,
    return_date DATETIME NOT NULL,
    client_id int not null,
    car_id int not null,
    passport_data varchar(40) not null ,
    price double not null,
	closed boolean not null,
    PRIMARY KEY (id),
    foreign key (client_id) REFERENCES accounts (id),
    FOREIGN KEY (car_id) REFERENCES cars (id)
);

CREATE TABLE return_requests
(
    id int auto_increment not null primary key,
	return_date DATETIME not null,
    order_id int not null,
    description varchar(200),
    return_mark boolean,
    repair_cost double not null,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- Storage procedure for cars

DELIMITER //
CREATE PROCEDURE add_car(
    var_number VARCHAR(10),
    var_model VARCHAR(50),
    var_mileage INT,
    var_year_of_issue INT,
    var_price_hour INT,
    var_transmission_id INT,
    var_fuel_type_id INT
)
BEGIN
    INSERT INTO cars (number, model, mileage, year_of_issue, price_hour, transmission_id, fuel_type_id, available)
    VALUES (var_number, var_model, var_mileage, var_year_of_issue, var_price_hour, var_transmission_id, var_fuel_type_id, true);
END //

CREATE PROCEDURE edit_car(
    var_id INT,
    var_number VARCHAR(10),
    var_model VARCHAR(50),
    var_mileage INT,
    var_year_of_issue INT,
    var_price_hour INT,
    var_transmission_id INT,
    var_fuel_type_id INT
)
BEGIN
    UPDATE cars SET
        number = var_number,
        model = var_model,
        mileage = var_mileage,
        year_of_issue = var_year_of_issue,
        price_hour = var_price_hour,
        transmission_id = var_transmission_id,
        fuel_type_id = var_fuel_type_id
    WHERE id = var_id;
END //

CREATE PROCEDURE delete_car(
    var_id INT
)
BEGIN
    DELETE FROM cars WHERE id = var_id;
END //

CREATE PROCEDURE read_all_cars()
BEGIN
    SELECT * FROM cars;
END //

CREATE PROCEDURE read_car(
    var_id INT
)
BEGIN
    SELECT * FROM cars WHERE id = var_id;
END //

-- STORAGE PROCEDURE FROM ACCOUNTS

DELIMITER //

CREATE PROCEDURE add_client(
    var_login VARCHAR(16),
    var_password VARCHAR(50),
    var_email VARCHAR(60),
    var_full_name VARCHAR(30),
    var_birthday_year INT
)
BEGIN
    INSERT INTO accounts (login, password, email, role, full_name, birthday_year)
    VALUES (var_login, var_password, var_email, false, var_full_name, var_birthday_year);
END //

CREATE PROCEDURE add_administrator(
    var_login VARCHAR(16),
    var_password VARCHAR(50),
    var_email VARCHAR(60),
    var_full_name VARCHAR(30)
)
BEGIN
    INSERT INTO accounts (login, password, email, role, full_name)
    VALUES  (var_login, var_password, var_email, true, var_full_name);
END //

CREATE PROCEDURE add_account(
    var_login VARCHAR(16),
    var_password VARCHAR(50),
    var_email VARCHAR(60),
    var_role BOOLEAN,
    var_full_name VARCHAR(30),
    var_birthday_year INT
)
BEGIN
    INSERT INTO accounts (login, password, email, role, full_name, birthday_year)
    VALUES  (var_login, var_password, var_email, var_role, var_full_name, var_birthday_year);
end //

CREATE PROCEDURE edit_account(
    var_id INT,
    var_login VARCHAR(16),
    var_password VARCHAR(50),
    var_full_name VARCHAR(30)
)
BEGIN
    UPDATE accounts SET
        login = var_login,
        password = var_password,
        full_name = var_full_name
    WHERE id = var_id;
END //

CREATE PROCEDURE edit_client(
    var_id INT,
    var_login VARCHAR(16),
    var_password VARCHAR(50),
	var_email VARCHAR(50),
    var_full_name VARCHAR(30),
    var_birthday_year INT
)
BEGIN
    UPDATE accounts SET
                        login = var_login,
                        password = var_password,
						email = var_email,
                        full_name = var_full_name,
                        birthday_year = var_birthday_year
    WHERE id = var_id;
END //

CREATE PROCEDURE delete_account(
    var_id INT
)
BEGIN
    DELETE FROM accounts WHERE id = var_id;
END //

CREATE PROCEDURE read_account(
    var_id INT
)
BEGIN
    SELECT * FROM accounts WHERE id = var_id;
END //

CREATE PROCEDURE read_all_accounts()
BEGIN
    SELECT * FROM accounts;
END //

CREATE PROCEDURE read_all_clients()
BEGIN
    SELECT * FROM accounts WHERE role = false;
END //

CREATE PROCEDURE log_in(
    var_login VARCHAR(16),
    var_password VARCHAR(50)
)
BEGIN
    SELECT * FROM accounts WHERE login = var_login AND password = var_password;
END //

CREATE PROCEDURE check_login(
    var_login VARCHAR(16)
)
BEGIN
    SELECT COUNT(*) FROM  accounts WHERE login = var_login;
END //

-- STORAGE PROCEDURE FROM Orders

CREATE PROCEDURE add_order(
    var_order_date DATETIME,
    var_rental_period INT,
    var_client_id INT,
    var_car_id INT,
    var_passport_data VARCHAR(40),
    var_price DOUBLE
)
BEGIN
    IF ((SELECT available FROM cars WHERE id = var_car_id) = true) THEN
        INSERT INTO orders(order_date, rental_period, return_date, client_id, car_id, passport_data, price, closed)
        VALUES (var_order_date, var_rental_period, DATE_ADD(var_order_date, INTERVAL var_rental_period HOUR), var_client_id, var_car_id, var_passport_data, var_price, false);
        UPDATE cars SET available = false WHERE id = var_car_id;
    END IF;
END //

CREATE PROCEDURE edit_order(
    var_order_id INT,
    var_order_date DATETIME,
    var_rental_period INT,
    var_client_id INT,
    var_car_id INT,
    var_passport_data VARCHAR(40),
    var_price DOUBLE
)
BEGIN
    UPDATE orders SET
      order_date = var_order_date,
      rental_period = var_rental_period,
      return_date = DATE_ADD(var_order_date, INTERVAL var_rental_period HOUR),
      client_id = var_client_id,
      car_id = var_car_id,
      passport_data = var_passport_data,
      price = var_price
    WHERE id = var_order_id;
END //

CREATE PROCEDURE delete_order(
    var_order_id INT
)
BEGIN
    DELETE FROM orders WHERE id = var_order_id;
END //

CREATE PROCEDURE read_all_orders()
BEGIN
    SELECT * FROM orders;
END //

CREATE PROCEDURE read_order(
    var_order_id INT
)
BEGIN
    SELECT * FROM orders WHERE id = var_order_id;
END //

CREATE PROCEDURE close_order(
    var_order_id INT,
    var_return_date DATETIME
)
BEGIN
    UPDATE orders SET closed = true
    WHERE id = var_order_id;
    CALL add_return_request(var_return_date, var_order_id, null, false, 0);
END //

-- STORAGE PROCEDURE FROM return_requests

CREATE PROCEDURE add_return_request(
	var_return_date DATETIME,
    var_order_id INT,
    var_description VARCHAR(200),
    var_return_mark BOOLEAN,
    var_repair_cost DOUBLE
)
BEGIN
    INSERT INTO return_requests(return_date, order_id, description, return_mark, repair_cost)
    VALUES (var_return_date, var_order_id, var_description, var_return_mark, var_repair_cost);
END //

CREATE PROCEDURE edit_return_request(
    var_return_req_id INT,
    var_return_date DATETIME,
    var_order_id INT,
    var_description VARCHAR(200),
    var_return_mark BOOLEAN,
    var_repair_cost DOUBLE
)
BEGIN
    UPDATE return_requests SET
       return_date = var_return_date,
       order_id = var_order_id,
       description = var_description,
       return_mark = var_return_mark,
       repair_cost = var_repair_cost
    WHERE id = var_return_req_id;
    IF (var_return_mark = true) THEN
        UPDATE cars SET available = true
        WHERE id = (SELECT car_id FROM orders WHERE id = var_order_id);
    END IF;
END //

CREATE PROCEDURE delete_return_request(
    var_return_req_id INT
)
BEGIN
    DELETE FROM return_requests WHERE id = var_return_req_id;
END //

CREATE PROCEDURE read_all_return_requests()
BEGIN
   SELECT * FROM return_requests;
END //

CREATE PROCEDURE read_return_request(
    var_return_req_id INT
)
BEGIN
    SELECT * FROM return_requests WHERE id = var_return_req_id;
END //

-- STORAGE PROCEDURE FROM transmission

CREATE PROCEDURE read_all_transmissions()
BEGIN
	SELECT * FROM transmissions;
END //

CREATE PROCEDURE read_transmission(
	var_id INT
)
BEGIN
	SELECT * FROM transmissions WHERE id = var_id;
END //

-- STORAGE PROCEDURE FROM fuel_types

CREATE PROCEDURE read_all_fuel_types()
BEGIN
	SELECT * FROM fuel_types;
END //

CREATE PROCEDURE read_fuel_type(
	var_id INT
)
BEGIN
	SELECT * FROM fuel_types WHERE id = var_id;
END //




-- INSERT TEMPLATE

-- add transmission

INSERT INTO transmissions(name) VALUES ('Механика');
INSERT INTO transmissions(name) VALUES ('Автомат');

-- add transmission

INSERT INTO fuel_types(name) VALUES ('Бензин');
INSERT INTO fuel_types(name) VALUES ('Дизель');

-- INSERT Cars (number, model, mileage, year_of_issue, price_hour, transmission_id, fuel_type_id)

CALL add_car('3487 AM-3', 'Volkswagen Passat CCI', 192000, 2009, 3, 2, 1);
CALL add_car('8731 AM-7','Proton Persona', 542000, 1997, 0.9, 1,  1);
CALL add_car('5721 AM-3','Mercedes-Benz 190', 240500, 1991, 0.8, 1, 1);
CALL add_car('5487 AM-4', 'Mazda 626 II', 154600, 1986, 0.7, 1, 1);
CALL add_car('8748 AM-7', 'Audi Q7 4L', 156600, 2008, 3, 2, 1);

CALL add_car('5463 AM-7', 'Mercedes-Benz E W211', 308000, 2005, 3, 2, 1);

CALL add_car('1424 AM-6', 'Audi A8 D3 Long', 85000, 2006, 3,	2, 2);
CALL add_car('4321 AM-7', 'Citroen Ecasion I', 328000, 1999, 1.25, 1, 1);
CALL add_car('4797 AM-7', 'Opel Corsa D CDX', 280000, 2007, 2, 1, 2);
CALL add_car('4562 AM-8', 'Renault Lagunna II Expression	85000' ,2001, 2, 2, 1, 1);





