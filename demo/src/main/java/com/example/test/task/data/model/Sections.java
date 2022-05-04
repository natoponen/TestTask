package com.example.test.task.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sections")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(name = "geological class",
                    joinColumns = @JoinColumn(name="id"))
    private List<GeologicalClass> geologicalClasses;

    @Override
    public String toString() {
        return "Section [id="+getId()+", name="+getName()+
                ", geological classes="+getGeologicalClasses()+"]";
    }

    public void addGeoClass(String name, String code) {
        GeologicalClass geologicalClassToAdd = new GeologicalClass();
        geologicalClassToAdd.setName(name);
        geologicalClassToAdd.setCode(code);
        this.geologicalClasses.add(geologicalClassToAdd);
    }
}
