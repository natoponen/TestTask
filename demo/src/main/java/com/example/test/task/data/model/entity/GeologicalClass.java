package com.example.test.task.data.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(name = "geological_classes")
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
