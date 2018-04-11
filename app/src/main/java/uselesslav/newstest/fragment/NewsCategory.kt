package uselesslav.newstest.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.adapters.NewsCategoriesAdapter
import uselesslav.newstest.adapters.SimpleDividerItemDecoration
import uselesslav.newstest.model.NewsCategory
import uselesslav.newstest.network.CallBacks
import uselesslav.newstest.network.NewsCategoryLoader

/**
 * Фрагмент с списком категорий
 */
class NewsCategory : Fragment(), NewsCategoriesAdapter.OnItemClick {
    /**
     * Массив новостных категорий
     */
    private var newsCategories: List<NewsCategory> = listOf()

    /**
     * Адаптер списка
     */
    private var adapter: NewsCategoriesAdapter = NewsCategoriesAdapter(newsCategories, this)

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
        val rootView = inflater.inflate(R.layout.fragment_news_category, container, false)

        // Заголовок окна
        activity!!.title = getString(R.string.app_name)

        progressBar = rootView.findViewById(R.id.pb_load)
        errorTextView = rootView.findViewById(R.id.tv_error)
        errorTextView.setOnClickListener({ loadNewsCategories(true) })

        // Инициализация списка
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_news_category)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.addItemDecoration(SimpleDividerItemDecoration(context!!, false))
        rv.adapter = adapter

        loadNewsCategories(false)

        return rootView
    }


    /**
     * Обработка нажатия на элемент списка
     */
    override fun onItemClick(newsCategory: NewsCategory) {

        // Создание набора отправляемых данных
        val bundle = Bundle()
        bundle.putInt(ListNews.idKey, newsCategory.id)
        bundle.putString(ListNews.nameKey, newsCategory.name)

        val fragment = ListNews()
        fragment.arguments = bundle

        // Открытие нужного окна
        fragmentManager!!
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .addToBackStack("ListNews")
                .commit()
    }

    /**
     * Загрузка списка новостей
     */
    private fun loadNewsCategories(restart: Boolean) {

        progressBar.visibility = ProgressBar.VISIBLE
        errorTextView.visibility = View.GONE

        val callbacks = CallBacks(
                NewsCategoryLoader(context!!),
                { list ->
                    when {
                        list == null -> showError(getString(R.string.error_load))
                        list.isEmpty() -> {
                            showError(getString(R.string.empty_list))
                            errorTextView.visibility = View.GONE
                        }
                        else -> {
                            newsCategories = list
                            adapter.changeDataSet(newsCategories)
                            errorTextView.visibility = View.GONE
                        }
                    }

                    progressBar.visibility = ProgressBar.GONE
                })

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            loaderManager.restartLoader<List<NewsCategory>>(id, Bundle.EMPTY, callbacks)
        } else {
            loaderManager.initLoader<List<NewsCategory>>(id, Bundle.EMPTY, callbacks)
        }
    }

    /**
     * Сообщение о ошибке
     */
    private fun showError(textError: String) {
        if (this.view != null) {
            val snackBar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry), { loadNewsCategories(true) })
            snackBar.show()
            errorTextView.visibility = View.VISIBLE
        }
    }
}
