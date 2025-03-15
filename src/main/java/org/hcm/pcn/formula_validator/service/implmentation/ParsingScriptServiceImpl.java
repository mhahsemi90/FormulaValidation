package org.hcm.pcn.formula_validator.service.implmentation;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.dto.*;
import org.hcm.pcn.formula_validator.enums.*;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.expression.*;
import org.hcm.pcn.formula_validator.service.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ParsingScriptServiceImpl implements ParsingScriptService {
    private final StatementGenerator statementGenerator;
    private final Map<String, BlockDto> allOperatorMap;
    private final Map<String, BlockDto> allKeyword;
    private final Map<String, BlockDto> allOperandMap;

    public ParsingScriptServiceImpl(
            StatementGenerator statementGenerator,
            @Qualifier("Operator") Map<String, BlockDto> allOperatorMap,
            @Qualifier("Keyword") Map<String, BlockDto> allKeyword) {
        this.statementGenerator = statementGenerator;
        this.allOperatorMap = allOperatorMap;
        this.allKeyword = allKeyword;
        this.allOperandMap = new LinkedHashMap<>();
    }

    @Override
    public List<Line> generateLineOfBlocksListFromStatementList(List<Statement> statementList) {
        List<Line> lineOfBlocksList = new ArrayList<>();
        Integer[] row = {0};
        Integer[] lineLevel = {0};
        Integer[] id = {0};
        Stack<Integer> parentIdStack = new Stack<>();
        statementList.forEach(statement -> {
            addStatementToLineOfBlocksList(lineOfBlocksList, statement, row, lineLevel, id, parentIdStack);
            row[0]++;
        });
        return lineOfBlocksList;
    }

    @Override
    public ValidationResult generateFormulaFromLineOfBlocksList(List<Line> lineList) {
        ValidationResult validationResult = new ValidationResult();
        List<String> elementListFromLineList = generateElementListFromLineList(0, lineList);
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
            statementGenerator.parsingToListOfStatement(testFormula.toString(), false);
        } catch (HandledError handledError) {
            validationResult.setValidationMessage(handledError.getMessage());
        }
        return validationResult;
    }

    @Override
    public ReWritingResult formulaRewritingBaseOnBasicStructure(List<Line> lineList) {
        ReWritingResult result = new ReWritingResult();
        ValidationResult validationResult = generateFormulaFromLineOfBlocksList(lineList);
        if (validationResult.getValidationMessage().equals("OK")) {
            StringBuilder script = new StringBuilder();
            validationResult.getGeneratedFormula()
                    .forEach(script::append);
            result.setValidationMessage("OK");
            result.setReWritingLineList(generateLineOfBlocksListFromStatementList(
                    statementGenerator.parsingToListOfStatement(script.toString(), false)
            ));

        } else {
            result.setReWritingLineList(lineList);
            result.setValidationMessage(validationResult.getValidationMessage());
        }
        return result;
    }

    @Override
    public List<BlockDto> loadOperandForTest() {
        List<BlockDto> blockDtoList = new ArrayList<>();
        for (GroupClassFroTest groupClassFroTest : GroupClassFroTest.values()) {
            BlockDto groupBlockDto = new BlockDto(BlockType.GROUP, groupClassFroTest.name(), groupClassFroTest.getTitle(), groupClassFroTest.getEnTitle());
            groupBlockDto.setBlockList(new ArrayList<>());
            for (OperandClassForTest operandClassForTest : OperandClassForTest.values()) {
                if (operandClassForTest.getGroupClassFroTest() == groupClassFroTest) {
                    BlockDto operandBlockDto = new BlockDto(operandClassForTest.getType(), operandClassForTest.getCode(), operandClassForTest.getTitle(), operandClassForTest.getEnTitle());
                    groupBlockDto.getBlockList().add(operandBlockDto);
                    operandBlockDto.setBlockList(new ArrayList<>());
                    for (SubVariableForTest subVariableForTest : SubVariableForTest.values()) {
                        if (subVariableForTest.getOperandClassForTest() == operandClassForTest) {
                            BlockDto subOperand = new BlockDto(subVariableForTest.getType(), subVariableForTest.getCode(), subVariableForTest.getTitle(), subVariableForTest.getEnTitle());
                            operandBlockDto.getBlockList().add(subOperand);
                        }
                    }
                    allOperandMap.put(operandBlockDto.getCode(), operandBlockDto);
                }
            }
            blockDtoList.add(groupBlockDto);
        }
        return blockDtoList;
    }

    private List<String> generateElementListFromLineList(Integer lineLevel, List<Line> lineList) {
        Stack<String> elementListFromLineList = new Stack<>();
        LineType innerLevel = CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getLineType() : null;
        while (CollectionUtils.isNotEmpty(lineList)) {
            LineType type = lineList.get(0).getLineType();
            if (lineList.get(0).getLineLevel().equals(lineLevel)) {
                if (elementListFromLineList.size() > 0
                        && (type == LineType.ELSE ||
                        type == LineType.ELSE_IF)) {
                    elementListFromLineList.pop();
                    elementListFromLineList.push(" ");
                }
                elementListFromLineList.addAll(generateScriptLine(lineList.remove(0)));
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
                elementListFromLineList.addAll(generateElementListFromLineList(newLineLevel, getSameLevel(lineList, lineLevel)));
                for (int j = 0; j < lineLevel; j++) {
                    elementListFromLineList.push("\t");
                }
                elementListFromLineList.push("}");
                elementListFromLineList.push("\n");
            }
        }
        return elementListFromLineList;
    }

    private List<Line> getSameLevel(List<Line> lineList, Integer lineLevel) {
        List<Line> newLineOfBlocks = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(lineList) && lineList.get(0).getLineLevel() > lineLevel) {
            newLineOfBlocks.add(lineList.remove(0));
        }
        return newLineOfBlocks;
    }

    private List<String> generateScriptLine(Line line) {
        Stack<String> finalElementList = new Stack<>();
        if (line.getLineType() != LineType.ELSE &&
                line.getLineType() != LineType.ELSE_IF) {
            for (int j = 0; j < line.getLineLevel(); j++) {
                finalElementList.push("\t");
            }
        }
        if (line.getLineType() == LineType.ELSE) {
            finalElementList.push("else");
        }
        if (line.getLineType() == LineType.IF) {
            finalElementList.push("if(");
        }
        if (line.getLineType() == LineType.ELSE_IF) {
            finalElementList.push("else if(");
        }
        if (line.getLineType() == LineType.FOR) {
            finalElementList.push("for(");
        }
        if (line.getLineType() == LineType.RETURN) {
            finalElementList.push("return ");
        }
        if (CollectionUtils.isNotEmpty(line.getBlockList())) {
            line.getBlockList().forEach(block -> {
                finalElementList.push(block.getCode());
                finalElementList.push(" ");
            });
            finalElementList.pop();
        }
        if (line.getLineType() == LineType.IF ||
                line.getLineType() == LineType.ELSE_IF ||
                line.getLineType() == LineType.FOR) {
            finalElementList.push(")");
        }
        if (line.getLineType() == LineType.EXPRESSION ||
                line.getLineType() == LineType.RETURN ||
                line.getLineType() == LineType.VARIABLE_DECLARATION)
            finalElementList.push(";");
        return finalElementList;
    }

    public void addStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            Statement statement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        switch (statement.getType()) {
            case EXPRESSION -> addExpressionStatementToLineOfBlocksList(
                    lineOfBlocksList,
                    (ExpressionStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack
            );
            case BLOCK -> {
                lineLevel[0]++;
                addBlockStatementToLineOfBlocksList(
                        lineOfBlocksList,
                        (BlockStatement) statement,
                        row,
                        lineLevel,
                        id,
                        parentIdStack
                );
                lineLevel[0]--;
                row[0]--;
            }
            case VARIABLE_DECLARATION -> addVariableDeclarationStatementToLineOfBlocksList(
                    lineOfBlocksList,
                    (VariableDeclarationStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack
            );
            case IF -> addIfStatementToLineOfBlocksList(
                    lineOfBlocksList,
                    (IfStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack
            );
            case LABEL -> addLabelStatementToLineOfBlocksList(
                    lineOfBlocksList,
                    (LabeledStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack
            );
            case RETURN -> addReturnStatementToLineOfBlocksList(
                    lineOfBlocksList,
                    (ReturnStatement) statement,
                    row,
                    lineLevel,
                    id,
                    parentIdStack
            );
        }

    }

    public void addExpressionToBlocks(List<BlockDto> blockDtoList, Expression expression) {
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
                addUnaryExpressionToBlocks(blockDtoList, (OneHandOperatorExpression) expression);
                break;
            case BINARY_EXPRESSION:
                addBinaryExpressionToBlocks(blockDtoList, expression);
                break;
            case ASSIGNMENT_EXPRESSION:
                addAssignmentExpressionToBlocks(blockDtoList, (TwoHandOperatorExpression) expression);
                break;
            case VARIABLE_DECLARATOR_EXPRESSION:
                addVariableExpressionToBlocks(blockDtoList, (VariableDeclaratorExpression) expression);
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
            List<Line> lineOfBlocksList,
            ExpressionStatement expressionStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        addExpressionToBlocks(blockDtoList, expressionStatement.getExpression());
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.EXPRESSION)
        );
    }

    public void addBlockStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            BlockStatement blockStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        blockStatement.getBodyList().forEach(statement -> {
            addStatementToLineOfBlocksList(lineOfBlocksList, statement, row, lineLevel, id, parentIdStack);
            row[0]++;
        });
    }

    public void addIfStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            IfStatement ifStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        LineType type = LineType.IF;
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (ifStatement.getTest() != null)
            addExpressionToBlocks(blockDtoList, ifStatement.getTest());
        if (CollectionUtils.isNotEmpty(lineOfBlocksList) &&
                lineOfBlocksList.get(lineOfBlocksList.size() - 1).getLineType() == LineType.ELSE) {
            parentIdStack.push(parentIdStack.peek());
            lineOfBlocksList.get(lineOfBlocksList.size() - 1).setLineType(LineType.ELSE_IF);
            lineOfBlocksList.get(lineOfBlocksList.size() - 1).setBlockList(blockDtoList);
            row[0]--;
        } else {
            parentIdStack.push(id[0]);
            lineOfBlocksList.add(
                    new Line(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, type)
            );
        }
        if (ifStatement.getConsequent() != null) {
            row[0]++;
            if (ifStatement.getConsequent().getType() == StatementType.BLOCK) {
                addStatementToLineOfBlocksList(lineOfBlocksList, ifStatement.getConsequent(), row, lineLevel, id, parentIdStack);
            } else {
                lineLevel[0]++;
                addStatementToLineOfBlocksList(lineOfBlocksList, ifStatement.getConsequent(), row, lineLevel, id, parentIdStack);
                lineLevel[0]--;
            }
        }
        if (ifStatement.getAlternate() != null) {
            row[0]++;
            List<BlockDto> alternateBlockListDto = new ArrayList<>();
            parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
            parentIdStack.push(id[0]);
            lineOfBlocksList.add(
                    new Line(id[0]++, parentId, row[0], lineLevel[0], alternateBlockListDto, LineType.ELSE)
            );
            row[0]++;
            switch (ifStatement.getAlternate().getType()) {
                case BLOCK, IF ->
                        addStatementToLineOfBlocksList(lineOfBlocksList, ifStatement.getAlternate(), row, lineLevel, id, parentIdStack);
                default -> {
                    lineLevel[0]++;
                    addStatementToLineOfBlocksList(lineOfBlocksList, ifStatement.getAlternate(), row, lineLevel, id, parentIdStack);
                    lineLevel[0]--;
                }
            }
            parentIdStack.pop();
        }
        parentIdStack.pop();
    }

    public void addLabelStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            LabeledStatement labeledStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockDtoList.add(BlockType.LABEL.getBlock(allOperandMap, labeledStatement.getLabel()));
        blockDtoList.add(BlockType.LABEL_ASSIGN.getBlock(allOperatorMap, ":"));
        parentIdStack.push(id[0]);
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.LABEL)
        );
        row[0]++;
        if (labeledStatement.getBody().getType() == StatementType.BLOCK) {
            addStatementToLineOfBlocksList(lineOfBlocksList, labeledStatement.getBody(), row, lineLevel, id, parentIdStack);
        } else {
            lineLevel[0]++;
            addStatementToLineOfBlocksList(lineOfBlocksList, labeledStatement.getBody(), row, lineLevel, id, parentIdStack);
            lineLevel[0]--;
        }
        parentIdStack.pop();
    }

    public void addReturnStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            ReturnStatement returnStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (returnStatement.getArgument() != null)
            addExpressionToBlocks(blockDtoList, returnStatement.getArgument());
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.RETURN)
        );
    }

    public void addVariableDeclarationStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            VariableDeclarationStatement variableDeclarationStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<BlockDto> blockDtoList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockDtoList.add(BlockType.KEYWORD.getBlock(allKeyword, variableDeclarationStatement.getKind()));
        boolean firstVariable = true;
        for (Expression expression : variableDeclarationStatement.getDeclaratorExpressionList()) {
            if (!firstVariable) {
                blockDtoList.add(BlockType.SEPARATOR.getBlock(allOperatorMap, ","));
            }
            addExpressionToBlocks(blockDtoList, expression);
            firstVariable = false;
        }
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockDtoList, LineType.VARIABLE_DECLARATION)
        );
    }

    public void addAssignmentExpressionToBlocks(List<BlockDto> blockDtoList, TwoHandOperatorExpression expression) {
        addExpressionToBlocks(blockDtoList, expression.getLeftChild());
        blockDtoList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockDtoList, expression.getRightChild());
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
                addExpressionToBlocks(operandBlockListDto, expression1);
                valueStack.push(operandBlockListDto);
            }
        }
        blockDtoList.addAll(valueStack.pop());
    }

    public void addUnaryExpressionToBlocks(List<BlockDto> blockDtoList, OneHandOperatorExpression expression) {
        blockDtoList.add(BlockType.ARITHMETIC_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockDtoList, expression.getArgument());
    }

    public void addVariableExpressionToBlocks(List<BlockDto> blockDtoList, VariableDeclaratorExpression expression) {
        String idName = ((Variable) expression.getVariable()).getIdName();
        blockDtoList.add(BlockType.VARIABLE.getBlock(allOperandMap, idName));
        if (expression.getInitiateValue() != null) {
            String value = ((Literal) expression.getInitiateValue()).getValue();
            blockDtoList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, "="));
            blockDtoList.add(BlockType.LITERAL.getBlock(value));
        }
    }

}
