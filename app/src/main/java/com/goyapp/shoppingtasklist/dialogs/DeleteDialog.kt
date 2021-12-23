package com.goyapp.shoppingtasklist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.goyapp.shoppingtasklist.databinding.DeleteDialogBinding
import com.goyapp.shoppingtasklist.databinding.NewListDialogBinding

object DeleteDialog {
    fun showdialog(context: Context,listener:Listener){
        var dialog : AlertDialog? = null  // Создали инстанцию диалога
        val builder = AlertDialog.Builder(context) // Создан билдер
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context)) // Инициализировали binding класс где находится разметка диалога

        builder.setView(binding.root) // Подключение разметки к билдеру

        binding.apply {
            bDelete.setOnClickListener {
               listener.onClick()
                dialog?.dismiss()

            }

            bCancel.setOnClickListener {
                dialog?.dismiss()
            }

        }
        dialog = builder.create() // Создали диалог
        dialog.window?.setBackgroundDrawable(null) // Убираем ненужный фон помимо нашего
        dialog.show() // Показываем диалог

    }
    interface Listener{ // Создание интерфейса, чтобы передать сохраненное название листа на Activity
        fun onClick()

    }
}