package com.example

import com.github.awsjavakit.attempt.Try.attempt
import software.amazon.awssdk.core.internal.waiters.ResponseOrException.response
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse
import java.util.concurrent.Callable

class AwsIdentity(
    private val stsClient: StsClient,
) {
    fun getCallerIdentity(): GetCallerIdentityResponse =
        stsClient.getCallerIdentity(
            GetCallerIdentityRequest.builder().build(),
        )

    fun isLoggedIn(): Boolean =
        runCatching { getCallerIdentity() }
            .map({ response -> response.userId() ?: "" })
            .map({ userId -> userId.isNotBlank() })
            .getOrDefault(false)
}
