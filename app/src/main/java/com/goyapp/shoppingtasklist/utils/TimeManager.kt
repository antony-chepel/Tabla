package com.goyapp.shoppingtasklist.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
      fun getCurrentTime() : String{ // Функция для получения текущего времени создания заметки, указываем определенный формат
        val formatter = SimpleDateFormat("hh:mm.ss - yyyy/MM/dd", Locale.getDefault()) // Создали формат временеи, в каком виде оно будет
        return formatter.format(Calendar.getInstance().time) // Возвращаем готовый формат с реальным временем
    }
}