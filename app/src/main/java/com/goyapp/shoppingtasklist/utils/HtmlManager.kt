package com.goyapp.shoppingtasklist.utils

import android.text.Html
import android.text.Spanned

object HtmlManager {
    fun getFromHtml(text:String) : Spanned{ // Функция которая содержит весь текст из базы данных с кодировкой,стилями,цветом и ткд, наследуемся от класса Spanned чтобы мы видели данные без Html тегов
        return if(android.os.Build.VERSION.SDK_INT<=android.os.Build.VERSION_CODES.N){ // Проверка на версию андроид, так как реализация преобразования текста разный
            Html.fromHtml(text) // Наш текст который в Html формате, превращаем в Spannable,чтобы оно применилось для нашего EditText

        }else{
          Html.fromHtml(text,Html.FROM_HTML_MODE_COMPACT)
        }

    }
    fun toHtml(text: Spanned) : String{ // Берем наш класс Spanned где указали стиль,текст и ткд, нам выдаст текст, это и есть Html, чтобы все применилось и сохранилось(стили,цвета)

        return if(android.os.Build.VERSION.SDK_INT<=android.os.Build.VERSION_CODES.N){ // Проверка на версию андроид, так как реализация преобразования текста разный
            Html.toHtml(text) // Наш текст который в Spannable формате, превращаем в Html,чтобы мы могли использовать

        }else{
            Html.toHtml(text,Html.FROM_HTML_MODE_COMPACT)
        }
    }
}