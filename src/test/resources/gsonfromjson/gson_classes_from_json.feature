Feature: A user wants to swiftly generate
	a bunch of POJO classes that can be both serialized
	and deserialized using gson framework.
	
Scenario: As a lazy developer I want to create a set of POJO classes
	with determined package name and root class name from a JSON file.

	Given I have a "sample2.json" json file
	And I want the root class to be called "MainClass"
	And I expect that this class will be in "my.project" package
	Then I generate POJO classess
	And The "MainClass" was generated
	And This class is in expected package
