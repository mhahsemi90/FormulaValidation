package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Table(name = "product")
@Entity(name = "Product")
public class Product {
    @Id
    @SequenceGenerator(
            name = "Product_ID",
            sequenceName = "Product_ID",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Product_ID"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @Column(
            name = "code",
            nullable = false,
            unique = true
    )
    private String code;
    @Column(
            name = "title",
            nullable = false
    )
    private String title;
    @Column(
            name = "enTitle",
            nullable = false
    )
    private String enTitle;

    public Product(String code, String title, String enTitle) {
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
    }
}
