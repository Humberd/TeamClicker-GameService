package com.teamclicker.gameservice.testConfig.endpointBuilders

import com.google.gson.*
import com.teamclicker.gameservice.Constants.JWT_HEADER_NAME
import com.teamclicker.gameservice.Constants.JWT_TOKEN_PREFIX
import com.teamclicker.gameservice.testConfig.models.SpringErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import java.lang.reflect.Type
import java.time.LocalDateTime
import kotlin.reflect.KClass


@Suppress("UNCHECKED_CAST")
abstract class HttpEndpointBuilder<
        out Child : HttpEndpointBuilder<Child, Body, Response>,
        Body : Any,
        Response : Any
        >(
    private val responseType: Type,
    private val http: TestRestTemplate
) {
    protected val pathVariables = hashMapOf<String, Any>()
    protected val queryParams = LinkedMultiValueMap<String, String>()
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

    fun addQueryParam(key: String, value: Any): Child {
        this.queryParams.put(key, listOf(value.toString()))
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

    fun expectError(callback: (ResponseEntity<SpringErrorResponse>) -> Unit = {}): ResponseEntity<SpringErrorResponse> {
        return expect(SpringErrorResponse::class.java, null, callback)
    }

    fun expectError(
        statusCode: Int,
        callback: (ResponseEntity<SpringErrorResponse>) -> Unit = {}
    ): ResponseEntity<SpringErrorResponse> {
        return expect(SpringErrorResponse::class.java, statusCode, callback)
    }

    fun <Err : Any> expectError(type: KClass<Err>, callback: (ResponseEntity<Err>) -> Unit = {}): ResponseEntity<Err> {
        return expect(type.java, null, callback)
    }

    fun <Err : Any> expectError(
        statusCode: Int,
        type: KClass<Err>,
        callback: (ResponseEntity<Err>) -> Unit = {}
    ): ResponseEntity<Err> {
        return expect(type.java, statusCode, callback)
    }

    fun expectSuccess(callback: (ResponseEntity<Response>) -> Unit = {}): ResponseEntity<Response> {
        return expect(responseType, 200, callback)
    }

    private fun <T> expect(type: Type, statusCode: Int?, callback: (ResponseEntity<T>) -> Unit): ResponseEntity<T> {
        val httpEntity = HttpEntity(body, headers)
        val response = build<T>(httpEntity, type)
        statusCode?.let {
            assertEquals(it, response.statusCodeValue)
        }
        // todo: check returning body type
//        if (type.typeName !== Void::class.java.typeName) {
//            val body = response.body as Any
//            assertEquals(type., body::class.java.typeName)
//        }

        callback(response)
        return response
    }

    private fun <T> build(httpEntity: HttpEntity<Body>, responseBodyType: Type): ResponseEntity<T> {
        val urlBuilder = UriComponentsBuilder.fromPath(url)
            .queryParams(queryParams)

        val response = http.exchange(
            urlBuilder.build(false).toUriString(),
            method,
            httpEntity,
            String::class.java,
            pathVariables
        )

        val newBody = gson.fromJson<T>(response.body, responseBodyType)
        val newResponse = ResponseEntity(newBody, response.headers, response.statusCode)

        return newResponse
    }

    companion object {

        var gson =
            GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
                @Throws(JsonParseException::class)
                override fun deserialize(
                    json: JsonElement,
                    type: Type,
                    jsonDeserializationContext: JsonDeserializationContext
                ): LocalDateTime {
                    return LocalDateTime.parse(json.asJsonPrimitive.asString)
                }
            }).create()
    }
}