package org.urielserv.uriel.packets.outgoing.users.currencies

import org.urielserv.uriel.CurrencyManager
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserCurrencyPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserCurrency

    override suspend fun construct() {
        val currencies = CurrencyManager.getCurrencies().filter { it.isSeasonal }

        appendInt(currencies.size)
        for (currency in currencies) {
            val habboCurrency = habbo.currencies.getCurrency(currency) ?: continue

            appendInt(currency.nitroId)
            appendInt(habboCurrency.amount)
        }
    }

}