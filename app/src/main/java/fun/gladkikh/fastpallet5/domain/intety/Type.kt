package com.anit.fastpallet4.domain.intity

enum class Type(val id: Int, val fullName: String, val nameServer: String) {
    CREATE_PALLET(1, "Создание паллет", "ФормированиеПалет"),
    INVENTORY_PALLET(2, "Инвентаризация", "ИнвентаризацияПалет"),
    ACTION_PALLET(3, "Перемещение", "ПеремещениеТоваров"),
    INVENTORY_DOC_PALLET(4, "Инвентаризация документа", "Инвентаризация документа");

    companion object {
        fun getTypeById(id: Int): Type? {
            return when (id) {
                1 -> CREATE_PALLET
                2 -> INVENTORY_PALLET
                3 -> ACTION_PALLET
                4 -> INVENTORY_DOC_PALLET
                else -> null
            }
        }
    }
}