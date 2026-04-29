package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class CalcTest {
    @Test
    @DisplayName("1 + 1 == 2")
    void t1() {
        //단순 덧셈이자 단순 반환. return값 고정에서 덧셈으로 수정되었다.
        assertThat(Calc.run("1 + 1")).isEqualTo(2);
    }

    @Test
    @DisplayName("2 + 1 == 3")
    void t2() {
        //앞 뒤 항목이 다른 덧셈. 진짜 덧셈 구현.
        assertThat(Calc.run("2 + 1")).isEqualTo(3);
    }

    @Test
    @DisplayName("2 + 2 == 4")
    void t3() {
        //마찬가지로 그냥 덧셈.
        assertThat(Calc.run("2 + 2")).isEqualTo(4);
    }

    @Test
    @DisplayName("1000 + 280 == 1280")
    void t4() {
        //좀 자릿수가 큰 덧셈
        assertThat(Calc.run("1000 + 280")).isEqualTo(1280);
    }

    @Test
    @DisplayName("2 - 1 == 1")
    void t5() {
        //뺄셈 구현.
        assertThat(Calc.run("2 - 1")).isEqualTo(1);
    }

    @Test
    @DisplayName("3 - 1 == 2")
    void t6() {
        assertThat(Calc.run("3 - 1")).isEqualTo(2);
    }

    @Test
    @DisplayName("100 - 20 == 80")
    void t7() {
        assertThat(Calc.run("100 - 20")).isEqualTo(80);
    }

    @Test
    @DisplayName("10 + 20 + 30 == 60")
    void t8() {
        //다항식 덧셈 구현.
        assertThat(Calc.run("10 + 20 + 30")).isEqualTo(60);
    }

    @Test
    @DisplayName("10 - 20 + 30 == 20")
    void t9() {
        //덧셈 뺄셈 동시 구현
        assertThat(Calc.run("10 - 20 + 30")).isEqualTo(20);
    }

    @Test
    @DisplayName("10 - 10 - 10 - 10 == -20")
    void t10() {

        assertThat(Calc.run("10 - 10 - 10 - 10")).isEqualTo(-20);
    }

    @Test
    @DisplayName("10 - 10 - 10 - 10 + 10 + 10 - 10 == -10")
    void t11() {
        assertThat(Calc.run("10 - 10 - 10 - 10 + 10 + 10 - 10")).isEqualTo(-10);
    }

    @Test
    @DisplayName("10 * 10 == 100")
    void t12() {
        //곱셈 구현
        assertThat(Calc.run("10 * 10")).isEqualTo(100);
    }

    @Test
    @DisplayName("10 * -10 == -100")
    void t13() {
        //음수와의 곱셈 구현. 이를 위해선 0에 +(-10)을 해주는 과정이 필요하다.
        assertThat(Calc.run("10 * -10")).isEqualTo(-100);
    }

    @Test
    @DisplayName("10 * 10 * 10 == 1000")
    void t14() {
        //다중 곱셈
        assertThat(Calc.run("10 * 10 * 10")).isEqualTo(1000);
    }
    @Test
    @DisplayName("10 + 5 * 2 == 20")
    void t15() {
        //곱셈과 덧셈이 섞인 다항식. 여기서부턴 우선순위가 필요하다.
        assertThat(Calc.run("10 + 5 * 2")).isEqualTo(20);
    }

    @Test
    @DisplayName("20 + 10 + 5 * 2 == 40")
    void t16() {
        assertThat(Calc.run("20 + 10 + 5 * 2")).isEqualTo(40);
    }

    @Test
    @DisplayName("10 * 20 + 10 + 5 * 2 == 220")
    void t17() {
        //같은 우선권을 가졌을 경우 앞에있는 곱셈을 먼제 실행한다.
        assertThat(Calc.run("10 * 20 + 10 + 5 * 2")).isEqualTo(220);
    }

    @Test
    @DisplayName("(10 + 20) == 30")
    void t18() {
        //괄호 계산 구현
        assertThat(Calc.run("(10 + 20)")).isEqualTo(30);
    }

    @Test
    @DisplayName("((10 + 20)) == 30")
    void t19() {

        //이중괄호(쓸모없는 괄호 구현)
        assertThat(Calc.run("((10 + 20))")).isEqualTo(30);
    }

    @Test
    @DisplayName("(((10 + 20))) == 30")
    void t20() {
        //삼중괄호 제거
        assertThat(Calc.run("(((10 + 20)))")).isEqualTo(30);
    }

    @Test
    @DisplayName("(20 + 20) + 20 == 60")
    void t21() {
        //괄호를 통한 우선순위가 필요한 덧셈
        assertThat(Calc.run("(20 + 20) + 20")).isEqualTo(60);
    }

    @Test
    @DisplayName("((20 + 20)) + 20 == 60")
    void t22() {
        assertThat(Calc.run("((20 + 20)) + 20")).isEqualTo(60);
    }

    @Test
    @DisplayName("(10 + 20) * 3 == 90")
    void t23() {
        //괄호와 곱셈이 뒤섞여있어 우선순위를 정해야하는 다항식
        assertThat(Calc.run("(10 + 20) * 3")).isEqualTo(90);
    }

    @Test
    @DisplayName("10 + (10 + 5) == 25")
    void t24() {
        assertThat(Calc.run("10 + (10 + 5)")).isEqualTo(25);
    }

    @Test
    @DisplayName("-(10 + 5) == -15")
    void t25() {
        //괄호에 음수곱=(-1)*을 하는 다항식
        assertThat(Calc.run("-(10 + 5)")).isEqualTo(-15);
    }

    @Test
    @DisplayName("-(8 + 2) * -(7 + 3) + 5 == 105")
    void t26() {
        //음수곱*음수곱을 하는 다항식
        assertThat(Calc.run("-(8 + 2) * -(7 + 3) + 5")).isEqualTo(105);
    }

    @Test
    @DisplayName("5 - (1 + 5) == -1")
    void t27() {
        assertThat(Calc.run("5 - (1 + 5)")).isEqualTo(-1);
    }

    @Test
    @DisplayName("3 * 1 + (1 - (4 * 1 - (1 - 1))) == 0")
    void t28() {
        //삼중괄호 다항식
        assertThat(Calc.run("3 * 1 + (1 - (4 * 1 - (1 - 1)))")).isEqualTo(0);
    }
}