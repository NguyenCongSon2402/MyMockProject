package com.example.mymockproject.model.response

data class CrewItemData(
    val adult: Boolean,
    val gender: Int,
    val known_for_department: String,
    val id: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String,
    val credit_id: String,
    val department: String,
    val job: String
)