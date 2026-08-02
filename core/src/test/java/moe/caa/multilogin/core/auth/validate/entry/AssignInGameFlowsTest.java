package moe.caa.multilogin.core.auth.validate.entry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AssignInGameFlows#incrementString(String)} 的单元测试。
 * <p>
 * 这个方法负责在游戏内用户名冲突时生成下一个候选名，
 * 结果会直接写入数据库并作为玩家的游戏内名称，因此边界行为必须稳定。
 */
class AssignInGameFlowsTest {

    @ParameterizedTest
    @DisplayName("末尾没有数字时追加 1")
    @CsvSource({
            "Steven, Steven1",
            "abc,    abc1",
            "A,      A1",
            "_,      _1"
    })
    void appendOneWhenNoTrailingDigit(String source, String expected) {
        assertEquals(expected, AssignInGameFlows.incrementString(source));
    }

    @ParameterizedTest
    @DisplayName("末尾有数字时递增")
    @CsvSource({
            "Steven1, Steven2",
            "Steven8, Steven9",
            "abc0,    abc1"
    })
    void incrementTrailingDigit(String source, String expected) {
        assertEquals(expected, AssignInGameFlows.incrementString(source));
    }

    @ParameterizedTest
    @DisplayName("数字进位")
    @CsvSource({
            "Steven9,  Steven10",
            "Steven19, Steven20",
            "Steven99, Steven100",
            "abc999,   abc1000"
    })
    void carryOver(String source, String expected) {
        assertEquals(expected, AssignInGameFlows.incrementString(source));
    }

    @Test
    @DisplayName("空字符串返回 1")
    void emptyStringReturnsOne() {
        assertEquals("1", AssignInGameFlows.incrementString(""));
    }

    @Test
    @DisplayName("全是 9 时进位到新的一位")
    void allNines() {
        assertEquals("10", AssignInGameFlows.incrementString("9"));
        assertEquals("100", AssignInGameFlows.incrementString("99"));
    }

    @Test
    @DisplayName("连续递增不会产生重复结果")
    void repeatedIncrementIsMonotonic() {
        String name = "Steven";
        String previous = name;
        for (int i = 0; i < 30; i++) {
            String next = AssignInGameFlows.incrementString(previous);
            assertTrue(!next.equals(previous), "第 " + i + " 次递增产生了重复结果: " + next);
            previous = next;
        }
        assertEquals("Steven30", previous);
    }

    @Test
    @DisplayName("生成的名称仍然只含合法字符")
    void resultKeepsLegalCharacters() {
        String name = "Steven";
        for (int i = 0; i < 200; i++) {
            name = AssignInGameFlows.incrementString(name);
            assertTrue(name.matches("^[0-9a-zA-Z_]+$"), "生成了非法名称: " + name);
        }
    }
}
