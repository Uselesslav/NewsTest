package uselesslav.newstest.network

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader

/**
 * Реагирует на события загрузчика
 */
class CallBacks<out T : Loader<D>, D>(private val loader: T, private val showNews: (D?) -> Unit) : LoaderManager.LoaderCallbacks<D> {

    override fun onCreateLoader(id: Int, args: Bundle?): T {
        return loader
    }

    override fun onLoadFinished(loader: Loader<D>, data: D?) {
        showNews(data)
    }

    override fun onLoaderReset(loader: Loader<D>) {
    }
}
