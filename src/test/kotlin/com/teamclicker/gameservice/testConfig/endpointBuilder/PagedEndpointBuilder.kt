package com.teamclicker.gameservice.testConfig.endpointBuilder

import com.google.gson.reflect.TypeToken
import org.springframework.boot.test.web.client.TestRestTemplate
import java.lang.reflect.Type


abstract class PagedEndpointBuilder<Child : EndpointBuilder<Child, Body, PageImpl<Response>>, Body : Any, Response : Any>(
    responseType: Type,
    http: TestRestTemplate
) :
    EndpointBuilder<Child, Body, PageImpl<Response>>(
        TypeToken.getParameterized(
            PageImpl::class.java,
            responseType
        ).type, http
    )