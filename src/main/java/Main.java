import statamentgenerator.MainStatementGeneratorImpl;
import statement.Statement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String so =
                "    if(s ===\n" +
                        "10){\n" +
                        "    }\n" +
                        "\n" +
                        "    if(s === 10 && l <= n || p=>100){\n" +
                        "     >>> >> ++-- /**/ '///'" +
                        "    }\n" +
                        "\n" +
                        "    var Timestamp = Java.type('java.sql.Timestamp');\n" +
                        "    var END_CALC_YEAR_1396 = 1396;\n" +
                        "    var minimumAllowance = 0;\n" +
                        "    var annualSeveranceBase = 0;\n" +
                        "    var currentYearSumSystemAmount = 0;\n" +
                        "    var actionParameterValues;\n" +
                        "    var lastActionOfLastYear;\n" +
                        "    var persianFirstDayOfYearDate;\n" +
                        "    var lastActionOfLastYearSumAmount = 0;\n" +
                        "    var childPayment;\n" +
                        "    var restorationAllowancePVDTO = 0;\n" +
                        "    var finalExecuteDate = null;\n" +
                        "    var lastActionParameterValue=null;\n" +
                        "    var jobChanged=null;\n" +
                        "    var T1 = 0;\n" +
                        "    print('\\n\\n\\n\\\"');\n" +
                        "    print('فرمول ترمیم حقوق');\n" +
                        "    var isEmployedOfCurrentYear = isEmployedCurrentYear(execDate);\n" +
                        "    logMethodResult('1-calcRestorationAllowance', '  isEmployedOfCurrentYear := ' + isEmployedOfCurrentYear + '\\t');\n" +
                        "    var lastAction = getLastAction(execDate); \n" +
                        "    if (lastAction != null) {\n" +
                        "        logMethodResult('2-LastAction', '  executionDate For lastAction := ' + execDate + ' -> ' + convertToJalaliDate(lastAction.executionDate) + '\\t');\n" +
                        "        lastActionParameterValue = getlastActionParameterValueSet();\n" +
                        "        logMethodResult('3-lastActionParameterValueSet', '  lastActionParameterValue := ' + lastActionParameterValue + '\\t');\n" +
                        "        jobChanged = isActionJobChanged(execDate, lastAction);\n" +
                        "        logMethodResult('4-isActionJobChanged', '  job Changed ? -> ' + jobChanged + '\\t');\n" +
                        "    }\n" +
                        "    var actionDateFirstDayOfYear = isActionDateFirstDayOfYear(execDate);\n" +
                        "\n" +
                        "    if (!jobChanged && !actionDateFirstDayOfYear && lastAction != null) {\n" +
                        "        var subResult = getRestorationAllowance(jobChanged, actionDateFirstDayOfYear, lastAction, execDate, lastActionParameterValue);\n" +
                        "\t\t    if(subResult != null)\n" +
                        "\t\t\t      return subResult;\n" +
                        "    }\n" +
                        "\n" +
                        "    minimumAllowance = getMinimumAllowance(execDate);\n" +
                        "\n" +
                        "    getAndreturnMinimumAllowance(minimumAllowance);\n" +
                        "\n" +
                        "\n" +
                        "    annualSeveranceBase = getAnnualSeveranceBase(execDate);\n" +
                        "    logMethodResult('13-AnnualSeveranceBase', '  annualSeveranceBase ------> ' + annualSeveranceBase + '\\t');\n" +
                        "    minimumAllowance = (minimumAllowance + annualSeveranceBase).toFixed(2);\n" +
                        "    logMethodResult('14-calcRestorationAllowance', '  minimumAllowance = minimumAllowance + annualSeveranceBase ------> ' + minimumAllowance + ' + ' + annualSeveranceBase + ' Then minimumAllowance := ' + minimumAllowance + '\\t');\n" +
                        "    currentYearSumSystemAmount = getCurrentYearSumSystemAmount(execDate);\n" +
                        "    logMethodResult('15-CurrentYearSumSystemAmount', '  currentYearSumSystemAmount  ' + currentYearSumSystemAmount + '\\t');\n" +
                        "\n" +
                        "    T1 = calcuteT1Method(minimumAllowance, currentYearSumSystemAmount);\n" +
                        "\n" +
                        "    var lastActionOfLastYear = getLastActionOfLastYear();\n" +
                        "    var timeStampexecDate = new Timestamp(execDate);\n" +
                        "    var lastYearExecTimeStamp;\n" +
                        "    finalExecuteDate = getFinalExecutionDatefromActionInLastYear();\n" +
                        "    if (lastActionOfLastYear == null && (getPersianYear(timeStampexecDate) > END_CALC_YEAR_1396) && !isEmployedOfCurrentYear) {\n" +
                        "        logMethodResult('19-getFinalExecutionDate\\\"', '  finalExecuteDate := ' + convertToJalaliDate(finalExecuteDate) + '\\t');\n" +
                        "        logMethodResult('20-calcRestorationAllowance', ' if lastActionOfLastYear == null && execDate > 1396 && !isEmployedOfCurrentYear-> ' + lastActionOfLastYear + ' && ' + timeStampexecDate + ' > ' + ' 1396 ' + ' && ' + !isEmployedOfCurrentYear + ' Then ' + '\\t');\n" +
                        "        lastActionOfLastYear = calcLastYearAction(finalExecuteDate);\n" +
                        "        logMethodResult('21-calcLastYearAction', ' lastActionOfLastYear := ' + lastActionOfLastYear + '\\t');\n" +
                        "    } else {\n" +
                        "        logMethodResult('22-calcRestorationAllowance', '  lastActionOfLastYear not Null Then break for condition 19,20,21 ' + '\\t');\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    lastActionOfLastYearSumAmount = clacuteAndreturnLastActionOfLastYearSumAmount(lastActionOfLastYear, execDate, annualSeveranceBase);\n" +
                        "\n" +
                        "    return calcuteAndReturnRestorationAllowancePVDTO(lastActionOfLastYearSumAmount, currentYearSumSystemAmount, T1);\n";
        String s = "f: h+2-{n:5} ";
        //"- ! + + h";
        MainStatementGeneratorImpl generator = new MainStatementGeneratorImpl();
        //Statement statement = generator.parsing(s);
        List<Statement> statementList = generator.parsing(s);
        System.out.println("builder");
    }













    /*private static List<String> getStatementSeparatorList() {
        List<String> statementSeparatorList = new ArrayList<>();
        statementSeparatorList.add(";");
        statementSeparatorList.add("{");
        statementSeparatorList.add("}");
        return statementSeparatorList;
    }

    static private Node createNode(List<String> sentence, List<List<String>> sentenceList) {
        Node node = new Node();
        if (CollectionUtils.isNotEmpty(sentence)) {
            Element currentElement = new Element();
            if (StringUtils.isNotBlank(sentence.get(0)) &&
                    sentence.get(0).startsWith("var ")) {
                node = new VarNode();
                currentElement.setCode(sentence.remove(0).substring(4));
            } else if (StringUtils.isNotBlank(sentence.get(0)) &&
                    sentence.get(0).startsWith("return ")) {
                node = new ReturnNode();
                currentElement.setCode(sentence.remove(0).substring(7));
            } else if (StringUtils.isNotBlank(sentence.get(0)) &&
                    sentence.get(0).equalsIgnoreCase("if") &&
                    sentence.size() > 1 &&
                    sentence.get(1).equalsIgnoreCase("(") &&
                    validPretenses(sentence)) {
                node = prepareIfNode(sentence, sentenceList);
            } else if (StringUtils.isNotBlank(sentence.get(0)) &&
                    sentence.get(0).equalsIgnoreCase("for") &&
                    sentence.size() > 1 &&
                    sentence.get(1).equalsIgnoreCase("(") &&
                    validPretenses(sentence)) {
                node = prepareForNode(sentence, sentenceList);
            } else {
                node = new SentenceNode();
                currentElement.setCode(sentence.remove(0));
            }
            if (StringUtils.isNotBlank(currentElement.getCode())) {
                node.setChildElement(currentElement);
                for (String s : sentence) {
                    Element nextElement = new Element();
                    nextElement.setCode(s);
                    currentElement.setNextElement(nextElement);
                    currentElement = nextElement;
                }
            }
        }
        return node;
    }

    private static Node prepareIfNode(List<String> sentence, List<List<String>> sentenceList) {
        IfNode ifNode = new IfNode();
        List<String> conditionSentence = new ArrayList<>();
        Stack<String> parenthesisStack = new Stack<>();
        do {
            conditionSentence.add(sentence.get(1));
            if (sentence.get(1).equalsIgnoreCase("("))
                parenthesisStack.push(sentence.get(1));
            if (sentence.get(1).equalsIgnoreCase(")"))
                parenthesisStack.pop();
            sentence.remove(1);
        } while (!parenthesisStack.isEmpty());
        conditionSentence.remove(0);
        conditionSentence.remove(conditionSentence.size() - 1);
        ifNode.setChildElement(createConditionElement(conditionSentence));
        if (sentence.get(1).equalsIgnoreCase("{")) {
            List<List<String>> ifSentenceList = new ArrayList<>();
            Stack<String> bracesStack = new Stack<>();
            bracesStack.push(sentence.remove(1));
            do {
                ifSentenceList.add(sentenceList.get(0));
                if (sentenceList.get(0).get(0).equalsIgnoreCase("{"))
                    bracesStack.push(sentence.get(1));
                if (sentenceList.get(0).get(0).equalsIgnoreCase("}"))
                    bracesStack.pop();
                sentenceList.remove(0);
            } while (!bracesStack.isEmpty());
            ifNode.setChildNode(createNode(ifSentenceList.remove(0), ifSentenceList));
        }
        sentence.remove(0);
        return ifNode;
    }

    private static Node prepareForNode(List<String> sentence, List<List<String>> sentenceList) {
        ForNode forNode = new ForNode();
        List<String> forSentence = new ArrayList<>();
        Stack<String> parenthesisStack = new Stack<>();
        do {
            forSentence.add(sentence.get(1));
            if (sentence.get(1).equalsIgnoreCase("("))
                parenthesisStack.push(sentence.get(1));
            if (sentence.get(1).equalsIgnoreCase(")"))
                parenthesisStack.pop();
            sentence.remove(1);
        } while (!parenthesisStack.isEmpty());
        forSentence.remove(0);
        forSentence.remove(forSentence.size() - 1);
        forNode.setChildElement(createForElement(forSentence));
        if (sentence.get(1).equalsIgnoreCase("{")) {
            List<List<String>> ifSentenceList = new ArrayList<>();
            Stack<String> bracesStack = new Stack<>();
            bracesStack.push(sentence.remove(1));
            do {
                ifSentenceList.add(sentenceList.get(0));
                if (sentenceList.get(0).get(0).equalsIgnoreCase("{"))
                    bracesStack.push(sentence.get(1));
                if (sentenceList.get(0).get(0).equalsIgnoreCase("}"))
                    bracesStack.pop();
                sentenceList.remove(0);
            } while (!bracesStack.isEmpty());
            forNode.setChildNode(createNode(ifSentenceList.remove(0), ifSentenceList));
        }
        sentence.remove(0);
        return forNode;
    }

    private static Element createConditionElement(List<String> conditionSentence) {
        String mainConditionSentence = conditionSentence.stream().reduce("", String::concat);
        mainConditionSentence = mainConditionSentence
                .replace("&&", "#_AND_#")
                .replace("&", "#_AND_#")
                .replace("||", "#_OR_#")
                .replace("|", "#_OR_#")
                .replace(">=", "#_GTE_#")
                .replace("<=", "#_LTE_#")
                .replace("!==", "#_NEB_#")
                .replace("===", "#_EB_#")
                .replace("!=", "#_NE_#")
                .replace("==", "#_E_#")
                .replace("<", "#_LT_#")
                .replace(">", "#_GT_#")
                .replace("(", "#_(_#")
                .replace(")", "#_)_#");
        Element firstElement = new Element();
        Element result = firstElement;
        for (String s : mainConditionSentence.split("#")) {
            Element secondElement = new Element();
            firstElement.setCode(getConditionStringMap().getOrDefault(s, s));
            firstElement.setNextElement(secondElement);
            firstElement = secondElement;
        }
        firstElement.setNextElement(null);
        return result;
    }

    private static Element createForElement(List<String> conditionSentence) {
        String mainConditionSentence = conditionSentence.stream().reduce("", String::concat);
        mainConditionSentence = mainConditionSentence
                .replace("&&", "#_AND_#")
                .replace("&", "#_AND_#")
                .replace("||", "#_OR_#")
                .replace("|", "#_OR_#")
                .replace(">=", "#_GTE_#")
                .replace("<=", "#_LTE_#")
                .replace("!==", "#_NEB_#")
                .replace("===", "#_EB_#")
                .replace("!=", "#_NE_#")
                .replace("==", "#_E_#")
                .replace("<", "#_LT_#")
                .replace(">", "#_GT_#")
                .replace("(", "#_(_#")
                .replace(")", "#_)_#");
        Element firstElement = new Element();
        Element result = firstElement;
        for (String s : mainConditionSentence.split("#")) {
            Element secondElement = new Element();
            firstElement.setCode(getConditionStringMap().getOrDefault(s, s));
            firstElement.setNextElement(secondElement);
            firstElement = secondElement;
        }
        firstElement.setNextElement(null);
        return result;
    }*/

}
