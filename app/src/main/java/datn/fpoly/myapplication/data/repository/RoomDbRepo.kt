package datn.fpoly.myapplication.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.roomdb.RoomDb
import datn.fpoly.myapplication.data.model.roomdb.RoomDbModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RoomDbRepo @Inject constructor(
    private val roomDb: RoomDb,
    private val authRepo: AuthRepo,
    private val gson: Gson
) {

    fun getCart():LiveData<Order?> = Transformations.map(roomDb.roomDAO().getCart(authRepo.getUser()?._id?:"empty")){ json ->
        if(json != null){
            gson.fromJson(json,Order::class.java)
        }else{
            insertCart(Order(idUser = authRepo.getUser()?._id))
            null
        }
    }

    private fun insertCart(order: Order) = roomDb.roomDAO().insert(RoomDbModel(key = order.idUser ?: "empty", value = gson.toJson(order)))

    fun updateCart(order: Order) = roomDb.roomDAO().update(RoomDbModel(key = order.idUser ?: "empty", value = gson.toJson(order)))

}