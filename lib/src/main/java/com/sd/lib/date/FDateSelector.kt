package com.sd.lib.date

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class FDateSelector(
   startDate: FDate = FDate(1900, 1, 1),
   endDate: FDate = fCurrentDate(),
) {
   data class State(
      val date: FDate?,
      val listYear: List<Int>,
      val listMonth: List<Int>,
      val listDayOfMonth: List<Int>,
      val indexOfYear: Int,
      val indexOfMonth: Int,
      val indexOfDayOfMonth: Int,
   )

   private val _startDate = startDate
   private val _endDate = endDate.coerceAtLeast(startDate)

   private val _stateFlow = MutableStateFlow(
      State(
         date = null,
         listYear = emptyList(),
         listMonth = emptyList(),
         listDayOfMonth = emptyList(),
         indexOfYear = -1,
         indexOfMonth = -1,
         indexOfDayOfMonth = -1,
      )
   )

   val stateFlow = _stateFlow.asStateFlow()

   val state: State
      get() = stateFlow.value

   val date: FDate?
      get() = state.date

   suspend fun setDate(date: FDate) {
      withContext(Dispatchers.IO) {
         @Suppress("NAME_SHADOWING")
         val date = date.coerceIn(_startDate, _endDate)
         _stateFlow.update { state ->
            if (state.date == date) {
               state
            } else {
               newState(state, date)
            }
         }
      }
   }

   private suspend fun newState(state: State, date: FDate): State {
      val listYear = state.listYear.ifEmpty {
         (_startDate.year.._endDate.year).toList()
      }.also { yield() }

      val listMonth = run {
         val end = if (date.year == _endDate.year) _endDate.month else 12
         if (end == state.listMonth.lastOrNull()) state.listMonth
         else (1..end).toList()
      }.also { yield() }

      val listDayOfMonth = run {
         val end = if (date.year == _endDate.year && date.month == _endDate.month) {
            _endDate.dayOfMonth
         } else {
            date.maxDayOfMonth()
         }
         if (end == state.listDayOfMonth.lastOrNull()) state.listDayOfMonth
         else (1..end).toList()
      }.also { yield() }

      return State(
         date = date,
         listYear = listYear,
         listMonth = listMonth,
         listDayOfMonth = listDayOfMonth,
         indexOfYear = date.year - listYear.first(),
         indexOfMonth = date.month - listMonth.first(),
         indexOfDayOfMonth = date.dayOfMonth - listDayOfMonth.first(),
      )
   }
}

suspend fun FDateSelector.selectYearWithIndex(index: Int) {
   val date = date ?: return
   val year = state.listYear.getOrNull(index) ?: return
   setDate(date.copy(year = year))
}

suspend fun FDateSelector.selectMonthWithIndex(index: Int) {
   val date = date ?: return
   val month = state.listMonth.getOrNull(index) ?: return
   setDate(date.copy(month = month))
}

suspend fun FDateSelector.selectDayOfMonthWithIndex(index: Int) {
   val date = date ?: return
   val dayOfMonth = state.listDayOfMonth.getOrNull(index) ?: return
   setDate(date.copy(dayOfMonth = dayOfMonth))
}