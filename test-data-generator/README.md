## Hzt Test data generator

This is a utility library for testing. It can be used to generate test data

Author: Hans Zuidervaart

## Usage
Add the following dependency to your project:
````xml
    <dependency>
        <groupId>org.hzt.utils</groupId>
        <artifactId>test-data-generator</artifactId>
        <version>${hzt.utils.version}</version>
    </dependency>
````

See the tests in this project to see how the swing utilities can be used

## Examples
````java
List<Painting> paintings = TestSampleGenerator.createPaintingList();
````
