package org.hcm.pcn.formula_validator.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcm.pcn.formula_validator.enums.BlockType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Table(name = "block")
@Entity(name = "Block")
public class Block {
    @Id
    @SequenceGenerator(
            name = "Block_ID",
            sequenceName = "Block_ID",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Block_ID"
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
            foreignKey = @ForeignKey(name = "block_to_block_id_fk")
    )
    private Block result;
    @OneToMany(mappedBy = "result", cascade = CascadeType.PERSIST)
    private List<Block> parentResultList;
    @OneToMany(mappedBy = "parentBlock", cascade = CascadeType.PERSIST)
    private List<ChildBlock> blockList = new ArrayList<>();
    @OneToMany(mappedBy = "childBlock", cascade = CascadeType.PERSIST)
    private List<ChildBlock> childToParent;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "block_product_id_fk")
    )
    private Product product;
    @OneToOne(mappedBy = "block", cascade = CascadeType.PERSIST)
    private Formula formula;

    public Block(String code, String title, String enTitle, BlockType type) {
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
        this.type = type;
    }
}
