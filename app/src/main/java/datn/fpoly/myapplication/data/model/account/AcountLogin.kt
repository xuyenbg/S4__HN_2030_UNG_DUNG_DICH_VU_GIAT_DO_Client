package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName

data class AcountLogin (
    @SerializedName("phone")
    var phone : String?,
    @SerializedName("userId")
    var userId : String?,
)