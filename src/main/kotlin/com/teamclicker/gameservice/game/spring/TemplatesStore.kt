package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.templates.CreatureTemplate
import com.teamclicker.gameservice.game.templates.ItemTemplate
import com.teamclicker.gameservice.game.templates.TemplateNotExistException
import org.springframework.stereotype.Service

/**
 * A store class that holds all template objects.
 *
 * Setting [instance] variable allows accessing from non-spring code.
 */
@Service
class TemplatesStore {
    internal lateinit var itemTemplates: Map<Long, ItemTemplate>
    internal lateinit var creatureTemplates: Map<Long, CreatureTemplate>

    init {
        instance = this
    }

    /**
     * Gets a cloned instance of a template, otherwise throws [TemplateNotExistException]
     */
    fun getItemTemplate(templateId: Long): ItemTemplate {
        return itemTemplates.getOrElse(templateId) {
            throw TemplateNotExistException("Item Template $templateId does not exist")
        }.copy()
    }

    /**
     * Gets a cloned instance of a template, otherwise throws [TemplateNotExistException]
     */
    fun getCreatureTemplate(templateId: Long): CreatureTemplate {
        return creatureTemplates.getOrElse(templateId) {
            throw TemplateNotExistException("Creature Template $templateId does not exist")
        }.copy()
    }

    companion object : KLogging() {
        lateinit var instance: TemplatesStore
            private set
    }
}