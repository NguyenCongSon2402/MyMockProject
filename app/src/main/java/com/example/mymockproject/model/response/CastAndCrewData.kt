package com.example.mymockproject.model.response

data class CastAndCrewData(
    val id: Int,
    val cast: ArrayList<CastItemData>,
    val crew: ArrayList<CrewItemData>
)