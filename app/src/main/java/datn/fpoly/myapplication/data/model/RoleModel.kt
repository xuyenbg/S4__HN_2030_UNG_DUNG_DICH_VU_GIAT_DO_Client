package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RoleModel (
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null
): Serializable