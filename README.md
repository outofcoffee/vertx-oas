# vertx-oas

Generates a Swagger/OpenAPI specification from a Vert.x Web Router.

## Usage

Assuming you're using Vert.x Web, use it with your `Router`:

```$java
// your normal Vert.x Web Router
Router router = Router.router(vertx);

// add paths etc.
router.post("/users").handler( routingContext -> { /* etc... */ });
router.get("/users/:userId").handler( routingContext -> { /* etc... */ });

// publish the Swagger/OpenAPI specification
RouterSpecGenerator.publishApiDocs(router, "/api/_spec");
```

In this example, the specification is published to `/api/_spec`.

## Limitations

- Doesn't understand regex paths
- Doesn't know the type of path parameters
- Doesn't know about request or response body models

## Prerequisites

- JDK8

## Publish

Publish to local Maven repository:

    ./gradlew publishToMavenLocal

Publish to remote Maven repository:

    ./gradlew publish

## Use in projects

Add repository:

    repositories {
        // ...etc.
        
        maven {
            url 's3://gatehillsoftware-maven/snapshots'
            credentials(AwsCredentials) {
                accessKey System.getenv('AWS_ACCESS_KEY_ID') ?: project.findProperty('AWS_ACCESS_KEY_ID')
                secretKey System.getenv('AWS_SECRET_ACCESS_KEY') ?: project.findProperty('AWS_SECRET_ACCESS_KEY')
            }
        }
    }
    
> Note: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` can be set as environment variables, or your `gradle.properties` file.

Add dependency:

    compile "com.gatehill.vertx-oas:vertx-oas:$version_vertx_oas"  
