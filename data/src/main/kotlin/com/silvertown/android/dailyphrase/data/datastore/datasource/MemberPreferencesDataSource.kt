package com.silvertown.android.dailyphrase.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.silvertown.android.dailyphrase.data.MemberPreferences
import com.silvertown.android.dailyphrase.domain.model.Member
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemberPreferencesDataSource @Inject constructor(
    private val memberPreferences: DataStore<MemberPreferences>,
) {
    val memberData = memberPreferences.data
        .map { preferences ->
            Member(
                id = preferences.id,
                name = preferences.name,
                imageUrl = preferences.imageUrl,
                email = "",
                quitAt = ""
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

}
