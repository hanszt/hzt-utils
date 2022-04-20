## Hzt fx Utils

---

This is a utility library for working with javafx in a more functional way

Author: Hans Zuidervaart

---
## Usage
Add the following dependency to your project:
````xml
    <dependency>
        <groupId>org.hzt.utils</groupId>
        <artifactId>fx-utils</artifactId>
        <version>${hzt.utils.version}</version>
    </dependency>
````

See the tests in this project to see how the fx utilities can be used

---
## Examples
````java
        IntegerProperty value = new SimpleIntegerProperty(2);

        value.addListener(forNewValue(newValue -> assertEquals(4, newValue)));

        value.set(4);
````
