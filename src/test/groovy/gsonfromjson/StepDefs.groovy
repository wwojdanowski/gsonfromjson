package gsonfromjson

import com.voovoo.antlr.GsonGenerator

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)


class Universe {

    def resourceLoader() {
        return Thread.currentThread().contextClassLoader
    }

    def loadFileAsStream(String file) {
        def withLoader = resourceLoader()
        def loadedFile = withLoader.getResourceAsStream file

        return loadedFile
    }

    def fileExists(String called) {

        def resource = resourceLoader().getResource called

        return !resource?.path.isEmpty()

    }

    def fullResourcePath(String resourceName) {

        def resource = resourceLoader().getResource resourceName

        return resource?.path
    }

    def rootClass
    def jsonPath
    def classes
    def classPackage
    def someClass
}

World {
    new Universe()
}

Before() {

}

Given(~/^I have a "([^"]*)" json file$/) { String json ->


    if (fileExists(json)) {
        jsonPath = fullResourcePath json
    } else {
        throw new Exception("File ${json} is not available!")
    }


}
And(~/^I want the root class to be called "([^"]*)"$/) { String rootClassName ->

    rootClass = rootClassName

}
Then(~/^I generate POJO classess$/) { ->

    def tool = new GsonGenerator()

    classes = tool.generateClasses(jsonPath, rootClass, classPackage)

}

And(~/^The "([^"]*)" was generated$/) { String className ->


    classes.each { element ->
        println element
    }

    someClass = classes.find { element ->
        element.getName() == className
    }

    assert someClass, "${className} was not created!"

}

And(~/^I expect that this class will be in "([^"]*)" package$/) { String expectedPackage ->
    classPackage = expectedPackage
}


And(~/^This class is in expected package$/) { ->
    assert someClass.getPackageName() == classPackage, "${someClass.getName()} class is not in ${classPackage} package!"
}


Then(~/^The "([^"]*)" java source file was generated in "([^"]*)" directory$/) { String file, String path ->

    def filePath = new File("target/${path}/${file}")

    assert filePath.exists(), "${file} was not created in ${path} directory!"
}

Then(~/^I execute the tool$/) { ->

    def jarFile = 'antlr-0.0.1-SNAPSHOT-jar-with-dependencies.jar'

    def command = "java -jar ${jarFile} -c ${rootClass} -p ${classPackage} -f ${jsonPath}".tokenize ' '

    def builder = new ProcessBuilder(command)

    builder.redirectErrorStream true
    builder.directory new File('target')

    def process = builder.start()

    def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))

    reader.eachLine { line ->
        println line
    }

    println "return code: ${builder.start().waitFor()}"

}