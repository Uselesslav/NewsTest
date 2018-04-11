package uselesslav.newstest.fragment

import android.os.Bundle
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
import uselesslav.newstest.network.CallBacks
import uselesslav.newstest.network.ListNewsLoader

/**
 * Список новостей конкретной категории
 */
class ListNews : BaseFragment(), ListNewsAdapter.OnItemClick {
    companion object {
        /**
         * Ключи для отправки/получения данных из другого фрагмента
         */
        const val idKey: String = "id"
        const val nameKey: String = "name"
    }

    /**
     * Категория загружаемых новостей
     */
    private var idCategory = 0

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
     * Текстовое поле с ошибкой
     */
    private lateinit var errorTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_list_news, container, false)
        progressBar = rootView.findViewById(R.id.pb_load)
        errorTextView = rootView.findViewById(R.id.tv_error)
        errorTextView.setOnClickListener({ load(true) })

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

        // Реализация пейджинга
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition == adapter.itemCount - 1) {

                    if (!isLoading) {

                        isLoading = true
                        load(true)
                    }
                }
            }
        })

        load(false)

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
    override fun load(restart: Boolean) {

        errorTextView.visibility = View.GONE
        progressBar.visibility = ProgressBar.VISIBLE

        val callbacks = CallBacks(
                ListNewsLoader(context!!, idCategory),
                { list ->
                    //проверка ответа
                    when {
                        // Если ответ = null, показать ошибку
                        list == null -> {
                            showError(getString(R.string.error_load))

                            // Если список пуст, показать текстовое окно
                            if (news.isEmpty()) {
                                errorTextView.text = getText(R.string.error_internet)
                                errorTextView.visibility = View.VISIBLE
                            }
                        }

                        // Если ответ и массив пусты, показать соответствующий текст
                        list.isEmpty() && news.isEmpty() -> {
                            errorTextView.visibility = View.VISIBLE
                            errorTextView.text = getText(R.string.empty_list_news)
                        }

                        // Заполнение списка и изменение видимости текстовых полей, если массив ответа не пуст
                        else -> {
                            news = list
                            adapter.changeDataSet(news)
                            errorTextView.visibility = View.GONE
                        }
                    }

                    // Изменение состояния флага загрузки и иконки прогресса
                    progressBar.visibility = ProgressBar.GONE
                    isLoading = false
                })

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
}