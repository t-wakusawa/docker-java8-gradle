#!/bin/bash -e

echo
echo "$(date): [INFO] Parameter Store からデータ取得開始..."

export DB_NAME=$(aws ssm get-parameters --names /app/datasource/database --query 'Parameters[0].Value' --output text);
export SECRET_NAME="/app/datasource/secret";

echo "$(date): [INFO] Secrets Manager からデータ取得開始..."

export DB_SECRETS=$(aws secretsmanager get-secret-value --secret-id $SECRET_NAME --query 'SecretString' --output text);
export DB_ENDPOINT=$(echo "$DB_SECRETS" |python -c "import sys,json; print(json.load(sys.stdin)['host'])");
export DB_USER=$(echo "$DB_SECRETS" |python -c "import sys,json; print(json.load(sys.stdin)['username'])");
export DB_PASSWORD=$(echo "$DB_SECRETS" |python -c "import sys,json; print(json.load(sys.stdin)['password'])");

rc=0
for key in DB_NAME SECRET_NAME DB_ENDPOINT DB_USER DB_PASSWORD; do
    value=$(eval echo '$'$key)
    if [ "$value" == "None" -o -z "$value" ]; then
        echo "$(date): [ERROR] $key の取得に失敗"
        ((rc += 1))
    fi
done
if [ "$rc" -gt 0 ]; then
    echo "$(date): [ERROR] ...エラー"
else
    echo "$(date): [INFO] ...完了"
fi