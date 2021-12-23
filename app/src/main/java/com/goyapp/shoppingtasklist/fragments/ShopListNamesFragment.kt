package com.goyapp.shoppingtasklist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goyapp.shoppingtasklist.activities.MainApp
import com.goyapp.shoppingtasklist.activities.ShopListActivity
import com.goyapp.shoppingtasklist.databinding.FragmentShopListNamesBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.ShopListNameAdapter
import com.goyapp.shoppingtasklist.dialogs.DeleteDialog
import com.goyapp.shoppingtasklist.dialogs.NewListDialog
import com.goyapp.shoppingtasklist.entities.ShopListName
import com.goyapp.shoppingtasklist.utils.TimeManager


class ShopListNamesFragment : BaseFragment(),ShopListNameAdapter.Listener {
    private lateinit var binding : FragmentShopListNamesBinding
    private  lateinit var adapter : ShopListNameAdapter

    private  val mainViewModel: MainViewModel by activityViewModels{ // Инициализируем ViewModel в фрагменте
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database) // Передача базы данных с помощью класса всего приложения MainApp где и запускается наш db на уровне всего приложения
    }
    override fun onClickNewFrag() {
       NewListDialog.showdialog(activity as AppCompatActivity, object : NewListDialog.Listener{
           override fun onClick(name: String) { // При нажатии на кнопку сохранить добавится новый список
            val shoplistname = ShopListName( // Заполняем данные, с помощью класса
                null,name, TimeManager.getCurrentTime(),0,0,""
            )
               mainViewModel.InsertShopListName(shoplistname) // Делаем insert в базу данных нашего заполненого класса
           }

       }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun observer(){
        mainViewModel.allShopListName.observe(viewLifecycleOwner,{ // Благодаря viewmodel и observer следим за изменением в списке, viewLifecycleOwner потому что находимся в Fragment
            adapter.submitList(it)


        })
    }


    private fun initRcView() = with(binding){
      rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()

    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showdialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.DeleteShopList(id,true)
            }

        })
    }

    override fun editItem(shopListName: ShopListName) { // Функция для обновления названия заметки
        NewListDialog.showdialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String) { // При нажатии на кнопку сохранить добавится новый список

                mainViewModel.UpdateListName(shopListName.copy(name = name)) // Делаем обновление, при этом оставляя все прошлые данные, только изменяя имя
            }

        },shopListName.name) // Передали текущее название list в edit при редаетировании
    }

    override fun onClickItem(shopListName: ShopListName) {

        val i = Intent(activity,ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME_KEY,shopListName) // Передаем весь наш ShoplistName
        }
        startActivity(i)
    }
}