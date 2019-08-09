package com.synowkrz.housemanager.babyTask.model

enum class EventType {
    FEED, DIAPER, SLEEP, BATH, PILLS
}

data class BasicBabyEvent(val eventType: EventType)
{
    val iconResource: String = when(eventType) {
        EventType.FEED -> "feed"
        EventType.DIAPER -> "diaper"
        EventType.SLEEP -> "sleep"
        EventType.BATH -> "bath"
        EventType.PILLS -> "pills"
    }
}

data class BabyEvent(val eventType:  EventType, var startTime : Long, var profile: String) {
    var babyEvent = BasicBabyEvent(eventType)
    var subType: String? = null
    var endTime : Long? = null
    var duration: Long? = null
    var amount: Int = 0
}