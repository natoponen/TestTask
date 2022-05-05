package com.example.test.task.data.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sections")
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(name = "GeologicalClass",
                    joinColumns = @JoinColumn(name="id"))
    private List<GeologicalClass> geologicalClasses;


    public void addGeoClass(String name, String code) {
        GeologicalClass geologicalClassToAdd = new GeologicalClass();
        geologicalClassToAdd.setName(name);
        geologicalClassToAdd.setCode(code);
        this.geologicalClasses.add(geologicalClassToAdd);
    }
}
