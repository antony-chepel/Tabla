package com.goyapp.shoppingtasklist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ListNameItemBinding
import com.goyapp.shoppingtasklist.databinding.ShopLibraryListItemBinding
import com.goyapp.shoppingtasklist.databinding.ShopListItemBinding
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName

class ShopListNameItemAdapter(private val listener: Listener) : ListAdapter<ShopListItem,ShopListNameItemAdapter.ShopItemHolder>(itemComparator()) {

    class ShopItemHolder( val view: View) :RecyclerView.ViewHolder(view) { // Класс хранит ссылку на разметку, на каждый Item

        fun setItemData(shopListName: ShopListItem, listener: Listener){   // Благодаря этой функции, заполняем элементы в разметке
             val binding = ShopListItemBinding.bind(view) // Создаем байдинг специально для этой разметки
            binding.apply {
                tvShopNameItem.text = shopListName.name
                tvShopInfoItem.text = shopListName.item_info
                checkboxItem.isChecked = shopListName.item_bought
                tvShopInfoItem.visibility = checkInfoVisibility(shopListName) // Присваеваем к видимости нашего элемента функцию, которая делает необходимую проверку
                setPaintFlagAndColor(binding) // Проверка нашего чекбокс в слушателе нажатия
                checkboxItem.setOnClickListener {

                    listener.onClickItem(shopListName.copy(item_bought = checkboxItem.isChecked),
                        CHECKBOX) // Запись состояния чекбокс, выбран или нет, тем самым обновляем в базе данных
                }
                eDbutton.setOnClickListener {
                    listener.onClickItem(shopListName, EDIT)
                }


            }


        }
        fun setDataLibrary(shopListName: ShopListItem, listener: Listener){   // Благодаря этой функции, заполняем элементы в разметке
            val binding = ShopLibraryListItemBinding.bind(view)  // Создаем байдинг специально для этой разметки
            binding.apply {
             tvLibName.text = shopListName.name
                imEditLib.setOnClickListener {
                    listener.onClickItem(shopListName, EDIT_LIB) // Передача того что мы будем редактировать конкретно libItem
                }
                imDeleteLib.setOnClickListener {
                    listener.onClickItem(shopListName, DELETE_LIB)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(shopListName, CLICK_ITEM_ADD_LIB)
                }
            }

        }

        private fun setPaintFlagAndColor(binding: ShopListItemBinding){ // Функция для того, чтобы менять цвет текста при выбранном checkbox
            binding.apply {
                if(checkboxItem.isChecked){ // Проверка, если чекбокс выбран, то перечеркиваем нужные элементы
                    tvShopNameItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvShopInfoItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                    tvShopNameItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.hint_color)) // Меняется цвет, в зависимости от выбранного чекбокса
                    tvShopInfoItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.hint_color))
                }else{
                    tvShopNameItem.paintFlags = Paint.ANTI_ALIAS_FLAG // Возвращение в первоначальный вид, с перечеркнутого вида
                    tvShopInfoItem.paintFlags = Paint.ANTI_ALIAS_FLAG


                    tvShopNameItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.black))
                    tvShopInfoItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.black))
                }
            }


        }


        private fun checkInfoVisibility(shopListName: ShopListItem) : Int{ // Функция для проверки есть ли унас что то в tv_info или нет
         return  if(shopListName.item_info.isEmpty()){ // Проверка если в tv_info null или пусто
             View.GONE
         }else {
             View.VISIBLE
         }
        }
        companion object{
            fun createShopItem(parent: ViewGroup) : ShopItemHolder{ // Выдает инициализированный класс NoteItemHolder который в себе хранит ссылку на загруженную в память разметку, функция для создание первой разметки
                return ShopItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_list_item,parent,false))
            }

            fun createItemLibrary(parent: ViewGroup) : ShopItemHolder{ // Выдает инициализированный класс NoteItemHolder который в себе хранит ссылку на загруженную в память разметку, функция для создание второй разметки
                return ShopItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_library_list_item,parent,false))
            }
        }

    }


    class itemComparator : DiffUtil.ItemCallback<ShopListItem>(){
        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int { // Функция для того чтобы возвращать какую разметку мы будем использовать, берем по нашему itemType. Это позволяет комбинировать разметки в Rc View
        return getItem(position).item_type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemHolder {
       return if(viewType == 0) { // Проверка на какой из ItemHolder создавать, тоесть какую разметку создавать в RcView
             ShopItemHolder.createShopItem(parent)  // Передается наша готовая разметка в функции create
        }else{
           ShopItemHolder.createItemLibrary(parent)
        }
    }

    override fun onBindViewHolder(holder: ShopItemHolder, position: Int) {
        return if(getItem(position).item_type == 0) { // Проверка по типу item какой нам надо заполнять RcView
            holder.setItemData(getItem(position), listener)
        } else{
            holder.setDataLibrary(getItem(position), listener)
        }  // Подсчет элементов, создан каждый элемент с помощью функции setData и так как это ListAdapter мы берем вместо массива, напрямую с помощью getItem, передача listener для всех элементов в массиве
    }

    interface Listener{ // Создаем интерфейс, чтобы передать его с адаптера в ViewModel
        fun onClickItem(shopListItem: ShopListItem,state:Int) // Метод который отвечает за нажатие на наш весь элемент
    }

    companion object{
        const val DELETE_LIB = 3
        const val EDIT = 0
        const val CHECKBOX = 1
        const val EDIT_LIB = 2
        const val CLICK_ITEM_ADD_LIB = 4

    }
}