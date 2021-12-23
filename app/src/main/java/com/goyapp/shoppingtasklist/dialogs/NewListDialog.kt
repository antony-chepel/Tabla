package com.goyapp.shoppingtasklist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.NewListDialogBinding

object NewListDialog {
    fun showdialog(context: Context,listener:Listener,name: String){
        var dialog : AlertDialog? = null  // Создали инстанцию диалога
        val builder = AlertDialog.Builder(context) // Создан билдер
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context)) // Инициализировали binding класс где находится разметка диалога

        builder.setView(binding.root) // Подключение разметки к билдеру



        binding.apply {
            if(name.isNotEmpty()){
//               button.text = context.getString(R.string.update_text_button) // Устанавливаем новый текст для кнопки если мы редактируем
                button.setText(R.string.update_text_button)
            }
            ednewListName.setText(name) // Передаем название заметки во время редактирования, если новая, то будет пустота
            button.setOnClickListener {
                val listname = ednewListName.text.toString()

                if(listname.isEmpty()){ // Проверка заполнил ли пользователь название списка
                    dialog?.dismiss()

                }else{
                  listener.onClick(listname) // Если не пусто, то сохраняем данные и едит текста и передаем на Activity
                    dialog?.dismiss()
                }

            }
        }
        dialog = builder.create() // Создали диалог
        dialog.window?.setBackgroundDrawable(null) // Убираем ненужный фон помимо нашего
        dialog.show() // Показываем диалог

    }
    interface Listener{ // Создание интерфейса, чтобы передать сохраненное название листа на Activity
        fun onClick(name:String)

    }
}