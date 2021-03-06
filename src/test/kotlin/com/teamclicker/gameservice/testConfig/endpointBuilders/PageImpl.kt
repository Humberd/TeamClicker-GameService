package com.teamclicker.gameservice.testConfig.endpointBuilders

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class PageImpl<T> {
    val serialVersionUID = 1L
    val number: Int = 0
    val size: Int = 0
    val totalPages: Int = 0
    val numberOfElements: Int = 0
    val totalElements: Long = 0
    val previousPage: Boolean = false
    val firstPage: Boolean = false
    val nextPage: Boolean = false
    val lastPage: Boolean = false
    val content: List<T> = emptyList()
}