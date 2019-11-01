package com.amazinline.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "customers")
data class Customer constructor(

    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "email")
    var email: String? = "",
    @ColumnInfo(name = "keywords")
    var identityKeyword: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "shirt_neck_size")
    var shirtNeckSize: String = "",
    @ColumnInfo(name = "shirt_arm_length")
    var shirtArmLength: String = "",
    @ColumnInfo(name = "shirt_size")
    var shirtSize: String = "",
    @ColumnInfo(name = "pant_waist_size")
    var pantWaistSize: String = "",
    @ColumnInfo(name = "pant_inseam")
    var pantInseam: String = "",
    @ColumnInfo(name = "pant_size")
    var pantSize: String = "",
    @ColumnInfo(name = "shoe_size")
    var shoeSize: String = "") : Serializable