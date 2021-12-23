package com.goyapp.shoppingtasklist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.goyapp.shoppingtasklist.R

object FragmentManager { // Создали обькт, чтобы использовать функции в нем без инициализации класса
    var currentFrag:BaseFragment? =null // Будем сохранять в переменную информацию, какой фрагмент именно использовать, наследуется от базового фрагмента

    fun setFragment(newFrag:BaseFragment,activity:AppCompatActivity){ // Класс который будет устанавливать Фрагмент или заменять текущий на другой
       val transaction = activity.supportFragmentManager.beginTransaction() // transaction благодаря которому можем добавлять,заменять,удалять фрагмент
        transaction.replace(R.id.placeholder,newFrag) // Замена на нужный фрашмент и указываем id framelayout где будет отображатся сам фрагмент
        transaction.commit() // Применяем, без этого ничего не сработает
        currentFrag = newFrag // Записываем наш выбранный фрагмент,таким образом система будет знать на какой именно фрагмент переключится
    }
}