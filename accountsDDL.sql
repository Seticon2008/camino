DROP TABLE charges CASCADE CONSTRAINTS;
DROP TABLE accounts CASCADE CONSTRAINTS;

CREATE TABLE accounts (
	account_name VARCHAR(8) NOT NULL,
	CONSTRAINT accounts_pk PRIMARY KEY (account_name)
);

CREATE TABLE charges (
	charge_id NUMBER(10) NOT NULL,
	account_charged VARCHAR(8) NOT NULL,
	charge_date DATE NOT NULL,
	charge_amount NUMBER(8,2) NOT NULL,
	charge_reason CHAR(40) NOT NULL,
	CONSTRAINT charges_pk PRIMARY KEY (charge_id),
	CONSTRAINT charges_account_fk FOREIGN KEY (account_charged)
		REFERENCES accounts (account_name)
);

INSERT INTO accounts VALUES ('uhcu0200');
INSERT INTO accounts VALUES ('disc5111');
INSERT INTO accounts VALUES ('citi9355');
INSERT INTO accounts VALUES ('wfar3431');

INSERT INTO charges VALUES (2024082701, 'disc5111', DATE '2024-08-27', 24.50, 'gasolina');
INSERT INTO charges VALUES (2024090201, 'disc5111', DATE '2024-09-02', 3.32, 'la compra');
INSERT INTO charges VALUES (2024090501, 'disc5111', DATE '2024-09-05', 5.64, 'lujo de comida');
INSERT INTO charges VALUES (2024090502, 'disc5111', DATE '2024-09-05', 2.84, 'la compra');
INSERT INTO charges VALUES (2024091201, 'disc5111', DATE '2024-09-12', 13.00, 'gasolina');
INSERT INTO charges VALUES (2024091202, 'disc5111', DATE '2024-09-12', 7.19, 'la compra');
INSERT INTO charges VALUES (2024091501, 'disc5111', DATE '2024-09-15', 6.00, 'la compra');
INSERT INTO charges VALUES (2024091801, 'disc5111', DATE '2024-09-18', 18.32, 'lujo de comida');
INSERT INTO charges VALUES (2024091901, 'disc5111', DATE '2024-09-19', 5.68, 'la compra');
INSERT INTO charges VALUES (2024092001, 'disc5111', DATE '2024-09-20', 12.20, 'gasolina');
INSERT INTO charges VALUES (2024092002, 'disc5111', DATE '2024-09-20', 30.78, 'lujo de comedia');
INSERT INTO charges VALUES (2024092401, 'disc5111', DATE '2024-09-24', 5.68, 'la compra');
INSERT INTO charges VALUES (2024092501, 'disc5111', DATE '2024-09-25', 5.50, 'la compra');
INSERT INTO charges VALUES (2024092601, 'disc5111', DATE '2024-09-26', 2.86, 'la compra');
