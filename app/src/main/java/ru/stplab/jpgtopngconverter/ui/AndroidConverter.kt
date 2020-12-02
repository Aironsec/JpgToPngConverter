package ru.stplab.jpgtopngconverter.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.stplab.jpgtopngconverter.mvp.model.IConverter
import ru.stplab.jpgtopngconverter.mvp.model.Image
import java.io.File
import java.io.FileOutputStream

class AndroidConverter(private val context: Context?): IConverter {

    override fun convert(image: Image): Completable = Completable.fromAction {

        context?.let { context ->
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                return@let
            }

            val bitmap = BitmapFactory.decodeByteArray(image.byteArray, 0, image.byteArray.size)
            val dstFile = File(context.getExternalFilesDir(null), "converted.png")
            FileOutputStream(dstFile).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

    }.subscribeOn(Schedulers.io())
}