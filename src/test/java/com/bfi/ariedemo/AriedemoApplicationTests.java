package com.bfi.ariedemo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
class AriedemoApplicationTests {

  @FunctionalInterface
  interface PrintName {
    String call(String name);

  }

  @Test
  void oldWay() {
    PrintName oldWay = new PrintName() {
      @Override
      public String call(String name) {
        return "Hello, " + name;
      }
    };
    System.out.println(oldWay.call("Old Way Function"));
  }

  @Test
  void lambdaPrinting() {
    PrintName lambda = (String name) -> {
      return "Hello, " + name;
    };
    System.out.println(lambda.call("lambda1"));

    lambda = (name) -> {
      return "Hello, " + name;
    };
    System.out.println(lambda.call("lambda2"));

    lambda = name -> {
      return "Hello, " + name;
    };
    System.out.println(lambda.call("lambda3"));

    lambda = name -> "Hello, " + name;
    System.out.println(lambda.call("lambda4"));

  }

  @FunctionalInterface
  interface PrintName2 {
    String call(String firstName, String lastName);
  }

  @Test
  void lambdaPrinting2() {
    PrintName2 lam = (firstName, lastName) -> "Hello, " + firstName + " " + lastName;
    System.out.println("lam: " + lam.call("Arie", "Pamungkas"));
  }

  @FunctionalInterface
  interface Calculate {
    int apply(int first, int second);
  }

  @Test
  void calculateLambda() {
    Calculate add = (a, b) -> a + b;
    Calculate sub = (a, b) -> a - b;
    Calculate mul = (a, b) -> a * b;
    Calculate div = (a, b) -> a / b;

    System.out.println(add.apply(4, 5));
    System.out.println(sub.apply(5, 2));
    System.out.println(mul.apply(5, 2));
    System.out.println(div.apply(10, 2));

//    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
//    int sum = numbers.stream().reduce(0, (e,b) -> e + b);
//    System.out.println(sum);

//    int sum = numbers.stream().reduce(0, (e,b) -> add.apply(e, b));
//    System.out.println(sum);

//    int sum = numbers.stream().reduce(0, add::apply);
//    System.out.println(sum);
  }

  @Test
  void exampleOfLambda() {
    var integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    var size = integers.size();

    //simple code
    //external iterator
//    for(int i=0; i<size; i++){
//      System.out.print(integers.get(i));
//    }

    //external iterator 2
//    for(int i: integers){
//      System.out.print(i);
//    }

    //internal iterator
//    integers.forEach((Integer value) -> System.out.print(value));

    //internal iterator 2
//    integers.forEach( value -> System.out.print(value));

    //method reference
//    integers.forEach(System.out::print);
  }

// ***************  Java Streams  ******************


  @Test
  void exampleOfStreamsPipelining() {
    //imperative style
    List<String> names = Arrays.asList("John", "Jane", "Jim", "Jimmy", "Sumanto", "George");
    //get only length more than 5
    //make it to lowercase
    //print it
    List<String> results = new ArrayList<>();
    for(String name : names){
      if(name.length()>5){
        results.add(name.toLowerCase());
      }
    }
    System.out.println(results);

//    names.stream()
//      .filter(n -> n.length() > 5)
//      .map(String::toLowerCase)
//      .sorted()
//      .forEach(System.out::println);
  }

  @Test
  void streamLazy(){
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    var result = numbers.stream()
      .filter(n -> n > 5)
      .filter(n -> n < 10)
      .map(n-> {
          System.out.println("this being called");
          return n * 10;
        }
      );
  }

  @Test
  void parallelStream(){
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    numbers.stream().forEach(System.out::print);
    System.out.println();
    numbers.parallelStream().forEach(System.out::print);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @ToString
  public class Cendol {
    String name;
    Integer rating;

    public Cendol setName(String name) {
      this.name = name;
      return this;
    }

    public Cendol withName(String name){
      return new Cendol(name, this.rating);
    }
  }

  @Test
  void mutableStream(){
    List<Cendol> cendols = List.of(new Cendol("Pak Jo", 4), new Cendol("Bu Jo", 3));

    List<Cendol> cendolNew = cendols.stream()
      .map(cendol -> cendol.withName("Semua sama"))
      .collect(Collectors.toList());

    cendolNew.forEach(System.out::println);
    System.out.println();
    cendols.forEach(System.out::println);
  }

  @Test
  void exampleOfStreams() {
    var numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    int result = 0;
    int factor = 5;
    for (var value : numbers) {
      if (value > 5 && value % 2 == 0) {
        result = value * factor;
        break;
      }
    }
    factor = 8;
    System.out.println(result);

//    result = numbers.stream()
//      .filter(value -> value > 5)
//      .filter(value -> value % 2 == 0)
//      .map(value -> value * factor)
//      .findFirst()
//      .orElse(0);
////    factor = 7 ;
//    System.out.println(result);

//    modify to method reference
  }


  @Test
  void exampleOfStreamsReduce() {
    var integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    int result = 0;
    for (var value : integers) {
      if (value % 2 == 0) {
        result += value;
      }
    }
    System.out.println(result);

//    System.out.println(integers.stream().filter(e->e%2==0).reduce(0, Integer::sum));
  }


  @Test
  void doNotDoThis() {
    var integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    var doubleEven = new ArrayList<Integer>();

    integers.stream()
      .filter(e -> e % 2 == 0)
      .forEach(e -> doubleEven.add(e));
    System.out.println(doubleEven);

//    best way
//    var doubleEven2 = integers.stream()
//      .filter(e->e%2==0)
//      .collect(Collectors.toList());
//    System.out.println(doubleEven2);
  }
}
