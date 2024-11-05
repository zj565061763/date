package com.sd.lib.date

import java.util.Calendar

class FDate internal constructor(
   val year: Int,
   val month: Int,
   val dayOfMonth: Int,
) : Comparable<FDate> {

   fun copy(
      year: Int = this.year,
      month: Int = this.month,
      dayOfMonth: Int = this.dayOfMonth,
   ): FDate {
      return fDate(
         year = year,
         month = month,
         dayOfMonth = dayOfMonth,
      )
   }

   override fun compareTo(other: FDate): Int {
      this.year.compareTo(other.year).let { if (it != 0) return it }
      this.month.compareTo(other.month).let { if (it != 0) return it }
      return this.dayOfMonth.compareTo(other.dayOfMonth)
   }

   override fun equals(other: Any?): Boolean {
      if (other !is FDate) return false
      return this.year == other.year
         && this.month == other.month
         && this.dayOfMonth == other.dayOfMonth
   }

   override fun hashCode(): Int {
      var result = year
      result = 31 * result + month
      result = 31 * result + dayOfMonth
      return result
   }

   override fun toString(): String {
      return "${year}-${month.leadingZero()}-${dayOfMonth.leadingZero()}"
   }
}

fun fDate(
   year: Int,
   month: Int,
   dayOfMonth: Int,
): FDate {
   val safeYear = year.coerceAtLeast(1)
   val safeMonth = month.coerceIn(1, 12)
   val safeDayOfMonth = dayOfMonth.coerceIn(1, maxDayOfMonth(safeYear, safeMonth))
   return FDate(
      year = safeYear,
      month = safeMonth,
      dayOfMonth = safeDayOfMonth,
   )
}

fun fCurrentDate(): FDate {
   return with(Calendar.getInstance()) {
      FDate(
         year = get(Calendar.YEAR),
         month = get(Calendar.MONTH) + 1,
         dayOfMonth = get(Calendar.DAY_OF_MONTH),
      )
   }
}

fun FDate.maxDayOfMonth(): Int {
   return maxDayOfMonth(year = year, month = month)
}

fun FDate.coerceIn(min: FDate, max: FDate): FDate {
   require(min <= max)
   return coerceAtLeast(min).coerceAtMost(max)
}

fun FDate.coerceAtLeast(min: FDate): FDate {
   if (this.year > min.year) return this
   if (this.year < min.year) return min

   if (this.month > min.month) return this
   if (this.month < min.month) return min

   if (this.dayOfMonth >= min.dayOfMonth) return this
   return min
}

fun FDate.coerceAtMost(max: FDate): FDate {
   if (this.year < max.year) return this
   if (this.year > max.year) return max

   if (this.month < max.month) return this
   if (this.month > max.month) return max

   if (this.dayOfMonth <= max.dayOfMonth) return this
   return max
}

private fun maxDayOfMonth(year: Int, month: Int): Int {
   require(month in 1..12)
   return with(Calendar.getInstance()) {
      set(Calendar.YEAR, year)
      set(Calendar.MONTH, month - 1)
      getActualMaximum(Calendar.DAY_OF_MONTH)
   }
}

private fun Int.leadingZero(size: Int = 2): String {
   return toString().let {
      val repeat = size - it.length
      if (repeat > 0) "0".repeat(repeat) + it
      else it
   }
}