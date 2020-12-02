package ru.stplab.jpgtopngconverter.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convert(image: Image): Completable
}