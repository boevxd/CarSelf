package dm.com.carlog.util

import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return !selectedDate.isAfter(LocalDate.now())
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}