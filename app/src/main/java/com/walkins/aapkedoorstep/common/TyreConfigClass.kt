package com.walkins.aapkedoorstep.common

import java.io.File
import java.io.InputStream

class TyreConfigClass {

    companion object {
        var selectedTyreConfigType = ""

        var LFVehicleMake = false
        var LFVehiclePattern = false
        var LFVehicleSize = false
        var LFVehicleVisualDetail = false

        var RFVehicleMake = false
        var RFVehiclePattern = false
        var RFVehicleSize = false
        var RFVehicleVisualDetail = false

        var RRVehicleMake = false
        var RRVehiclePattern = false
        var RRVehicleSize = false
        var RRVehicleVisualDetail = false

        var LRVehicleMake = false
        var LRVehiclePattern = false
        var LRVehicleSize = false
        var LRVehicleVisualDetail = false

        var LFCompleted = false
        var LRCompleted = false
        var RFCompleted = false
        var RRCompleted = false

        var selectedMakeURL = ""

        var clickedTyre = ""
        var pendingTyre = ""

        var TyreLFObject = "TyreLFObject"
        var TyreLRObject = "TyreLRObject"
        var TyreRFObject = "TyreRFObject"
        var TyreRRObject = "TyreRRObject"

        var serviceDetailData = "serviceDetailData"

        var nextDueDate = ""
        var moreSuggestions = ""
        var CarPhoto_1 = ""
        var CarPhoto_2 = ""

        var backgroundWebServiceCallTime = "backgroundWebServiceCallTime"

        var serviceList = "serviceList"
        var commentList = "commentList"
        var issueList = "issueList"

        var car_1_stream: InputStream? = null
        var car_2_stream: InputStream? = null

        var car_1_file:File?=null
        var car_2_file:File?=null

    }
}