package ru.stplab.jpgtopngconverter.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@OneExecution
interface MainView: MvpView {
    @AddToEndSingle
    fun showJpg(uri: String)
    @AddToEndSingle
    fun showProgress()

    fun showConvertError()
    fun showConvertCancel()
    fun showOk()
    fun hideProgress()
    fun openPick()
}