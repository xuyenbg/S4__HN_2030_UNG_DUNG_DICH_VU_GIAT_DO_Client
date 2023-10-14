package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Person (

    @SerializedName("createdAt" ) var createdAt : Date? = null,
    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("avatar"    ) var avatar    : String? = null,
    @SerializedName("id"        ) var id        : String? = null

) {
    override fun toString(): String {
        return "Person(createdAt=$createdAt, name=$name, avatar=$avatar, id=$id)"
    }
}
