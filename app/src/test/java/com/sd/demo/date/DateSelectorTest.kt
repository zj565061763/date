package com.sd.demo.date

import com.sd.lib.date.FDateSelector
import com.sd.lib.date.fDate
import com.sd.lib.date.selectDayOfMonthWithIndex
import com.sd.lib.date.selectMonthWithIndex
import com.sd.lib.date.selectYearWithIndex
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DateSelectorTest {
   @Test
   fun test() = runTest {
      val startDate = fDate(2020, 10, 10)
      val endDate = fDate(2024, 10, 10)

      val selector = FDateSelector(
         startDate = startDate,
         endDate = endDate,
      )

      fun checkCommon() {
         val state = selector.state
         val date = state.date!!

         // Year
         assertEquals(startDate.year, state.listYear.first())
         assertEquals(endDate.year, state.listYear.last())
         assertEquals(endDate.year - startDate.year + 1, state.listYear.size)

         // Month
         val endMonth = if (date.year == endDate.year) endDate.month else 12
         assertEquals(1, state.listMonth.first())
         assertEquals(endMonth, state.listMonth.last())
         assertEquals(endMonth, state.listMonth.size)

         // Index
         assertEquals(state.listYear.indexOf(date.year), state.indexOfYear)
         assertEquals(state.listMonth.indexOf(date.month), state.indexOfMonth)
         assertEquals(state.listDayOfMonth.indexOf(date.dayOfMonth), state.indexOfDayOfMonth)
      }

      fun checkDayOfMonth(endDayOfMonth: Int) {
         val state = selector.state
         val date = state.date!!
         val end = if (date.year == endDate.year && date.month == endDate.month) {
            endDate.dayOfMonth
         } else {
            endDayOfMonth
         }
         assertEquals(1, state.listDayOfMonth.first())
         assertEquals(end, state.listDayOfMonth.last())
         assertEquals(end, state.listDayOfMonth.size)
      }

      fDate(2023, 1, 1).let { date ->
         selector.setDate(date)
         checkCommon()
      }
      fDate(2024, 1, 1).let { date ->
         selector.setDate(date)
         checkCommon()
      }

      listOf(1, 3, 5, 7, 8, 10, 12)
         .map { fDate(2023, it, 1) }
         .forEach { date ->
            selector.setDate(date)
            checkDayOfMonth(31)
         }
      listOf(4, 6, 9, 11)
         .map { fDate(2023, it, 1) }
         .forEach { date ->
            selector.setDate(date)
            checkDayOfMonth(30)
         }

      listOf(1, 3, 5, 7, 8, 10, 12)
         .map { fDate(2024, it, 1) }
         .forEach { date ->
            selector.setDate(date)
            checkDayOfMonth(31)
         }
      listOf(4, 6, 9, 11)
         .map { fDate(2024, it, 1) }
         .forEach { date ->
            selector.setDate(date)
            checkDayOfMonth(30)
         }

      fDate(2023, 1, 1).let { date ->
         selector.setDate(date)
         assertEquals(date, selector.state.date)

         selector.selectYearWithIndex(1)
         assertEquals(2021, selector.state.date!!.year)

         selector.selectMonthWithIndex(1)
         assertEquals(2, selector.state.date!!.month)

         selector.selectDayOfMonthWithIndex(1)
         assertEquals(2, selector.state.date!!.dayOfMonth)
      }
   }
}