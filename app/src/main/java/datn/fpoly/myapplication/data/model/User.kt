package datn.fpoly.myapplication.data.model
import com.google.gson.annotations.SerializedName


data class User (
    @SerializedName("username" )
    var username : String? = null,
    @SerializedName("password" )
    var password : String? = null
)