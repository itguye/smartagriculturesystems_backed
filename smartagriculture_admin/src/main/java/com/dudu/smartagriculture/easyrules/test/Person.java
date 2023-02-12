package com.dudu.smartagriculture.easyrules.test;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Person {

    public Person(String name,Integer age){
        this.name = name;
        this.age = age;
    }

    private boolean adult;

    private String name;

    private Integer age;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
