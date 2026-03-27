package io.github.angelogalvao.example.ai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;

@Entity
public class Ride {
    @Id
    @GeneratedValue
    private Long id;

    public String name;
    public double rating;
}
