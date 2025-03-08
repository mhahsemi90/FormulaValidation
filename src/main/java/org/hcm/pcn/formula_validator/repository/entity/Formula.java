package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "formula")
@Entity(name = "Formula")
@Audited()
@EntityListeners(AuditingEntityListener.class)
public class Formula {
    @Id
    @SequenceGenerator(
            name = "Formula_ID",
            sequenceName = "Formula_ID",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Formula_ID"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @Column(
            name = "formula",
            nullable = false
    )
    private String formula;
    @OneToOne
    @JoinColumn(
            name = "block_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "formula_to_block_id_fk")
    )
    @Audited(targetAuditMode = NOT_AUDITED)
    private Block block;
    @Version
    private Long version;
    @CreatedDate
    private Timestamp createdAt;
}
