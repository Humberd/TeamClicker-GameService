package com.teamclicker.gameservice.testConfig.endpointBuilders

import com.google.gson.reflect.TypeToken
import org.springframework.boot.test.web.client.TestRestTemplate
import java.lang.reflect.Type


abstract class PagedHttpEndpointBuilder<Child : HttpEndpointBuilder<Child, Body, PageImpl<Response>>, Body : Any, Response : Any>(
    responseType: Type,
    http: TestRestTemplate
) :
    HttpEndpointBuilder<Child, Body, PageImpl<Response>>(
        TypeToken.getParameterized(
            PageImpl::class.java,
            responseType
        ).type, http
    )