package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "childBlock")
@Entity(name = "ChildBlock")
public class ChildBlock {
    @Id
    @SequenceGenerator(
            name = "ChildBlock_ID",
            sequenceName = "ChildBlock_ID",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ChildBlock_ID"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "parent_block_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "parent_block_to_block_id_fk")
    )
    private Block parentBlock;
    @ManyToOne
    @JoinColumn(
            name = "child_block_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "child_block_to_block_id_fk")
    )
    private Block childBlock;
}
