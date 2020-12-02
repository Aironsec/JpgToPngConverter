package ru.stplab.jpgtopngconverter.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.stplab.jpgtopngconverter.R
import ru.stplab.jpgtopngconverter.mvp.model.Image
import ru.stplab.jpgtopngconverter.mvp.model.Repo
import ru.stplab.jpgtopngconverter.mvp.presenter.MainPresenter
import ru.stplab.jpgtopngconverter.mvp.view.MainView

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    companion object {
        const val REQUEST_CODE_GET_JPG = 111
    }

    private val presenter by moxyPresenter {
        MainPresenter(
            Repo(),
            AndroidConverter(this),
            AndroidSchedulers.mainThread()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageViewJpg.setOnClickListener { presenter.clickImage() }
    }

    override fun openPick() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Jpg"),
            REQUEST_CODE_GET_JPG
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GET_JPG) {
            data?.data?.let { uri ->
                presenter.showJpg(uri.toString())
                val bytes = contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }
                bytes?.let {
                    presenter.imageSelect(Image(bytes))

                }
            }
        }
    }

    override fun showJpg(uri: String) {
        imageViewJpg.background = null
        imageViewJpg.setImageURI(uri.toUri())
    }

    var convertDialog: Dialog? = null
    override fun showProgress() {
        convertDialog = AlertDialog.Builder(this)
            .setMessage(R.string.convert_in_progress)
            .setNegativeButton(R.string.cancel) { dialog, which -> presenter.convertCancel() }
            .create()
        convertDialog?.show()

    }

    override fun hideProgress() {
        convertDialog?.dismiss()
    }

    override fun showOk() {
        Toast.makeText(this, R.string.convertation_success, Toast.LENGTH_SHORT).show()
    }

    override fun showConvertCancel() {
        Toast.makeText(this, R.string.convertation_cancel, Toast.LENGTH_SHORT).show()
    }

    override fun showConvertError() {
        Toast.makeText(this, R.string.convertation_error, Toast.LENGTH_SHORT).show()
    }
}