package com.voovoo.antlr

class Resource {

    def resources = { resource ->

        if (Thread.currentThread().contextClassLoader.getResource(resource)?.path != null) {
            true
        } else {
            false
        }
    }

    def file = { name ->
        name
    }

    def json(file) {
        [called: { nameOfFile ->
            [exists_in: { exists ->
                    def found = exists(file(nameOfFile))
                    [directory: found]
                }
            ]
        },
        path: { nameOfFile ->
            Thread.currentThread().contextClassLoader.getResource(nameOfFile)?.path
        }]
    }

    //json(file).path("file.json)
    //json(file).called("file.json").exists_in(resources).directory()

    def static resource(closure) {
        def resource = new Resource()

        closure.delegate = resource

        closure()
    }

}

