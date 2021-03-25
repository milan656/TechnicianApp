package com.jkadvantagandbadsha.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPermissionModel(


    @SerializedName("permission")
    @Expose var permission: String

)