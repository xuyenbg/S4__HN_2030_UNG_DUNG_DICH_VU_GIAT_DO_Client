package datn.fpoly.myapplication.data.repository

import com.google.gson.Gson
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.roomdb.RoomDb
import datn.fpoly.myapplication.data.model.roomdb.RoomDbModel
import javax.inject.Inject

class RoomDbRepo @Inject constructor(
    private val roomDb: RoomDb,
    private val gson: Gson
) {
    fun getCart(key:String):Order = gson.fromJson(roomDb.roomDAO().getCart(key),Order::class.java)

    fun insertCart(order: Order) = roomDb.roomDAO().insert(RoomDbModel(key = order.idUser ?: "empty", value = gson.toJson(order)))

    fun updateCart(order: Order) = roomDb.roomDAO().update(RoomDbModel(key = order.idUser ?: "empty", value = gson.toJson(order)))

}