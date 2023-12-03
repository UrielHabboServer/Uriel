package org.urielserv.uriel.game.habbos

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.navigator.UserNavigatorWindowSettingsSchema
import org.urielserv.uriel.game.habbos.currencies.HabboCurrencies
import org.urielserv.uriel.game.habbos.inventory.HabboInventory
import org.urielserv.uriel.game.habbos.navigator.HabboNavigatorWindowSettings
import org.urielserv.uriel.game.habbos.navigator.saved_searches.HabboNavigatorSavedSearches
import org.urielserv.uriel.game.habbos.subscriptions.HabboSubscriptions
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Represents a Habbo user.
 *
 * @param info The information of the Habbo user.
 */
class Habbo internal constructor(
    val info: HabboInfo,
) {

    var client: UrielServerClient? = null

    val currencies = HabboCurrencies(this)
    val inventory = HabboInventory(this)
    val subscriptions = HabboSubscriptions(this)

    lateinit var navigatorWindowSettings: HabboNavigatorWindowSettings
    val navigatorSearches = HabboNavigatorSavedSearches(this)

    init {
        loadNavigatorWindowSettings()
    }

    private fun loadNavigatorWindowSettings() {
        this.navigatorWindowSettings =
            Database.sequenceOf(UserNavigatorWindowSettingsSchema).find { it.userId eq info.id }
                ?: Database.sequenceOf(UserNavigatorWindowSettingsSchema).also {
                    it.add(
                        HabboNavigatorWindowSettings {
                            this.habboInfo = info
                            x = 100
                            y = 100
                            width = 425
                            height = 535
                            isLeftPanelOpen = false
                            resultsMode = 0
                        }
                    )
                }.find { it.userId eq info.id }!!
    }

}