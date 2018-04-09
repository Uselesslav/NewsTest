package uselesslav.newstest.fragment

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.model.News
import uselesslav.newstest.network.NewsLoader
import uselesslav.newstest.setDate
import uselesslav.newstest.setTime


@Suppress("DEPRECATION")
/**
 * Окно со списком новостей
 */
class News : Fragment() {
    companion object {
        /**
         * Ключи для передачи информации в этот фрагмент
         */
        const val idKey: String = "id"
        const val titleKey: String = "title"
        const val dateKey: String = "date"
        const val shortDescriptionKey: String = "shortDescription"
    }


    /**
     * id новостной категории
     */
    var idNews: Int = 0

    /**
     * Текстовое поле с выжимкой новости
     */
    private lateinit var textViewShortNews: TextView

    /**
     * Текстовое поле с полной новостью
     */
    private lateinit var textViewFullNews: TextView

    /**
     * Индикатор прогресса
     */
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)

        val textViewDate = rootView.findViewById<TextView>(R.id.tv_date)
        val textViewTime = rootView.findViewById<TextView>(R.id.tv_time)
        progressBar = rootView.findViewById(R.id.pb_load)
        textViewShortNews = rootView.findViewById(R.id.tv_short_news)
        textViewFullNews = rootView.findViewById(R.id.tv_full_news)

        if (this.arguments != null) {
            // Заголовок окна
            activity!!.title = arguments!![titleKey].toString()

            // Получение параметров из предыдущего фрагмента
            idNews = arguments!![idKey] as Int

            textViewShortNews.text = resources.getString(R.string.short_news, arguments!![shortDescriptionKey].toString())

            textViewDate.setDate(arguments!![dateKey].toString())
            textViewTime.setTime(arguments!![dateKey].toString())
        }

        loadNews(false)

        return rootView
    }

    /**
     * Загрузка новости
     */
    private fun loadNews(restart: Boolean) {

        val callbacks = NewsCallbacks()
        progressBar.visibility = View.VISIBLE

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            loaderManager.restartLoader<News>(id, Bundle.EMPTY, callbacks)
        } else {
            loaderManager.initLoader<News>(id, Bundle.EMPTY, callbacks)
        }
    }

    /**
     * Отображение данных
     */
    private fun showNews(news: News?) {
        if (news == null) {
            showError(getString(R.string.error_load))
        } else {
            textViewFullNews.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= 24) {
                textViewFullNews.text = Html.fromHtml(news.fullDescription, Html.FROM_HTML_MODE_LEGACY)
            } else {
                textViewFullNews.text = Html.fromHtml(news.fullDescription)
            }
        }
        progressBar.visibility = View.GONE
    }

    /**
     * Сообщение о ошибке
     */
    private fun showError(textError: String) {
        if (this.view != null) {
            val snackBar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry), { loadNews(true) })
            snackBar.show()
        }
    }

    internal inner class NewsCallbacks : LoaderManager.LoaderCallbacks<News> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<News> {
            return NewsLoader(context!!, idNews)
        }

        override fun onLoadFinished(loader: Loader<News>, data: News?) {
            showNews(data)
        }

        override fun onLoaderReset(loader: Loader<News>) {
        }
    }
}