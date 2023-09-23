package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.List;

public class ImperativeExample {

    public static void main(String[] args){
        var nameList = List.of("Truong", "ben", "chloe");
        var newNamesList = namesGreaterThanSize(nameList, 3);
        System.out.println(newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> nameList, int size) {
        var newNamesList = new ArrayList<String>();
        for(String name: nameList){
            if(name.length() > 3){
                newNamesList.add(name);
            }
        }
        return newNamesList;
    }
}
