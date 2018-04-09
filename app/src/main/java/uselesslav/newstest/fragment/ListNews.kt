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

    /**
     * Категория и страница загружаемых новостей
     */
    var page: Int = 0
    var idCategory = 0

    /**
     * Массив новостей
     */
    private var news: List<uselesslav.newstest.model.News> = listOf()

    /**
     * Флаг загрузки
     */
    private var isLoading = false

    /**
     * Адаптер списка
     */
    private var adapter: ListNewsAdapter = ListNewsAdapter(news, this)

    /**
     * Индикатор прогресса
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Текстовое поле с ошибкой
     */
    private lateinit var errorTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_list_news, container, false)
        progressBar = rootView.findViewById(R.id.pb_load)
        errorTextView = rootView.findViewById(R.id.tv_error)
        errorTextView.setOnClickListener({ loadNews(true) })

        if (this.arguments != null) {
            // Заголовок окна
            activity!!.title = arguments!![nameKey].toString()
            idCategory = arguments!![idKey] as Int
        }


        // Инициализация списка
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_short_news)
        val layoutManager = LinearLayoutManager(activity)
        rv.layoutManager = layoutManager
        rv.addItemDecoration(SimpleDividerItemDecoration(context!!, false))
        rv.adapter = adapter

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lastvisibleitemposition = layoutManager.findLastVisibleItemPosition()

                if (lastvisibleitemposition == adapter.itemCount - 1) {

                    if (!isLoading) {

                        isLoading = true
                        page += 1
                        loadNews(true)
                    }
                }
            }
        })

        loadNews(false)

        return rootView
    }

    /**
     * Обработка нажатия на элемент списка
     */
    override fun onItemClick(news: uselesslav.newstest.model.News) {

        // Создание набора отправляемых данных
        val bundle = Bundle()
        bundle.putInt(News.idKey, news.id)
        bundle.putString(News.titleKey, news.title)
        bundle.putString(News.dateKey, news.date)
        bundle.putString(News.shortDescriptionKey, news.shortDescription)

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

        errorTextView.visibility = View.GONE
        progressBar.visibility = ProgressBar.VISIBLE

        val callbacks = ListNewsCallbacks()

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            if (isLoading) {
                loaderManager.getLoader<List<uselesslav.newstest.model.News>>(id)!!.startLoading()
            } else {
                loaderManager.restartLoader<List<uselesslav.newstest.model.News>>(id, Bundle.EMPTY, callbacks)
            }
        } else {
            loaderManager.initLoader<List<uselesslav.newstest.model.News>>(id, Bundle.EMPTY, callbacks)
        }
    }

    /**
     * Отображение данных
     */
    private fun showNews(list: List<uselesslav.newstest.model.News>?) {
        //проверка ответа
        if (list == null) {
            // Если ответ = null, показать ошибку
            showError(getString(R.string.error_load))

            // Если список пуст, показать текстовое окно
            if (news.isEmpty()) {
                errorTextView.text = getText(R.string.error_internet)
                errorTextView.visibility = View.VISIBLE
            }

        } else if (list.isEmpty() && news.isEmpty()) {
            // Если ответ и массив пусты, показать соответствующий текст
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = getText(R.string.empty_list_news)

        } else {
            // Заполнение списка и изменение видимости текстовых полей, если массив ответа не пуст
            news = list
            adapter.changeDataSet(news)
            errorTextView.visibility = View.GONE
        }

        // Изменение состояния флага загрузки и иконки прогресса
        progressBar.visibility = ProgressBar.GONE
        isLoading = false
    }

    /**
     * Сообщение о ошибке
     */
    private fun showError(textError: String) {
        if (this.view != null) {
            val snackbar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry), { loadNews(true) })
            snackbar.show()
        }
    }

    internal inner class ListNewsCallbacks : LoaderManager.LoaderCallbacks<List<uselesslav.newstest.model.News>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<uselesslav.newstest.model.News>> {
            return ListNewsLoader(context!!, idCategory)
        }

        override fun onLoadFinished(loader: Loader<List<uselesslav.newstest.model.News>>, data: List<uselesslav.newstest.model.News>?) {
            showNews(data)
        }

        override fun onLoaderReset(loader: Loader<List<uselesslav.newstest.model.News>>) {
        }
    }
}