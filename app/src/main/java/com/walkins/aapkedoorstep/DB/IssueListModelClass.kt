package com.walkins.aapkedoorstep.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issuelist")
class IssueListModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0
    var issueId: Int? = 0
    var name: String? = ""
    var isSelected: Boolean = false
}