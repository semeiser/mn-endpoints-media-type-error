package com.example

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.RxHttpClient
import io.micronaut.management.endpoint.annotation.Endpoint
import io.micronaut.management.endpoint.annotation.Read
import io.micronaut.management.endpoint.annotation.Selector
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ProducesEndpointSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    @Unroll
    void "content type should be set to text/plain via #desc"() {
        when:
        def response = client.exchange(HttpRequest.GET(url)).blockingFirst()

        then:
        response.code() == HttpStatus.OK.code
        response.contentType.get().name == MediaType.TEXT_PLAIN

        where:
        url             | desc
        "/produces"     | "produces annotation"
        "/produces/foo" | "read annotation parameter"
    }
}

@Endpoint(id = 'produces', defaultSensitive = false, defaultEnabled = true)
class ProducesEndpoint {

    String myValue = 'init'

    @Read
    @Produces(MediaType.TEXT_PLAIN)
    String value() {
        "test $myValue"
    }

    @Read(produces = MediaType.TEXT_PLAIN)
    String named(@Selector String name) {
        "test $name"
    }
}
