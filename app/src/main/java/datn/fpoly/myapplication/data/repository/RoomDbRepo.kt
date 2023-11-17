package datn.fpoly.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.roomdb.RoomDb
import datn.fpoly.myapplication.data.model.roomdb.RoomDbModel
import javax.inject.Inject

class RoomDbRepo @Inject constructor(
    private val roomDb: RoomDb,
    private val authRepo: AuthRepo,
    private val gson: Gson
) {

    fun getCart():LiveData<OrderBase?> = Transformations.map(roomDb.roomDAO().getCart(authRepo.getUser()?.id?:"empty")){ json ->
        if(json != null){
            gson.fromJson(json,OrderBase::class.java)
        }else{
            insertCart(OrderBase(idUser = authRepo.getUser()?.id))
            null
        }
    }

    private fun insertCart(orderBase: OrderBase) = roomDb.roomDAO().insert(RoomDbModel(key = orderBase.idUser ?: "empty", value = gson.toJson(orderBase)))

    fun updateCart(orderBase: OrderBase){
        var total = 0.0
        orderBase.listItem.forEach{
            total += it.total ?: 0.0
        }
        orderBase.total = total
        roomDb.roomDAO().update(RoomDbModel(key = orderBase.idUser ?: "empty", value = gson.toJson(orderBase)))
    }

    fun clearCart(){
       updateCart(orderBase = OrderBase(idUser = authRepo.getUser()?.id))
    }

}