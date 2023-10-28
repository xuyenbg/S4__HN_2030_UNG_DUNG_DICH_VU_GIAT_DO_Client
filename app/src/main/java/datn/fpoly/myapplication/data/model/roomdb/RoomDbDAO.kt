package datn.fpoly.myapplication.data.model.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDbDAO {
    @Insert
    fun insert(roomDbModel: RoomDbModel)

    @Update
    fun update(roomDbModel: RoomDbModel)

    @Query("select RoomDbModel.value from RoomDbModel where RoomDbModel.`key` = :key limit 1")
    fun getCart(key:String):LiveData<String?>
}