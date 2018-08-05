# Team Clicker Game Service

`docker run --name tc-postgres -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres`

## Deployment requirements

### Files

 1. _src/**main**/resources/jwt_public_key.der_ - **public** jwt validation key

How to generate keys -> https://stackoverflow.com/a/19387517/4256929

### Environment variables

 1. _TC_GAME_DATABASE_URL_ - url to the PostgreSQL database, for example:
 `127.0.0.1:4321/game-service`
 2. _TC_GAME_DATABASE_USERNAME_ - PostgreSQL database username, for example:
 `admin`
 3. _TC_GAME_DATABASE_PASSWORD_ - PostgreSQL database user password, for example:
 `admin123`
 4. _TC_KAFKA_URL_ - Kafka url, for example: `192.168.99.100:32769`

## Deployment Testing requirements

### Files

 1. _src/**test**/resources/jwt_public_key.der_ - **public** jwt validation key

How to generate keys -> https://stackoverflow.com/a/19387517/4256929

### Environment variables

 1. _TC_GAME__**_TESTS_**__DATABASE_URL_ - url to the PostgreSQL database, for example:
 `127.0.0.1:4321/game-service-tests`
 2. _TC_GAME__**_TESTS_**__DATABASE_USERNAME_ - PostgreSQL database username, for example:
 `admin`
 3. _TC_GAME__**_TESTS_**__DATABASE_PASSWORD_ - PostgreSQL database user password, for example:
 `admin123`
 4. _TC_KAFKA_URL_ - Kafka url, for example: `192.168.99.100:32769`


Admin jwt token

```text
eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYxLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiQURNSU4iXSwianRpIjoiMjFiMzM0OTMtYjI1Zi00NjMyLThlY2EtY2QxMzQ1ZDI1YzEyIiwiZXhwIjoxMDE3MzQxOTUzOSwiaWF0IjoxNTMzNDE5NTM5fQ.WI5d85Po_NLHdNEEKzzOWWwYcriKnbVpslYljnSFKk5_YMgNkLd76maJTsM_caGQvfu1SYzQIUK9hHe66r1ULL7nA14P40omueprq9QWauEMbApbsYvhsuE-aL-Upc-QR6Fj8FR2ho3y3eA_SvMSn-DS98nnSYLzWkhX-IbwA19SPcNcufFj4ebP-HPD1DOvPGRZwg-0hj6VYgNZ3o4qhxDfIlK-j301dANHZ23AwMWdC3t_8rFpzv1nTrr4cj_oDb-fb2iz_XUtUCKSjyfwQ_IBiENz7piyePpRIEXMdraUlnuhaxQa7hC7Rk96Khmfd-J3VNW5QldpsAKx9e270A
```

User jwt token

```text
eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYyLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiVVNFUiJdLCJqdGkiOiIyMDAyNWVhMy1mYmY2LTQyMmYtOGUyNC0zOWFkM2I5ZTUwODMiLCJleHAiOjEwMTczNDE5NjQ4LCJpYXQiOjE1MzM0MTk2NDh9.KnPEWt3tmPMmCPpbP4TV5G9KdDA8OmWU-_-JT9qVOjXbA0ncUuHFxLyqJgI1iaIjhR7b_4QgxVWoAClXmF7fqGJLuBIgtATHm-udfJwZtDPQ3EZm2_FsmF4ybOU3XZoiYy9_y7IOO4jwk-jzAhsjmJjILl26Y9cyflL5ArayKtFiHMXfH6DYUh6IfQkjId4xKbbTN8LUaeTBPTBH_syG8aBr5w5fXtTc2peNY-_Iofmiv2O7_jY4kXt5ENgeE4-vD0xDUGYKDpKYZyGQ9sjXyrkDzqpzSYEcrxqLSJj_sTOT18KP7dQjmrCaQOgprn2eUGp9NGi9q6VD4pKlh8Sy8w
```