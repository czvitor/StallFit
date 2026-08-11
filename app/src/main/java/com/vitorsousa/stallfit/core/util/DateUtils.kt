package com.vitorsousa.stallfit.core.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/** Central place for the day-bucket math shared by the workout and nutrition modules. */
object DateUtils {

    private val zone: ZoneId get() = ZoneId.systemDefault()

    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()

    fun epochDayToLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    fun startOfDayMillis(epochDay: Long = todayEpochDay()): Long =
        LocalDate.ofEpochDay(epochDay).atStartOfDay(zone).toInstant().toEpochMilli()

    fun endOfDayMillis(epochDay: Long = todayEpochDay()): Long =
        LocalDate.ofEpochDay(epochDay).plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

    fun startOfWeekMillis(): Long {
        val monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return monday.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    fun formatDayLabel(epochDay: Long): String {
        val date = epochDayToLocalDate(epochDay)
        val today = LocalDate.now()
        return when (date) {
            today -> "Hoje"
            today.minusDays(1) -> "Ontem"
            else -> {
                val weekday = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("pt", "BR"))
                "$weekday, ${date.format(DateTimeFormatter.ofPattern("dd/MM"))}"
            }
        }
    }

    fun formatTime(epochMillis: Long): String =
        java.time.Instant.ofEpochMilli(epochMillis)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
}
