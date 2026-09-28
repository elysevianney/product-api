-- Dans psql, definir d'abord : \set db_password 'votre-mot-de-passe-local'
-- Puis executer : \i sql/setup.sql
\set ON_ERROR_STOP on

SELECT format('CREATE ROLE productuser LOGIN PASSWORD %L', :'db_password')
WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'productuser')
\gexec

SELECT format('ALTER ROLE productuser WITH PASSWORD %L', :'db_password')
\gexec

SELECT 'CREATE DATABASE productdb OWNER productuser'
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'productdb')
\gexec
