package org.urielserv.uriel.game.habbos.currencies

import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.urielserv.uriel.CurrencyManager
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserCurrenciesSchema
import org.urielserv.uriel.extensions.scheduleRepeating
import org.urielserv.uriel.extensions.toDuration
import org.urielserv.uriel.game.currencies.UrielCurrency
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.tick_loop.jobs.RepeatingJob
import kotlin.time.Duration.Companion.seconds


class HabboCurrencies(
    val habbo: Habbo
) {

    private val _currencies = mutableMapOf<Int, HabboCurrency>()
    val currencies: List<HabboCurrency>
        get() = _currencies.values.toList()

    private val currencyTimerTasks = mutableMapOf<HabboCurrency, RepeatingJob>()

    init {
        for (currency in CurrencyManager.currencies.values) {
            var habboCurrency = Database.sequenceOf(UserCurrenciesSchema)
                .find { (it.userId eq habbo.info.id) and (it.currencyId eq currency.id) }

            if (habboCurrency != null)
                _currencies[currency.id] = habboCurrency
            else {
                val newCurrency = HabboCurrency {
                    this.currency = currency
                    this.habboInfo = this@HabboCurrencies.habbo.info
                    this.amount = currency.default
                }

                Database.sequenceOf(UserCurrenciesSchema).add(newCurrency)
                _currencies[currency.id] = newCurrency

                habboCurrency = newCurrency
            }

            if (currency.autoTimerTime.toDuration() <= 0.seconds) continue

            currencyTimerTasks[habboCurrency] = scheduleRepeating(
                interval = currency.autoTimerTime.toDuration(),
                delay = currency.autoTimerTime.toDuration()
            ) {
                habboCurrency.modifyBy(currency.autoTimerAmount)
            }
        }
    }

    fun getCurrency(currency: UrielCurrency): HabboCurrency? {
        return _currencies[currency.id]
    }

    fun getById(id: Int): HabboCurrency? {
        return _currencies[id]
    }

    fun getByName(name: String): HabboCurrency? {
        return _currencies.values.firstOrNull { it.currency.name == name || "${it.currency.name}s" == name }
    }

    fun unload() {
        for ((_, currency) in _currencies) {
            currency.flushChanges()
        }

        for ((_, task) in currencyTimerTasks) {
            task.isCancelled = true
        }
    }

}