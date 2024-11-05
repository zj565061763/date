package com.sd.demo.date

import com.sd.lib.date.fCurrentDate
import com.sd.lib.date.fDate
import com.sd.lib.date.maxDayOfMonth
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateTest {
   @Test
   fun `test date`() {
      fDate(2024, 10, 20).run {
         assertEquals(2024, year)
         assertEquals(10, month)
         assertEquals(20, dayOfMonth)
      }
   }

   @Test
   fun `test leading zero`() {
      val dateStr = fDate(2024, 1, 1).toString()
      assertEquals("2024-01-01", dateStr)
   }

   @Test
   fun `test copy`() {
      val date = fDate(2024, 10, 20)

      (date.year + 1).let { year ->
         assertEquals(year, date.copy(year = year).year)
      }

      (date.month + 1).let { month ->
         assertEquals(month, date.copy(month = month).month)
      }

      (date.dayOfMonth + 1).let { dayOfMonth ->
         assertEquals(dayOfMonth, date.copy(dayOfMonth = dayOfMonth).dayOfMonth)
      }

      date.copy().let { copy ->
         assertEquals(date.year, copy.year)
         assertEquals(date.month, copy.month)
         assertEquals(date.dayOfMonth, copy.dayOfMonth)
      }
   }

   @Test
   fun `test fCurrentDate`() {
      val calendar = Calendar.getInstance()
      val date = fCurrentDate()
      assertEquals(calendar.get(Calendar.YEAR), date.year)
      assertEquals(calendar.get(Calendar.MONTH), date.month - 1)
      assertEquals(calendar.get(Calendar.DAY_OF_MONTH), date.dayOfMonth)
   }

   @Test
   fun `test equals`() {
      val date = fDate(2024, 10, 20)
      assertEquals(true, date == date.copy())
      assertEquals(false, date == date.copy(year = date.year + 1))
      assertEquals(false, date == date.copy(month = date.month + 1))
      assertEquals(false, date == date.copy(dayOfMonth = date.dayOfMonth + 1))
      assertEquals(
         false,
         date == date.copy(
            year = date.year + 1,
            month = date.month + 1,
            dayOfMonth = date.dayOfMonth + 1,
         )
      )
   }

   @Test
   fun `test safe start`() {
      fDate(0, 0, 0).run {
         assertEquals(1, year)
         assertEquals(1, month)
         assertEquals(1, dayOfMonth)
      }
   }

   @Test
   fun `test safe end`() {
      fDate(0, 100, 0).run {
         assertEquals(12, month)
      }

      listOf(1, 3, 5, 7, 8, 10, 12)
         .map { fDate(2024, it, 100) }
         .forEach { date ->
            assertEquals(31, date.dayOfMonth)
         }

      listOf(4, 6, 9, 11)
         .map { fDate(2024, it, 100) }
         .forEach { date ->
            assertEquals(30, date.dayOfMonth)
         }

      fDate(2024, 2, 100).run {
         assertEquals(29, dayOfMonth)
      }

      fDate(2023, 2, 100).run {
         assertEquals(28, dayOfMonth)
      }
   }

   @Test
   fun `test maxDayOfMonth`() {
      listOf(1, 3, 5, 7, 8, 10, 12)
         .map { fDate(2024, it, 1) }
         .forEach { date ->
            assertEquals(31, date.maxDayOfMonth())
         }

      listOf(4, 6, 9, 11)
         .map { fDate(2024, it, 1) }
         .forEach { date ->
            assertEquals(30, date.maxDayOfMonth())
         }

      fDate(2024, 2, 1).run {
         assertEquals(29, maxDayOfMonth())
      }

      fDate(2023, 2, 1).run {
         assertEquals(28, maxDayOfMonth())
      }
   }
}