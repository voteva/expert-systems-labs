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

    var daylightHours: List<Int> = mutableListOf(8, 12, 16, 20)
    var nightTime: List<Int> = mutableListOf(0, 4, 8, 20)

    var offRoad: List<Int> = mutableListOf(0, 0, 5, 10)
    var countrysideRoad: List<Int> = mutableListOf(10, 20, 30, 40)
    var cityRoad: List<Int> = mutableListOf(50, 60, 80, 90)
    var expressway: List<Int> = mutableListOf(80, 80, 90, 100)

    var rainy: List<Int> = mutableListOf(0, 10, 20, 30)
    var mainlyCloudy: List<Int> = mutableListOf(40, 60, 70, 80)
    var sunny: List<Int> = mutableListOf(40, 50, 70, 100)

    var veryLow: List<Int> = mutableListOf(5, 10, 10, 15)
    var low: List<Int> = mutableListOf(10, 20, 20, 30)
    var medium: List<Int> = mutableListOf(40, 50, 60, 70)
    var high: List<Int> = mutableListOf(75, 80, 90, 100)
    var veryHigh: List<Int> = mutableListOf(80, 90, 100, 100)
}