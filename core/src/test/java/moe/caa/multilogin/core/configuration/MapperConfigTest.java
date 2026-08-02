package moe.caa.multilogin.core.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MapperConfig} 的单元测试。
 * <p>
 * 这个类维护 ChatSession 数据包在各协议版本下的包 ID 映射，
 * 内部的 TreeMap 覆写了 put 方法来实现「同一个包 ID 只保留最低协议版本」的去重逻辑，
 * 映射错误会直接导致玩家聊天异常或被踢出，因此需要覆盖。
 */
class MapperConfigTest {

    @Test
    @DisplayName("内置默认映射覆盖 1.19.3 至 1.21.6")
    void defaultMappingPresent() {
        MapperConfig config = new MapperConfig(newDataFolder());
        Map<Integer, Integer> mapping = config.getPacketMapping();

        assertEquals(0x20, mapping.get(761));
        assertEquals(0x06, mapping.get(762));
        assertEquals(0x07, mapping.get(765));
        assertEquals(0x08, mapping.get(768));
        assertEquals(0x09, mapping.get(771));
    }

    @Test
    @DisplayName("低于 761 的协议版本不会被记录")
    void ignoreProtocolBelowMinimum() {
        MapperConfig config = new MapperConfig(newDataFolder());
        Map<Integer, Integer> mapping = config.getPacketMapping();

        mapping.put(760, 0x11);
        mapping.put(340, 0x12);

        assertFalse(mapping.containsKey(760));
        assertFalse(mapping.containsKey(340));
    }

    @Test
    @DisplayName("同一个包 ID 只保留最低的协议版本")
    void keepLowestProtocolForSamePacketId() {
        MapperConfig config = new MapperConfig(newDataFolder());
        Map<Integer, Integer> mapping = config.getPacketMapping();

        // 0x09 已经属于 771，再用更高的协议版本注册同一个包 ID 时不应覆盖
        mapping.put(772, 0x09);
        assertTrue(mapping.containsKey(771));
        assertFalse(mapping.containsKey(772));

        // 用更低的协议版本注册同一个包 ID 时，应当迁移到更低的版本上
        mapping.put(770, 0x09);
        assertFalse(mapping.containsKey(771));
        assertEquals(0x09, mapping.get(770));
    }

    @Test
    @DisplayName("新的包 ID 会被正常记录")
    void newPacketIdIsRecorded() {
        MapperConfig config = new MapperConfig(newDataFolder());
        Map<Integer, Integer> mapping = config.getPacketMapping();

        mapping.put(773, 0x0A);
        assertEquals(0x0A, mapping.get(773));
    }

    @Test
    @DisplayName("保存后重新读取可以还原映射")
    void saveAndReloadRoundTrip(@TempDir Path tempDir) throws Exception {
        File dataFolder = tempDir.toFile();

        MapperConfig config = new MapperConfig(dataFolder);
        config.getPacketMapping().put(773, 0x0A);
        config.save();

        File mapperFile = new File(dataFolder, "mapper.yml");
        assertTrue(mapperFile.isFile(), "save() 没有写出 mapper.yml");

        String content = Files.readString(mapperFile.toPath());
        // 包 ID 以 16 进制形式写出，便于和 Minecraft 协议文档对照
        assertTrue(content.contains("0x0A"), "包 ID 没有以 16 进制写出: " + content);

        MapperConfig reloaded = new MapperConfig(dataFolder);
        reloaded.reload();
        assertEquals(0x0A, reloaded.getPacketMapping().get(773));
        assertEquals(0x20, reloaded.getPacketMapping().get(761));
    }

    @Test
    @DisplayName("读取不存在的文件时保留内置默认值")
    void reloadMissingFileKeepsDefaults() {
        MapperConfig config = new MapperConfig(newDataFolder());
        assertDoesNotThrow(config::reload);
        assertEquals(0x20, config.getPacketMapping().get(761));
    }

    /**
     * 返回一个不存在的目录，用于只操作内存映射、不触碰磁盘的测试
     */
    private File newDataFolder() {
        return new File(System.getProperty("java.io.tmpdir"), "multilogin-mapper-test-not-exists");
    }
}
