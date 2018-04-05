package uselesslav.newstest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.model.ShortNews
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
        var time: TextView = view.findViewById(R.id.tv_time)

        /**
         * Заполнение разметки
         */
        fun bind(shortNews: ShortNews) {

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            try {
                calendar.time = dateFormat.parse(shortNews.date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val year = intToString(calendar[Calendar.YEAR])
            val month = intToString(calendar[Calendar.MONTH])
            val day = intToString(calendar[Calendar.DAY_OF_MONTH])

            val hour = intToString(calendar[Calendar.HOUR])
            val minute = intToString(calendar[Calendar.MINUTE])
            val second = intToString(calendar[Calendar.SECOND])

            date.text = date.resources.getString(R.string.date, day, month, year)
            time.text = time.resources.getString(R.string.time, hour, minute, second)

            title.text = shortNews.title
            shortDescription.text = shortNews.shortDescription
        }

        private fun intToString(int: Int): String {
            if (int < 10) {
                return "0" + int.toString()
            } else {
                return int.toString()
            }
        }
    }
}