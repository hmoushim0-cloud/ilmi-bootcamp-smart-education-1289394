package sa.factory.ilmibootcampsmarte.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IlmiBootcampEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smart_educationDao(): IlmiBootcampDao
}
