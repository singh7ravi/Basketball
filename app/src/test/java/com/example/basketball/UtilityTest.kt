package com.example.basketball
import com.example.basketball.data.local.model.ScheduleItem
import com.example.basketball.data.local.model.Team
import com.example.basketball.data.local.model.TeamInfo
import com.example.basketball.domain.utils.FilterType
import com.example.basketball.domain.utils.Utility
import org.junit.Assert.*
import org.junit.Test

class UtilityTest {

    private val scheduleItems = listOf(
        ScheduleItem("ArenaOne", "New York", 1, "stt", "2025-05-10T10:00:00Z", null, TeamInfo("1", "TeamA", "", "", "", "0"), TeamInfo("2", "TeamB", "", "", "", "0")),
        ScheduleItem("ArenaTwo", "Los Angeles", 2, "stt", "2025-05-11T12:00:00Z", null, TeamInfo("3", "TeamC", "", "", "", "0"), TeamInfo("4", "TeamD", "", "", "", "0")),
        ScheduleItem("ArenaThree", "Chicago", 3, "stt", "2025-05-12T14:00:00Z", null, TeamInfo("5", "TeamE", "", "", "", "0"), TeamInfo("6", "TeamF", "", "", "", "0"))
    )

    private val teamsList = listOf(
        Team(tid = "1610612764", tn = "Wizards", ta = "WAS", color = "White", logo = "logo_a"),
        Team(tid = "1610612765", tn = "Pistons", ta = "DET", color = "Black", logo = "logo_b"),
        Team(tid = "1610612766", tn = "Hornets", ta = "CHA", color = "Gray", logo = "logo_c"),
    )

    @Test
    fun `filterUsersItemsByNamePrefix should return matching arenas or cities`() {
        val result = Utility.filterUsersItemsByNamePrefix(scheduleItems, "ArenaT")
        assertEquals(2, result.size)
        assertEquals("ArenaTwo", result[0].arena_name)

        val resultCity = Utility.filterUsersItemsByNamePrefix(scheduleItems, "New")
        assertEquals(1, resultCity.size)
        assertEquals("New York", resultCity[0].arena_city)
    }

    @Test
    fun `getFilterListByOption should return filtered items by st`() {
        val filters = listOf(FilterType.FUTURE, FilterType.LIVE)
        val result = Utility.getFilterListByOption(scheduleItems, filters)
        assertEquals(2, result.size)
        assertTrue(result.all { it.st in listOf(1, 2) })
    }

    @Test
    fun `filterUsersByGameType should return correct enums`() {
        val filterStrings = listOf("FUTURE GAME", "LIVE GAME", "INVALID GAME")
        val result = Utility.filterUsersByGameType(filterStrings)
        assertEquals(2, result.size)
        assertTrue(result.contains(FilterType.FUTURE))
        assertTrue(result.contains(FilterType.LIVE))
    }

    @Test
    fun `getTeamLogo should return correct logo or empty`() {
        val logo = Utility.getTeamLogo("1610612764", teamsList)
        assertEquals("logo_a", logo)

        val missingLogo = Utility.getTeamLogo("99", teamsList)
        assertEquals("", missingLogo)
    }

    @Test
    fun `getTeam should return correct team name or empty`() {
        val name = Utility.getTeam("1610612766", teamsList)
        assertEquals("Hornets", name)

        val missingName = Utility.getTeam("1610612765", teamsList)
        assertEquals("Pistons", missingName)
    }
}
