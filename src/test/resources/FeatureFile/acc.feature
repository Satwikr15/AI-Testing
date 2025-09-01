Feature: Automation of AI Application

  Scenario: Validation of DokGPT with all types of queries
    Given login process
    When navigate to Ai workbench
    And Search for DokGPT application and click on it
    And user sends general questions from file "src/test/resources/testdata/DokGPT_Questions.csv" and validates responses
    And user sends video-type questions from file "src/test/resources/testdata/DokGPT_Video_Questions.csv" and validates responses
    And user sends web search questions from file "src/test/resources/testdata/DokGPT_WebSearch_Questions.csv" and validates responses



  Scenario: Validation of Karl Application
    Given Navigate to url
    When Click on Add Connection
    And user conversation with Karl