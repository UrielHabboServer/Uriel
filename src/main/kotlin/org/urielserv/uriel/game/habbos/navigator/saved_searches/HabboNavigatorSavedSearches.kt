package org.urielserv.uriel.game.habbos.navigator.saved_searches

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.navigator.UserNavigatorSavedSearchesSchema
import org.urielserv.uriel.game.habbos.Habbo

class HabboNavigatorSavedSearches(
    val habbo: Habbo
) {

    private val savedSearches = mutableListOf<NavigatorSavedSearch>()

    init {
        Database.sequenceOf(UserNavigatorSavedSearchesSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { savedSearches.add(it) }

        if (!hasSavedSearch("official-root")) {
            registerSavedSearch(
                NavigatorSavedSearch {
                    searchCode = "official-root"
                    filter = ""
                }
            )
        }

        if (!hasSavedSearch("my-root")) {
            registerSavedSearch(
                NavigatorSavedSearch {
                    searchCode = "my"
                    filter = ""
                }
            )
        }

        if (!hasSavedSearch("favorites")) {
            registerSavedSearch(
                NavigatorSavedSearch {
                    searchCode = "favorites"
                    filter = ""
                }
            )
        }
    }

    fun hasSavedSearch(searchCode: String): Boolean =
        savedSearches.any { it.searchCode == searchCode }

    fun getSavedSearch(searchCode: String): NavigatorSavedSearch? =
        savedSearches.firstOrNull { it.searchCode == searchCode }

    fun getSavedSearches(): List<NavigatorSavedSearch> =
        savedSearches.toList()

    fun registerSavedSearch(savedSearch: NavigatorSavedSearch) {
        savedSearches.add(savedSearch)

        Database.sequenceOf(UserNavigatorSavedSearchesSchema)
            .add(NavigatorSavedSearch {
                habboInfo = this@HabboNavigatorSavedSearches.habbo.info
                searchCode = savedSearch.searchCode
                filter = savedSearch.filter
            })
    }

}