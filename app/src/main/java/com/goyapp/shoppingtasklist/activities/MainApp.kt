package com.goyapp.shoppingtasklist.activities

import android.app.Application
import com.goyapp.shoppingtasklist.db.MainDatabase

class MainApp: Application() { // Создаем базу данных на уровне всего приложения, унаследуемся от класса Application, который позвоялет инициализировать на уровне всего приложения, также получить доступ в любом месте приложения
    val database by lazy { MainDatabase.getDatabase(this) } // В блоке lazy, чтобы создать инстанцию, 1 раз, в случае запуска этого кода повторно, оно не запустится, так как дб будет инициализировано
}