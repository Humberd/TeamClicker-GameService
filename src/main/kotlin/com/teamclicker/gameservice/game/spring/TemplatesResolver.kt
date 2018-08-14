package com.teamclicker.gameservice.game.spring

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teamclicker.gameservice.Constants.CREATURE_TEMPLATES_PATH
import com.teamclicker.gameservice.Constants.ITEM_TEMPLATES_PATH
import com.teamclicker.gameservice.Constants.TEMPLATES_PATH
import com.teamclicker.gameservice.extensions.KLogging
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import javax.annotation.PostConstruct

/**
 * Reads template files, maps them to objects and saves them.
 *
 * TODO: Add validation of json objects against Kotlin classes
 */
@Service
class TemplatesResolver(
    private val resourceLoader: ResourceLoader,
    private val gson: Gson,
    private val templatesStore: TemplatesStore
) {

    @PostConstruct
    fun onInit() {
        templatesStore.also {
            it.itemTemplates = resolveTemplates(ITEM_TEMPLATES_PATH) { it.templateId }
            it.creatureTemplates = resolveTemplates(CREATURE_TEMPLATES_PATH) { it.templateId }
        }
    }

    /**
     * Reads templates from a given [fileName] and maps the them into the map,
     * where [Key] is a templateId type and [T] it a template object
     *
     * It needs to consume [idExtractor], because within the method
     * there is no way to find out the exact type of the template,
     * from which we could get its id.
     * The caller knows the template type, thus is able to retrieve its id.
     */
    final inline internal fun <Key : Any, reified T : Any> resolveTemplates(
        fileName: String,
        idExtractor: (T) -> Key
    ): Map<Key, T> {
        val path = "${TEMPLATES_PATH}/${fileName}"

        val typeObject = TypeToken.getParameterized(List::class.java, T::class.java)
        val list: List<T> = gson.fromJson(readFile(path).reader(), typeObject.type)

        val map = HashMap<Key, T>(list.size)
        for (listItem in list) {
            map.put(idExtractor(listItem), listItem)
        }

        return map
    }

    internal fun readFile(path: String): File {
        val resource = resourceLoader.getResource(path)
        if (!resource.isFile) {
            throw FileNotFoundException("$path is not a file.")
        }
        return resource.file
    }


    companion object : KLogging()
}