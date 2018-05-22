package com.voovoo.antlr

import spock.lang.Narrative
import spock.lang.Specification

@Narrative('''We would like our tool to be able to generate POJO classes from array fields of the json file.''')
class JSONArraysSupport extends Specification {


    def setup() {

    }

    def "test utility with JSON array in object"() {

        def jsonFile = "sample_with_array.json"

        given: "${jsonFile} json file is available for processing"

        Resource.resource {
            json file called "sample_with_array.json" exists_in resources directory
        }

        when: "we run the generator tool"
        def jsonPath = Resource.resource {
            json file path "sample_with_array.json"
        }

        GsonGenerator.main(["-c", "TestClass","-p", "test.package", "-f", jsonPath, "-d", Resource.resource {
            json file path ""
        }] as String[])

        then: "TestClass.java file exists in test/package directory"
        def filePath = new File("target/test-classes/test/package/TestClass.java")
        filePath.exists()

    }

    def jsonfile(String s) {

    }
}
