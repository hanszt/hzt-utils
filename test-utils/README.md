## Hzt Test data generator

---

This is a utility library for testing. It can be used to generate test data

Author: Hans Zuidervaart

## Usage

Add the following dependency to your project:

````xml

<dependency>
    <groupId>org.hzt.utils</groupId>
    <artifactId>test-utils</artifactId>
    <version>${hzt.utils.version}</version>
</dependency>
````

See the [tests](src/test/java/org/hzt/test) in this project to see how the swing utilities can be used

## Examples

````java
class Example {
    List<Painting> paintings = TestSampleGenerator.createPaintingList();
}
````
