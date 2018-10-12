# Functional Util

A package of utility classes that offer several useful methods to functional programming in Java.

## NullSafeUtils Class 

This is a utility class, which provides methods to safety return value without throwing an exception (for example NullPointerException).

 **Typical use:**
 
`return get(()->object.getX().getY().getZ());`

instead of

`if (object!=null && object.getX()!=null && object.getX().getY()!=null) {`

`return object.getX().getY().getZ();`

`}`


**other useful methods**

`<S> S get(Supplier<S> supplier, S defaultValue)` - default value

`<S> S getIf(Supplier<S> supplier, Predicate<S> condition)` - extra condition

`<S> boolean isEqual(S expected, Supplier<S> supplier)` - expected value

**and several checking methods**

`<S> boolean isNull(Supplier<S> supplier)`

`boolean isTrue(Supplier<Boolean> supplier)`

`boolean isFalse(Supplier<Boolean> supplier)`

`boolean allNull(Supplier<Object>... suppliers)`

`boolean allNotNull(Supplier<Object>... suppliers)`

`boolean anyNull(Supplier<Object>... suppliers)`

`boolean anyNotNull(Supplier<Object>... suppliers)`

## Predicates Class 

Predicates is a utility class, which provides methods to operate on predicates

Logical operations: NOT, AND, OR, NAND, NOR, XAND, XOR
Extra operations: NONE(=NOR), ALL(=AND)

Multi arguments (more than two arguments) logical operations: AND, OR, NAND, NOR
Extra operations: NONE(=NOR), ALL(=AND)

**You can negate a predicate easily**:

`list.stream().filter(predicate(this::predicate1).negate())`

or better

`list.stream().filter(not(this::predicate1))`

instead of:

`I don't know how to negate a member reference in a filter.`

**You can operate on many member references**:

`list.stream().filter(and(this::predicate1, this::predicate2, this::predicate3))`

instead of:

`list.stream().filter(this::predicate1).filter(this::predicate2).filter(this::predicate3)`

**You can use other logical operations (and, or, nand, nor)**:

`list.stream().filter(or(this::predicate1, this::predicate2, this::predicate3))`

instead of:

`How to do it in Java 8?`

**Usage of XOR operation**:

`list.stream().filter(xor(this::predicate1, this::predicate2))`

instead of:

`How to do it in Java 8?`

## Maven

```xml
<dependency>
    <groupId>com.pa.util</groupId>
    <artifactId>functional-util</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Requirements

* JDK >= 1.8

### Releases

- 1.0.0
  - First version of functional-util

