package com.goyapp.shoppingtasklist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ActivityNewNoteBinding
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.fragments.NoteFragment
import com.goyapp.shoppingtasklist.utils.HtmlManager
import com.goyapp.shoppingtasklist.utils.MyTouchListener
import com.goyapp.shoppingtasklist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewNoteBinding
    private var note: NoteItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        onClickColorPicker()
        actionMenuCallback()
    }

    private fun onClickColorPicker() = with(binding){ // Добавляем слушатели нажатия для наших цветов
        imRed.setOnClickListener {

            setColorForSelectedText(R.color.picker_red)
        }
        imBlack.setOnClickListener {  setColorForSelectedText(R.color.picker_black)}
        imBlue.setOnClickListener {  setColorForSelectedText(R.color.picker_blue) }
        imOrange.setOnClickListener {  setColorForSelectedText(R.color.picker_orange)}
        imPurple.setOnClickListener {  setColorForSelectedText(R.color.picker_purple)}
        imGreen.setOnClickListener {  setColorForSelectedText(R.color.picker_green) }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){ // Инициализировали наш TouchListener и прикрепили к элементу, в данном случае к linearlayout
        binding.colorPickerLayout.setOnTouchListener(MyTouchListener())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_nemu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_note){
           setResult()


        } else if(item.itemId == android.R.id.home){ // Активация кнопки home, проверка условия
          finish()
        }else if(item.itemId == R.id.bold_style){ // Слушатель для выделения текста жирным
          setBoldSelectedText()

        }else if(item.itemId == R.id.color_picker){
           if(binding.colorPickerLayout.isShown){ // Проверка на нажатие,если он виден на экране,то при нажатии на ту же кнопку закроется и наоборот
               closeColorPicker()
           }else{
               openColorPicker()
           }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setBoldSelectedText() = with(binding){
        val startPos = edDesc.selectionStart // Переменная будет хранить в себе позицию откуда начинается выделение текста
        val endPos = edDesc.selectionEnd   // Переменная будет хранить в себе позицию где заканчивается выделение текста


        val styles = edDesc.text.getSpans(startPos,endPos,StyleSpan::class.java) // Берем стили которые уже есть в выделенном отрезке текста

        var boldStyle : StyleSpan? = null // Инстанция нашего boldstyle для проверки какой шрифт уже на данный момент в нашем отрезке выделенного текста

        if(styles.isNotEmpty()){ // Проверка, если у нас уже выбран какой то стиль для текста, тоесть notEmpty
            edDesc.text.removeSpan(styles[0]) // Убираем этот стиль, и так как у нас он только один, это bold, тогда в наш массив стилей передаем только 1(0)
        }else{ // Если у нас не выбран стиль
            boldStyle = StyleSpan(Typeface.BOLD) // Присваиваем нашей инстанции bold style

        }
        edDesc.text.setSpan(boldStyle,startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Устанавливаем наш стиль к тексту
        edDesc.text.trim() // trim удаляет все пробелы
        edDesc.setSelection(startPos) // Позиция нашего курсора с начала


    }

    private fun setColorForSelectedText(colorId : Int) = with(binding){ // Функция для установки цвета в выделенном тексте, передаем id цвета
        val startPos = edDesc.selectionStart // Переменная будет хранить в себе позицию откуда начинается выделение текста
        val endPos = edDesc.selectionEnd   // Переменная будет хранить в себе позицию где заканчивается выделение текста


        val styles = edDesc.text.getSpans(startPos,endPos,ForegroundColorSpan::class.java) // Берем стили с цветом которые уже есть в выделенном отрезке текста

        if(styles.isNotEmpty()){ // Проверка, если у нас уже выбран какой то стиль для текста, тоесть notEmpty
            edDesc.text.removeSpan(styles[0]) // Убираем этот стиль, и так как у нас он только один, это bold, тогда в наш массив стилей передаем только 1(0)
        }
        edDesc.text.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity,colorId)),startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Устанавливаем наш стиль и цвет к тексту, переадем в параметрах наш colorid который будет выбирать пользователь
        edDesc.text.trim() // trim удаляет все пробелы
        edDesc.setSelection(startPos) // Позиция нашего курсора с начала


    }

    private fun getNote(){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY) // Делаем инстанцию нашего сериализейбл, так как мы не можем передать null как NoteItem
        if(sNote != null) {
            note = sNote as NoteItem
            fillNote() // Запускаем функцию нашей проверка
        }


    }

    private fun fillNote() = with(binding){ // Функция для заполнения наших полей, если приходит не null
            edTitle.setText(note?.title) // Установка текста с заметки которая приходит
            edDesc.setText(HtmlManager.getFromHtml(note?.desc_content!!).trim()) // Превращаем из Html в Spannable, чтобы приенилось для нашего EditText все стили,цвета с сохранением в базу данных
    }

    private fun setResult(){
        var editstate = "new" // Создали переменную для проверки, мы будем передавать новосозданную заметку или обновленную, по умолчанию new
        val tempNote: NoteItem? = if(note == null) {
            createNewNote()  // Если у нас данные note пустые, то создаем нашу заметку впервые через Insert

        }else {
            editstate = "update"
            updateNote() // Если у нас данные были заполнены до этого, то при нажатии на сохранить, обновится ранее созданная заметка с обновленными данными

        }

        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY,tempNote) // Передаем подготовленные данные на наш фрагмент
            putExtra(NoteFragment.UPDATE_NOTE_KEY,editstate) // Передаем данные по ключу для проверки что мы делаем обновляем или создаем новое
        }

        setResult(RESULT_OK,i)          // Проверка на результат который прийдет при нажатии на кнопку Save
        finish()
    }

    private fun updateNote() : NoteItem? = with(binding){
       return note?.copy(title = edTitle.text.toString(), desc_content = HtmlManager.toHtml(edDesc.text) ) // Благодаря copy обновляем то что у нас было в заметке, и остается наша заметка без изменений, только перезаписывается, также для content используем функцию для превращение в Html
    }

    private fun createNewNote() : NoteItem{ // Заполняем каждый из заметок,нащим готовым классом NoteItem данными из заметок
        return NoteItem(null,binding.edTitle.text.toString(),HtmlManager.toHtml(binding.edDesc.text) ,TimeManager.getCurrentTime(),"")

    }





    private fun actionBarSettings(){ // Создали как настройки action bar, где активируем кнопку домой
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker(){ // Функция для открытия colorpicker
        binding.colorPickerLayout.visibility = View.VISIBLE // Делаем наш layout видимым
        val openAnim = AnimationUtils.loadAnimation(this,R.anim.open_color_picker) // Загрузка созданной анимации
        binding.colorPickerLayout.startAnimation(openAnim) // Запуск анимации к layout
        

    }

    private fun closeColorPicker(){ // Функция для закрытия colorpicker
        val openAnim = AnimationUtils.loadAnimation(this,R.anim.close_color_picker) // Загрузка созданной анимации
        openAnim.setAnimationListener(object : Animation.AnimationListener{ // Делаем слушатель,который отслеживает состояние анимации
            override fun onAnimationStart(anim: Animation?) {

            }

            override fun onAnimationEnd(anim: Animation?) {
                binding.colorPickerLayout.visibility = View.GONE //Делаем невидимым наш layout когда анимация заканчивается
            }

            override fun onAnimationRepeat(anim: Animation?) {

            }

        })
        binding.colorPickerLayout.startAnimation(openAnim) // Запуск анимации к layout


    }
    private fun actionMenuCallback(){ // Функция для удаления меню при выделении текста
        val actionmenucallback = object : ActionMode.Callback{ // Создаем колбэк для того чтобы работать с этим меню
            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear() // Как только меню постарается нарисоватся, то его сразу стираем
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear() // Как только меню постарается нарисоватся, то его сразу стираем
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
        binding.edDesc.customSelectionActionModeCallback = actionmenucallback  // Callback который следит за выделениями в edText и переадем наш созданный коллбэк
    }
}