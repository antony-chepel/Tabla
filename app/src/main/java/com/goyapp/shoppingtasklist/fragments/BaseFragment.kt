package com.goyapp.shoppingtasklist.fragments

import androidx.fragment.app.Fragment

 abstract class BaseFragment : Fragment() { // Мы говорим системе андроид, что те классы которые наследуются от текущего класса есть одна и та же функция, но она выполняет разные действия в зависимости от фрагмента
     abstract fun onClickNewFrag()

}