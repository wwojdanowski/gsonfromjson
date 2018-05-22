Feature: A text file with class code can be generated from a json input.

  Scenario:
    Given I have a "car.json" json file
    And I want the root class to be called "Car"
    And I expect that this class will be in "euro.vehicles" package
    Then I execute the tool
    Then The "Car.java" java source file was generated in "euro/vehicles" directory