package com.goyapp.shoppingtasklist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName


@Database(entities = [LibraryItem::class,NoteItem::class,ShopListItem::class,ShopListName::class],version = 1) // Перечислили все таблицы которые будут использоватся в базе данных, также версия, она меняется в случае изменений в одной из них
 abstract class MainDatabase : RoomDatabase() { // Класс создания базы данных
    abstract fun getDao() :Dao // Подключение интерфейса Dao

  companion object{  // Возможность использовать функцию, переменные в другом классе без инициализации класса
   @Volatile // ! Делает доступ к примеру инстанцию нашей базы данных для все потоков
   private var INSTANCE : MainDatabase? = null
   fun getDatabase(context:Context) : MainDatabase{ // Функция для получения инстанции базы данных
      return INSTANCE ?: synchronized(this){// Когда мы будем пытатся получить инстанцию базы данных, она вернет если она создана или нет и хочет получить доступ другой класс или поток, то благодаря sync блокируется
       val instance = Room.databaseBuilder(  // Проверка если где то на каком то потоке уже используется база данных,создание бд
        context.applicationContext,
        MainDatabase::class.java,
        "shoppinglist.db"
       ).build()
      instance  // Вернули instance
      }

   }

  }
}