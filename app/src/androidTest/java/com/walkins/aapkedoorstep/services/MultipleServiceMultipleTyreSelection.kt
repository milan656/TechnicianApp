package com.walkins.aapkedoorstep.services

class MultipleServiceMultipleTyreSelection {

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

    var singleTyreSelection = false
    var vehicleWiseStoreData = false

    var vehicleMake_lf = ""
    var vehiclePattern_lf = ""
    var vehicleSize_lf = ""
    var issueResolveArrayList_lf = arrayListOf<String>()

    var vehicleMake_lr = ""
    var vehiclePattern_lr = ""
    var vehicleSize_lr = ""
    var issueResolveArrayList_lr = arrayListOf<String>()

    var vehicleMake_rf = ""
    var vehiclePattern_rf = ""
    var vehicleSize_rf = ""
    var issueResolveArrayList_rf = arrayListOf<String>()

    var vehicleMake_rr = ""
    var vehiclePattern_rr = ""
    var vehicleSize_rr = ""
    var issueResolveArrayList_rr = arrayListOf<String>()

    var moreSuggestions = ""
    var suggestionsArrayList = arrayListOf<String>()
    var manuFacturingDate_lf = ""
    var manuFacturingDate_lr = ""
    var manuFacturingDate_rf = ""
    var manuFacturingDate_rr = ""

    var serviceList = arrayListOf<String>()
    var car_1_photo = ""
    var car_2_photo = ""

    var psi_in = ""
    var psi_out = ""
    var weight = ""

    var sidewell_lf = ""
    var shoulder_lf = ""
    var treadWear_lf = ""
    var treadDepth_lf = ""
    var rimDamage_lf = ""
    var bubble_lf = ""

    var sidewell_lr = ""
    var shoulder_lr = ""
    var treadWear_lr = ""
    var treadDepth_lr = ""
    var rimDamage_lr = ""
    var bubble_lr = ""

    var sidewell_rf = ""
    var shoulder_rf = ""
    var treadWear_rf = ""
    var treadDepth_rf = ""
    var rimDamage_rf = ""
    var bubble_rf = ""

    var sidewell_rr = ""
    var shoulder_rr = ""
    var treadWear_rr = ""
    var treadDepth_rr = ""
    var rimDamage_rr = ""
    var bubble_rr = ""

}