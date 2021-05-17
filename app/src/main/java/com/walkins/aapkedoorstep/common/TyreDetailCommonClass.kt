package com.walkins.aapkedoorstep.common

import android.net.Uri
import java.io.InputStream

class TyreDetailCommonClass {

    companion object {

        var tyreType: String? = ""
        var vehicleMake: String? = ""
        var vehicleMakeId: String? = ""
        var vehicleMakeURL: String? = ""
        var vehiclePattern: String? = ""
        var vehiclePatternId: String? = ""
        var vehicleSize: String? = ""
        var vehicleSizeId: String? = ""
        var manufaturingDate: String? = ""
        var psiInTyreService: String? = ""
        var psiOutTyreService: String? = ""
        var weightTyreService: String? = ""

        var sidewell: String? = ""
        var shoulder: String? = ""
        var treadWear: String? = ""
        var treadDepth: String? = ""
        var rimDamage: String? = ""
        var bubble: String? = ""
        var issueResolvedArr: ArrayList<String>? = ArrayList()
        var visualDetailPhotoUrl: String? = ""
        var isCameraSelectedVisualDetail = false

        var chk1Make: String? = ""
        var chk2Make: String? = ""
        var chk3Make: String? = ""

        var chk1Pattern: String? = ""
        var chk2Pattern: String? = ""
        var chk3Pattern: String? = ""

        var chk1Size: String? = ""
        var chk2Size: String? = ""
        var chk3Size: String? = ""

        var isCompleted = false

        var make_id: Int = -1
        var model_id: Int = -1

        var inputStream: InputStream? = null

        var tyre_Uri_LF: Uri? = null
        var tyre_Uri_LR: Uri? = null
        var tyre_Uri_RF: Uri? = null
        var tyre_Uri_RR: Uri? = null

    }
}