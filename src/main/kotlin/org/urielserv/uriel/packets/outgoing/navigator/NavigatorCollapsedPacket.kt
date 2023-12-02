package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorCollapsedPacket : Packet() {

    override val packetId = OutgoingPacketIDs.NavigatorCollapsed
    
    override suspend fun construct() {
        appendInt(46)
        appendString("new_ads")
        appendString("friend_finding")
        appendString("staffpicks")
        appendString("with_friends")
        appendString("with_rights")
        appendString("query")
        appendString("recommended")
        appendString("my_groups")
        appendString("favorites")
        appendString("history")
        appendString("top_promotions")
        appendString("campaign_target")
        appendString("friends_rooms")
        appendString("groups")
        appendString("metadata")
        appendString("history_freq")
        appendString("highest_score")
        appendString("competition")
        appendString("category__Agencies")
        appendString("category__Role Playing")
        appendString("category__Global Chat & Discussi")
        appendString("category__GLOBAL BUILDING AND DE")
        appendString("category__global party")
        appendString("category__global games")
        appendString("category__global fansite")
        appendString("category__global help")
        appendString("category__Trading")
        appendString("category__global personal space")
        appendString("category__Habbo Life")
        appendString("category__TRADING")
        appendString("category__global official")
        appendString("category__global trade")
        appendString("category__global reviews")
        appendString("category__global bc")
        appendString("category__global personal space")
        appendString("eventcategory__Hottest Events")
        appendString("eventcategory__Parties & Music")
        appendString("eventcategory__Role Play")
        appendString("eventcategory__Help Desk")
        appendString("eventcategory__Trading")
        appendString("eventcategory__Games")
        appendString("eventcategory__Debates & Discuss")
        appendString("eventcategory__Grand Openings")
        appendString("eventcategory__Friending")
        appendString("eventcategory__Jobs")
        appendString("eventcategory__Group Events")
    }
    
}