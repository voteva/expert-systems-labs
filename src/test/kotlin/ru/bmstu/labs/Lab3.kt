package ru.bmstu.labs

import ru.bmstu.labs.fuzzy_logic.MamdaniAlgorithm
import ru.bmstu.labs.fuzzy_logic.model.*
import ru.bmstu.labs.fuzzy_logic.model.road.RoadFuzzySet
import ru.bmstu.labs.fuzzy_logic.model.road.RoadFuzzySetIntervals
import kotlin.test.Test

/**
 * Day time, Road quality, Weather -> Speed
 *
 * Day time: Daylight hours, Night time
 * Road quality: Off road, Countryside road, City road, Expressway
 * Weather: Rainy, Mainly cloudy, Sunny
 *
 * Speed: Very low, Low, Medium, High, Very high
 */
class Lab3 {

    @Test
    fun fuzzyLogicRoad() {
        val rules: List<Rule> = mutableListOf(
                Rule(
                        mutableListOf(
                                Condition("Off road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.offRoad)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Rainy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.rainy))),
                        mutableListOf(
                                Conclusion("Very low", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.veryLow)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Off road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.offRoad)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Sunny", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.sunny))),
                        mutableListOf(
                                Conclusion("Low", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.low)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Countryside road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.countrysideRoad)),
                                Condition("Night time", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.nightTime)),
                                Condition("Rainy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.rainy))),
                        mutableListOf(
                                Conclusion("Very low", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.veryLow)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Countryside road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.countrysideRoad)),
                                Condition("Night time", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.nightTime)),
                                Condition("Mainly cloudy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.mainlyCloudy))),
                        mutableListOf(
                                Conclusion("Low", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.low)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Countryside road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.countrysideRoad)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Sunny", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.sunny))),
                        mutableListOf(
                                Conclusion("Medium", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.medium)))
                ),
                Rule(
                        mutableListOf(
                                Condition("City road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.cityRoad)),
                                Condition("Night time", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.nightTime)),
                                Condition("Rainy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.rainy))),
                        mutableListOf(
                                Conclusion("Low", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.low)))
                ),
                Rule(
                        mutableListOf(
                                Condition("City road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.cityRoad)),
                                Condition("Night time", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.nightTime)),
                                Condition("Mainly cloudy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.mainlyCloudy))),
                        mutableListOf(
                                Conclusion("Medium", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.medium)))
                ),
                Rule(
                        mutableListOf(
                                Condition("City road", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.cityRoad)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Sunny", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.sunny))),
                        mutableListOf(
                                Conclusion("High", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.high)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Expressway", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.expressway)),
                                Condition("Night time", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.nightTime)),
                                Condition("Rainy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.rainy))),
                        mutableListOf(
                                Conclusion("Medium", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.medium)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Expressway", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.expressway)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Mainly cloudy", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.mainlyCloudy))),
                        mutableListOf(
                                Conclusion("High", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.high)))
                ),
                Rule(
                        mutableListOf(
                                Condition("Expressway", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.expressway)),
                                Condition("Daylight hours", Variable(1), RoadFuzzySet(RoadFuzzySetIntervals.daylightHours)),
                                Condition("Sunny", Variable(2), RoadFuzzySet(RoadFuzzySetIntervals.sunny))),
                        mutableListOf(
                                Conclusion("Very high", Variable(0), RoadFuzzySet(RoadFuzzySetIntervals.veryHigh)))
                )
        )

        val algorithm = MamdaniAlgorithm(rules, arrayOf(80.0, 12.0, 90.0))
        val result = algorithm.run()

        println("Result for speed: $result")
    }
}
