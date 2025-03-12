![logo_90.png](documentation/logo_90.png)

Matriarch is a library to help your testing process. It generates objects with random values in a easy way,
bringing you the chance to force values to be placed in defined fields.




### Set up
Add the dependency to your project
#### Maven

```xml

<dependency>
    <groupId>dev.agiro</groupId>
    <artifactId>matriarch</artifactId>
    <version>0.1.0</version>
    <scope>test</scope>
</dependency>
```
#### Gradle 
```kotlin
    testImplementation("dev.agiro:matriarch:0.1.0")
```

### Usage
The matriarch objetMother generator has three ways to use it. 
Most useful is as [builder](README.md#as-builder), but also could be used as a [field instance](README.md#field-instance-used-as-factory) or as a [Junit ParametrizedTest](README.md#as-annotation-for-junit-parametrized-test-) to inject parameters to your test

### As Builder.

This way you can use the `Mother` as a static method. It receives a Map<String, Overrider> with the coordinates and values that you want to
override.

If you only want a random object of `SomeType`, it is as simple as:

```java
var randomSomeType = Mother.forClass(SomeType.class).build;
```
If you need to define some specific fields for your use case, could be defines as follow:

```java
var randomSomeType = Mother.forClass(SomeType.class)
        .override("sStringValue", "my String Value")
        .override("otherStringValue", "A-\\d{4}(-V\\d+)?", OverriderType.REGEX)
        .override("anObjectValue", new OtherObject())
        .build;
```

There is several ways to define your overrides, a more detailed options are listed below:

```java
    import java.time.Instant;

var claimCategorizedEvent = Mother.builder(PurchaseAggregate.class)
        /* 1.i */.override("aggregate.user.valid", Overrider.with(techWarrantyFlag.toString()))
        /* 1.ii */.override("aggregate.user.role", "STANDARD")
        /* 1.iii */.override("aggregate.user.address", Overrider.with("C/Mississippi 56, 8B"))
        /* 2.i */.override("aggregate.user.objectId", Overrider.regex("A\\d{10}"))
        /* 2.ii */.override("aggregate.user.objectType", "\\d{4}", OverriderType.REGEX)
        /* 3.i */.override("aggregate.user.objectSubType", Overrider.object(new PartEs2("1234")))
        /* 3.ii */.override("aggregate.purchase.date", Instant.now())
        /* 4.i */.override("aggregate.purchase.claimDate", Overrider.nullValue())
        /* 4.ii */.override("aggregate.purchase.voucher", null)
        /* 5.i */.override("aggregate.list[3].date", Instant.now())
        /* 5.ii */.override("aggregate.map[thomas].date", Instant.now())
        .build();
 ```

there are two types of overrides:

1. Overrider.with(String): produces String or try to generate and object. it receives a value that you want to set. Could be used as:
    1. `.override("aggregate.user.valid", Overrider.with(techWarrantyFlag.toString()))`
    2. `.override("aggregate.user.role", "STANDARD")`
    3. `.override("aggregate.user.address", Overrider.with("C/Mississippi 56, 8B"))`
2. Overrider.regex(String): it receives a regex pattern that you want to set. Cold be used as:
   1. `.override("aggregate.user.objectId", Overrider.regex("A\\d{10}"))`
   2. `.override("aggregate.user.objectType", "\\d{4}", OverriderType.REGEX)`
3. Overrider.object(Object): it receives an object that you want to set. Cold be used as:
   1. `.override("aggregate.user.objectSubType", Overrider.object(new PartEs2("1234")))`
   2. `.override("aggregate.purchase.date", Instant.now())`
4. Overrider.nullValue(): it will set the field as null. Could be used as:
   1. `.override("aggregate.purchase.claimDate", Overrider.nullValue())`
   2. `.override("aggregate.purchase.voucher", null)`
5. You can Overrire a List element using `[<index>]` and Map value using `[<key>]`
   1. by default the list size is random between 1 and 15. If you specify an index bigger than that, the list will be filled as this index (randomly in cases where no override definition exists)
   2. Map objects are limited at the moment to a Map<Object, Object>, it will try to generate:
     - A **key** with the `<key>` to the object defined as key 
     - A **value** as defined.  !is not allowed to generate random values for Map or Collection (if you need it you have to pass them as a Overrider.object(<CollectionValue>))     

### As Annotation for Junit Parametrized Test 
Add a `@MotherFactoryResource` annotation to define the parameters

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(targetClass = CreatePurchaseCommand.class,
                jsonOverrides = "{\"version\": 3, \"partRequestList\": [{\"part\": \"PART1\"}, {\"part\": \"PART2\"}]}",
                overrides = {
                        @OverrideField(field = "actionId", value = "PF-[AZ]*{3}\\d{4}-V\\d", isRegexPattern = true),
                        @OverrideField(field = "claim.value", value = "CLA-[AZ]*{3}\\d{4}-V\\d", isRegexPattern = true)
                })})
void test(CreatePartDataCommand command) {
    //...
}
```

- args: the list of objects that you want to create
- @RandomArg: the object that you want to create
    - targetClass: the class that you want to create
    - jsonOverrides: a json containing the coordinates that you want to override in json format. It allows to override nested objects and Lists ( at
      the moment Maps are not implemented)
    - overrides: the list of fields that you want to override
        - field: the field that you want to override
            - value: the value that you want to set
            - isRegexPattern: if the value is a regex pattern

### Field Instance used as Factory.

this way is not recommended, but it is possible to use the `Mother` as a field. it receives a Map<String, Overrider> with the coordinates
and values that you want to override.

```java
    class PartDataCreatorTest extends UnitTestCase {

    Mother<User> flashDataMother = new Mother<>(User.class);

    @Test
    void test() {
        //Using a field declared objectMother to create the object
        final User user = flashDataMother.create(Map.of("name.value", Overrider.with("John"),
                                                                  "height.value", Overrider.with("193.5")));
    }
}
```

## Known pattern values

We could use custom coordinate/override values with a file `patterns.yaml` located in `src/test/resources` as this example:
it will put values by defaults to certain coordinates:
> at the moment could be used only with type "string" (default if missing),  "regex" and list

```yaml
patterns:
  - coordinate: user.sub
    value: USER_ID_\d{5}
    type: regex
  - coordinate: user.role
    value: ADMIN,STANDARD,VISITOR
    type: list
```
#### Other usages
1. As a Junit `@ParametrizedTest` source
2. As a instance


# Thanks to Opensource
This library is using some other opensource projects very thanks to :
 - https://www.datafaker.net
 - https://github.com/FasterXML/jackson
