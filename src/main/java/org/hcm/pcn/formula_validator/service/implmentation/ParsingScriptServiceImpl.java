package org.hcm.pcn.formula_validator.service.implmentation;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.dto.*;
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
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ParsingScriptServiceImpl implements ParsingScriptService {
    private final StatementGenerator statementGenerator;
    private final Map<String, Block> allOperatorMap;
    private final Map<String, Block> allKeyword;
    private final Map<String, Block> allOperandMap;

    public ParsingScriptServiceImpl(
            StatementGenerator statementGenerator,
            @Qualifier("Operator") Map<String, Block> allOperatorMap,
            @Qualifier("Keyword") Map<String, Block> allKeyword) {
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
    public List<Block> loadOperandForTest() {
        List<Block> blockList = new ArrayList<>();
        for (GroupClassFroTest groupClassFroTest : GroupClassFroTest.values()) {
            Block groupBlock = new Block(BlockType.GROUP, groupClassFroTest.name(), groupClassFroTest.getTitle(), groupClassFroTest.getEnTitle());
            groupBlock.setBlockList(new ArrayList<>());
            for (OperandClassForTest operandClassForTest : OperandClassForTest.values()) {
                if (operandClassForTest.getGroupClassFroTest() == groupClassFroTest) {
                    Block operandBlock = new Block(operandClassForTest.getType(), operandClassForTest.getCode(), operandClassForTest.getTitle(), operandClassForTest.getEnTitle());
                    groupBlock.getBlockList().add(operandBlock);
                    operandBlock.setBlockList(new ArrayList<>());
                    for (SubVariableForTest subVariableForTest : SubVariableForTest.values()) {
                        if (subVariableForTest.getOperandClassForTest() == operandClassForTest) {
                            Block subOperand = new Block(subVariableForTest.getType(), subVariableForTest.getCode(), subVariableForTest.getTitle(), subVariableForTest.getEnTitle());
                            operandBlock.getBlockList().add(subOperand);
                        }
                    }
                    allOperandMap.put(operandBlock.getCode(), operandBlock);
                }
            }
            blockList.add(groupBlock);
        }
        return blockList;
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

    public void addExpressionToBlocks(List<Block> blockList, Expression expression) {
        switch (expression.getType()) {
            case VARIABLE_EXPRESSION:
                Variable variable = (Variable) expression;
                blockList.add(BlockType.VARIABLE.getBlock(allOperandMap, variable.getIdName()));
                break;
            case LITERAL_EXPRESSION:
                Literal literal = (Literal) expression;
                blockList.add(BlockType.LITERAL.getBlock(literal.getValue()));
                break;
            case UNARY_EXPRESSION:
                addUnaryExpressionToBlocks(blockList, (OneHandOperatorExpression) expression);
                break;
            case BINARY_EXPRESSION:
                addBinaryExpressionToBlocks(blockList, expression);
                break;
            case ASSIGNMENT_EXPRESSION:
                addAssignmentExpressionToBlocks(blockList, (TwoHandOperatorExpression) expression);
                break;
            case VARIABLE_DECLARATOR_EXPRESSION:
                addVariableExpressionToBlocks(blockList, (VariableDeclaratorExpression) expression);
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
        List<Block> blockList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        addExpressionToBlocks(blockList, expressionStatement.getExpression());
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockList, LineType.EXPRESSION)
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
        List<Block> blockList = new ArrayList<>();
        LineType type = LineType.IF;
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (ifStatement.getTest() != null)
            addExpressionToBlocks(blockList, ifStatement.getTest());
        if (CollectionUtils.isNotEmpty(lineOfBlocksList) &&
                lineOfBlocksList.get(lineOfBlocksList.size() - 1).getLineType() == LineType.ELSE) {
            parentIdStack.push(parentIdStack.peek());
            lineOfBlocksList.get(lineOfBlocksList.size() - 1).setLineType(LineType.ELSE_IF);
            lineOfBlocksList.get(lineOfBlocksList.size() - 1).setBlockList(blockList);
            row[0]--;
        } else {
            parentIdStack.push(id[0]);
            lineOfBlocksList.add(
                    new Line(id[0]++, parentId, row[0], lineLevel[0], blockList, type)
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
            List<Block> alternateBlockList = new ArrayList<>();
            parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
            parentIdStack.push(id[0]);
            lineOfBlocksList.add(
                    new Line(id[0]++, parentId, row[0], lineLevel[0], alternateBlockList, LineType.ELSE)
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
        List<Block> blockList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockList.add(BlockType.LABEL.getBlock(allOperandMap, labeledStatement.getLabel()));
        blockList.add(BlockType.LABEL_ASSIGN.getBlock(allOperatorMap, ":"));
        parentIdStack.push(id[0]);
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockList, LineType.LABEL)
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
        List<Block> blockList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        if (returnStatement.getArgument() != null)
            addExpressionToBlocks(blockList, returnStatement.getArgument());
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockList, LineType.RETURN)
        );
    }

    public void addVariableDeclarationStatementToLineOfBlocksList(
            List<Line> lineOfBlocksList,
            VariableDeclarationStatement variableDeclarationStatement,
            Integer[] row,
            Integer[] lineLevel,
            Integer[] id,
            Stack<Integer> parentIdStack) {
        List<Block> blockList = new ArrayList<>();
        Integer parentId = !parentIdStack.empty() ? parentIdStack.peek() : null;
        blockList.add(BlockType.KEYWORD.getBlock(allKeyword, variableDeclarationStatement.getKind()));
        boolean firstVariable = true;
        for (Expression expression : variableDeclarationStatement.getDeclaratorExpressionList()) {
            if (!firstVariable) {
                blockList.add(BlockType.SEPARATOR.getBlock(allOperatorMap, ","));
            }
            addExpressionToBlocks(blockList, expression);
            firstVariable = false;
        }
        lineOfBlocksList.add(
                new Line(id[0]++, parentId, row[0], lineLevel[0], blockList, LineType.VARIABLE_DECLARATION)
        );
    }

    public void addAssignmentExpressionToBlocks(List<Block> blockList, TwoHandOperatorExpression expression) {
        addExpressionToBlocks(blockList, expression.getLeftChild());
        blockList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockList, expression.getRightChild());
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

    public void addBinaryExpressionToBlocks(List<Block> blockList, Expression expression) {
        List<Expression> expressionList = new ArrayList<>();
        callTravers(expression, expressionList);
        Stack<List<Block>> valueStack = new Stack<>();
        for (Expression expression1 : expressionList) {
            if (expression1.getType() == ExpressionType.BINARY_EXPRESSION) {
                TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) expression1;
                List<Block> secondBlockList = valueStack.pop();
                List<Block> firstBlockList = valueStack.pop();
                List<Block> result = new ArrayList<>(firstBlockList);
                result.add(BlockType.ARITHMETIC_OPERATOR.getBlock(allOperatorMap, operatorExpression.getOperator()));
                result.addAll(secondBlockList);
                valueStack.push(result);
            } else {
                List<Block> operandBlockList = new ArrayList<>();
                addExpressionToBlocks(operandBlockList, expression1);
                valueStack.push(operandBlockList);
            }
        }
        blockList.addAll(valueStack.pop());
    }

    public void addUnaryExpressionToBlocks(List<Block> blockList, OneHandOperatorExpression expression) {
        blockList.add(BlockType.ARITHMETIC_OPERATOR.getBlock(allOperatorMap, expression.getOperator()));
        addExpressionToBlocks(blockList, expression.getArgument());
    }

    public void addVariableExpressionToBlocks(List<Block> blockList, VariableDeclaratorExpression expression) {
        String idName = ((Variable) expression.getVariable()).getIdName();
        blockList.add(BlockType.VARIABLE.getBlock(allOperandMap, idName));
        if (expression.getInitiateValue() != null) {
            String value = ((Literal) expression.getInitiateValue()).getValue();
            blockList.add(BlockType.ASSIGNMENT_OPERATOR.getBlock(allOperatorMap, "="));
            blockList.add(BlockType.LITERAL.getBlock(value));
        }
    }

}
