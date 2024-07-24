package com.silvertown.android.dailyphrase.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.silvertown.android.dailyphrase.data.MemberPreferences
import com.silvertown.android.dailyphrase.domain.model.Member
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MemberPreferencesDataSource @Inject constructor(
    private val memberPreferences: DataStore<MemberPreferences>,
) {
    val memberData = memberPreferences.data
        .map { preferences ->
            Member(
                id = preferences.id.takeIf { it.toInt() != 0 } ?: DEFAULT_ID.toLong(),
                name = preferences.name.takeIf { it.isNotBlank() } ?: DEFAULT_NAME,
                imageUrl = preferences.imageUrl.takeIf { it.isNotBlank() } ?: DEFAULT_IMAGE_URL,
                sharedCount = preferences.sharedCount.takeIf { it >= 0 } ?: DEFAULT_SHARED_COUNT,
                email = DEFAULT_EMAIL,
                quitAt = DEFAULT_QUIT_AT
            )
        }

    suspend fun getMemberId(): Long {
        return memberPreferences.data
            .map { preferences ->
                preferences.id
            }.firstOrNull() ?: -1
    }

    suspend fun setMember(
        memberId: Long,
        name: String,
        imageUrl: String,
    ) {
        memberPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setId(memberId)
                .setName(name)
                .setImageUrl(imageUrl)
                .build()
        }
    }

    suspend fun setName(name: String) {
        memberPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setName(name)
                .build()
        }
    }

    suspend fun setImageUrl(imageUrl: String) {
        memberPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setImageUrl(imageUrl)
                .build()
        }
    }

    suspend fun setMemberId(memberId: Long) {
        memberPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setId(memberId)
                .build()
        }
    }

    suspend fun updateSharedCount(count: Int) {
        try {
            memberPreferences.updateData { currentPreferences ->
                currentPreferences.toBuilder()
                    .setSharedCount(count)
                    .build()
            }
        } catch (ioException: IOException) {
            Timber.tag("MemberPref").e(ioException, "Failed to update member preferences")
        }
    }

    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_NAME = "User"
        const val DEFAULT_IMAGE_URL =
            "https://cdn.pixabay.com/photo/2015/06/25/04/50/hand-print-820913_1280.jpg"
        const val DEFAULT_SHARED_COUNT = 0
        const val DEFAULT_EMAIL = ""
        const val DEFAULT_QUIT_AT = ""
    }
}
