package ru.stplab.jpgtopngconverter.mvp.presenter

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.stplab.jpgtopngconverter.mvp.model.IConverter
import ru.stplab.jpgtopngconverter.mvp.model.Image
import ru.stplab.jpgtopngconverter.mvp.model.Repo
import ru.stplab.jpgtopngconverter.mvp.view.MainView

class MainPresenter(private val repo: Repo, private val converter: IConverter, private val mainThreadScheduler: Scheduler) : MvpPresenter<MainView>() {

    private var convertDisposable: Disposable? = null

    fun imageSelect(image: Image) {
        viewState.showProgress()
        convertDisposable = converter.convert(image)
            .observeOn(mainThreadScheduler)
            .subscribe({
                viewState.hideProgress()
                viewState.showOk()
            },{
                viewState.hideProgress()
                viewState.showConvertError()
            })
    }

    fun convertCancel(){
        convertDisposable?.dispose()
        viewState.hideProgress()
        viewState.showConvertCancel()
    }

    fun showJpg(uri: String) {
        viewState.showJpg(uri)
        repo.stringUriJpg = uri
    }

    fun clickImage() = viewState.openPick()
}