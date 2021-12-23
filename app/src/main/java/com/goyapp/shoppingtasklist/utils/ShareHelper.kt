package com.goyapp.shoppingtasklist.utils

import android.content.Intent
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import java.lang.StringBuilder

object ShareHelper {
    fun shareShopList(shopList : List<ShopListItem>,listName : String) : Intent{ // Функция которая будет запускатся, когда хотим поделится переданным списком покупок в другом приложении, возвращать будет Intent, чтобы запустилось окошко с выбором приложения
        val intent = Intent(Intent.ACTION_SEND) // Выбераем из констант что мы будем делать
        intent.type = "text/plane" // Тип текст
        intent.apply { //Помещаем в intent сформированные данные
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList,listName)) // Передача в виде текста

        }
        return intent

    }

    private fun makeShareText(shopList : List<ShopListItem>,listName : String) : String{ // Функция,чтобы сделать текст красивым, не в одну строку
        val sBuilder = StringBuilder() // Стрингбилдер, чтобы оформлять и создавать шаблон текста
        sBuilder.append("<<$listName>>") // Название списка
        sBuilder.append("\n") // Перейти на следущую строку
        var counter = 0 // Чтобы записывать сколько элементов будет

      shopList.forEach {
         sBuilder.append("${++counter} - ${it.name} (${it.item_info})") // Установка нумерации, название списка и дополнительная инофрмация
          sBuilder.append("\n") // Перейти на следущую строку


      }
        return sBuilder.toString()
    }
}