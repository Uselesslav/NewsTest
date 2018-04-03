package uselesslav.newstest.model

import com.google.gson.annotations.SerializedName

class NewsCategory(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String)