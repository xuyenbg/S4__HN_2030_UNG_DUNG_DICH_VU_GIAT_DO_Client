package datn.fpoly.myapplication.data.model.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDbModel(
    @PrimaryKey
    var key:String,
    @ColumnInfo(name = "value")
    var value:String
)