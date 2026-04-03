package com.example.steps

import com.example.AwsIdentity
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse

class AwsAuthenticationSteps {
  private lateinit var accountNumber: String
  private lateinit var awsIdentity: AwsIdentity
  private lateinit var identityResponse: GetCallerIdentityResponse

  @Given("an AWS account with number {long}")
  fun anAwsAccountWithNumber(accountNumber: Long) {
    this.accountNumber = accountNumber.toString()
  }

  @Given("that AWS credentials are stored in the profile {string}")
  fun thatAwsCredentialsAreStoredInTheProfile(profileName: String) {
    val mockStsClient = mockk<StsClient>()
    val mockResponse =
        GetCallerIdentityResponse.builder()
            .account(accountNumber)
            .arn("arn:aws:iam::$accountNumber:user/$profileName")
            .userId("AIDEXAMPLE")
            .build()
    every { mockStsClient.getCallerIdentity(any<GetCallerIdentityRequest>()) } returns mockResponse
    awsIdentity = AwsIdentity(mockStsClient)
  }

  @When("the entity is asking for the caller identity")
  fun theEntityIsAskingForTheCallerIdentity() {
    identityResponse = awsIdentity.getCallerIdentity()
  }

  @Then("it received an answer indicating that it is logged in")
  fun itReceivedAnAnswerIndicatingThatItIsLoggedIn() {
    assertEquals(accountNumber, identityResponse.account())
  }
}
