package com.goyapp.shoppingtasklist.utils

import android.view.MotionEvent
import android.view.View

class MyTouchListener : View.OnTouchListener { // Создаем наш TouchListener для того чтобы взаимодейстовать с элементов, перетаскивать по экрану к примеру
    var xDelta  = 0.0f //Переменные оси по которым мы двигем
    var yDelta  = 0.0f
    override fun onTouch(view: View, event: MotionEvent?): Boolean {
        when(event?.action){ // Проверка на какое действие выполняется с элементов, отпустили его,удерживаем
            MotionEvent.ACTION_DOWN->{// // Запускается каждый раз как мы отпускаем обьект

                 xDelta = view.x - event.rawX // Узнаем куда мы подвинули, настоящая позиция где был элемент минус куда мы двигаем
                 yDelta = view.y - event.rawY // Узнаем куда мы подвинули, настоящая позиция где был элемент минус куда мы двигаем
            }

            MotionEvent.ACTION_MOVE->{ // Запускается каждый раз как мы двигаем элемента,слушатель движения
                view.x = xDelta + event.rawX // Показывает текущее движение элемента
                view.y = yDelta + event.rawY


            }
        }
        return true
    }
}