package com.goyapp.shoppingtasklist.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.goyapp.shoppingtasklist.databinding.EditItemDialogBinding
import com.goyapp.shoppingtasklist.entities.ShopListItem

object UpdateListDialog {
    fun showdialog(context: Context,item : ShopListItem,listener:Listener){ // Передаем item, по которому будет обновление
        var dialog : AlertDialog? = null  // Создали инстанцию диалога
        val builder = AlertDialog.Builder(context) // Создан билдер
        val binding = EditItemDialogBinding.inflate(LayoutInflater.from(context)) // Инициализировали binding класс где находится разметка диалога

        builder.setView(binding.root) // Подключение разметки к билдеру

         binding.apply {
             edName.setText(item.name) // Значение которые уже были
             edDesc.setText(item.item_info)
             if(item.item_type == 1) edDesc.visibility = View.GONE // Если редактирование libraryitem , то прячем edDesc, проверка благодаря типу
          bUpdate.setOnClickListener {
              if(edName.text.toString().isNotEmpty()) {
                  listener.onClick(item.copy(name = edName.text.toString(), item_info = edDesc.text.toString())) // Перезаписываем обновленные значение

              }
              dialog?.dismiss()
          }
         }
        dialog = builder.create() // Создали диалог
        dialog.window?.setBackgroundDrawable(null) // Убираем ненужный фон помимо нашего
        dialog.show() // Показываем диалог

    }
    interface Listener{ // Создание интерфейса, чтобы передать сохраненное название листа на Activity
        fun onClick(item:ShopListItem)

    }
}