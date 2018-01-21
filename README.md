# vertx-oas

Generates a Swagger/OpenAPI specification from a Vert.x Web Router.

## Usage

Assuming you're using Vert.x Web, use it with your `Router`:

```java
// your normal Vert.x Web Router
Router router = Router.router(vertx);

// add paths etc.
router.post("/users").handler( routingContext -> { /* etc... */ });
router.get("/users/:userId").handler( routingContext -> { /* etc... */ });

// publish the Swagger/OpenAPI specification
RouterSpecGenerator.publishApiDocs(router, "/api/spec");
```

In this example, the specification is published to `/api/spec`.

Example specification:

```yaml
openapi: "3.0.1"
info:
  title: "Vert.x APIs"
  description: "This specification was generated from a Vert.x Web Router."
paths:
  /users:
    post:
      parameters: []
  /users/{userId}:
    get:
      parameters:
      - name: "userId"
        required: true
        allowEmptyValue: false
```

## Limitations

- Doesn't understand regex paths
- Doesn't know the type of path parameters
- Doesn't know about request or response body models

## Use in your project

Add repository:

    repositories {
        // ...etc.
        
        maven {
            url 'https://s3-eu-west-1.amazonaws.com/gatehillsoftware-maven/snapshots'
        }
    }
    
Add dependency:

    compile "com.gatehill.vertx-oas:vertx-oas:$version_vertx_oas"  

## Build

### Prerequisites

- JDK8

### Compile and test

    ./gradlew clean build

### Publish

Publish to local Maven repository:

    ./gradlew publishToMavenLocal

Publish to remote Maven repository:

    ./gradlew publish
