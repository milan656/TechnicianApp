package com.walkins.technician.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("uuid")
    @Expose
    val uuid: String,
    @SerializedName("first_name")
    @Expose
    val firstName: String,
    @SerializedName("last_name")
    @Expose
    val lastName: String,
    @SerializedName("business_name")
    @Expose
    val businessName: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("mobile")
    @Expose
    val mobile: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("pan_number")
    @Expose
    val panNumber: String,
    @SerializedName("gst")
    @Expose
    val gst: String,
    @SerializedName("ifsc_code")
    @Expose
    val ifscCode: String,
    @SerializedName("bank_name")
    @Expose
    val bankName: String,
    @SerializedName("bank_acc_number")
    @Expose
    val bankAccNumber: String,
    @SerializedName("birth_date")
    @Expose
    val birthDate: String,
    @SerializedName("anniversary_date")
    @Expose
    val anniversaryDate: String,
    @SerializedName("store_anniversary_date")
    @Expose
    val storeAnniversaryDate: String,
    @SerializedName("services")
    @Expose
    val services: String,
    @SerializedName("tyre_brands")
    @Expose
    val tyreBrands: String,
    @SerializedName("vehicle_category")
    @Expose
    val vehicleCategory: String,
    @SerializedName("address")
    @Expose
    val address: String,
    @SerializedName("city")
    @Expose
    val city: String,
    @SerializedName("state")
    @Expose
    val state: String,
    @SerializedName("pin_code")
    @Expose
    val pinCode: String

)