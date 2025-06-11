# features/login.feature
Feature: User Login Functionality

  As a user
  I want to log in to the application
  So that I can access my account

  @smoke
  Scenario: Login with valid standard user credentials
    Given verify user is on Login Page
    When user enters Valid username and password
    And click on Login Button
    Then user should see the Home page

  @smoke
  Scenario: Login with problem user credentials
    Given verify user is on Login Page
    When user enters Invalid username and password
    And click on Login Button
    Then user should see the error Message
