CREATE TABLE dim_currency 
(
	Id INT auto_increment primary key,
    currency_code	VARCHAR(50),
    currency_name	VARCHAR(50),
    currency_symbol	VARCHAR(50)
);

INSERT INTO dim_currency (currency_code, currency_name, currency_symbol) VALUES
	('USD', 'United States Dollar', '$'),
	('EUR', 'Euro', '€'),
	('GBP', 'British Pound Sterling', '£'),
	('JPY', 'Japanese Yen', '¥'),
	('AUD', 'Australian Dollar', 'A$'),
	('CAD', 'Canadian Dollar', 'CA$'),
	('CNY', 'Chinese Yuan', '¥'),
	('INR', 'Indian Rupee', '₹'),
	('BRL', 'Brazilian Real', 'R$'),
	('RUB', 'Russian Ruble', '₽'),
	('SGD', 'Singapore Dollar', 'S$'),
	('NZD', 'New Zealand Dollar', 'NZ$'),
	('CHF', 'Swiss Franc', 'CHF'),
	('ZAR', 'South African Rand', 'R'),
	('KRW', 'South Korean Won', '₩'),
	('TRY', 'Turkish Lira', '₺'),
	('MXN', 'Mexican Peso', 'Mex$'),
	('HKD', 'Hong Kong Dollar', 'HK$'),
	('SEK', 'Swedish Krona', 'kr'),
	('NOK', 'Norwegian Krone', 'kr'),
	('VND', 'Vietnamese Dong', '₫');