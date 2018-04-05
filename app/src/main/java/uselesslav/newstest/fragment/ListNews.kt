package uselesslav.newstest.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.adapters.ListNewsAdapter
import uselesslav.newstest.adapters.SimpleDividerItemDecoration
import uselesslav.newstest.model.ShortNews
import uselesslav.newstest.network.ListNewsLoader

/**
 * Список новостей конкретной категории
 */
class ListNews : Fragment(), ListNewsAdapter.OnItemClick {
    companion object {
        /**
         * Ключи для отправки/получения данных из другого фрагмента
         */
        const val idKey: String = "id"
        const val nameKey: String = "name"
    }

    var page: Int = 0
    var idCategory = 0

    /**
     * Массив новостей
     */
    private var news: List<ShortNews> = listOf()

    /**
     * Адаптер списка
     */
    private var adapter: ListNewsAdapter = ListNewsAdapter(news, this)

    /**
     * Индикатор прогресса
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Информационное окно
     */
    private lateinit var textInfo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_list_news, container, false)

        if (this.arguments != null) {
            // Заголовок окна
            activity!!.title = arguments!![nameKey].toString()
            idCategory = arguments!![idKey] as Int
        }

        progressBar = rootView.findViewById(R.id.pb_load)
        textInfo = rootView.findViewById(R.id.tv_info)

        // Инициализация списка
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_short_news)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.addItemDecoration(SimpleDividerItemDecoration(context!!, false))
        rv.adapter = adapter

        loadNews(false)

        return rootView
    }

    /**
     * Обработка нажатия на элемент списка
     */
    override fun onItemClick(shortNews: ShortNews) {

        // Создание набора отправляемых данных
        val bundle = Bundle()
        bundle.putInt(News.id, shortNews.id)
        bundle.putString(News.title, shortNews.title)
        bundle.putString(News.date, shortNews.date)
        bundle.putString(News.shortDescription, shortNews.shortDescription)

        val fragment = News()
        fragment.arguments = bundle

        // Открытие нужного окна
        fragmentManager!!
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .addToBackStack("News")
                .commit()
    }

    /**
     * Загрузка списка новостей
     */
    private fun loadNews(restart: Boolean) {

        textInfo.visibility = View.GONE
        progressBar.visibility = ProgressBar.VISIBLE

        val callbacks = ListNewsCallbacks()

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            loaderManager.restartLoader<List<ShortNews>>(id, Bundle.EMPTY, callbacks)
        } else {
            loaderManager.initLoader<List<ShortNews>>(id, Bundle.EMPTY, callbacks)
        }
    }

    /**
     * Отображение данных
     */
    private fun showNews(list: List<ShortNews>?) {
        if (list == null) {
            showError(getString(R.string.error_load))
        } else if (list.isEmpty()) {
            textInfo.visibility = View.VISIBLE
            showError(getString(R.string.empty_list))
        } else {
            news = list
            adapter.changeDataSet(news)
        }

        progressBar.visibility = ProgressBar.GONE
    }

    /**
     * Сообщение о ошибке
     */
    private fun showError(textError: String) {
        if (this.view != null) {
            val snackbar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.retry), { loadNews(false) })
            snackbar.duration = 4000
            snackbar.show()
        }
    }

    internal inner class ListNewsCallbacks : LoaderManager.LoaderCallbacks<List<ShortNews>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<ShortNews>> {
            return ListNewsLoader(context!!, idCategory, page)
        }

        override fun onLoadFinished(loader: Loader<List<ShortNews>>, data: List<ShortNews>?) {
            showNews(data)
        }

        override fun onLoaderReset(loader: Loader<List<ShortNews>>) {
        }
    }
}