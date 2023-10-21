package datn.fpoly.myapplication.data.model.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomDbModel::class], version = 2, exportSchema = false)
abstract class RoomDb :RoomDatabase(){
    abstract fun roomDAO():RoomDbDAO
    companion object{
        @Volatile
        private var INSTANCE:RoomDb? = null
        fun getDatabase(context: Context):RoomDb{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,RoomDb::class.java,"user_db")
                    .allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}