#!/bin/bash -e

echo
echo "$(date): [INFO] Amazon Aurora MySQL に初期データ登録開始..."

mysql -u $DB_USER -p$DB_PASSWORD -h $DB_ENDPOINT -t -e "TRUNCATE ${DB_NAME}.books" >/dev/null 2>&1
mysql -u $DB_USER -p$DB_PASSWORD -h $DB_ENDPOINT << _EOF_
CREATE DATABASE IF NOT EXISTS ${DB_NAME};
USE ${DB_NAME};
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published VARCHAR(255) NOT NULL,
    reading_status VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO books(name,author,published,reading_status)VALUES("Harry Potter and the Philosopher's Stone","J. K. Rowling","1997","Completed");
INSERT INTO books(name,author,published,reading_status)VALUES("The Hobbit","J. R. R. Tolkien","1937","Started");
INSERT INTO books(name,author,published,reading_status)VALUES("And Then There Were None","Agatha Christie","1939","In progress");
_EOF_

COUNT=$(mysql -u $DB_USER -p$DB_PASSWORD -h $DB_ENDPOINT -e "SELECT COUNT(*) FROM ${DB_NAME}.books" -sN)
if [ ! -z "$COUNT" -a "$COUNT" -gt 0 ]; then
    echo
    echo "${DB_NAME}.books"
    mysql -u $DB_USER -p$DB_PASSWORD -h $DB_ENDPOINT -t -e "SELECT * FROM ${DB_NAME}.books"
    echo
    echo "$(date): [INFO] ...完了"
    echo
else
    echo "$(date): [ERROR] ...エラー"
    echo
fi

