package org.hcm.pcn.formula_validator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String code;
    private String title;
    private String enTitle;
    private List<BlockDto> blockList;
}
