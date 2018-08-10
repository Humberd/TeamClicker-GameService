package com.teamclicker.gameservice.controllers.helpers

object Users {
    val ALICE
        get() = UserMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYyLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiVVNFUiJdLCJqdGkiOiIyMDAyNWVhMy1mYmY2LTQyMmYtOGUyNC0zOWFkM2I5ZTUwODMiLCJleHAiOjEwMTczNDE5NjQ4LCJpYXQiOjE1MzM0MTk2NDh9.KnPEWt3tmPMmCPpbP4TV5G9KdDA8OmWU-_-JT9qVOjXbA0ncUuHFxLyqJgI1iaIjhR7b_4QgxVWoAClXmF7fqGJLuBIgtATHm-udfJwZtDPQ3EZm2_FsmF4ybOU3XZoiYy9_y7IOO4jwk-jzAhsjmJjILl26Y9cyflL5ArayKtFiHMXfH6DYUh6IfQkjId4xKbbTN8LUaeTBPTBH_syG8aBr5w5fXtTc2peNY-_Iofmiv2O7_jY4kXt5ENgeE4-vD0xDUGYKDpKYZyGQ9sjXyrkDzqpzSYEcrxqLSJj_sTOT18KP7dQjmrCaQOgprn2eUGp9NGi9q6VD4pKlh8Sy8w"
        )

    val GRACE_ADMON
        get() = AdminMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYxLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiQURNSU4iXSwianRpIjoiMjFiMzM0OTMtYjI1Zi00NjMyLThlY2EtY2QxMzQ1ZDI1YzEyIiwiZXhwIjoxMDE3MzQxOTUzOSwiaWF0IjoxNTMzNDE5NTM5fQ.WI5d85Po_NLHdNEEKzzOWWwYcriKnbVpslYljnSFKk5_YMgNkLd76maJTsM_caGQvfu1SYzQIUK9hHe66r1ULL7nA14P40omueprq9QWauEMbApbsYvhsuE-aL-Upc-QR6Fj8FR2ho3y3eA_SvMSn-DS98nnSYLzWkhX-IbwA19SPcNcufFj4ebP-HPD1DOvPGRZwg-0hj6VYgNZ3o4qhxDfIlK-j301dANHZ23AwMWdC3t_8rFpzv1nTrr4cj_oDb-fb2iz_XUtUCKSjyfwQ_IBiENz7piyePpRIEXMdraUlnuhaxQa7hC7Rk96Khmfd-J3VNW5QldpsAKx9e270A"
        )

    val ANONYMOUS
        get() = Anonymous()

}

sealed class TestEntity(
    val token: String?
)

class UserMock(
    token: String
) : TestEntity(token)

class AdminMock(
    token: String
) : TestEntity(token)

class Anonymous : TestEntity(null)
