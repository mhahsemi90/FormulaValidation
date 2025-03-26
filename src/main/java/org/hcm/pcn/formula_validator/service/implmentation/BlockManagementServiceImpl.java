package org.hcm.pcn.formula_validator.service.implmentation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.enums.BlockType;
import org.hcm.pcn.formula_validator.enums.ExceptionMessage;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.hcm.pcn.formula_validator.repository.entity.ChildBlock;
import org.hcm.pcn.formula_validator.repository.service.BlockRepository;
import org.hcm.pcn.formula_validator.repository.service.ChildBlockRepository;
import org.hcm.pcn.formula_validator.repository.service.ProductRepository;
import org.hcm.pcn.formula_validator.service.interfaces.BlockManagementService;
import org.hcm.pcn.formula_validator.service.mapper.BlockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BlockManagementServiceImpl implements BlockManagementService {
    private final ProductRepository productRepository;
    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final ChildBlockRepository childBlockRepository;

    public BlockManagementServiceImpl(
            ProductRepository productRepository,
            BlockRepository blockRepository,
            BlockMapper blockMapper,
            ChildBlockRepository childBlockRepository
    ) {
        this.productRepository = productRepository;
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
        this.childBlockRepository = childBlockRepository;
    }

    @Override
    @Transactional
    public String findGroupCodeBlock(String code) {
        List<String> blockList = blockRepository.findGroupBlockList(code);
        if (CollectionUtils.isNotEmpty(blockList))
            return blockList.get(0);
        return null;
    }

    @Override
    public List<BlockDto> getAllBlockByProductCode(String productCode, String lang) {
        return blockMapper
                .blockListToBlockDtoListMapper(
                        blockRepository
                                .findAllByProductCode(productCode)
                        , this
                );
    }

    @Override
    public Map<String, BlockDto> getAllOperandMapByProductCode(String productCode) {
        return null;
    }

    @Override
    public List<BlockDto> getAllBlockByProductCodeAndType(String productCode, BlockType blockType, String lang) {
        return blockMapper
                .blockListToBlockDtoListMapper(
                        blockRepository
                                .findAllByProductCodeAndType(productCode, blockType)
                        , this
                );
    }

    @Override
    @Transactional
    public Boolean addBlock(BlockDto blockDto, String lang) {
        if (blockRepository.existsByCode(blockDto.getCode()))
            ExceptionMessage.CODE_IS_DUPLICATE.throwException(lang);
        Block block = new Block(
                blockDto.getCode(),
                blockDto.getTitle(),
                blockDto.getEnTitle(),
                blockDto.getType(),
                productRepository
                        .streamByCode(blockDto.getProductCode())
                        .findFirst()
                        .orElseThrow(() ->
                                ExceptionMessage.PRODUCT_CODE_NOT_EXIST
                                        .getExceptionWithParam(lang, blockDto.getProductCode())
                        )
        );
        try {
            block = blockRepository.save(block);
        } catch (Exception e) {
            ExceptionMessage.PERSIST_BLOCK_HAS_ERROR.throwExceptionWithParam(lang, block, e.getMessage());
        }
        if (StringUtils.isNotBlank(blockDto.getGroup())) {
            Block groupDbBlock = blockRepository
                    .streamByCode(blockDto.getGroup())
                    .findFirst()
                    .orElse(null);
            if (groupDbBlock != null) {
                ChildBlock childBlock = new ChildBlock(groupDbBlock, block);
                try {
                    childBlockRepository.save(childBlock);
                } catch (Exception e) {
                    ExceptionMessage.ADD_BLOCK_TO_GROUP_HAS_ERROR.throwExceptionWithParam(lang, block, groupDbBlock, e.getMessage());
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean editBlock(BlockDto blockDto, String lang) {
        Block dbBlock = blockRepository
                .streamByCode(blockDto.getCode())
                .findFirst()
                .orElseThrow(() -> ExceptionMessage.CODE_IS_NOT_VALID.getException(lang));
        dbBlock.setTitle(blockDto.getTitle());
        dbBlock.setEnTitle(blockDto.getEnTitle());
        dbBlock.setType(blockDto.getType());
        try {
            blockRepository.save(dbBlock);
        } catch (Exception e) {
            ExceptionMessage.EDIT_BLOCK_HAS_ERROR.throwExceptionWithParam(lang, dbBlock, e.getMessage());
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean addBlockToList(String parentCode, List<String> childCode, String lang) {
        if (
                CollectionUtils.isNotEmpty(childCode)
                        && childCode.contains(parentCode)
        )
            ExceptionMessage.PARENT_BLOCK_AND_CHILD_BLOCK_IS_SAME.throwExceptionWithParam(lang, parentCode, childCode);
        Block dbParentBlock = blockRepository
                .streamByCode(parentCode)
                .findFirst()
                .orElseThrow(() -> ExceptionMessage.PARENT_CODE_IS_NOT_VALID.getException(lang));
        List<Block> dbChildCode = blockRepository
                .streamByCodeIn(childCode)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dbChildCode))
            ExceptionMessage.CHILD_CODE_IS_NOT_VALID.throwException(lang);
        List<ChildBlock> childBlockList = new ArrayList<>();
        dbChildCode.forEach(o -> childBlockList.add(new ChildBlock(dbParentBlock, o)));
        try {
            childBlockRepository.saveAll(childBlockList);
        } catch (Exception e) {
            ExceptionMessage.ADD_BLOCK_TO_HAS_ERROR.throwExceptionWithParam(lang, childCode, parentCode, e.getMessage());
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteBlockFromList(String parentCode, List<String> childCode, String lang) {
        AtomicInteger contDelete = new AtomicInteger(0);
        try {
            childCode.forEach(o ->
                    contDelete.getAndAdd(
                            childBlockRepository.deleteAllByParentBlock_CodeAndChildBlock_Code(parentCode, o)
                    )
            );
        } catch (Exception e) {
            ExceptionMessage.DELETE_BLOCK_FROM_HAS_ERROR.throwExceptionWithParam(lang, childCode, parentCode, e.getMessage());
        }
        return true;
    }
}
