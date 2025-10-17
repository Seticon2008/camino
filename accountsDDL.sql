DROP TABLE charges CASCADE CONSTRAINTS;
DROP TABLE accounts CASCADE CONSTRAINTS;

CREATE TABLE accounts (
	account_name VARCHAR(12) NOT NULL,
	CONSTRAINT accounts_pk PRIMARY KEY (account_name)
);

CREATE TABLE charges (
	charge_id NUMBER(10) NOT NULL,
	account_charged VARCHAR(12) NOT NULL,
	charge_date DATE NOT NULL,
	charge_amount NUMBER(8,2) NOT NULL,
	charge_reason CHAR(40) NOT NULL,
	CONSTRAINT charges_pk PRIMARY KEY (charge_id),
	CONSTRAINT charges_account_fk FOREIGN KEY (account_charged)
		REFERENCES accounts (account_name)
);

INSERT INTO accounts VALUES ('exaccount');
INSERT INTO charges VALUES (2024080601, 'exaccount', DATE '2024-08-06', 10.00, 'groceries');