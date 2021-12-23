package com.goyapp.shoppingtasklist.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ShopListActivityBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.ShopListNameItemAdapter
import com.goyapp.shoppingtasklist.dialogs.UpdateListDialog
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import com.goyapp.shoppingtasklist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(),ShopListNameItemAdapter.Listener {
    private  lateinit var binding : ShopListActivityBinding
    private var adapter : ShopListNameItemAdapter? = null
    private lateinit var textwatcher : TextWatcher
    private  var edItem: EditText? = null // Инстанция EditText где мы вводим название item
    private lateinit var saveItem :MenuItem // Инстанция иконки сохранить в actionbar, чтобы работать с ним(прятать,показывать)
    private var shoplistName : ShopListName? = null // Инстанция нашего ShopListName чтобы от туда получать данные, в нашем случае чтобы брать id и знать на какой элемент нажато
    private  val mainViewModel: MainViewModel by viewModels{ // Инициализируем ViewModel в activity
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database) // Передача базы данных с помощью класса всего приложения MainApp где и запускается наш db на уровне всего приложения
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShopListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        rcView()
        listItemObserver()
    }

    private fun rcView() = with(binding){ // Инициализируем rcView и подключение адаптера
        adapter = ShopListNameItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter

    }

    private fun init(){
        shoplistName = intent.getSerializableExtra(SHOP_LIST_NAME_KEY) as ShopListName // Получаем наш целый класс с фрагмента
//        binding.tvTest.text = shoplistName?.name // Знак вопроса означает берем из класса название нашего списка, если он не null
    }




    private fun expandActionView() : MenuItem.OnActionExpandListener{ // Функция которая возвращает listener в котором будет отслеживатся состояние нашего edittext который появляется и исчезает при нажатии
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(menu: MenuItem?): Boolean {
                saveItem.isVisible = true // Кнопка сохранить появляется при появлении edText
                edItem?.addTextChangedListener(textwatcher) // Добавляется watcher для edText как только он появляется
                libraryitemObserver() // Как только открывается меню, подключается observer данных из библиотеки
                mainViewModel.getAllItemsFromList(shoplistName?.id!!).removeObservers(this@ShopListActivity) // Как только в тексте что то пишется, то отключается observer основного списка items
                mainViewModel.getAllLibraryItems("%%") // Покажет все элементы которые были сохранены при открытии
                return true
            }

            override fun onMenuItemActionCollapse(menu: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textwatcher)  // Убирается watcher для edText как только он исчезает
                invalidateOptionsMenu() // Благодаря этой функции меню перерисовывается, чтобы элементы снова появились и все корректно работало
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity) // Уберается observer как только закрывается edit text
                edItem?.setText("") // Очистка editem при закрытии
                listItemObserver()
                return true
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_list_item -> {
                addNewItem(edItem?.text.toString())
            }
            R.id.delete_list -> { // При нажатии на удалить список внутри элементов, удаляется полностью весь список
                shoplistName?.id?.let { mainViewModel.DeleteShopList(it,true) }
                finish()
            }
            R.id.clear_list -> { // При нажатии на очистить список, очищаются только элементы внутри
                shoplistName?.id?.let { mainViewModel.DeleteShopList(it,false) }
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(shoplistName?.let { // Запуск как intent с помощью chooser
                    ShareHelper.shareShopList(adapter?.currentList!!,
                        it.name)
                },"Share by"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewItem(name:String){ // Функция для сохранения Item
        if(name.isEmpty()) return // Если EditText null то просто выходим из функции
        val item = ShopListItem(
            null,name,"",false,shoplistName?.id!!,0
        )
        edItem?.setText("") // Очистили текст после нажатия на кнопку
        mainViewModel.InsertShopListItem(item) // Сохранение item


    }


    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shoplistName?.id!!).observe(this,{
            adapter?.submitList(it) // Подключаем к адаптеру list, где будут отслеживатся обновления
            binding.tvEmpty.visibility = if (it.isEmpty()){ // Проверка если список пуст, то показываем текст что Нету Элементов, а иначе прячем
                View.VISIBLE
            } else{
                View.GONE
            }

        })

    }

    private fun libraryitemObserver(){ // Отслеживание за изменениями в списке данных библиотеки
       mainViewModel.libraryItems.observe(this,{
           val tempShopList = ArrayList<ShopListItem>() // Создается временный список
         it.forEach { item -> // Перегрузка адаптера, указывваем только то что будет использоватся для libraryItem, это id,name и главное item_type 1, которое указывает на другую разметку
           val shopItem = ShopListItem(
               item.id,item.name,"",false,0,1
           )
             tempShopList.add(shopItem)
         }
           adapter?.submitList(tempShopList)
           binding.tvEmpty.visibility = if (it.isEmpty()){ // Проверка если список пуст, то показываем текст что Нету Элементов, а иначе прячем
               View.VISIBLE
           } else{
               View.GONE
           }
       })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Создаем меню, сначала надуваем, потом возращается true
        menuInflater.inflate(R.menu.shop_list_menu,menu)
        saveItem = menu?.findItem(R.id.save_list_item)!! // Находим saveitem
        val newItem = menu.findItem(R.id.new_list_item)!! // Находим newItem
        edItem = newItem.actionView.findViewById(R.id.ed_new_shop_item) as EditText // Инициализировали и нашли EditText внутри item, указано,что это будет не просто actionview а именно EditText
        newItem.setOnActionExpandListener(expandActionView()) // Добавлен слушатель на кнопку при котором и будет срабатывать появление и скрытие edText
        saveItem.isVisible = false // Указано, что сразу кнопка спрятана
        textwatcher = textWatcher() // Инициализация textWatcher

        return true

    }

    private fun textWatcher() : TextWatcher{ // Специальный interface который следит за изменениями в тексте
        return object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(letter: CharSequence?, p1: Int, p2: Int, p3: Int) {
                  mainViewModel.getAllLibraryItems("%$letter%") // Считывается изменение по букве, благодаря шаблону %%
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
    }


    companion object{
        const val SHOP_LIST_NAME_KEY = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem,state : Int) { // Передача int значение по которому проверяется для чего конкретно используется интерфейс
        when(state){
            ShopListNameItemAdapter.CHECKBOX -> mainViewModel.UpdateShopListItem(shopListItem)
            ShopListNameItemAdapter.EDIT -> editListItem(shopListItem)
            ShopListNameItemAdapter.CLICK_ITEM_ADD_LIB -> addNewItem(shopListItem.name) // Переиспользование функции, при нажатии на элемент добавляется в список item
            ShopListNameItemAdapter.EDIT_LIB -> editLibItem(shopListItem)
            ShopListNameItemAdapter.DELETE_LIB ->{
                mainViewModel.DeleteLibraryItem(shopListItem.id!!) // Удаляем с библиотеки по id с shoplistItem
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") // Обновление адаптера с данными которые ввели, по той же букве что было в edItem
                libraryitemObserver()

            }
        }

    }

    private fun editListItem(shopListItem: ShopListItem){ // Обновление Item
        UpdateListDialog.showdialog(this,shopListItem, object : UpdateListDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.UpdateShopListItem(item) // Перезапись item, в самом диалоге сделана работа проверок и конкретно что перезаписуем

            }

        })

    }

    private fun editLibItem(shopListItem: ShopListItem){ // Обновление LibraryItem
        UpdateListDialog.showdialog(this,shopListItem, object : UpdateListDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.UpdateLibraryItem(LibraryItem(item.id,item.name)) // Перегрузка с ShoplistItem нужные данные в LibraryItem, благодаря чему будет обновлятся нужная информация
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") // Обновление адаптера с данными которые ввели, по той же букве что было в edItem
                libraryitemObserver()
            }

        })

    }

    private fun saveItemCount(){ // Функция для подсчета выбранных элементов в списке

        var checkedItemCounter = 0
        adapter?.currentList?.forEach{ // Перебираем все item которые есть, чтобы из них выбрать только отмеченные
            if(it.item_bought) {
                checkedItemCounter ++ // Если отмечены, добавляется в список отмеченных
            }
        }
        val tempshopListItem =  shoplistName?.copy(
            countItemAll = adapter?.itemCount!!, // Подсчитали количество item, через адаптер
             countItemBought = checkedItemCounter
        )
        tempshopListItem?.let { mainViewModel.UpdateListName(it) } // Добавляем измененные значения

    }


    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()

    }


}