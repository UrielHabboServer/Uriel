package org.urielserv.uriel.game.navigator

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.navigator.NavigatorFlatCategoriesSchema
import org.urielserv.uriel.database.schemas.navigator.NavigatorPublicCategoriesSchema
import org.urielserv.uriel.game.navigator.searches.filters.*
import org.urielserv.uriel.game.navigator.tabs.NavigatorHotelViewTab
import org.urielserv.uriel.game.navigator.tabs.NavigatorMyViewTab
import org.urielserv.uriel.game.navigator.tabs.NavigatorOfficialViewTab
import org.urielserv.uriel.game.navigator.tabs.NavigatorTab

class UrielNavigatorManager {

    private val tabs = mutableListOf<NavigatorTab>()
    private val filters = mutableListOf<NavigatorSearchFilter>()

    private val publicCategories = mutableListOf<NavigatorPublicCategory>()
    private val flatCategories = mutableListOf<NavigatorFlatCategory>()

    init {
        loadTabs()
        loadFilters()

        loadPublicCategories()
        loadFlatCategories()
    }

    private fun loadTabs() {
        registerTab(NavigatorOfficialViewTab())
        registerTab(NavigatorHotelViewTab())
        registerTab(NavigatorMyViewTab())
    }

    private fun loadFilters() {
        registerFilter(NavigatorAnythingSearchFilter())
        registerFilter(NavigatorRoomNameSearchFilter())
        registerFilter(NavigatorOwnerSearchFilter())
        registerFilter(NavigatorTagSearchFilter())
    }

    private fun loadPublicCategories() {
        Database.sequenceOf(NavigatorPublicCategoriesSchema)
            .forEach {
                publicCategories.add(it)
            }
    }

    private fun loadFlatCategories() {
        Database.sequenceOf(NavigatorFlatCategoriesSchema)
            .forEach {
                flatCategories.add(it)
            }
    }

    fun getTab(tabId: String): NavigatorTab? {
        return tabs.firstOrNull { it.id == tabId }
    }

    fun getTabs(): List<NavigatorTab> {
        return tabs.toList()
    }

    fun registerTab(tab: NavigatorTab) {
        tabs.add(tab)
    }

    fun unregisterTab(tab: NavigatorTab) {
        tabs.remove(tab)
    }

    fun unregisterTab(tabId: String) {
        tabs.removeIf { it.id == tabId }
    }

    fun getFilter(filterId: String): NavigatorSearchFilter? {
        return filters.firstOrNull { it.id == filterId }
    }

    fun registerFilter(filter: NavigatorSearchFilter) {
        filters.add(filter)
    }

    fun getPublicCategories(): List<NavigatorPublicCategory> {
        return publicCategories.toList()
    }

    fun getPublicCategory(id: Int): NavigatorPublicCategory? {
        return publicCategories.firstOrNull { it.id == id }
    }

    fun getFlatCategories(): List<NavigatorFlatCategory> {
        return flatCategories.toList()
    }

    fun getFlatCategory(id: Int): NavigatorFlatCategory? {
        return flatCategories.firstOrNull { it.id == id }
    }

}