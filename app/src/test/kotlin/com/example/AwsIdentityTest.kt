package com.example

import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse

class AwsIdentityTest {
  private lateinit var stsClient: StsClient

  @BeforeEach
  fun init() {
    this.stsClient = mockStsClient()
  }

  @Test
  fun shouldReturnTrueWhenUserIsLoggedIn() {
    val awsIdentity = AwsIdentity(stsClient)
    awsIdentity.isLoggedIn() shouldBe true
  }

  private fun mockStsClient(): StsClient {
    val stsClient = mockk<StsClient>()
    val mockResponse =
        GetCallerIdentityResponse.builder()
            .account(randomString())
            .arn(randomString())
            .userId(randomString())
            .build()
    every { stsClient.getCallerIdentity(any<GetCallerIdentityRequest>()) }.returns(mockResponse)
    return stsClient
  }
}
