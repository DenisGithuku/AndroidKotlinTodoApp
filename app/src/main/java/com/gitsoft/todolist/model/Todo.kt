package com.gitsoft.todolist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@Entity(tableName = "todo_table")

data class Todo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Int,
    val title: String?,
    val description: String?,
    val addedAt: String?,
    var isComplete: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(todoId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(addedAt)
        parcel.writeValue(isComplete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}