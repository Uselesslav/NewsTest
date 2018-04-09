package uselesslav.newstest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.model.News
import uselesslav.newstest.setDate
import uselesslav.newstest.setTime

/**
 * Адаптер списка новостей
 */
class ListNewsAdapter(
        private var shortNews: List<News>,
        private var onItemClick: OnItemClick)
    : RecyclerView.Adapter<ListNewsAdapter.NewsHolder>() {

    /**
     * Реализация обработчика нажатий
     */
    private val internalListener = View.OnClickListener { view ->
        val shortNews = view.tag as News
        onItemClick.onItemClick(shortNews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder =
            NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_news, parent, false))

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val shortNews = shortNews[position]

        // Заполнение разметки
        holder.bind(shortNews)
        holder.itemView.tag = shortNews
        holder.itemView.setOnClickListener(internalListener)
    }

    override fun getItemCount() = shortNews.size

    /**
     * Обновление списка
     */
    fun changeDataSet(list: List<News>) {
        shortNews = list
        notifyDataSetChanged()
    }

    /**
     * Интерфейс обработки нажатий на элемент списка
     */
    interface OnItemClick {

        fun onItemClick(news: News)

    }

    /**
     * Элемент списка
     */
    class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Текстовые поля
         */
        private var title: TextView = view.findViewById(R.id.tv_title)
        private var shortDescription: TextView = view.findViewById(R.id.tv_short_description)
        private var date: TextView = view.findViewById(R.id.tv_date)
        private var time: TextView = view.findViewById(R.id.tv_time)

        /**
         * Заполнение разметки
         */
        fun bind(shortNews: News) {

            title.text = shortNews.title
            shortDescription.text = shortNews.shortDescription
            date.setDate(shortNews.date)
            time.setTime(shortNews.date)
        }
    }
}