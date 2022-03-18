## Hzt Swing Utils

This is a utility library for working with java swing in a more functional way

Author: Hans Zuidervaart

## Usage
Add the following dependency to your project:
````
    <dependency>
        <groupId>org.hzt.utils</groupId>
        <artifactId>swing-utils</artifactId>
        <version>${hzt.utils.version}</version>
    </dependency>
````

See the tests in this project to see how the swing utilities can be used

## Examples
````
AtomicBoolean isCalled = new AtomicBoolean(false);
        Window frame = new Frame();
        frame.addWindowListener((WindowActivatedListener) e -> isCalled.set(true));
````
