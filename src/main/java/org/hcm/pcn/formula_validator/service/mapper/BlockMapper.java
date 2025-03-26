package org.hcm.pcn.formula_validator.service.mapper;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.hcm.pcn.formula_validator.repository.entity.ChildBlock;
import org.hcm.pcn.formula_validator.repository.service.ProductRepository;
import org.hcm.pcn.formula_validator.service.interfaces.BlockManagementService;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "blockList", source = "blockList", qualifiedByName = "creatBlockListFromChildBlockList")
    @Mapping(target = "group", ignore = true)
    @Named("blockToBlockDtoMapper")
    BlockDto blockToBlockDtoMapper(Block block, @Context BlockManagementService blockManagementService);

    @Named("creatBlockListFromChildBlockList")
    default List<BlockDto> creatBlockListFromChildBlockList(List<ChildBlock> childBlockList, @Context BlockManagementService blockManagementService) {
        if (childBlockList == null)
            return null;
        return childBlockList
                .stream()
                .map(ChildBlock::getChildBlock)
                .map(o -> blockToBlockDtoMapper(o, blockManagementService))
                .collect(Collectors.toList());
    }


    @IterableMapping(qualifiedByName = "blockToBlockDtoMapper")
    List<BlockDto> blockListToBlockDtoListMapper(List<Block> blockList, @Context BlockManagementService blockManagementService);

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

    @AfterMapping
    default void getGroupCode(
            Block block,
            @MappingTarget BlockDto blockDto,
            @Context BlockManagementService blockManagementService) {
        if (block != null && block.getCode() != null)
            blockDto.setGroup(
                    blockManagementService.findGroupCodeBlock(block.getCode())
            );
    }
}
