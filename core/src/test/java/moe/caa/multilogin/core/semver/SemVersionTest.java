package moe.caa.multilogin.core.semver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SemVersion} 的单元测试。
 * <p>
 * 重点覆盖历史上出现过的缺陷：
 * <ul>
 *     <li>needUpgrade 曾经要求 patch 必须严格递增，导致次版本号和主版本号的升级完全无法被发现。</li>
 *     <li>of 曾经会对非法输入抛出异常，导致调用它的周期性更新检查任务被永久终止。</li>
 * </ul>
 */
class SemVersionTest {

    @Test
    @DisplayName("解析不带后缀的版本号")
    void parsePlainVersion() {
        SemVersion version = SemVersion.of("1.2.3");
        assertNotNull(version);
        assertEquals(1, version.getMajor());
        assertEquals(2, version.getMinor());
        assertEquals(3, version.getPatch());
        assertEquals("1.2.3", version.toString());
    }

    @Test
    @DisplayName("解析带预发布后缀的版本号")
    void parseSuffixedVersion() {
        SemVersion version = SemVersion.of("1.0.0-RC.4");
        assertNotNull(version);
        assertEquals(1, version.getMajor());
        assertEquals(0, version.getMinor());
        assertEquals(0, version.getPatch());
        assertEquals(4, version.getSuffixesBd());
        assertEquals("1.0.0-RC.4", version.toString());
    }

    @ParameterizedTest
    @DisplayName("非法输入返回 null 而不是抛出异常")
    @ValueSource(strings = {
            "",
            "   ",
            "1.2",
            "1.2.3.4",
            "abc",
            "1.2.x",
            "1.2.3-RC",
            "1.2.3-RC.x",
            "1.2.3-UNKNOWN.1",
            "1.2.3-RC.1-RC.2",
            "Build_031d31d",
            "build_abcdef"
    })
    void invalidInputReturnsNull(String input) {
        assertDoesNotThrow(() -> SemVersion.of(input));
        assertNull(SemVersion.of(input));
    }

    @Test
    @DisplayName("远端 latest 文件带行尾回车时仍能正确解析")
    void parseVersionWithTrailingCarriageReturn() {
        // 修复前这里会抛出 NumberFormatException，导致更新检查的周期任务被永久终止
        SemVersion version = assertDoesNotThrow(() -> SemVersion.of("0.6.12\r"));
        assertNotNull(version);
        assertEquals(SemVersion.of("0.6.12"), version);
    }

    @Test
    @DisplayName("null 输入返回 null")
    void nullInputReturnsNull() {
        assertNull(SemVersion.of(null));
    }

    @Test
    @DisplayName("解析时容忍首尾空白和行尾回车")
    void parseTrimsWhitespace() {
        assertEquals(SemVersion.of("1.2.3"), SemVersion.of("  1.2.3\n"));
        assertEquals(SemVersion.of("1.2.3"), SemVersion.of("1.2.3\r\n"));
    }

    @ParameterizedTest
    @DisplayName("修订号递增时需要升级")
    @CsvSource({
            "0.6.11, 0.6.12, true",
            "0.6.11, 0.6.11, false",
            "0.6.11, 0.6.10, false",
            "1.0.0,  1.0.1,  true"
    })
    void patchUpgrade(String current, String remote, boolean expected) {
        assertUpgrade(current, remote, expected);
    }

    @ParameterizedTest
    @DisplayName("次版本号和主版本号递增时也需要升级")
    @CsvSource({
            "0.6.11, 0.7.0,  true",
            "0.6.11, 0.7.1,  true",
            "0.6.11, 1.0.0,  true",
            "1.2.3,  2.0.0,  true",
            "1.2.3,  1.3.0,  true",
            "0.7.0,  0.6.11, false",
            "1.0.0,  0.9.9,  false",
            "2.0.0,  1.9.9,  false"
    })
    void minorAndMajorUpgrade(String current, String remote, boolean expected) {
        assertUpgrade(current, remote, expected);
    }

    @ParameterizedTest
    @DisplayName("预发布通道的升降级判断")
    @CsvSource({
            // 同版本号内后缀等级提升需要升级
            "1.0.0-BETA.1, 1.0.0-RC.1,   true",
            "1.0.0-BETA.1, 1.0.0-BETA.2, true",
            "1.0.0-RC.1,   1.0.0,        true",
            // 不从稳定版回退到预发布版
            "1.0.0,        1.1.0-RC.1,   false",
            "1.0.0,        1.0.0-RC.1,   false",
            // 后缀等级相同时按版本号比较
            "1.0.0-RC.1,   1.1.0-RC.1,   true",
            "1.1.0-RC.1,   1.0.0-RC.1,   false"
    })
    void suffixUpgrade(String current, String remote, boolean expected) {
        assertUpgrade(current, remote, expected);
    }

    @Test
    @DisplayName("needUpgradeIgnoreSuffixes 只比较三段版本号")
    void ignoreSuffixes() {
        SemVersion current = SemVersion.of("1.0.0");
        assertNotNull(current);
        assertTrue(current.needUpgradeIgnoreSuffixes(SemVersion.of("1.0.1")));
        assertTrue(current.needUpgradeIgnoreSuffixes(SemVersion.of("1.1.0")));
        assertTrue(current.needUpgradeIgnoreSuffixes(SemVersion.of("2.0.0")));
        // 后缀被忽略，所以低等级后缀的更高版本号仍然算更新
        assertTrue(current.needUpgradeIgnoreSuffixes(SemVersion.of("1.1.0-RC.1")));
        assertFalse(current.needUpgradeIgnoreSuffixes(SemVersion.of("1.0.0")));
        assertFalse(current.needUpgradeIgnoreSuffixes(SemVersion.of("0.9.9")));
    }

    @Test
    @DisplayName("equals 与 hashCode 按值比较")
    void equality() {
        assertEquals(SemVersion.of("1.2.3"), SemVersion.of("1.2.3"));
        assertEquals(SemVersion.of("1.2.3").hashCode(), SemVersion.of("1.2.3").hashCode());
        assertNotEquals(SemVersion.of("1.2.3"), SemVersion.of("1.2.4"));
        assertNotEquals(SemVersion.of("1.0.0"), SemVersion.of("1.0.0-RC.1"));
    }

    private void assertUpgrade(String current, String remote, boolean expected) {
        SemVersion currentVersion = SemVersion.of(current);
        SemVersion remoteVersion = SemVersion.of(remote);
        assertNotNull(currentVersion, "无法解析当前版本 " + current);
        assertNotNull(remoteVersion, "无法解析远端版本 " + remote);
        assertEquals(expected, currentVersion.needUpgrade(remoteVersion),
                String.format("当前 %s，远端 %s，期望 needUpgrade=%s", current, remote, expected));
    }
}
