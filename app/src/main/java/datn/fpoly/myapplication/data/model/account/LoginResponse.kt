package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("message")
    val message : String,
    @SerializedName("user")
    val user : AccountResponse
        ){
}