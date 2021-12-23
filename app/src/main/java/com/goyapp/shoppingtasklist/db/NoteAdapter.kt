package com.goyapp.shoppingtasklist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.NoteListItemBinding
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.utils.HtmlManager

class NoteAdapter( private  val listener:Listener) : ListAdapter<NoteItem,NoteAdapter.NoteItemHolder>(itemComparator()) {

    class NoteItemHolder(view: View) :RecyclerView.ViewHolder(view) { // Класс хранит ссылку на разметку, на каждый Item
        private val binding = NoteListItemBinding.bind(view) // Наш view с разметкой превращается в binding
        fun setData(noteItem: NoteItem,listener:Listener) = with(binding){   // Благодаря этой функции, заполняем элементы в разметке
                tvTitle.text = noteItem.title
                tvDesc.text =  HtmlManager.getFromHtml(noteItem.desc_content).trim() // Чтобы показало без Html тегов,также убрали пробелы с помощью trim()
                tvTime.text = noteItem.note_time
                imDelete.setOnClickListener {
                    listener.deleteItem(noteItem.id!!) // Добавили наш listener в адаптер
                }
               itemView.setOnClickListener { // Создание слушателя на весь итем, внутри которого наш метод с интерфейса
                   listener.onClickItem(noteItem)
               }
        }
        companion object{
            fun create(parent: ViewGroup) : NoteItemHolder{ // Выдает инициализированный класс NoteItemHolder который в себе хранит ссылку на загруженную в паять разметку
                return NoteItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item,parent,false))
            }
        }

    }


    class itemComparator : DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemHolder {
        return NoteItemHolder.create(parent)  // Передается наша готовая разметка в функции create
    }

    override fun onBindViewHolder(holder: NoteItemHolder, position: Int) {
        return holder.setData(getItem(position),listener)  // Подсчет элементов, создан каждый элемент с помощью функции setData и так как это ListAdapter мы берем вместо массива, напрямую с помощью getItem, передача listener для всех элементов в массиве
    }

    interface Listener{ // Создаем интерфейс, чтобы передать его с адаптера в ViewModel
        fun deleteItem(id:Int) // Передаем в параметр id, так как по нему будем удалять с базы данных
        fun onClickItem(noteItem: NoteItem) // Метод который отвечает за нажатие на наш весь элемент
    }
}