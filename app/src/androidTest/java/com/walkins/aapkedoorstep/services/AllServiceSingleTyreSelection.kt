package com.walkins.aapkedoorstep.services

class AllServiceSingleTyreSelection {

    var invalidNumber: String? = "0900222414"
    var invalidNumber_2: String? = "9000090000"
    var validNumber: String? = "9898987777"
    var blankManufacturingDate: String? = ""
    var zeroManufacturingDate: String? = "0000"
    var twoletterManufacturingDate: String? = "20"
    var inValidWeekOfYearManufacturingDate: String? = "2321"
    var inValidYearManufacturingDate: String? = "2123"
    var validManufacturingDate: String? = "2121"

    var typeRotation_service = "Type Rotation" //Type Rotation
    var nitrogen_topup_service = "Nitrogen Top Up" //Nitrogen Top up
    var nitrogen_refill_service = "Nitrogen Refill" //Nitrogen Refill
    var wheel_balancing_service = "Wheel Balancing" //Wheel Balancing

    var singleTyreSelection = true
    var vehicleWiseStoreData = false

    var vehicleMakeURL = ""
    var vehicleMake = ""
    var vehiclePattern = ""
    var vehicleSize = ""
    var suggestionsArrayList = arrayListOf<String>()
    var issueResolveArrayList = arrayListOf<String>()

    var moreSuggestions = ""
    var manuFacturingDate = ""

    var serviceList = arrayListOf<String>()
    var car_1_photo = ""
    var car_2_photo = ""

    var psi_in = ""
    var psi_out = ""
    var weight = ""

    var sidewell = ""
    var shoulder = ""
    var treadWear = ""
    var treadDepth = ""
    var rimDamage = ""
    var bubble = ""

    var editFlowEnable = true


}