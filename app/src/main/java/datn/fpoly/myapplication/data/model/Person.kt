package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Person (

    @SerializedName("createdAt" ) var createdAt : Date? = null,
    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("avatar"    ) var avatar    : String? = null,
    @SerializedName("id"        ) var id        : String? = null

) {
    override fun toString(): String {
        return "Person(createdAt=${SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(createdAt ?: Date())}, name=$name, avatar=$avatar, id=$id)"
    }
}
