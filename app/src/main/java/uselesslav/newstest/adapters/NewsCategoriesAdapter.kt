package uselesslav.newstest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.models.NewsCategory

/**
 * Адаптер списка новостных категорий
 */
class NewsCategoriesAdapter(
        private var newsCategories: List<NewsCategory>,
        private var onItemClick: OnItemClick)
    : RecyclerView.Adapter<NewsCategoriesAdapter.NewsCategoryHolder>() {

    /**
     * Реализация обработчика нажатий
     */
    private val internalListener = View.OnClickListener { view ->
        val newsCategory = view.tag as NewsCategory
        onItemClick.onItemClick(newsCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsCategoryHolder =
            NewsCategoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_news_category, parent, false))

    override fun onBindViewHolder(holder: NewsCategoryHolder, position: Int) {
        val newsCategory = newsCategories[position]

        // Заполнение разметки
        holder.bind(newsCategory)
        holder.itemView.tag = newsCategory
        holder.itemView.setOnClickListener(internalListener)
    }

    override fun getItemCount() = newsCategories.size

    /**
     * Обновление списка
     */
    fun changeDataSet(list: List<NewsCategory>) {
        newsCategories = list
        notifyDataSetChanged()
    }

    /**
     * Интерфейс обработки нажатий на элемент списка
     */
    interface OnItemClick {

        fun onItemClick(newsCategory: NewsCategory)

    }

    /**
     * Элемент списка
     */
    class NewsCategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Текстовое поле с названием категории
         */
        var name: TextView = view.findViewById(R.id.tv_name)

        /**
         * Заполнение разметки
         */
        fun bind(newsCategory: NewsCategory) {
            name.text = newsCategory.name
        }
    }
}