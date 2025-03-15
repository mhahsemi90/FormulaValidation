package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcm.pcn.formula_validator.enums.BlockType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "local_variable")
@Entity(name = "LocalVariable")
public class LocalVariable {
    @Id
    @SequenceGenerator(
            name = "Local_Variable_ID",
            sequenceName = "Local_Variable_ID",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Local_Variable_ID"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @Column(
            name = "code",
            nullable = false
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
    @Enumerated(
            EnumType.STRING
    )
    private BlockType type;
    @ManyToOne
    @JoinColumn(
            name = "block_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "local_variable_to_block_id_fk")
    )
    private Block result;
    @ManyToOne
    @JoinColumn(
            name = "formula_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "local_variable_to_formula_id_fk")
    )
    private Formula formula;
}
