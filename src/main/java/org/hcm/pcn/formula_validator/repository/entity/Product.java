package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

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
            name = "productName",
            nullable = false
    )
    private String productName;
    @OneToMany(mappedBy = "product")
    private List<Block> blockList = new ArrayList<>();

    public Product(String productName) {
        this.productName = productName;
    }
}
