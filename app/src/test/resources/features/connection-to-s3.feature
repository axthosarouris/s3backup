Feature: Upload single file to S3
  This feature describes the process of uploading a file in AWS S3

  Scenario: Entity has Profile authentication
    Given an AWS account with number 123456789012
    And that AWS credentials are stored in the profile "personal"
    When the entity is asking for the caller identity
    Then it received an answer indicating that it is logged in
