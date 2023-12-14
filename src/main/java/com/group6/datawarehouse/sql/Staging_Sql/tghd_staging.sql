create database staging;
use staging;
CREATE TABLE tghd_staging 
(
	id int auto_increment primary key,
    bank_source	VARCHAR(512),
    date	VARCHAR(512),
    date_time	VARCHAR(512),
    currency_symbol	VARCHAR(512),
    currency_name	VARCHAR(512),
    buy_cash	VARCHAR(512),
    buy_transger	VARCHAR(512),
    sale_cash	VARCHAR(512),
    sale_transfer	VARCHAR(512)
);

INSERT INTO tghd_staging (bank_source, date, date_time, currency_symbol, currency_name, buy_cash, buy_transger, sale_cash, sale_transfer) VALUES
	('Vietcombank', '19/11/2023', '22:00:00', 'AUD', 'AUSTRALIAN DOLLAR', '15,269.91', '15,424.15', '15,920.17', '15,920.17'),
	('Vietcombank', '19/11/2024', '22:00:00', 'CAD', 'CANADIAN DOLLAR', '17,186.74', '17,360.34', '17,918.62', '17,918.62'),
	('Vietcombank', '19/11/2025', '22:00:00', 'CHF', 'SWISS FRANC', '26,605.48', '26,874.22', '27,738.45', '27,738.45'),
	('Vietcombank', '19/11/2026', '22:00:00', 'CNY', 'YUAN RENMINBI', '3,277.93', '3,311.04', '3,418.03', '3,418.03'),
	('Vietcombank', '19/11/2027', '22:00:00', 'DKK', 'DANISH KRONE', '-', '3,464.51', '3,597.45', '3,597.45'),
	('Vietcombank', '19/11/2028', '22:00:00', 'EUR', 'EURO', '25,645.14', '25,904.18', '27,053.32', '27,053.32'),
	('Vietcombank', '19/11/2029', '22:00:00', 'GBP', 'POUND STERLING', '29,318.52', '29,614.66', '30,567.02', '30,567.02'),
	('Vietcombank', '19/11/2030', '22:00:00', 'HKD', 'HONGKONG DOLLAR', '3,030.20', '3,060.81', '3,159.24', '3,159.24'),
	('Vietcombank', '19/11/2031', '22:00:00', 'INR', 'INDIAN RUPEE', '-', '290.52', '302.16', '302.16'),
	('Vietcombank', '19/11/2032', '22:00:00', 'JPY', 'YEN', '156.55', '158.13', '165.71', '165.71'),
	('Vietcombank', '19/11/2033', '22:00:00', 'KRW', 'KOREAN WON', '16.23', '18.04', '19.68', '19.68'),
	('Vietcombank', '19/11/2034', '22:00:00', 'KWD', 'KUWAITI DINAR', '-', '78,393.80', '81,533.97', '81,533.97'),
	('Vietcombank', '19/11/2035', '22:00:00', 'MYR', 'MALAYSIAN RINGGIT', '-', '5,123.94', '5,236.08', '5,236.08'),
	('Vietcombank', '19/11/2036', '22:00:00', 'NOK', 'NORWEGIAN KRONER', '-', '2,174.33', '2,266.82', '2,266.82'),
	('Vietcombank', '19/11/2037', '22:00:00', 'RUB', 'RUSSIAN RUBLE', '-', '258.23', '285.88', '285.88'),
	('Vietcombank', '19/11/2038', '22:00:00', 'SAR', 'SAUDI RIAL', '-', '6,450.89', '6,709.29', '6,709.29'),
	('Vietcombank', '19/11/2039', '22:00:00', 'SEK', 'SWEDISH KRONA', '-', '2,244.99', '2,340.48', '2,340.48'),
	('Vietcombank', '19/11/2040', '22:00:00', 'SGD', 'SINGAPORE DOLLAR', '17,534.94', '17,712.06', '18,281.65', '18,281.65'),
	('Vietcombank', '19/11/2041', '22:00:00', 'THB', 'THAILAND BAHT', '608.96', '676.62', '702.58', '702.58'),
	('Vietcombank', '19/11/2042', '22:00:00', 'USD', 'US DOLLAR', '24,045.00', '24,075.00', '24,415.00', '24,415.00');
