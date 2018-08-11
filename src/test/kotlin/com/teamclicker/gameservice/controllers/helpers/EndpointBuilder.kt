package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.Constants.JWT_HEADER_NAME
import com.teamclicker.gameservice.Constants.JWT_TOKEN_PREFIX
import com.teamclicker.gameservice.testConfig.models.SpringErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class EndpointBuilder<
        out Child : EndpointBuilder<Child, Body, Response>,
        Body : Any,
        Response : Any
        >(
    private val responseType: Class<Response>,
    private val http: TestRestTemplate
) {
    protected val pathVariables = hashMapOf<String, Any>()
    protected var body: Body? = null
    protected val headers = HttpHeaders()

    abstract val url: String
    abstract val method: HttpMethod

    /**
     * Makes a http request to a signIn endpoint and saves its jwt to the headers map
     */
    fun with(user: TestEntity): Child {
        user.token?.let {
            headers[JWT_HEADER_NAME] = "$JWT_TOKEN_PREFIX$it"
        }

        return this as Child
    }

    fun sending(body: Body?): Child {
        this.body = body
        return this as Child
    }

    fun addPathVariable(key: String, value: Any): Child {
        this.pathVariables.put(key, value)
        return this as Child
    }

    fun addHeader(key: String, value: String): Child {
        this.headers.add(key, value)
        return this as Child
    }

    fun <Err : Any> expectError(type: KClass<Err>, callback: (ResponseEntity<Err>) -> Unit = {}): ResponseEntity<Err> {
        return expect(type.java, null, callback)
    }

    fun <Err : Any> expectError(statusCode: Int, type: KClass<Err>, callback: (ResponseEntity<Err>) -> Unit = {}): ResponseEntity<Err> {
        return expect(type.java, statusCode, callback)
    }

    fun expectError(callback: (ResponseEntity<SpringErrorResponse>) -> Unit = {}): ResponseEntity<SpringErrorResponse> {
        return expect(SpringErrorResponse::class.java, null, callback)
    }

    fun expectError(statusCode: Int, callback: (ResponseEntity<SpringErrorResponse>) -> Unit = {}): ResponseEntity<SpringErrorResponse> {
        return expect(SpringErrorResponse::class.java, statusCode, callback)
    }

    fun expectSuccess(callback: (ResponseEntity<Response>) -> Unit = {}): ResponseEntity<Response> {
        return expect(responseType, 200, callback)
    }

    private fun <T> expect(type: Class<T>, statusCode: Int?, callback: (ResponseEntity<T>) -> Unit): ResponseEntity<T> {
        val httpEntity = HttpEntity(body, headers)
        val response = build(httpEntity, type)
        statusCode?.let {
            assertEquals(it, response.statusCodeValue)
        }
        if (type.typeName !== Void::class.java.typeName) {
            val body = response.body as Any
            assertEquals(type.canonicalName, body::class.java.canonicalName)
        }

        callback(response)
        return response
    }

    private fun <T> build(httpEntity: HttpEntity<Body>, responseBodyType: Class<T>): ResponseEntity<T> {
        return http.exchange(url, method, httpEntity, responseBodyType, pathVariables)
    }
}