package com.example.thetraffic.models

import java.io.Serializable

class CameraModel: Serializable {

    var id: Int? = null
    var rasm: String? = null
    var nomi: String? = null
    var malumot: String? = null
    var kategoriya: String? = null

    constructor()
    constructor(id: Int?, rasm: String?, nomi: String?, malumot: String?, kategoriya: String?) {
        this.id = id
        this.rasm = rasm
        this.nomi = nomi
        this.malumot = malumot
        this.kategoriya = kategoriya
    }

    constructor(rasm: String?, nomi: String?, malumot: String?, kategoriya: String?) {
        this.rasm = rasm
        this.nomi = nomi
        this.malumot = malumot
        this.kategoriya = kategoriya
    }


}