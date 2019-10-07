package ru.bmstu.labs.fuzzy_logic.model.road

/**
 * Road quality, Weather -> Speed
 *
 * Day time: Daylight hours, Night time
 * Road quality: Off road, Countryside road, City road, Expressway
 * Weather: Rainy, Mainly cloudy, Sunny
 *
 * Speed: Very low, Low, Medium, High, Very high
 */
object RoadFuzzySetIntervals {

    var daylightHours: List<Int> = mutableListOf(7, 10, 16, 19)
    var nightTime: List<Int> = mutableListOf(0, 4, 8, 20)

    var offRoad: List<Int> = mutableListOf(0, 5, 10, 15)
    var countrysideRoad: List<Int> = mutableListOf(10, 20, 45, 55)
    var cityRoad: List<Int> = mutableListOf(50, 65, 70, 85)
    var expressway: List<Int> = mutableListOf(80, 85, 95, 100)

    var rainy: List<Int> = mutableListOf(0, 20, 30, 45)
    var mainlyCloudy: List<Int> = mutableListOf(40, 50, 60, 80)
    var sunny: List<Int> = mutableListOf(75, 80, 90, 100)

    var veryLow: List<Int> = mutableListOf(5, 10, 15, 25)
    var low: List<Int> = mutableListOf(20, 25, 35, 45)
    var medium: List<Int> = mutableListOf(40, 50, 70, 80)
    var high: List<Int> = mutableListOf(75, 80, 85, 100)
    var veryHigh: List<Int> = mutableListOf(95, 95, 100, 100)
}