package com.sd.demo.date

import com.sd.lib.date.coerceAtLeast
import com.sd.lib.date.coerceAtMost
import com.sd.lib.date.fDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DateCompareTest {
   @Test
   fun `test compareTo`() {
      val date = fDate(2024, 10, 20)
      assertEquals(0, date.compareTo(date.copy()))

      (-1).let { result ->
         assertEquals(result, date.compareTo(date.copy(year = date.year + 2)))
         assertEquals(result, date.compareTo(date.copy(month = date.month + 2)))
         assertEquals(result, date.compareTo(date.copy(dayOfMonth = date.dayOfMonth + 2)))
         assertEquals(
            result,
            date.compareTo(
               date.copy(
                  year = date.year + 2,
                  month = date.month + 2,
                  dayOfMonth = date.dayOfMonth + 2,
               )
            )
         )
      }

      (1).let { result ->
         assertEquals(result, date.compareTo(date.copy(year = date.year - 2)))
         assertEquals(result, date.compareTo(date.copy(month = date.month - 2)))
         assertEquals(result, date.compareTo(date.copy(dayOfMonth = date.dayOfMonth - 2)))
         assertEquals(
            result,
            date.compareTo(
               date.copy(
                  year = date.year - 2,
                  month = date.month - 2,
                  dayOfMonth = date.dayOfMonth - 2,
               )
            )
         )
      }
   }

   @Test
   fun `test coerceAtLeast`() {
      val date = fDate(2024, 10, 20)

      listOf(
         date.copy(year = date.year + 1),
         date.copy(month = date.month + 1),
         date.copy(dayOfMonth = date.dayOfMonth + 1),
         date.copy(
            year = date.year + 1,
            month = date.month + 1,
            dayOfMonth = date.dayOfMonth + 1,
         ),
      ).forEach { item ->
         assertEquals(item, date.coerceAtLeast(item))
      }

      listOf(
         date.copy(year = date.year - 1),
         date.copy(month = date.month - 1),
         date.copy(dayOfMonth = date.dayOfMonth - 1),
         date.copy(
            year = date.year - 1,
            month = date.month - 1,
            dayOfMonth = date.dayOfMonth - 1,
         ),
      ).forEach { item ->
         assertEquals(date, date.coerceAtLeast(item))
      }
   }

   @Test
   fun `test coerceAtMost`() {
      val date = fDate(2024, 10, 20)

      listOf(
         date.copy(year = date.year - 1),
         date.copy(month = date.month - 1),
         date.copy(dayOfMonth = date.dayOfMonth - 1),
         date.copy(
            year = date.year - 1,
            month = date.month - 1,
            dayOfMonth = date.dayOfMonth - 1,
         ),
      ).forEach { item ->
         assertEquals(item, date.coerceAtMost(item))
      }

      listOf(
         date.copy(year = date.year + 1),
         date.copy(month = date.month + 1),
         date.copy(dayOfMonth = date.dayOfMonth + 1),
         date.copy(
            year = date.year + 1,
            month = date.month + 1,
            dayOfMonth = date.dayOfMonth + 1,
         ),
      ).forEach { item ->
         assertEquals(date, date.coerceAtMost(item))
      }
   }
}