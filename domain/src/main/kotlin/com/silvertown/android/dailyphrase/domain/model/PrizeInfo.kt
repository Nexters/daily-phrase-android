package com.silvertown.android.dailyphrase.domain.model

data class PrizeInfo(
    val total: Int,
    val items: List<Item>,
) {
    data class Item(
        val eventId: Int,
        val imageUrl: String,
        val manufacturer: String,
        val myEntryCount: Int,
        val name: String,
        val prizeId: Int,
        val requiredTicketCount: Int,
        val totalParticipantCount: Int,
        val shortName: String,
        val totalEntryCount: Int,
        val myTicketCount: Int,
        val entryResult: EntryResult,
    ) {
        data class EntryResult(
            val status: Status,
            val phoneNumber: String?,
            val isChecked: Boolean,
        ) {
            enum class Status(val value: String) {
                ENTERED("ENTERED"),
                WINNING("WINNING"),
                MISSED("MISSED"),
                UNKNOWN("UNKNOWN"),
                ;

                companion object {
                    fun ofValue(value: String): Status {
                        return entries.find { it.value == value } ?: UNKNOWN
                    }
                }
            }

            fun toWinningEntryResult(phoneNumber: String): EntryResult {
                return EntryResult(
                    status = Status.WINNING,
                    phoneNumber = phoneNumber,
                    isChecked = true
                )
            }

            fun toCheckedEntryResult(): EntryResult {
                return this.copy(isChecked = true)
            }
        }
    }
}
