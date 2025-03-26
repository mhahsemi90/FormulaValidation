package org.hcm.pcn.formula_validator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.enums.BlockType;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.interfaces.BlockManagementService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BlockManagementController {
    private final BlockManagementService blockManagementService;
    private final ObjectMapper objectMapper;

    public BlockManagementController(
            BlockManagementService blockManagementService,
            ObjectMapper objectMapper) {
        this.blockManagementService = blockManagementService;
        this.objectMapper = objectMapper;
    }

    @QueryMapping
    String getAllBlockForFormula(@Argument String productCode, @Argument BlockType blockType, @Argument String lang) {
        String result;
        try {
            if (blockType == null) {
                result = objectMapper.writeValueAsString(
                        blockManagementService.getAllBlockByProductCode(productCode, lang)
                );
            } else {
                result = objectMapper.writeValueAsString(
                        blockManagementService.getAllBlockByProductCodeAndType(productCode, blockType, lang)
                );
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @QueryMapping
    List<BlockDto> getAllBlockList(@Argument String productCode, @Argument BlockType blockType, @Argument String lang) {
        if (blockType == null)
            return blockManagementService.getAllBlockByProductCode(productCode, lang);
        return blockManagementService.getAllBlockByProductCodeAndType(productCode, blockType, lang);
    }

    @MutationMapping
    Boolean addBlock(@Argument BlockDto block, @Argument String lang) {
        return blockManagementService.addBlock(block, lang);
    }

    @MutationMapping
    Boolean editBlock(@Argument BlockDto block, @Argument String lang) {
        return blockManagementService.editBlock(block, lang);
    }

    @MutationMapping
    Boolean addBlockToList(@Argument String parentCode, @Argument List<String> childCode, @Argument String lang) {
        return blockManagementService.addBlockToList(parentCode, childCode, lang);
    }

    @MutationMapping
    Boolean deleteBlockFromList(@Argument String parentCode, @Argument List<String> childCode, @Argument String lang) {
        return blockManagementService.deleteBlockFromList(parentCode, childCode, lang);
    }
}
