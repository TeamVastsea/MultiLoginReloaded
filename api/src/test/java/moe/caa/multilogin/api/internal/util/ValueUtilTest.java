package moe.caa.multilogin.api.internal.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ValueUtil} 的单元测试。
 * <p>
 * 这些方法被用于消息变量替换、UUID 与字节数组互转以及配置读取，
 * 是插件里被调用最频繁的一批纯函数。
 */
class ValueUtilTest {

    @Test
    @DisplayName("按变量名替换占位符")
    void transPapiByName() {
        String result = ValueUtil.transPapi("你好 {name}，你的 UUID 是 {uuid}。",
                new Pair<>("name", "Steven"),
                new Pair<>("uuid", "abc"));
        assertEquals("你好 Steven，你的 UUID 是 abc。", result);
    }

    @Test
    @DisplayName("按下标替换占位符")
    void transPapiByIndex() {
        String result = ValueUtil.transPapi("{0} 和 {1}",
                new Pair<>("a", "第一"),
                new Pair<>("b", "第二"));
        assertEquals("第一 和 第二", result);
    }

    @Test
    @DisplayName("同一个占位符出现多次时全部替换")
    void transPapiReplacesAllOccurrences() {
        String result = ValueUtil.transPapi("{name} {name} {name}", new Pair<>("name", "X"));
        assertEquals("X X X", result);
    }

    @Test
    @DisplayName("未提供的占位符原样保留")
    void transPapiKeepsUnknownPlaceholder() {
        String result = ValueUtil.transPapi("{name} {unknown}", new Pair<>("name", "X"));
        assertEquals("X {unknown}", result);
    }

    @Test
    @DisplayName("不传变量时原样返回")
    void transPapiWithoutPairs() {
        assertEquals("没有变量", ValueUtil.transPapi("没有变量"));
    }

    @Test
    @DisplayName("List 形式的变量替换与可变参数一致")
    void transPapiListOverload() {
        List<Pair<?, ?>> pairs = Arrays.asList(new Pair<>("name", "Steven"), new Pair<>("id", 7));
        assertEquals("Steven-7", ValueUtil.transPapi("{name}-{id}", pairs));
    }

    @Test
    @DisplayName("UUID 与字节数组可以无损互转")
    void uuidBytesRoundTrip() {
        for (int i = 0; i < 50; i++) {
            UUID uuid = UUID.randomUUID();
            byte[] bytes = ValueUtil.uuidToBytes(uuid);
            assertEquals(16, bytes.length);
            assertEquals(uuid, ValueUtil.bytesToUuid(bytes));
        }
    }

    @Test
    @DisplayName("长度不为 16 的字节数组转 UUID 返回 null")
    void bytesToUuidRejectsWrongLength() {
        assertNull(ValueUtil.bytesToUuid(new byte[0]));
        assertNull(ValueUtil.bytesToUuid(new byte[15]));
        assertNull(ValueUtil.bytesToUuid(new byte[17]));
    }

    @Test
    @DisplayName("解析带短横线和不带短横线的 UUID")
    void getUuidOrNull() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, ValueUtil.getUuidOrNull(uuid.toString()));
        assertEquals(uuid, ValueUtil.getUuidOrNull(uuid.toString().replace("-", "")));
    }

    @Test
    @DisplayName("非法 UUID 字符串返回 null")
    void getUuidOrNullRejectsInvalid() {
        assertNull(ValueUtil.getUuidOrNull("not-a-uuid"));
        assertNull(ValueUtil.getUuidOrNull(""));
    }

    @Test
    @DisplayName("isEmpty 判空")
    void isEmpty() {
        assertTrue(ValueUtil.isEmpty(null));
        assertTrue(ValueUtil.isEmpty(""));
        assertFalse(ValueUtil.isEmpty(" "));
        assertFalse(ValueUtil.isEmpty("a"));
    }

    @Test
    @DisplayName("join 使用不同的最后一个分隔符")
    void join() {
        assertEquals("", ValueUtil.join(", ", " and ", Collections.emptyList()));
        assertEquals("A", ValueUtil.join(", ", " and ", Collections.singletonList("A")));
        assertEquals("A and B", ValueUtil.join(", ", " and ", Arrays.asList("A", "B")));
        assertEquals("A, B and C", ValueUtil.join(", ", " and ", Arrays.asList("A", "B", "C")));
    }

    @Test
    @DisplayName("sha256 长度固定且结果稳定")
    void sha256() throws Exception {
        byte[] first = ValueUtil.sha256("https://textures.minecraft.net/texture/abc");
        byte[] second = ValueUtil.sha256("https://textures.minecraft.net/texture/abc");
        assertEquals(32, first.length);
        assertArrayEquals(first, second);
        assertFalse(Arrays.equals(first, ValueUtil.sha256("其他内容")));
    }

    @Test
    @DisplayName("xuid 转 UUID 高位为 0")
    void xuidToUUID() {
        UUID uuid = ValueUtil.xuidToUUID("1234567890");
        assertEquals(0, uuid.getMostSignificantBits());
        assertEquals(1234567890L, uuid.getLeastSignificantBits());
    }

    @Test
    @DisplayName("连接码为 6 位数字")
    void generateLinkCode() {
        for (int i = 0; i < 100; i++) {
            String code = ValueUtil.generateLinkCode();
            assertEquals(6, code.length());
            assertTrue(code.matches("^[0-9]{6}$"), "生成了非法连接码: " + code);
        }
    }
}
