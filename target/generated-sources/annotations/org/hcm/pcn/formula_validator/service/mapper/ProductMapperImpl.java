package org.hcm.pcn.formula_validator.service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.FormulaDto;
import org.hcm.pcn.formula_validator.dto.LocalVariableDto;
import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.hcm.pcn.formula_validator.repository.entity.ChildBlock;
import org.hcm.pcn.formula_validator.repository.entity.Formula;
import org.hcm.pcn.formula_validator.repository.entity.LocalVariable;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-18T10:50:03+0330",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto productToProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setCode( product.getCode() );
        productDto.setTitle( product.getTitle() );
        productDto.setEnTitle( product.getEnTitle() );
        productDto.setBlockList( blockListToBlockDtoList( product.getBlockList() ) );

        return productDto;
    }

    @Override
    public ProductDto productToProductDtoWithoutBlockList(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setCode( product.getCode() );
        productDto.setTitle( product.getTitle() );
        productDto.setEnTitle( product.getEnTitle() );

        return productDto;
    }

    @Override
    public List<ProductDto> productListToProductDtoListWithoutBlockList(List<Product> product) {
        if ( product == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( product.size() );
        for ( Product product1 : product ) {
            list.add( productToProductDtoWithoutBlockList( product1 ) );
        }

        return list;
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDto.getId() );
        product.setCode( productDto.getCode() );
        product.setTitle( productDto.getTitle() );
        product.setEnTitle( productDto.getEnTitle() );
        product.setBlockList( blockDtoListToBlockList( productDto.getBlockList() ) );

        return product;
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

    protected List<BlockDto> blockListToBlockDtoList(List<Block> list) {
        if ( list == null ) {
            return null;
        }

        List<BlockDto> list1 = new ArrayList<BlockDto>( list.size() );
        for ( Block block : list ) {
            list1.add( blockToBlockDto( block ) );
        }

        return list1;
    }

    protected LocalVariable localVariableDtoToLocalVariable(LocalVariableDto localVariableDto) {
        if ( localVariableDto == null ) {
            return null;
        }

        LocalVariable localVariable = new LocalVariable();

        localVariable.setId( localVariableDto.getId() );
        localVariable.setCode( localVariableDto.getCode() );
        localVariable.setTitle( localVariableDto.getTitle() );
        localVariable.setEnTitle( localVariableDto.getEnTitle() );
        localVariable.setType( localVariableDto.getType() );
        localVariable.setResult( blockDtoToBlock( localVariableDto.getResult() ) );

        return localVariable;
    }

    protected List<LocalVariable> localVariableDtoListToLocalVariableList(List<LocalVariableDto> list) {
        if ( list == null ) {
            return null;
        }

        List<LocalVariable> list1 = new ArrayList<LocalVariable>( list.size() );
        for ( LocalVariableDto localVariableDto : list ) {
            list1.add( localVariableDtoToLocalVariable( localVariableDto ) );
        }

        return list1;
    }

    protected Formula formulaDtoToFormula(FormulaDto formulaDto) {
        if ( formulaDto == null ) {
            return null;
        }

        Formula formula = new Formula();

        formula.setId( formulaDto.getId() );
        formula.setFormula( formulaDto.getFormula() );
        formula.setLocalVariableList( localVariableDtoListToLocalVariableList( formulaDto.getLocalVariableList() ) );
        formula.setVersion( formulaDto.getVersion() );
        formula.setCreatedAt( formulaDto.getCreatedAt() );

        return formula;
    }

    protected ChildBlock blockDtoToChildBlock(BlockDto blockDto) {
        if ( blockDto == null ) {
            return null;
        }

        ChildBlock childBlock = new ChildBlock();

        childBlock.setId( blockDto.getId() );

        return childBlock;
    }

    protected List<ChildBlock> blockDtoListToChildBlockList(List<BlockDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ChildBlock> list1 = new ArrayList<ChildBlock>( list.size() );
        for ( BlockDto blockDto : list ) {
            list1.add( blockDtoToChildBlock( blockDto ) );
        }

        return list1;
    }

    protected Block blockDtoToBlock(BlockDto blockDto) {
        if ( blockDto == null ) {
            return null;
        }

        Block block = new Block();

        block.setId( blockDto.getId() );
        block.setCode( blockDto.getCode() );
        block.setTitle( blockDto.getTitle() );
        block.setEnTitle( blockDto.getEnTitle() );
        block.setType( blockDto.getType() );
        block.setResult( blockDtoToBlock( blockDto.getResult() ) );
        block.setFormula( formulaDtoToFormula( blockDto.getFormula() ) );
        block.setBlockList( blockDtoListToChildBlockList( blockDto.getBlockList() ) );

        return block;
    }

    protected List<Block> blockDtoListToBlockList(List<BlockDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Block> list1 = new ArrayList<Block>( list.size() );
        for ( BlockDto blockDto : list ) {
            list1.add( blockDtoToBlock( blockDto ) );
        }

        return list1;
    }
}
