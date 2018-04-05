package uselesslav.newstest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.model.ShortNews

/**
 * Адаптер списка новостей
 */
class ListNewsAdapter(
        private var shortNews: List<ShortNews>,
        private var onItemClick: OnItemClick)
    : RecyclerView.Adapter<ListNewsAdapter.NewsHolder>() {

    /**
     * Реализация обработчика нажатий
     */
    private val internalListener = View.OnClickListener { view ->
        val shortNews = view.tag as ShortNews
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
    fun changeDataSet(list: List<ShortNews>) {
        shortNews = list
        notifyDataSetChanged()
    }

    /**
     * Интерфейс обработки нажатий на элемент списка
     */
    interface OnItemClick {

        fun onItemClick(news: ShortNews)

    }

    /**
     * Элемент списка
     */
    class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Текстовые поля
         */
        var title: TextView = view.findViewById(R.id.tv_title)
        var shortDescription: TextView = view.findViewById(R.id.tv_short_description)
        var date: TextView = view.findViewById(R.id.tv_date)

        /**
         * Заполнение разметки
         */
        fun bind(shortNews: ShortNews) {
            title.text = shortNews.title
            shortDescription.text = shortNews.shortDescription
            date.text = shortNews.date
        }
    }
}