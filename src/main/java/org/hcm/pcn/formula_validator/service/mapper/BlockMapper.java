package org.hcm.pcn.formula_validator.service.mapper;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.hcm.pcn.formula_validator.repository.service.ProductRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    @Mapping(target = "productCode", source = "product.code")
    @Named("blockToBlockDtoMapper")
    BlockDto blockToBlockDtoMapper(Block block);

    @IterableMapping(qualifiedByName = "blockToBlockDtoMapper")
    List<BlockDto> blockListToBlockDtoListMapper(List<Block> blockList);

    @Mapping(target = "parentResultList", ignore = true)
    @Mapping(target = "parentResultLocalVariableList", ignore = true)
    @Mapping(target = "childToParent", ignore = true)
    @Named("blockDtoToBlockMapper")
    Block blockDtoToBlockMapper(BlockDto blockDto, @Context ProductRepository productRepository);

    @IterableMapping(qualifiedByName = "blockDtoToBlockMapper")
    List<Block> blockDtoListToBlockListMapper(List<BlockDto> blockDtoList, @Context ProductRepository productRepository);

    @AfterMapping
    default void productCodeToProductMap(
            BlockDto blockDto,
            @MappingTarget Block block,
            @Context ProductRepository productRepository) {
        if (blockDto != null) {
            String productCode = blockDto.getProductCode();
            if (productCode != null) {
                block.setProduct(
                        productRepository
                                .streamByCode(productCode)
                                .findFirst()
                                .orElse(null)
                );
            }
        }
    }
}
