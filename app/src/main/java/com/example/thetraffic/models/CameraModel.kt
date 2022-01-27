package com.example.thetraffic.models

import java.io.Serializable

class CameraModel: Serializable {

    var id: Int? = null
    var rasm: String? = null
    var nomi: String? = null
    var malumot: String? = null
    var kategoriya: String? = null
    var like: Int? = null
    var tanlangan: String? = null

    constructor()
    constructor(
        id: Int?,
        rasm: String?,
        nomi: String?,
        malumot: String?,
        kategoriya: String?,
        like: Int?,
        tanlangan: String?
    ) {
        this.id = id
        this.rasm = rasm
        this.nomi = nomi
        this.malumot = malumot
        this.kategoriya = kategoriya
        this.like = like
        this.tanlangan = tanlangan
    }

    constructor(
        rasm: String?,
        nomi: String?,
        malumot: String?,
        kategoriya: String?,
        like: Int?,
        tanlangan: String?
    ) {
        this.rasm = rasm
        this.nomi = nomi
        this.malumot = malumot
        this.kategoriya = kategoriya
        this.like = like
        this.tanlangan = tanlangan
    }


}