#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

brew services start postgresql@16
read -r -s -p "Mot de passe local pour productuser : " DB_PASSWORD
echo
if [[ -z "$DB_PASSWORD" ]]; then
    echo "Le mot de passe ne peut pas être vide." >&2
    exit 1
fi
export DB_PASSWORD

psql -X -v ON_ERROR_STOP=1 -v db_password="$DB_PASSWORD" -d postgres -f sql/setup.sql
echo "Swagger : http://localhost:8080/swagger-ui.html"
mvn spring-boot:run
