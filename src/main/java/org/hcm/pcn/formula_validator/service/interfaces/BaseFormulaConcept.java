package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.enums.BlockType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BaseFormulaConcept {
    default List<String> getBinaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("+");
        stringList.add("-");
        stringList.add("*");
        stringList.add("/");
        stringList.add("%");
        stringList.add("**");
        stringList.add("<<");
        stringList.add(">>");
        stringList.add(">>>");
        stringList.add(">");
        stringList.add("<");
        stringList.add(">=");
        stringList.add("<=");
        stringList.add("==");
        stringList.add("===");
        stringList.add("!=");
        stringList.add("!==");
        stringList.add("&");
        stringList.add("|");
        stringList.add("^");
        return stringList;
    }

    default List<String> getUpdateUnaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("--");
        stringList.add("++");
        return stringList;
    }

    default List<String> getUnaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("-");
        stringList.add("+");
        stringList.add("!");
        return stringList;
    }

    default List<String> getLogicalList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("&&");
        stringList.add("||");
        return stringList;
    }

    default List<Character> getAllSeparatorCharacter() {
        List<Character> characterList = new ArrayList<>();
        characterList.add('!');
        characterList.add('\n');
        characterList.add('&');
        characterList.add('|');
        characterList.add('(');
        characterList.add(')');
        characterList.add('+');
        characterList.add('-');
        characterList.add('*');
        characterList.add('/');
        characterList.add(',');
        characterList.add(':');
        characterList.add(';');
        characterList.add('=');
        characterList.add('>');
        characterList.add('<');
        characterList.add('?');
        characterList.add(' ');
        characterList.add('{');
        characterList.add('}');
        characterList.add('[');
        characterList.add(']');
        return characterList;
    }

    default List<String> getHaveSequenceString() {
        List<String> haveSequenceStringList = new ArrayList<>();
        haveSequenceStringList.add("+");
        haveSequenceStringList.add("-");
        haveSequenceStringList.add("*");
        haveSequenceStringList.add("/");
        haveSequenceStringList.add("%");
        haveSequenceStringList.add("=");
        haveSequenceStringList.add("!");
        haveSequenceStringList.add("&");
        haveSequenceStringList.add("|");
        haveSequenceStringList.add(">");
        haveSequenceStringList.add("<");
        return haveSequenceStringList;
    }

    default List<String> getArithmeticOperatorList() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("+");
        operatorList.add("-");
        operatorList.add("*");
        operatorList.add("/");
        operatorList.add("%");
        operatorList.add("++");
        operatorList.add("--");
        operatorList.add("**");
        return operatorList;
    }

    default List<String> getAssignmentOperatorList() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("=");
        operatorList.add("+=");
        operatorList.add("-=");
        operatorList.add("*=");
        operatorList.add("/=");
        operatorList.add("%=");
        operatorList.add("**=");
        operatorList.add("<<=");
        operatorList.add(">>=");
        operatorList.add(">>>=");
        operatorList.add("&=");
        operatorList.add("^=");
        operatorList.add("|=");
        return operatorList;
    }

    default List<String> getComparisonOperatorList() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("==");
        operatorList.add("===");
        operatorList.add("!=");
        operatorList.add("!==");
        operatorList.add(">");
        operatorList.add("<");
        operatorList.add(">=");
        operatorList.add("<=");
        operatorList.add("?");
        return operatorList;
    }

    default List<String> getLogicalOperatorList() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("!");
        operatorList.add("&&");
        operatorList.add("||");

        return operatorList;
    }

    default List<String> getBitwiseOperatorList() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("&");
        operatorList.add("|");
        operatorList.add("~");
        operatorList.add("^");
        operatorList.add(">");
        operatorList.add("<<");
        operatorList.add(">>");
        operatorList.add(">>>");
        return operatorList;
    }

    default List<String> getAllOperator() {
        List<String> operatorList = new ArrayList<>();
        operatorList.addAll(getArithmeticOperatorList());
        operatorList.addAll(getAssignmentOperatorList());
        operatorList.addAll(getComparisonOperatorList());
        operatorList.addAll(getLogicalOperatorList());
        operatorList.addAll(getBitwiseOperatorList());
        return operatorList;
    }

    default List<String> getAllOperatorAndNotOperator() {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("\n");
        operatorList.add("(");
        operatorList.add(")");
        operatorList.add(",");
        operatorList.add(".");
        operatorList.add(":");
        operatorList.add(";");
        operatorList.add("{");
        operatorList.add("}");
        operatorList.add("[");
        operatorList.add("]");
        operatorList.addAll(getArithmeticOperatorList());
        operatorList.addAll(getAssignmentOperatorList());
        operatorList.addAll(getComparisonOperatorList());
        operatorList.addAll(getLogicalOperatorList());
        operatorList.addAll(getBitwiseOperatorList());
        return operatorList;
    }

    default List<String> getAllKeyword() {
        List<String> keyWordList = new ArrayList<>();
        keyWordList.add("arguments");
        keyWordList.add("await");
        keyWordList.add("break");
        keyWordList.add("case");
        keyWordList.add("catch");
        keyWordList.add("class");
        keyWordList.add("const");
        keyWordList.add("continue");
        keyWordList.add("debugger");
        keyWordList.add("default");
        keyWordList.add("delete");
        keyWordList.add("do");
        keyWordList.add("else");
        keyWordList.add("enum");
        keyWordList.add("eval");
        keyWordList.add("export");
        keyWordList.add("extends");
        keyWordList.add("finally");
        keyWordList.add("for");
        keyWordList.add("function");
        keyWordList.add("if");
        keyWordList.add("implements");
        keyWordList.add("import");
        keyWordList.add("in");
        keyWordList.add("instanceof");
        keyWordList.add("interface");
        keyWordList.add("let");
        keyWordList.add("new");
        keyWordList.add("package");
        keyWordList.add("private");
        keyWordList.add("protected");
        keyWordList.add("public");
        keyWordList.add("return");
        keyWordList.add("static");
        keyWordList.add("super");
        keyWordList.add("switch");
        keyWordList.add("this");
        keyWordList.add("throw");
        keyWordList.add("try");
        keyWordList.add("typeof");
        keyWordList.add("var");
        keyWordList.add("void");
        keyWordList.add("while");
        keyWordList.add("with");
        keyWordList.add("yield");
        keyWordList.addAll(getAllLiteralKeyword());
        return keyWordList;
    }

    default List<String> getAllLiteralKeyword() {
        List<String> keyWordList = new ArrayList<>();
        keyWordList.add("false");
        keyWordList.add("true");
        keyWordList.add("null");
        keyWordList.add("undefined");
        return keyWordList;
    }

    default List<Character> getNoChar() {
        List<Character> characterList = new ArrayList<>();
        characterList.add('\n');
        characterList.add('\t');
        return characterList;
    }

    default Map<String, BlockDto> getAllOperatorMap() {
        enum AllOperator {
            O1(BlockType.ARITHMETIC_OPERATOR, "+", "+", "+"),
            O2(BlockType.ARITHMETIC_OPERATOR, "-", "-", "-"),
            O3(BlockType.ARITHMETIC_OPERATOR, "*", "×", "*"),
            O4(BlockType.ARITHMETIC_OPERATOR, "/", "÷", "/"),
            O5(BlockType.ARITHMETIC_OPERATOR, "%", "%", "%"),
            O6(BlockType.ARITHMETIC_OPERATOR, "++", "++", "++"),
            O7(BlockType.ARITHMETIC_OPERATOR, "--", "--", "--"),
            O8(BlockType.ARITHMETIC_OPERATOR, "**", "**", "**"),
            O9(BlockType.ASSIGNMENT_OPERATOR, "=", "=", "="),
            O10(BlockType.ASSIGNMENT_OPERATOR, "+=", "+=", "+="),
            O11(BlockType.ASSIGNMENT_OPERATOR, "-=", "-=", "-="),
            O12(BlockType.ASSIGNMENT_OPERATOR, "*=", "*=", "*="),
            O13(BlockType.ASSIGNMENT_OPERATOR, "/=", "/=", "/="),
            O14(BlockType.ASSIGNMENT_OPERATOR, "%=", "%=", "%="),
            O15(BlockType.ASSIGNMENT_OPERATOR, "**=", "**=", "**="),
            O16(BlockType.ASSIGNMENT_OPERATOR, "<<=", "<<=", "<<="),
            O17(BlockType.ASSIGNMENT_OPERATOR, ">>=", ">>=", ">>="),
            O18(BlockType.ASSIGNMENT_OPERATOR, ">>>=", ">>>=", ">>>="),
            O19(BlockType.ASSIGNMENT_OPERATOR, "&=", "&=", "&="),
            O20(BlockType.ASSIGNMENT_OPERATOR, "^=", "^=", "^="),
            O21(BlockType.ASSIGNMENT_OPERATOR, "|=", "|=", "|="),
            O22(BlockType.COMPARISON_OPERATOR, "==", "==", "=="),
            O23(BlockType.COMPARISON_OPERATOR, "===", "===", "==="),
            O24(BlockType.COMPARISON_OPERATOR, "!=", "!=", "!="),
            O25(BlockType.COMPARISON_OPERATOR, "!==", "!==", "!=="),
            O26(BlockType.COMPARISON_OPERATOR, ">", ">", ">"),
            O27(BlockType.COMPARISON_OPERATOR, "<", "<", "<"),
            O28(BlockType.COMPARISON_OPERATOR, ">=", ">=", ">="),
            O29(BlockType.COMPARISON_OPERATOR, "<=", "<=", "<="),
            O30(BlockType.COMPARISON_OPERATOR, "?", "?", "?"),
            O31(BlockType.LOGICAL_OPERATOR, "!", "!", "!"),
            O32(BlockType.LOGICAL_OPERATOR, "&&", "&&", "&&"),
            O33(BlockType.LOGICAL_OPERATOR, "||", "||", "||"),
            O34(BlockType.BITWISE_OPERATOR, "&", "&", "&"),
            O35(BlockType.BITWISE_OPERATOR, "|", "|", "|"),
            O36(BlockType.BITWISE_OPERATOR, "~", "~", "~"),
            O37(BlockType.BITWISE_OPERATOR, "^", "^", "^"),
            O38(BlockType.BITWISE_OPERATOR, ">", ">", ">"),
            O39(BlockType.BITWISE_OPERATOR, "<<", "<<", "<<"),
            O40(BlockType.BITWISE_OPERATOR, ">>", ">>", ">>"),
            O41(BlockType.BITWISE_OPERATOR, ">>>", ">>>", ">>>");

            AllOperator(BlockType type, String code, String title, String enTitle) {
                this.type = type;
                this.code = code;
                this.title = title;
                this.enTitle = enTitle;
            }

            private final BlockType type;
            private final String code;
            private final String title;
            private final String enTitle;

            public BlockType getType() {
                return type;
            }

            public String getCode() {
                return code;
            }

            public String getTitle() {
                return title;
            }

            public String getEnTitle() {
                return enTitle;
            }
        }
        Map<String, BlockDto> operatorMap = new LinkedHashMap<>();
        for (AllOperator value : AllOperator.values()) {
            BlockDto operator = new BlockDto();
            operator.setType(value.getType());
            operator.setCode(value.getCode());
            operator.setTitle(value.getTitle());
            operator.setEnTitle(value.getEnTitle());
            operatorMap.put(operator.getCode(), operator);
        }
        return operatorMap;
    }


    default Map<String, BlockDto> getAllKeywordMap() {
        enum AllKeyword {
            K0(BlockType.KEYWORD, "arguments", "متغییرها", "arguments"),
            K1(BlockType.KEYWORD, "await", "در انتظار", "await"),
            K2(BlockType.KEYWORD, "break", "شکستن", "break"),
            K3(BlockType.KEYWORD, "case", "مورد", "case"),
            K4(BlockType.KEYWORD, "catch", "گرفتن", "catch"),
            K5(BlockType.KEYWORD, "class", "کلاس", "class"),
            K6(BlockType.KEYWORD, "const", "ثابت", "const"),
            K7(BlockType.KEYWORD, "continue", "ادامه", "continue"),
            K8(BlockType.KEYWORD, "debugger", "اشکال زدا", "debugger"),
            K9(BlockType.KEYWORD, "default", "پیش فرض", "default"),
            K11(BlockType.KEYWORD, "delete", "حذف", "delete"),
            K12(BlockType.KEYWORD, "do", "انجام دادن", "do"),
            K13(BlockType.KEYWORD, "else", "در غیر این صورت", "else"),
            K14(BlockType.KEYWORD, "enum", "enum", "enum"),
            K15(BlockType.KEYWORD, "eval", "ارزیابی", "eval"),
            K16(BlockType.KEYWORD, "export", "خروجی", "export"),
            K17(BlockType.KEYWORD, "extends", "گسترش", "extends"),
            K18(BlockType.KEYWORD, "finally", "نهایتا", "finally"),
            K19(BlockType.KEYWORD, "for", "برای", "for"),
            K20(BlockType.KEYWORD, "function", "تابع", "function"),
            K21(BlockType.KEYWORD, "if", "اگر", "if"),
            K22(BlockType.KEYWORD, "implements", "پیاده سازی", "implements"),
            K23(BlockType.KEYWORD, "import", "ورود", "import"),
            K24(BlockType.KEYWORD, "in", "در", "in"),
            K25(BlockType.KEYWORD, "instanceof", "نمونه از", "instanceof"),
            K26(BlockType.KEYWORD, "interface", "رابط", "interface"),
            K27(BlockType.KEYWORD, "let", "متغییر", "let"),
            K28(BlockType.KEYWORD, "new", "جدید", "new"),
            K29(BlockType.KEYWORD, "package", "بسته", "package"),
            K30(BlockType.KEYWORD, "private", "خصوصی", "private"),
            K31(BlockType.KEYWORD, "protected", "محافظت شده", "protected"),
            K32(BlockType.KEYWORD, "public", "عمومی", "public"),
            K33(BlockType.KEYWORD, "return", "بازگشت", "return"),
            K34(BlockType.KEYWORD, "static", "ثابت", "static"),
            K35(BlockType.KEYWORD, "super", "super", "super"),
            K36(BlockType.KEYWORD, "switch", "گزینه", "switch"),
            K37(BlockType.KEYWORD, "this", "این", "this"),
            K38(BlockType.KEYWORD, "throw", "پرتاب کردن", "throw"),
            K39(BlockType.KEYWORD, "try", "تلاش", "try"),
            K40(BlockType.KEYWORD, "typeof", "نوع", "typeof"),
            K41(BlockType.KEYWORD, "var", "متغییر", "var"),
            K42(BlockType.KEYWORD, "void", "تهی", "void"),
            K43(BlockType.KEYWORD, "while", "تا زمانیکه", "while"),
            K44(BlockType.KEYWORD, "with", "با", "with"),
            K45(BlockType.KEYWORD, "yield", "حاصل", "yield");

            AllKeyword(BlockType type, String code, String title, String enTitle) {
                this.type = type;
                this.code = code;
                this.title = title;
                this.enTitle = enTitle;
            }

            private final BlockType type;
            private final String code;
            private final String title;
            private final String enTitle;

            public BlockType getType() {
                return type;
            }

            public String getCode() {
                return code;
            }

            public String getTitle() {
                return title;
            }

            public String getEnTitle() {
                return enTitle;
            }
        }
        Map<String, BlockDto> keywordMap = new LinkedHashMap<>();
        for (AllKeyword value : AllKeyword.values()) {
            BlockDto keyword = new BlockDto();
            keyword.setType(value.getType());
            keyword.setCode(value.getCode());
            keyword.setTitle(value.getTitle());
            keyword.setEnTitle(value.getEnTitle());
            keywordMap.put(keyword.getCode(), keyword);
        }
        return keywordMap;
    }
}

