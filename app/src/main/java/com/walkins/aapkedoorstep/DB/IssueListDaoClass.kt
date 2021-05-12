package com.walkins.aapkedoorstep.DB

import androidx.room.*
import androidx.room.Dao
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData

@Dao
interface IssueListDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIssue(book: IssueListModelClass)

    @Query(value = "Select * from issuelist")
    fun getAllIssue(): List<IssueListModelClass>

    @Query("delete from issuelist")
    fun deleteAll()

    @Update
    fun update(model: IssueListModelClass)


}