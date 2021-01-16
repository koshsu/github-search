package com.koshsu.githubsearch.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [Index("repoId")]
)
data class Owner(
    @PrimaryKey(autoGenerate = false) @SerializedName("id") val ownerId: Long,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    var repoId: Long = 0L
)