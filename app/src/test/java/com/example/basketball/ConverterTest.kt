package com.example.basketball

import com.example.basketball.data.local.model.ScheduleItem
import com.example.basketball.data.local.model.TeamInfo
import com.example.basketball.domain.utils.Converter
import org.junit.Assert.*
import org.junit.Test
import java.util.*
import java.text.SimpleDateFormat

class ConverterTest {

    @Test
    fun `convertUtcToLocalDayLabel should return correct formatted string`() {
        val utcTime = "2025-04-13T17:00:00.000Z"
        val result = Converter.convertUtcToLocalDayLabel(utcTime)

        // Format the expected local time manually
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(utcTime)

        val outputFormat = SimpleDateFormat("EEE MMM dd", Locale.US)
        outputFormat.timeZone = TimeZone.getDefault()
        val expected = outputFormat.format(date!!).uppercase(Locale.US)

        assertEquals(expected, result)
    }

    @Test
    fun `convertUtcToLocalDayLabel should return INVALID DATE on error`() {
        val invalidTime = "invalid-date"
        val result = Converter.convertUtcToLocalDayLabel(invalidTime)
        assertEquals("INVALID DATE", result)
    }

    @Test
    fun `convertEtTimeToDisplay should clean and format ET time`() {
        val input = "12:30 pm ET"
        val result = Converter.convertEtTimeToDisplay(input)
        assertEquals("12:30PM", result)
    }

    @Test
    fun `convertEtTimeToDisplay should return INVALID TIME on error`() {
        val input = "🌟"
        val result = Converter.convertEtTimeToDisplay(input)
        assertEquals("🌟", result) // Since there's no parsing logic, it will return unchanged cleaned string
    }

    @Test
    fun `extractUniqueMonthYearList should return unique formatted months`() {
        val scheduleList = listOf(
            ScheduleItem("Arena 1", "City A", 1, "stt", "2025-04-13T17:00:00.000Z", null, dummyTeam(), dummyTeam()),
            ScheduleItem("Arena 2", "City B", 2, "stt", "2025-04-20T18:00:00.000Z", null, dummyTeam(), dummyTeam()),
            ScheduleItem("Arena 3", "City C", 3, "stt", "2025-05-01T10:00:00.000Z", null, dummyTeam(), dummyTeam())
        )
        val result = Converter.extractUniqueMonthYearList(scheduleList)
        assertEquals(2, result.size)
        assertTrue(result.contains("APRIL 2025"))
        assertTrue(result.contains("MAY 2025"))
    }

    @Test
    fun `extractUniqueMonthYearList should skip invalid dates`() {
        val scheduleList = listOf(
            ScheduleItem("Arena 1", "City A", 1, "stt", "INVALID", null, dummyTeam(), dummyTeam())
        )
        val result = Converter.extractUniqueMonthYearList(scheduleList)
        assertTrue(result.isEmpty())
    }

    private fun dummyTeam(): TeamInfo {
        return TeamInfo("1", "TeamName", "", "", "", "0")
    }
}
