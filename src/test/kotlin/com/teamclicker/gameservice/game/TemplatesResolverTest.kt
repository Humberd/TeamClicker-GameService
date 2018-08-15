package com.teamclicker.gameservice.game

import com.google.gson.Gson
import com.teamclicker.gameservice.game.spring.TemplatesResolver
import com.teamclicker.gameservice.game.spring.TemplatesStore
import com.teamclicker.gameservice.game.templates.CreatureTemplate
import com.teamclicker.gameservice.game.templates.ItemTemplate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TemplatesResolverTest {

    @Autowired
    lateinit var resourceLoader: ResourceLoader
    @Autowired
    lateinit var gson: Gson

    lateinit var templatesResolver: TemplatesResolver
    lateinit var templatesStore: TemplatesStore

    @BeforeEach
    fun setUp() {
        templatesStore = TemplatesStore()
        templatesResolver = TemplatesResolver(resourceLoader, gson, templatesStore)
    }

    @Test
    fun `should read template items`() {
        val results = templatesStore.itemTemplates

        assert(results.isNotEmpty())
        assert(results.entries.first().value is ItemTemplate)
    }

    @Test
    fun `should read template creatures`() {
        val results = templatesStore.creatureTemplates

        assert(results.isNotEmpty())
        assert(results.entries.first().value is CreatureTemplate)
    }
}