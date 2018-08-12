package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.testConfig.endpointBuilders.AdminMock
import com.teamclicker.gameservice.testConfig.endpointBuilders.Anonymous
import com.teamclicker.gameservice.testConfig.endpointBuilders.UserMock

object Users {
    val ALICE
        get() = UserMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYyLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiVVNFUiJdLCJqdGkiOiIyMDAyNWVhMy1mYmY2LTQyMmYtOGUyNC0zOWFkM2I5ZTUwODMiLCJleHAiOjEwMTczNDE5NjQ4LCJpYXQiOjE1MzM0MTk2NDh9.KnPEWt3tmPMmCPpbP4TV5G9KdDA8OmWU-_-JT9qVOjXbA0ncUuHFxLyqJgI1iaIjhR7b_4QgxVWoAClXmF7fqGJLuBIgtATHm-udfJwZtDPQ3EZm2_FsmF4ybOU3XZoiYy9_y7IOO4jwk-jzAhsjmJjILl26Y9cyflL5ArayKtFiHMXfH6DYUh6IfQkjId4xKbbTN8LUaeTBPTBH_syG8aBr5w5fXtTc2peNY-_Iofmiv2O7_jY4kXt5ENgeE4-vD0xDUGYKDpKYZyGQ9sjXyrkDzqpzSYEcrxqLSJj_sTOT18KP7dQjmrCaQOgprn2eUGp9NGi9q6VD4pKlh8Sy8w"
        )

    val BOB
        get() = UserMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjEsImF1dGhlbnRpY2F0aW9uTWV0aG9kIjoiVVNFUk5BTUVfUEFTU1dPUkQiLCJyb2xlcyI6WyJVU0VSIl0sImp0aSI6IjM3ZjQ1MzJjLTU4ZDQtNGM4My1iMGVkLWM4ZjA0NWIwOTZhYyIsImV4cCI6MTAxNzM5NzgwNjgsImlhdCI6MTUzMzk3ODA2OH0.YxiHD4-UrrXI1Jh8Hl5QXwcYJpW3yqJ4lvbsnSlXKyzA2Y-kKDwO55b7UL-xtq7gLx2J91RuXfLD02Weuv7_heAfa3kxWivcn2pvpnXI-SGFDRjzhaO3YNDpL_zSSmYeObhFj_6KfreaN1MqDSafeyK6HEgFG-9yEnc6t6rjxM7ySQpJwlCfyhLVZZ4HKg3rVDuQ2SUeVsHimoKZCvn6ySODvMqBjpgaW-0sC5XN7p9NRQbi5Op5sefAe5AR9GnPvEf59P1MHS44qfRP07kMbasovvEgLaGMDJDrARSip17glWn4n9s5mt1WMx33iPILscLDCubaiNceGcQwOyi3ug"
        )

    val CHUCK
        get() = UserMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjIsImF1dGhlbnRpY2F0aW9uTWV0aG9kIjoiVVNFUk5BTUVfUEFTU1dPUkQiLCJyb2xlcyI6WyJVU0VSIl0sImp0aSI6Ijg0OTMyMDEzLWRjYTAtNDVkNS05NDg2LWM4YWVlZDhkYzE1NSIsImV4cCI6MTAxNzM5NzgxNjIsImlhdCI6MTUzMzk3ODE2Mn0.LGA_vC1S7a0Pk3pL8elSrkSHa20OEkL-_cxD3KbTlMtyk_yHVEJ78Ih-xjNOn0a_jR2QE7fbKjTSVXRmOnNFRuXnyMZuQRUo2JIX88vc2NryVIKTCVIScPW-O1Zo46jGcCUvz3QpiAYvW0o2SI7s2delwxUMKf_mgowGbPuViKG-iRpwjz7DaYJsR2HgF3G6STVjS2hxQ5T-ZL_4s75NJm1v1Pe7G_q_aocFEnlPOXNmyVX7mN7Jd8F6ksGln5H7cYNfZmmGhuw8_N_K3M55OUFbzMRcC5MvoC3WBz1LFjz-Vqiq3zE5tK4Bj7HdRAhLKqxDuPnG46WvrZKmnAB4hg"
        )

    val GRACE_ADMON
        get() = AdminMock(
            token = "eyJhbGciOiJSUzUxMiJ9.eyJhY2NvdW50SWQiOjYxLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlVTRVJOQU1FX1BBU1NXT1JEIiwicm9sZXMiOlsiQURNSU4iXSwianRpIjoiMjFiMzM0OTMtYjI1Zi00NjMyLThlY2EtY2QxMzQ1ZDI1YzEyIiwiZXhwIjoxMDE3MzQxOTUzOSwiaWF0IjoxNTMzNDE5NTM5fQ.WI5d85Po_NLHdNEEKzzOWWwYcriKnbVpslYljnSFKk5_YMgNkLd76maJTsM_caGQvfu1SYzQIUK9hHe66r1ULL7nA14P40omueprq9QWauEMbApbsYvhsuE-aL-Upc-QR6Fj8FR2ho3y3eA_SvMSn-DS98nnSYLzWkhX-IbwA19SPcNcufFj4ebP-HPD1DOvPGRZwg-0hj6VYgNZ3o4qhxDfIlK-j301dANHZ23AwMWdC3t_8rFpzv1nTrr4cj_oDb-fb2iz_XUtUCKSjyfwQ_IBiENz7piyePpRIEXMdraUlnuhaxQa7hC7Rk96Khmfd-J3VNW5QldpsAKx9e270A"
        )

    val ANONYMOUS
        get() = Anonymous()
}
