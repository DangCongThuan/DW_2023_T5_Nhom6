CREATE TABLE dim_bank 
(
	id INT auto_increment primary key,
    bank_name	VARCHAR(200),
    bank_code	VARCHAR(50),
    country_id	INT
);

INSERT INTO dim_bank (bank_name, bank_code, country_id) VALUES
	('Ngân hàng Thương mại Cổ phần Ngoại Thương Việt Nam', 'VCB', '15'),
	('Ngân hàng Công Thương Việt Nam', 'CTG', '15'),
	('Ngân hàng TMCP Quân đội', 'MB', '15'),
	('Ngân hàng Thương mại Cổ phần Kỹ Thương Việt Nam', 'TCB', '15'),
	('Ngân hàng Thương mại Cổ phần Sài Gòn-Hà Nội', 'BID', '15'),
	('Ngân hàng Thương mại Cổ phần Đầu tư và Phát triển Việt Nam', 'BIDV', '15'),
	('Ngân hàng Thương mại Cổ phần Tiên Phong', 'TPBank', '15'),
	('Ngân hàng Thương mại Cổ phần Đông Á', 'DAB', '15'),
	('Ngân hàng Thương mại Cổ phần Á Châu', 'ACB', '15'),
	('Ngân hàng Thương mại Cổ phần Xây Dựng VietinBank', 'CTG', '15');