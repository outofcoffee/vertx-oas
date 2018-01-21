# vertx-oas

Generates a Swagger/OpenAPI specification from a Vert.x Web Router.

## Usage

Assuming you're using Vert.x Web, use it with your `Router` as follows:

```java
// your normal Vert.x Web Router with paths etc.
Router router = Router.router(vertx);
router.post("/users").handler( routingContext -> { /* etc... */ });
router.get("/users/:userId").handler( routingContext -> { /* etc... */ });

// publish the Swagger/OpenAPI specification to a URL
RouterSpecGenerator.publishApiDocs(router, "/api/spec");
```

In this example, the specification is published to `/api/spec`.

You can obtain YAML or JSON versions of the specification by adding the appropriate file extension.

---

For example, fetching `/api/spec.yaml` would produce:

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

### Maven

Add repository:

```xml
<project>
...
  <repositories>
    <repository>
      <id>gatehillsoftware-snapshots</id>
      <name>Gatehill Software Snapshots</name>
      <url>https://s3-eu-west-1.amazonaws.com/gatehillsoftware-maven/snapshots</url>
    </repository>
    <repository>
      <id>sonatype-snapshots</id>
      <name>Sonatype OSS Snapshots</name>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
  </repositories>
...
</project>
```
    
Add dependency:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.gatehill.vertx-oas</groupId>
        <artifactId>vertx-oas</artifactId>
        <version>1.0.1</version>
    </dependency>
    ...
</dependencies>
```

### Gradle

Add repository:

    repositories {
        maven {
            url 'https://s3-eu-west-1.amazonaws.com/gatehillsoftware-maven/snapshots'
        }
        maven {
            url 'https://oss.sonatype.org/content/repositories/snapshots'
        }
    }
    
Add dependency:

    compile 'com.gatehill.vertx-oas:vertx-oas:1.0.1'  

## Build

If you'd like to build the code locally, follow these instructions.

### Prerequisites

- JDK8

### Compile and test

    ./gradlew clean build

### Publish

Publish to local Maven repository:

    ./gradlew publishToMavenLocal

Publish to remote Maven repository:

    ./gradlew publish

## Contributing

* Pull requests are welcome.
* Please run `ktlint` on your branch.

## Author

Pete Cornish (outofcoffee@gmail.com)
