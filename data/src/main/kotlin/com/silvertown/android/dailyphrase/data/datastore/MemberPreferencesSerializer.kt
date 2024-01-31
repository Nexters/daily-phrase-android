package com.silvertown.android.dailyphrase.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.silvertown.android.dailyphrase.data.MemberPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MemberPreferencesSerializer @Inject constructor() : Serializer<MemberPreferences> {
    override val defaultValue: MemberPreferences = MemberPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MemberPreferences =
        try {
            MemberPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: MemberPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
