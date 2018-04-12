package uselesslav.newstest.fragments

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.widget.ProgressBar
import uselesslav.newstest.R

abstract class BaseFragment : Fragment() {
    /**
     * Индикатор прогресса
     */
    protected open lateinit var progressBar: ProgressBar

    /**
     * Загрузка
     */
    protected abstract fun load(restart: Boolean)

    /**
     * Сообщение о ошибке
     */
    protected fun showError(textError: String) {
        if (this.view != null) {
            val snackBar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry), { load(true) })
            snackBar.show()
        }
    }
}