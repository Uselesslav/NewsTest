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
import uselesslav.newstest.R
import uselesslav.newstest.adapters.NewsCategoriesAdapter
import uselesslav.newstest.adapters.SimpleDividerItemDecoration
import uselesslav.newstest.model.NewsCategory
import uselesslav.newstest.network.NewsCategoryLoader

/**
 * Фрагмент с списком категорий
 */
class NewsCategory : Fragment() {
    /**
     * Массив новостных категорий
     */
    private var newsCategories: List<NewsCategory> = listOf()

    /**
     * Адаптер списка
     */
    private var adapter: NewsCategoriesAdapter = NewsCategoriesAdapter(newsCategories)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_news_category, container, false)

        // Инициализация списка
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_news_category)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.addItemDecoration(SimpleDividerItemDecoration(context!!, false))
        rv.adapter = adapter

        loadNewsCategories(false)

        return rootView
    }

    /**
     * Загрузка списка новостей
     */
    private fun loadNewsCategories(restart: Boolean) {

        val callbacks = NewsCategoryCallbacks()

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            loaderManager.restartLoader<List<NewsCategory>>(id, Bundle.EMPTY, callbacks)
        } else {
            loaderManager.initLoader<List<NewsCategory>>(id, Bundle.EMPTY, callbacks)
        }
    }

    /**
     * Отображение данных
     */
    private fun showNewsCategories(list: List<NewsCategory>?) {
        if (list == null) {
            showError(getString(R.string.error_load))
        } else if (list.isEmpty()) {
            showError(getString(R.string.empty_list))
        } else {
            newsCategories = list
            adapter.changeDataSet(newsCategories)
        }
    }

    /**
     * Сообщение о ошибке
     */
    private fun showError(textError: String) {
        if (this.view != null) {
            val snackbar = Snackbar.make(this.view!!, textError, Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.retry), { v -> loadNewsCategories(false) })
            snackbar.duration = 4000
            snackbar.show()
        }
    }

    internal inner class NewsCategoryCallbacks : LoaderManager.LoaderCallbacks<List<NewsCategory>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<NewsCategory>> {
            return NewsCategoryLoader(context!!)
        }

        override fun onLoadFinished(loader: Loader<List<NewsCategory>>, data: List<NewsCategory>?) {
            showNewsCategories(data)
        }

        override fun onLoaderReset(loader: Loader<List<NewsCategory>>) {
        }
    }
}
