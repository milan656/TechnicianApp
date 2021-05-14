package com.walkins.aapkedoorstep.model.login.servicemodel.servicedata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walkins.aapkedoorstep.model.login.service.ServiceModelData

data class ServiceDataByIdData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("uuid")
    @Expose
    val uuid: String,
    @SerializedName("vehicle_id")
    @Expose
    val vehicleId: Int,
    @SerializedName("dealer_id")
    @Expose
    val dealerId: Int,
    @SerializedName("service_user_id")
    @Expose
    val serviceUserId: String,
    @SerializedName("service_activity_history_id")
    @Expose
    val serviceActivityHistoryId: String,
    @SerializedName("date_of_service")
    @Expose
    val dateOfService: String,
    @SerializedName("technician_name")
    @Expose
    val technicianName: String,
    @SerializedName("walkins_team_member")
    @Expose
    val walkinsTeamMember: String,
    @SerializedName("front_left_tyre_make")
    @Expose
    val frontLeftTyreMake: String,
    @SerializedName("front_right_tyre_make")
    @Expose
    val frontRightTyreMake: String,
    @SerializedName("front_left_tyre_make_image")
    @Expose
    val front_left_tyre_make_image: String,
    @SerializedName("front_right_tyre_make_image")
    @Expose
    val front_right_tyre_make_image: String,
    @SerializedName("back_left_tyre_make_image")
    @Expose
    val back_left_tyre_make_image: String,
    @SerializedName("back_right_tyre_make_image")
    @Expose
    val back_right_tyre_make_image: String,
    @SerializedName("back_left_tyre_make")
    @Expose
    val backLeftTyreMake: String,
    @SerializedName("back_right_tyre_make")
    @Expose
    val backRightTyreMake: String,
    @SerializedName("front_left_tyre_size")
    @Expose
    val frontLeftTyreSize: String,
    @SerializedName("front_right_tyre_size")
    @Expose
    val frontRightTyreSize: String,
    @SerializedName("back_left_tyre_size")
    @Expose
    val backLeftTyreSize: String,
    @SerializedName("back_right_tyre_size")
    @Expose
    val backRightTyreSize: String,
    @SerializedName("front_left_tyre_pattern")
    @Expose
    val frontLeftTyrePattern: String,
    @SerializedName("front_right_tyre_pattern")
    @Expose
    val frontRightTyrePattern: String,
    @SerializedName("back_left_tyre_pattern")
    @Expose
    val backLeftTyrePattern: String,
    @SerializedName("back_right_tyre_pattern")
    @Expose
    val backRightTyrePattern: String,
    @SerializedName("front_left_tyre_side_wall")
    @Expose
    val frontLeftTyreSideWall: String,
    @SerializedName("front_right_tyre_side_wall")
    @Expose
    val frontRightTyreSideWall: String,
    @SerializedName("back_left_tyre_side_wall")
    @Expose
    val backLeftTyreSideWall: String,
    @SerializedName("back_right_tyre_side_wall")
    @Expose
    val backRightTyreSideWall: String,
    @SerializedName("front_left_tyre_shoulder")
    @Expose
    val frontLeftTyreShoulder: String,
    @SerializedName("front_right_tyre_shoulder")
    @Expose
    val frontRightTyreShoulder: String,
    @SerializedName("back_left_tyre_shoulder")
    @Expose
    val backLeftTyreShoulder: String,
    @SerializedName("back_right_tyre_shoulder")
    @Expose
    val backRightTyreShoulder: String,
    @SerializedName("front_left_tyre_tread_wear")
    @Expose
    val frontLeftTyreTreadWear: String,
    @SerializedName("front_right_tyre_tread_wear")
    @Expose
    val frontRightTyreTreadWear: String,
    @SerializedName("back_left_tyre_tread_wear")
    @Expose
    val backLeftTyreTreadWear: String,
    @SerializedName("back_right_tyre_tread_wear")
    @Expose
    val backRightTyreTreadWear: String,
    @SerializedName("front_left_tyre_tread_depth")
    @Expose
    val frontLeftTyreTreadDepth: String,
    @SerializedName("front_right_tyre_tread_depth")
    @Expose
    val frontRightTyreTreadDepth: String,
    @SerializedName("back_left_tyre_tread_depth")
    @Expose
    val backLeftTyreTreadDepth: String,
    @SerializedName("back_right_tyre_tread_depth")
    @Expose
    val backRightTyreTreadDepth: String,
    @SerializedName("front_left_tyre_rim_demage")
    @Expose
    val frontLeftTyreRimDemage: String,
    @SerializedName("front_right_tyre_rim_demage")
    @Expose
    val frontRightTyreRimDemage: String,
    @SerializedName("back_left_tyre_rim_demage")
    @Expose
    val backLeftTyreRimDemage: String,
    @SerializedName("back_right_tyre_rim_demage")
    @Expose
    val backRightTyreRimDemage: String,
    @SerializedName("front_left_tyre_buldge_bubble")
    @Expose
    val frontLeftTyreBuldgeBubble: String,
    @SerializedName("front_right_tyre_buldge_bubble")
    @Expose
    val frontRightTyreBuldgeBubble: String,
    @SerializedName("back_left_tyre_buldge_bubble")
    @Expose
    val backLeftTyreBuldgeBubble: String,
    @SerializedName("back_right_tyre_buldge_bubble")
    @Expose
    val backRightTyreBuldgeBubble: String,
    @SerializedName("front_left_tyre_psi_in")
    @Expose
    val frontLeftTyrePsiIn: String,
    @SerializedName("front_right_tyre_psi_in")
    @Expose
    val frontRightTyrePsiIn: String,
    @SerializedName("back_left_tyre_psi_in")
    @Expose
    val backLeftTyrePsiIn: String,
    @SerializedName("back_right_tyre_psi_in")
    @Expose
    val backRightTyrePsiIn: String,
    @SerializedName("front_left_tyre_psi_out")
    @Expose
    val frontLeftTyrePsiOut: String,
    @SerializedName("front_right_tyre_psi_out")
    @Expose
    val frontRightTyrePsiOut: String,
    @SerializedName("back_left_tyre_psi_out")
    @Expose
    val backLeftTyrePsiOut: String,
    @SerializedName("back_right_tyre_psi_out")
    @Expose
    val backRightTyrePsiOut: String,
    @SerializedName("front_left_tyre_weight")
    @Expose
    val frontLeftTyreWeight: String,
    @SerializedName("front_right_tyre_weight")
    @Expose
    val front_right_tyre_weight: String,
    @SerializedName("back_left_tyre_weight")
    @Expose
    val backLeftTyreWeight: String,
    @SerializedName("back_right_tyre_weight")
    @Expose
    val backRightTyreWeight: String,
    @SerializedName("front_left_tyre_wheel_rotation")
    @Expose
    val frontLeftTyreWheelRotation: String,
    @SerializedName("back_left_tyre_wheel_rotation")
    @Expose
    val backLeftTyreWheelRotation: String,
    @SerializedName("back_right_tyre_wheel_rotation")
    @Expose
    val backRightTyreWheelRotation: String,
    @SerializedName("front_left_tyre_wheel_image")
    @Expose
    val frontLeftTyreWheelImage: String,
    @SerializedName("back_left_tyre_wheel_image")
    @Expose
    val backLeftTyreWheelImage: String,
    @SerializedName("back_right_tyre_wheel_image")
    @Expose
    val backRightTyreWheelImage: String,
    @SerializedName("wheel_rotation_text")
    @Expose
    val wheelRotationText: String,
    @SerializedName("service_narration")
    @Expose
    val serviceNarration: String,
    @SerializedName("technician_suggestion")
    @Expose
    val technicianSuggestion: String,
    @SerializedName("front_left_manufacturing_date")
    @Expose
    val frontLeftManufacturingDate: String,
    @SerializedName("front_right_manufacturing_date")
    @Expose
    val frontRightManufacturingDate: String,
    @SerializedName("back_left_manufacturing_date")
    @Expose
    val backLeftManufacturingDate: String,
    @SerializedName("back_right_manufacturing_date")
    @Expose
    val backRightManufacturingDate: String,
    @SerializedName("front_left_issues_to_be_resolved")
    @Expose
    val frontLeftIssuesToBeResolved: List<String>,
    @SerializedName("front_right_issues_to_be_resolved")
    @Expose
    val frontRightIssuesToBeResolved: List<String>,
    @SerializedName("back_left_issues_to_be_resolved")
    @Expose
    val backLeftIssuesToBeResolved: List<String>,
    @SerializedName("back_right_issues_to_be_resolved")
    @Expose
    val backRightIssuesToBeResolved: List<String>,
    @SerializedName("additional_comments")
    @Expose
    val additionalComments: String,
    @SerializedName("summary_from_expert")
    @Expose
    val summaryFromExpert: String,
    @SerializedName("service_suggestions")
    @Expose
    val serviceSuggestions: String,
    @SerializedName("next_service_due")
    @Expose
    val nextServiceDue: String,
    @SerializedName("last_service_history")
    @Expose
    val lastServiceHistory: String,
    @SerializedName("actual_service_date")
    @Expose
    val actualServiceDate: String,
    @SerializedName("future_service_date")
    @Expose
    val futureServiceDate: String,
    @SerializedName("service_scheduled_date")
    @Expose
    val service_scheduled_date: String,
    @SerializedName("services_to_do")
    @Expose
    val servicesToDo: List<Int>,
    @SerializedName("car_photo_1")
    @Expose
    val carPhoto1: String,
    @SerializedName("car_photo_2")
    @Expose
    val carPhoto2: String,
    @SerializedName("created_by")
    @Expose
    val createdBy: String,
    @SerializedName("updated_by")
    @Expose
    val updatedBy: String,
    @SerializedName("created_at")
    @Expose
    val createdAt: String,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String,
    @SerializedName("technician_suggestions")
    @Expose
    val technicianSuggestions: List<String>,
    @SerializedName("front_right_tyre_wheel_image")
    @Expose
    val frontRightTyreWheelImage: String,
    @SerializedName("service")
    @Expose
    val service: List<ServiceModelData>,
    @SerializedName("reg_number")
    @Expose
    val regNumber: String,
    @SerializedName("make")
    @Expose
    val make: String,
    @SerializedName("make_image")
    @Expose
    val makeImage: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("model_image")
    @Expose
    val modelImage: String,
    @SerializedName("technician_image")
    @Expose
    val technicianImage: String,
    @SerializedName("comment_id")
    @Expose
    val comment_id: List<Int>,
    @SerializedName("front_left_tyre_wheel_rotation")
    @Expose
    val front_left_tyre_wheel_rotation: String,
    @SerializedName("back_left_tyre_wheel_rotation")
    @Expose
    val back_left_tyre_wheel_rotation: String,
    @SerializedName("front_right_tyre_wheel_rotation")
    @Expose
    val front_right_tyre_wheel_rotation: String,
    @SerializedName("back_right_tyre_wheel_rotation")
    @Expose
    val back_right_tyre_wheel_rotation: String

)