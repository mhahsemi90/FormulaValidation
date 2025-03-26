package org.hcm.pcn.formula_validator.service.implmentation;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.enums.BlockType;
import org.hcm.pcn.formula_validator.enums.ExpressionType;
import org.hcm.pcn.formula_validator.enums.LineType;
import org.hcm.pcn.formula_validator.enums.StatementType;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.expression.*;
import org.hcm.pcn.formula_validator.service.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ParsingScriptServiceImpl implements ParsingScriptService {
    private final StatementGenerator statementGenerator;
    private final Map<String, BlockDto> allOperatorMap;
    private final Map<String, BlockDto> allKeyword;
    //private final Map<String, BlockDto> allOperandMap;
    private final MessageSource messageSource;

    public ParsingScriptServiceImpl(
            StatementGenerator statementGenerator,
            @Qualifier("Operator") Map<String, BlockDto> allOperatorMap,
            @Qualifier("Keyword") Map<String, BlockDto> allKeyword,
            @Qualifier("MessageBundleSource") MessageSource messageSource) {
        this.statementGenerator = statementGenerator;
        this.allOperatorMap = allOperatorMap;
        this.allKeyword = allKeyword;
        this.messageSource = messageSource;
        //this.allOperandMap = new LinkedHashMap<>();
    }

    @Override
    public List<LineDto> generateLineOfBlocksListFromStatementList(List<Statement> statementList, Map<String, BlockDto> allOperandMap) {
        List<LineDto> lineDtoOfBlocksList = new ArrayList<>();
        Integer[] row = {0};
        Integer[] lineLevel = {0};
        Integer[] id = {0};
        Stack<Integer> parentIdStack = new Stack<>();
        statementList.forEach(statement -> {
            addStatementToLineOfBlocksList(lineDtoOfBlocksList, statement, row, lineLevel, id, parentIdStack, allOperandMap);
            row[0]++;
        });
        return lineDtoOfBlocksList;
    }

    @Override
    public ValidationResult generateFormulaFromLineOfBlocksList(List<LineDto> lineDtoList, String lang) {
        ValidationResult validationResult = new ValidationResult();
        List<String> elementListFromLineList = generateElementListFromLineList(0, lineDtoList);
        List<String> generatedFormula = new ArrayList<>();
        StringBuilder testFormula = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (String s : elementListFromLineList) {
            line.append(s);
            testFormula.append(s);
            if (s.equals("\n")) {
                generatedFormula.add(line.toString());
                line = new StringBuilder();
            }
        }
        if (line.length() > 0)
            generatedFormula.add(line.toString());
        validationResult.setGeneratedFormula(generatedFormula);
        validationResult.setValidationMessage("OK");
        try {
            statementGenerator.parsingToListOfStatement(testFormula.toString(), false, lang);
        } catch (HandledError e) {
            validationResult.setValidationMessage(
                    messageSource.getMessage(
                            e.getMessage(),
                            e.getArgs(),
                            e.getLang() != null ? new Locale(e.getLang()) : LocaleContextHolder.getLocale()
                    )
            );
        }
        return validationResult;
    }

    @Override
    public ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList, Map<String, BlockDto> allOperandMap, String lang) {
        ReWritingResult result = new ReWritingResult();
        ValidationResult validationResult = generateFormulaFromLineOfBlocksList(lineDtoList, lang);
        if (validationResult.getValidationMessage().equals("OK")) {
            StringBuilder script = new StringBuilder();
            validationResult.getGeneratedFormula()
                    .forEach(script::append);
            result.setValidationMessage("OK");
            result.setReWritingLineList(generateLineOfBlocksListFromStatementList(
                    statementGenerator.parsingToListOfStatement(script.toString(), false, lang)
                    , allOperandMap
            ));

        } else {
            result.setReWritingLineList(lineDtoList);
            result.setValidationMessage(validationResult.getValidationMessage());
        }
        return result;
    }

    private List<String> generateElementListFromLineList(Integer lineLevel, List<LineDto> lineDtoList) {
        Stack<String> elementListFromLineList = new Stack<>();
        LineType innerLevel = CollectionUtils.isNotEmpty(lineDtoList) ? lineDtoList.get(0).getLineType() : null;
        while (CollectionUtils.isNotEmpty(lineDtoList)) {
            LineType type = lineDtoList.get(0).getLineType();
            if (lineDtoList.get(0).getLineLevel().equals(lineLevel)) {
                if (elementListFromLineList.size() > 0
                        && (type == LineType.ELSE ||
                        type == LineType.ELSE_IF)) {
                    elementListFromLineList.pop();
                    elementListFromLineList.push(" ");
                }
                elementListFromLineList.addAll(generateScriptLine(lineDtoList.remove(0)));
                if (type == LineType.EXPRESSION ||
                        type == LineType.RETURN ||
                        type == LineType.VARIABLE_DECLARATION)
                    elementListFromLineList.push("\n");
            } else {
                Integer newLineLevel = lineLevel + 1;
                if (innerLevel == LineType.EXPRESSION ||
                        type == LineType.RETURN ||
                        type == LineType.VARIABLE_DECLARATION) {
                    for (int j = 0; j < lineLevel; j++) {
                        elementListFromLineList.push("\t");
                    }
                }
                elementListFromLineList.push(" {");
                elementListFromLineList.push("\n");
                elementListFromLineList.addAll(generateElementListFromLineList(newLineLevel, getSameLevel(lineDtoList, lineLevel)));
                for (int j = 0; j < lineLevel; j++) {
                    elementListFromLineList.push("\t");
                }
                elementListFromLineList.push("}");
                elementListFromLineList.push("\n");
            }
        }
        return elementListFromLineList;
    }

    private List<LineDto> getSameLevel(List<LineDto> lineDtoList, Integer lineLevel) {
        List<LineDto> newLineOfBlockDtos = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(lineDtoList) && lineDtoList.get(0).getLineLevel() > lineLevel) {
            newLineOfBlockDtos.add(lineDtoList.remove(0));
        }
        return newLineOfBlockDtos;
    }

    private List<String> generateScriptLine(LineDto lineDto) {
        Stack<String> finalElementList = new Stack<>();
        if (lineDto.getLineType() != LineType.ELSE &&
                lineDto.getLineType() != LineType.ELSE_IF) {
            for (int j = 0; j < lineDto.getLineLevel(); j++) {
                finalElementList.push("\t");
            }
        }
        if (lineDto.getLineType() == LineType.ELSE) {
            finalElementList.push("else");
        }
        if (lineDto.getLineType() == LineType.IF) {
            finalElementList.push("if(");
        }
        if (lineDto.getLineType() == LineType.ELSE_IF) {
            finalElementList.push("else if(");
        }
        if (lineDto.getLineType() == LineType.FOR) {
            finalElementList.push("for(");
        }
        if (lineDto.getLineType() == LineType.RETURN) {
            finalElementList.push("return ");
        }
        if (CollectionUtils.isNotEmpty(lineDto.getBlockList())) {
            lineDto.getBlockList().forEach(block -> {
                finalElementList.push(block.getCode());
                finalElementList.push(" ");
            });
            finalElementList.pop();
        }
        if (lineDto.getLineType() == LineType.IF ||
                lineDto.getLineType() == LineType.ELSE_IF ||
                lineDto.getLineType() == LineType.FOR) {
            finalElementList.push(")");
        }
        if (lineDto.getLineType() == LineType.EXPRESSION ||
                lineDto.getLineType() == LineType.RETURN ||
                lineDto.getLineType() == LineType.VARIABLE_DECLARATION)
            finalElementList.push(";");
        return finalElementList;
    }

    public void addStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            Statement statement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        switch (statement.getType()) {
            case EXPRESSION -> addExpressionStatementToLineOfBlocksList(
                    lineDtoOfBlocksList,
                    (ExpressionStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack,
                    allOperandMap
            );
            case BLOCK -> {
                lineLevel[0]++;
                addBlockStatementToLineOfBlocksList(
                        lineDtoOfBlocksList,
                        (BlockStatement) statement,
                        row,
                        lineLevel,
                        id,
                        parentIdStack,
                        allOperandMap
                );
                lineLevel[0]--;
                row[0]--;
            }
            case VARIABLE_DECLARATION -> addVariableDeclarationStatementToLineOfBlocksList(
                    lineDtoOfBlocksList,
                    (VariableDeclarationStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack,
                    allOperandMap
            );
            case IF -> addIfStatementToLineOfBlocksList(
                    lineDtoOfBlocksList,
                    (IfStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack,
                    allOperandMap
            );
            case LABEL -> addLabelStatementToLineOfBlocksList(
                    lineDtoOfBlocksList,
                    (LabeledStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack,
                    allOperandMap
            );
            case RETURN -> addReturnStatementToLineOfBlocksList(
                    lineDtoOfBlocksList,
                    (ReturnStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack,
                    allOperandMap
            );
        }

    }

    public void addExpressionToBlocks(List<BlockDto> blockDtoList, Expression expression, Map<String, BlockDto> allOperandMap) {
        switch (expression.getType()) {
            case VARIABLE_EXPRESSION:
                Variable variable = (Variable) expression;
                blockDtoList.add(BlockType.VARIABLE.getBlock(allOperandMap, variable.getIdName()));
                break;
            case LITERAL_EXPRESSION:
                Literal literal = (Literal) expression;
                blockDtoList.add(BlockType.LITERAL.getBlock(literal.getValue()));
                break;
            case UNARY_EXPRESSION:
                addUnaryExpressionToBlocks(blockDtoList, (OneHandOperatorExpression) expression, allOperandMap);
                break;
            case BINARY_EXPRESSION:
                addBinaryExpressionToBlocks(blockDtoList, expression);
                break;
            case ASSIGNMENT_EXPRESSION:
                addAssignmentExpressionToBlocks(blockDtoList, (TwoHandOperatorExpression) expression, allOperandMap);
                break;
            case VARIABLE_DECLARATOR_EXPRESSION:
                addVariableExpressionToBlocks(blockDtoList, (VariableDeclaratorExpression) expression, allOperandMap);
                break;
            case CALL_EXPRESSION:
            case ARRAY_EXPRESSION:
            case ARRAY_PATTERN_EXPRESSION:
            case CONDITIONAL_EXPRESSION:
            case UPDATE_EXPRESSION:
            case LOGICAL_EXPRESSION:
            case PARENTHESIS_EXPRESSION:
            case SEQUENCE_EXPRESSION:
            case MEMBER_EXPRESSION:
            case OBJECT_EXPRESSION:
            case PROPERTY_EXPRESSION:
                break;
        }
    }

    public void addExpressionStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            ExpressionStatement expressionStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        addExpressionToBlocks(blockDtoList, expressionStatement.getExpression(), allOperandMap);
        lineDtoOfBlocksList.add(
                new LineDto(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.EXPRESSION)
        );
    }

    public void addBlockStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            BlockStatement blockStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        blockStatement.getBodyList().forEach(statement -> {
            addStatementToLineOfBlocksList(lineDtoOfBlocksList, statement, row, lineLevel, id, parentIdStack, allOperandMap);
            row[0]++;
        });
    }

    public void addIfStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            IfStatement ifStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        LineType type = LineType.IF;
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (ifStatement.getTest() != null)
            addExpressionToBlocks(blockDtoList, ifStatement.getTest(), allOperandMap);
        if (CollectionUtils.isNotEmpty(lineDtoOfBlocksList) &&
                lineDtoOfBlocksList.get(lineDtoOfBlocksList.size() - 1).getLineType() == LineType.ELSE) {
            parentIdStack.push(parentIdStack.peek());
            lineDtoOfBlocksList.get(lineDtoOfBlocksList.size() - 1).setLineType(LineType.ELSE_IF);
            lineDtoOfBlocksList.get(lineDtoOfBlocksList.size() - 1).setBlockList(blockDtoList);
            row[0]--;
        } else {
            parentIdStack.push(id[0]);
            lineDtoOfBlocksList.add(
                    new LineDto(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, type)
            );
        }
        if (ifStatement.getConsequent() != null) {
            row[0]++;
            if (ifStatement.getConsequent().getType() == StatementType.BLOCK) {
                addStatementToLineOfBlocksList(lineDtoOfBlocksList, ifStatement.getConsequent(), row, lineLevel, id, parentIdStack, allOperandMap);
            } else {
                lineLevel[0]++;
                addStatementToLineOfBlocksList(lineDtoOfBlocksList, ifStatement.getConsequent(), row, lineLevel, id, parentIdStack, allOperandMap);
                lineLevel[0]--;
            }
        }
        if (ifStatement.getAlternate() != null) {
            row[0]++;
            List<BlockDto> alternateBlockListDto = new ArrayList<>();
            parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
            parentIdStack.push(id[0]);
            lineDtoOfBlocksList.add(
                    new LineDto(id[0]++, parentId, row[0], lineLevel[0], alternateBlockListDto, LineType.ELSE)
            );
            row[0]++;
            switch (ifStatement.getAlternate().getType()) {
                case BLOCK, IF ->
                        addStatementToLineOfBlocksList(lineDtoOfBlocksList, ifStatement.getAlternate(), row, lineLevel, id, parentIdStack, allOperandMap);
                default -> {
                    lineLevel[0]++;
                    addStatementToLineOfBlocksList(lineDtoOfBlocksList, ifStatement.getAlternate(), row, lineLevel, id, parentIdStack, allOperandMap);
                    lineLevel[0]--;
                }
            }
            parentIdStack.pop();
        }
        parentIdStack.pop();
    }

    public void addLabelStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            LabeledStatement labeledStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockDtoList.add(BlockType.LABEL.getBlock(allOperandMap, labeledStatement.getLabel()));
        blockDtoList.add(BlockType.LABEL_ASSIGN.getBlock(allOperatorMap, ":"));
        parentIdStack.push(id[0]);
        lineDtoOfBlocksList.add(
                new LineDto(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.LABEL)
        );
        row[0]++;
        if (labeledStatement.getBody().getType() == StatementType.BLOCK) {
            addStatementToLineOfBlocksList(lineDtoOfBlocksList, labeledStatement.getBody(), row, lineLevel, id, parentIdStack, allOperandMap);
        } else {
            lineLevel[0]++;
            addStatementToLineOfBlocksList(lineDtoOfBlocksList, labeledStatement.getBody(), row, lineLevel, id, parentIdStack, allOperandMap);
            lineLevel[0]--;
        }
        parentIdStack.pop();
    }

    public void addReturnStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            ReturnStatement returnStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (returnStatement.getArgument() != null)
            addExpressionToBlocks(blockDtoList, returnStatement.getArgument(), allOperandMap);
        lineDtoOfBlocksList.add(
                new LineDto(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.RETURN)
        );
    }

    public void addVariableDeclarationStatementToLineOfBlocksList(
            List<LineDto> lineDtoOfBlocksList,
            VariableDeclarationStatement variableDeclarationStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack,
            Map<String, BlockDto> allOperandMap) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockDtoList.add(BlockType.KEYWORD.getBlock(allKeyword, variableDeclarationStatement.getKind()));
        boolean firstVariable = true;
        for (Expression expression : variableDeclarationStatement.getDeclaratorExpressionList()) {
            if (!firstVariable) {
                blockDtoList.add(BlockType.SEPARATOR.getBlock(allOperatorMap, ","));
            }
            addExpressionToBlocks(blockDtoList, expression, allOperandMap);
            firstVariable = false;
        }
        lineDtoOfBlocksList.add(
                new LineDto(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.VARIABLE_DECLARATION)
        );
    }

    public void addAssignmentExpressionToBlocks(List<BlockDto> blockDtoList, TwoHandOperatorExpression expression, Map<String, BlockDto> allOperandMap) {
        addExpressionToBlocks(blockDtoList, expression.getLeftChild(), allOperandMap);
        blockDtoList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockDtoList, expression.getRightChild(), allOperandMap);
    }

    public void callTravers(Expression expression, List<Expression> expressionList) {
        if (expression.getType() == ExpressionType.BINARY_EXPRESSION) {
            TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) expression;
            if (operatorExpression.getLeftChild() != null)
                callTravers(operatorExpression.getLeftChild(), expressionList);
            if (operatorExpression.getRightChild() != null)
                callTravers(operatorExpression.getRightChild(), expressionList);
        }
        expressionList.add(expression);
    }

    public void addBinaryExpressionToBlocks(List<BlockDto> blockDtoList, Expression expression) {
        List<Expression> expressionList = new ArrayList<>();
        callTravers(expression, expressionList);
        Stack<List<BlockDto>> valueStack = new Stack<>();
        for (Expression expression1 : expressionList) {
            if (expression1.getType() == ExpressionType.BINARY_EXPRESSION) {
                TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) expression1;
                List<BlockDto> secondBlockListDto = valueStack.pop();
                List<BlockDto> firstBlockListDto = valueStack.pop();
                List<BlockDto> result = new ArrayList<>(firstBlockListDto);
                result.add(BlockType.ARITHMETIC_OPERATOR.getBlock(allOperatorMap, operatorExpression.getOperator()));
                result.addAll(secondBlockListDto);
                valueStack.push(result);
            } else {
                List<BlockDto> operandBlockListDto = new ArrayList<>();
                addExpressionToBlocks(operandBlockListDto, expression1, allOperatorMap);
                valueStack.push(operandBlockListDto);
            }
        }
        blockDtoList.addAll(valueStack.pop());
    }

    public void addUnaryExpressionToBlocks(List<BlockDto> blockDtoList, OneHandOperatorExpression expression, Map<String, BlockDto> allOperandMap) {
        blockDtoList.add(BlockType.ARITHMETIC_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockDtoList, expression.getArgument(), allOperandMap);
    }

    public void addVariableExpressionToBlocks(List<BlockDto> blockDtoList, VariableDeclaratorExpression expression, Map<String, BlockDto> allOperandMap) {
        String idName = ((Variable) expression.getVariable()).getIdName();
        blockDtoList.add(BlockType.VARIABLE.getBlock(allOperandMap, idName));
        if (expression.getInitiateValue() != null) {
            String value = ((Literal) expression.getInitiateValue()).getValue();
            blockDtoList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, "="));
            blockDtoList.add(BlockType.LITERAL.getBlock(value));
        }
    }

}
