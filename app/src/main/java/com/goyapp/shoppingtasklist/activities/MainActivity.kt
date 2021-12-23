package com.goyapp.shoppingtasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ActivityMainBinding
import com.goyapp.shoppingtasklist.dialogs.NewListDialog
import com.goyapp.shoppingtasklist.fragments.FragmentManager
import com.goyapp.shoppingtasklist.fragments.NoteFragment
import com.goyapp.shoppingtasklist.fragments.ShopListNamesFragment

class MainActivity : AppCompatActivity(),NewListDialog.Listener {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this) // Запускаем как первый Fragment
        setButtonNavListener()
    }

    private fun setButtonNavListener(){ // Создали слушатель нажатий на item в нашем ButtonNavMenu
        binding.bnav.setOnItemSelectedListener {
            when(it.itemId){ // Прогоняем через when на какой из item было нажатие
                R.id.settings->{
                    Log.d("MyLog", "Click Settings")
                }
                R.id.notes->{
                    FragmentManager.setFragment(NoteFragment.newInstance(),this) // Запуск определенного фрагмента для Note
                }
                R.id.shop_list->{
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this) // Запуск определенного фрагмента для ShopListNames
                }
                R.id.new_item->{
                    FragmentManager.currentFrag?.onClickNewFrag()

                }
            }

            true
        }
    }

    override fun onClick(name: String) {
        Log.d("Test", "Name : $name")
    }
}