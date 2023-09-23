package com.learnreactiveprogramming.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {
  public static void main(String[] args) {
    var nameList = List.of("Truong", "ben", "chloe", "chloe");
    var newNamesList = namesGreaterThanSize(nameList, 3);
    System.out.println(newNamesList);
  }

  private static List<String> namesGreaterThanSize(List<String> nameList, int size) {
    return nameList.stream()
        .filter(name -> name.length() > size)
        .distinct()
        .map(String::toUpperCase)
        .sorted()
        .collect(Collectors.toList());
  }
}
