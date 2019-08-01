package com.synowkrz.housemanager.babyTask.model

data class Baby(val babyProfile: BabyProfile,
                val feedings: List<Feeding>,
                val diapers: List<Diaper>) {
}