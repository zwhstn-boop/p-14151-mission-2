package com.back;
import java.util.Arrays;

//구현 목표
//솔직히 처음부터 제대로 구현하지는 못했기 때문에 답을 봐 가며 역으로 리팩토링 과정을 거치고, 최대한 TDD 형태로 코드를 이해하고 해석하려 노력했습니다.
/*
사칙연산에서 필요한 것은 무엇이며 조건은 무엇이 있는가?
t28까지에서 구현해야 하는데 필요한 것.
1.덧셈 뺄셈.
-(뻴셈 역시 음수의 덧셈이다.) 구현 자체는 쉽다. 그냥 음수도 정수형으로 받으니까 그걸 그대로 더해주면 된다.
2. 곱셈.
-곱셈 역시 덧셈의 반복이나 따로 구현할 필요는 없다. 그러나 덧셈(뻴셈)보다 우선순위가 높다. 따라서 우선순위를 책정하는 변수가 필요하다.
3. 괄호 계산.
-곱셈과는 별개의 우선순위 변수가 필요하다. 혹은 곱셈의 우선순위 변수에 +1을 해주면 된다. 중괄호, 대괄호는 아직 테스트에 없다.
4.다항식 계산.각 괄호 안의 계산을 끝낸 이후 계산을 하면 된다. 이중으로 중첩된 상태의 계산이니 재귀 함수가 필요해 보인다.
5. 구현 순서. 덧셈,뺄셈,곱셈(우선순위설정), 괄호계산, 곱셈이 포함된 괄호계산, 이중괄호,삼중괄호.
 */

//우선순위=괄호 안에 있는 곱셉->괄호 안에 있는 덧셈뺄셈->괄호가 없는 곱셈->괄호가 없는 덧셈뺄셈 순.

public class Calc {
    private static boolean isDebug = true;
    //깊이 확인용 디버그 논리함수

    public static int run(String expr) {
        return _run(expr, 0);
        //각 항목 반환 후 실행
    }

    private static void printRsOnDebugMode(int rs, int depth) {
        if (isDebug) {
            System.out.println("  ".repeat(depth) + "rs[" + depth + "] = " + rs);
        }
    }

    private static int _run(String expr, int depth) {
        expr = expr.trim();//특정 변수가 아니라 문자열로 받아서 쪼갠다.

        if (isDebug) {
            System.out.print("  ".repeat(depth) + "expr[" + depth + "] { raw : " + expr);
        }

        expr = removeUnnecessaryBrackets(expr);//쓸데없는 괄호 지우고
        expr = transformMinusOuterBracketToPlus(expr); //뺄셈은 음수 덧셈으로 바꾸고
        expr = expr.replaceAll(" - ", " + -");

        if (isDebug) {
            System.out.println(", clean : " + expr + " }");
        }

        if (expr.contains("(")) {// 1.일단 괄호가 있는지 체크한다.
            String[] exprBits = splitTwoPartsBy(expr, '+');

            if (exprBits != null) { //괄호가 있다 우선순위 +1 하고 그 안의 내용 실행
                int rs = _run(exprBits[0], depth + 1) + _run(exprBits[1], depth + 1);

                printRsOnDebugMode(rs, depth);

                return rs;
            }

            exprBits = splitTwoPartsBy(expr, '*'); //괄호가 없다면 곱셈을 실행한다.

            if (exprBits != null) { //우선순위 지정
                int rs = _run(exprBits[0], depth + 1) * _run(exprBits[1], depth + 1);

                printRsOnDebugMode(rs, depth);

                return rs;
            }

            throw new IllegalArgumentException("Invalid expression: " + expr); //쪼갠 값 지정
        }
        //스트림을 통한 우선순위 설정

        if (expr.contains(" * ") && expr.contains(" + ")) {
            //곱셈과 덧셈(+뻴셈)이 같이 있으면 곱셈을 먼저 실시한다.
            String[] exprBits = expr.split(" \\+ ");
            //스트림화
            int rs = Arrays.stream(exprBits)
                    .map(exprBit -> _run(exprBit, depth + 1))
                    .reduce((a, b) -> a + b)
                    .orElse(0);

            printRsOnDebugMode(rs, depth);

            return rs;
        }

        if (expr.contains(" * ")) {//그냥 곱셈
            String[] exprBits = expr.split(" \\* ");
            //스트림화
            int rs = Arrays.stream(exprBits)
                    .map(Integer::parseInt)
                    .reduce((a, b) -> a * b)
                    .orElse(0);

            printRsOnDebugMode(rs, depth);

            return rs;
        }

        String[] exprBits = expr.split(" \\+ ");
        // 그냥덧셈(뻴셈) 스트림화
        int rs = Arrays.stream(exprBits)
                .map(Integer::parseInt)
                .reduce((a, b) -> a + b)
                .orElse(0);

        printRsOnDebugMode(rs, depth);

        return rs;
    }

    private static String transformMinusOuterBracketToPlus(String expr) {
        //컴퓨터의 뺄셈은 음수를 더하는 것이다. 괄호 앞에 음스화 하면서 더하기를 하는 예외처리
        if (expr.startsWith("-(") && expr.endsWith(")")) {
            return expr.substring(1, expr.length()) + " * -1";
        }

        return expr;
    }

    private static String[] splitTwoPartsBy(String expr, char splitBy) {
        int bracketDepth = 0;
        //괄호 열렸을때 괄호 안의 내용물 쪼개기

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (bracketDepth == 0 && c == splitBy) {
                return new String[]{expr.substring(0, i), expr.substring(i + 1)};
            } else if (c == '(') {
                bracketDepth++;
            } else if (c == ')') {
                bracketDepth--;
            }
        }

        return null;
    }

    private static String removeUnnecessaryBrackets(String expr) {
        //계산 끝난 괄호나 의미 없는 괄호는 열고 닫아라
        if (isOneBracketed(expr)) {
            return removeUnnecessaryBrackets(expr.substring(1, expr.length() - 1));
        }

        return expr;
    }

    private static boolean isOneBracketed(String expr) {
        if (!expr.startsWith("(") || !expr.endsWith(")")) return false;
        //괄호로 각 항목을 깊이화(괄호 안에서 세부 계산을 하는가?)

        int bracketDepth = 0;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == '(') {
                bracketDepth++;
            } else if (c == ')') {
                bracketDepth--;
            }

            if (bracketDepth == 0 && i != expr.length() - 1) return false;
        }

        return true;
    }
}
