package com.goyapp.shoppingtasklist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goyapp.shoppingtasklist.activities.MainApp
import com.goyapp.shoppingtasklist.activities.NewNoteActivity
import com.goyapp.shoppingtasklist.databinding.FragmentNoteBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.NoteAdapter
import com.goyapp.shoppingtasklist.entities.NoteItem


class NoteFragment : BaseFragment(),NoteAdapter.Listener {
    private lateinit var binding:FragmentNoteBinding
    lateinit var adapter : NoteAdapter // Инстанция адаптера
    private lateinit var activeLaunch : ActivityResultLauncher<Intent> // Создали инстанцию ActivityLauncher
    private  val mainViewModel: MainViewModel by activityViewModels{ // Инициализируем ViewModel в фрагменте
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database) // Передача базы данных с помощью класса всего приложения MainApp где и запускается наш db на уровне всего приложения
    }
    override fun onClickNewFrag() { // В зависимости какой фрагмент вызывается, будет запускатся это функция
        activeLaunch.launch(Intent(activity,NewNoteActivity::class.java)) // Запускаем Intent с помощью launcher и ждем результата
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  mainViewModel.allNotes.observe(this,{ // Эта функция запускается если есть какие то обновления в списке со всем записями, выдает каждый раз обновленный список когда View доступен


      //  })
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // Эта функция запускается когда уже все View созданы
        super.onViewCreated(view, savedInstanceState)
        initRcView() // Передаем наш готовый адаптер только в том случае когда уже все View созданы, иначе не будет работать
        observer()
    }

    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner,{ // Благодаря viewmodel и observer следим за изменением в списке, viewLifecycleOwner потому что находимся в Fragment
            adapter.submitList(it)

        })
    }


    private fun initRcView() = with(binding){
        rcViewNote.layoutManager = LinearLayoutManager(activity)   // Расположение элементов в списке
        adapter = NoteAdapter(this@NoteFragment) // Инициализировали адаптер,передача listener именно нашего фрагмента
        rcViewNote.adapter = adapter  // Указали какой какой адаптер будет обновлять наш список

    }

    private fun onEditResult(){
        activeLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ // Регистрируем наш activity launcher где будет приходить данные с запущенного Activity, если они есть
            if(it.resultCode == Activity.RESULT_OK){
                val edistate = it.data?.getStringExtra(UPDATE_NOTE_KEY)
                if(edistate== "new") {
                    mainViewModel.InsertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem) // Получаем в виде байтов, чтобы передавать данные целым классом
                }else{
                    mainViewModel.UpdateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }
            }

        }
    }

    companion object { // Чтобы у нас была только 1 инстанция фрагмента, singleton
        const val NEW_NOTE_KEY = "note_key"
        const val UPDATE_NOTE_KEY = "update_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.DeleteNote(id)
    }

    override fun onClickItem(noteItem: NoteItem) {
        val i = Intent(activity,NewNoteActivity::class.java).apply { // Передаем уже заполненные раннее данные на экран где создаем заметку(NewNoteActivity), чтобы обновить
            putExtra(NEW_NOTE_KEY,noteItem) // Передали наш весь класс NoteItem
        }
        activeLaunch.launch(i)
    }
}