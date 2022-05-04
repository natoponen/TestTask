package com.example.test.task.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Table(name = "geological class")
public class GeologicalClass {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Override
    public String toString() {
        return "Geological class [name="+getName()+
                ", code="+getCode()+"]";
    }
}
