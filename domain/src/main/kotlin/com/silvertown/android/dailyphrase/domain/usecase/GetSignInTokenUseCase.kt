package com.silvertown.android.dailyphrase.domain.usecase

import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import javax.inject.Inject
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.SignInToken

class GetSignInTokenUseCase @Inject constructor(
    val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(token: String): Result<SignInToken> =
        memberRepository.signInWithKaKaoTokenViaServer(token)
}
