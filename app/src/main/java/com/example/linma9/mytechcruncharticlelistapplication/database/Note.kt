package org.androidsummit.notepad.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import org.threeten.bp.Instant
//import java.time.Instant

// code from lab, not used in this project
@Entity(tableName = "note")
class Note {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "text")
    var text: String? = null

    @ColumnInfo(name = "created_at")
    var createdAt = Instant.now()

    @ColumnInfo(name = "updated_at")
    var updatedAt = createdAt
}

//class Note {
//
//    @PrimaryKey(autoGenerate = true)
//    var id = 0
//
//    @ColumnInfo(name = "text")
//    var text: String? = null
//
//    @ColumnInfo(name = "created_at")
//    var createdAt = Instant.now()
//
//    @ColumnInfo(name = "updated_at")
//    var updatedAt = createdAt
//}
