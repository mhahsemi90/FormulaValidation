package org.hcm.pcn.formula_validator.service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.FormulaDto;
import org.hcm.pcn.formula_validator.dto.LocalVariableDto;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.hcm.pcn.formula_validator.repository.entity.ChildBlock;
import org.hcm.pcn.formula_validator.repository.entity.Formula;
import org.hcm.pcn.formula_validator.repository.entity.LocalVariable;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.hcm.pcn.formula_validator.repository.service.ProductRepository;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-23T08:58:47+0330",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class BlockMapperImpl implements BlockMapper {

    @Override
    public BlockDto blockToBlockDtoMapper(Block block) {
        if ( block == null ) {
            return null;
        }

        BlockDto blockDto = new BlockDto();

        blockDto.setProductCode( blockProductCode( block ) );
        blockDto.setId( block.getId() );
        blockDto.setCode( block.getCode() );
        blockDto.setTitle( block.getTitle() );
        blockDto.setEnTitle( block.getEnTitle() );
        blockDto.setType( block.getType() );
        blockDto.setResult( blockToBlockDto( block.getResult() ) );
        blockDto.setFormula( formulaToFormulaDto( block.getFormula() ) );
        blockDto.setBlockList( childBlockListToBlockDtoList( block.getBlockList() ) );

        return blockDto;
    }

    @Override
    public List<BlockDto> blockListToBlockDtoListMapper(List<Block> blockList) {
        if ( blockList == null ) {
            return null;
        }

        List<BlockDto> list = new ArrayList<BlockDto>( blockList.size() );
        for ( Block block : blockList ) {
            list.add( blockToBlockDtoMapper( block ) );
        }

        return list;
    }

    @Override
    public Block blockDtoToBlockMapper(BlockDto blockDto, ProductRepository productRepository) {
        if ( blockDto == null ) {
            return null;
        }

        Block block = new Block();

        block.setId( blockDto.getId() );
        block.setCode( blockDto.getCode() );
        block.setTitle( blockDto.getTitle() );
        block.setEnTitle( blockDto.getEnTitle() );
        block.setType( blockDto.getType() );
        block.setResult( blockDtoToBlock( blockDto.getResult(), productRepository ) );
        block.setFormula( formulaDtoToFormula( blockDto.getFormula(), productRepository ) );
        block.setBlockList( blockDtoListToChildBlockList( blockDto.getBlockList(), productRepository ) );

        productCodeToProductMap( blockDto, block, productRepository );

        return block;
    }

    @Override
    public List<Block> blockDtoListToBlockListMapper(List<BlockDto> blockDtoList, ProductRepository productRepository) {
        if ( blockDtoList == null ) {
            return null;
        }

        List<Block> list = new ArrayList<Block>( blockDtoList.size() );
        for ( BlockDto blockDto : blockDtoList ) {
            list.add( blockDtoToBlockMapper( blockDto, productRepository ) );
        }

        return list;
    }

    private String blockProductCode(Block block) {
        if ( block == null ) {
            return null;
        }
        Product product = block.getProduct();
        if ( product == null ) {
            return null;
        }
        String code = product.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    protected LocalVariableDto localVariableToLocalVariableDto(LocalVariable localVariable) {
        if ( localVariable == null ) {
            return null;
        }

        LocalVariableDto localVariableDto = new LocalVariableDto();

        localVariableDto.setId( localVariable.getId() );
        localVariableDto.setCode( localVariable.getCode() );
        localVariableDto.setTitle( localVariable.getTitle() );
        localVariableDto.setEnTitle( localVariable.getEnTitle() );
        localVariableDto.setType( localVariable.getType() );
        localVariableDto.setResult( blockToBlockDto( localVariable.getResult() ) );

        return localVariableDto;
    }

    protected List<LocalVariableDto> localVariableListToLocalVariableDtoList(List<LocalVariable> list) {
        if ( list == null ) {
            return null;
        }

        List<LocalVariableDto> list1 = new ArrayList<LocalVariableDto>( list.size() );
        for ( LocalVariable localVariable : list ) {
            list1.add( localVariableToLocalVariableDto( localVariable ) );
        }

        return list1;
    }

    protected FormulaDto formulaToFormulaDto(Formula formula) {
        if ( formula == null ) {
            return null;
        }

        FormulaDto formulaDto = new FormulaDto();

        formulaDto.setId( formula.getId() );
        formulaDto.setFormula( formula.getFormula() );
        formulaDto.setVersion( formula.getVersion() );
        formulaDto.setCreatedAt( formula.getCreatedAt() );
        formulaDto.setLocalVariableList( localVariableListToLocalVariableDtoList( formula.getLocalVariableList() ) );

        return formulaDto;
    }

    protected BlockDto childBlockToBlockDto(ChildBlock childBlock) {
        if ( childBlock == null ) {
            return null;
        }

        BlockDto blockDto = new BlockDto();

        blockDto.setId( childBlock.getId() );

        return blockDto;
    }

    protected List<BlockDto> childBlockListToBlockDtoList(List<ChildBlock> list) {
        if ( list == null ) {
            return null;
        }

        List<BlockDto> list1 = new ArrayList<BlockDto>( list.size() );
        for ( ChildBlock childBlock : list ) {
            list1.add( childBlockToBlockDto( childBlock ) );
        }

        return list1;
    }

    protected BlockDto blockToBlockDto(Block block) {
        if ( block == null ) {
            return null;
        }

        BlockDto blockDto = new BlockDto();

        blockDto.setId( block.getId() );
        blockDto.setCode( block.getCode() );
        blockDto.setTitle( block.getTitle() );
        blockDto.setEnTitle( block.getEnTitle() );
        blockDto.setType( block.getType() );
        blockDto.setResult( blockToBlockDto( block.getResult() ) );
        blockDto.setFormula( formulaToFormulaDto( block.getFormula() ) );
        blockDto.setBlockList( childBlockListToBlockDtoList( block.getBlockList() ) );

        return blockDto;
    }

    protected LocalVariable localVariableDtoToLocalVariable(LocalVariableDto localVariableDto, ProductRepository productRepository) {
        if ( localVariableDto == null ) {
            return null;
        }

        LocalVariable localVariable = new LocalVariable();

        localVariable.setId( localVariableDto.getId() );
        localVariable.setCode( localVariableDto.getCode() );
        localVariable.setTitle( localVariableDto.getTitle() );
        localVariable.setEnTitle( localVariableDto.getEnTitle() );
        localVariable.setType( localVariableDto.getType() );
        localVariable.setResult( blockDtoToBlock( localVariableDto.getResult(), productRepository ) );

        return localVariable;
    }

    protected List<LocalVariable> localVariableDtoListToLocalVariableList(List<LocalVariableDto> list, ProductRepository productRepository) {
        if ( list == null ) {
            return null;
        }

        List<LocalVariable> list1 = new ArrayList<LocalVariable>( list.size() );
        for ( LocalVariableDto localVariableDto : list ) {
            list1.add( localVariableDtoToLocalVariable( localVariableDto, productRepository ) );
        }

        return list1;
    }

    protected Formula formulaDtoToFormula(FormulaDto formulaDto, ProductRepository productRepository) {
        if ( formulaDto == null ) {
            return null;
        }

        Formula formula = new Formula();

        formula.setId( formulaDto.getId() );
        formula.setFormula( formulaDto.getFormula() );
        formula.setLocalVariableList( localVariableDtoListToLocalVariableList( formulaDto.getLocalVariableList(), productRepository ) );
        formula.setVersion( formulaDto.getVersion() );
        formula.setCreatedAt( formulaDto.getCreatedAt() );

        return formula;
    }

    protected ChildBlock blockDtoToChildBlock(BlockDto blockDto, ProductRepository productRepository) {
        if ( blockDto == null ) {
            return null;
        }

        ChildBlock childBlock = new ChildBlock();

        childBlock.setId( blockDto.getId() );

        return childBlock;
    }

    protected List<ChildBlock> blockDtoListToChildBlockList(List<BlockDto> list, ProductRepository productRepository) {
        if ( list == null ) {
            return null;
        }

        List<ChildBlock> list1 = new ArrayList<ChildBlock>( list.size() );
        for ( BlockDto blockDto : list ) {
            list1.add( blockDtoToChildBlock( blockDto, productRepository ) );
        }

        return list1;
    }

    protected Block blockDtoToBlock(BlockDto blockDto, ProductRepository productRepository) {
        if ( blockDto == null ) {
            return null;
        }

        Block block = new Block();

        block.setId( blockDto.getId() );
        block.setCode( blockDto.getCode() );
        block.setTitle( blockDto.getTitle() );
        block.setEnTitle( blockDto.getEnTitle() );
        block.setType( blockDto.getType() );
        block.setResult( blockDtoToBlock( blockDto.getResult(), productRepository ) );
        block.setFormula( formulaDtoToFormula( blockDto.getFormula(), productRepository ) );
        block.setBlockList( blockDtoListToChildBlockList( blockDto.getBlockList(), productRepository ) );

        productCodeToProductMap( blockDto, block, productRepository );

        return block;
    }
}
