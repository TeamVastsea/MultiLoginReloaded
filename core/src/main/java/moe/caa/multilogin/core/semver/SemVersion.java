package moe.caa.multilogin.core.semver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import moe.caa.multilogin.api.internal.util.ValueUtil;

import java.util.Locale;

/**
 * 语义化版本号处理工具
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class SemVersion {
    private final int major;
    private final int minor;
    private final int patch;
    private final VersionSuffix suffixes;
    private final int suffixesBd;

    /**
     * 解析一个语义化版本号，无法解析时返回 null 而不是抛出异常。
     * <p>
     * 这个方法的输入通常来自远程仓库的 latest 文件，内容不可控（可能带有 CRLF、空行或非法格式），
     * 一旦抛出异常就会中断调用方所在的周期任务，因此这里必须容错。
     *
     * @param version 版本号字符串，例如 {@code 1.0.0} 或 {@code 1.0.0-RC.4}
     * @return 解析后的版本号，无法解析时为 null
     */
    public static SemVersion of(String version) {
        if (ValueUtil.isEmpty(version)) return null;
        version = version.trim();
        if (version.isEmpty()) return null;
        if (version.toLowerCase(Locale.ROOT).startsWith("build_")) return null;

        try {
            // 1.0.0-RC.4
            String[] split = version.split("-");
            if (split.length > 2) return null;

            String[] mmp = split[0].split("\\.");
            if (mmp.length != 3) return null;

            int major = Integer.parseInt(mmp[0]);
            int minor = Integer.parseInt(mmp[1]);
            int patch = Integer.parseInt(mmp[2]);

            if (split.length == 1) {
                return new SemVersion(major, minor, patch, VersionSuffix.NONE, -1);
            }

            String[] suffix = split[1].split("\\.");
            if (suffix.length != 2) return null;
            return new SemVersion(major, minor, patch,
                    VersionSuffix.valueOf(suffix[0].toUpperCase(Locale.ROOT)), Integer.parseInt(suffix[1]));
        } catch (IllegalArgumentException e) {
            // NumberFormatException 和 VersionSuffix.valueOf 的非法枚举名都属于 IllegalArgumentException
            return null;
        }
    }

    @Override
    public String toString() {
        if (suffixes == VersionSuffix.NONE) return String.format("%d.%d.%d", major, minor, patch);
        return String.format("%d.%d.%d-%s.%d", major, minor, patch, suffixes.name(), suffixesBd);
    }

    /**
     * 判断给定的版本是否比当前版本更新，即是否需要升级到给定版本。
     *
     * @param version 待比较的版本
     * @return 给定版本比当前版本更新时为 true
     */
    public boolean needUpgrade(SemVersion version) {
        // 不从稳定通道回退到预发布通道。
        // 比如当前是 1.0.0，那么 1.1.0-RC.1 不推荐更新。
        if (version.suffixes.mj < suffixes.mj) return false;
        return compare(this, version) < 0;
    }

    /**
     * 只比较主版本号、次版本号和修订号，忽略预发布后缀。
     *
     * @param version 待比较的版本
     * @return 给定版本的三段版本号比当前版本更新时为 true
     */
    public boolean needUpgradeIgnoreSuffixes(SemVersion version) {
        return compareNumber(this, version) < 0;
    }

    /**
     * 比较三段版本号，a 小于 b 时返回负数
     */
    private static int compareNumber(SemVersion a, SemVersion b) {
        if (a.major != b.major) return Integer.compare(a.major, b.major);
        if (a.minor != b.minor) return Integer.compare(a.minor, b.minor);
        return Integer.compare(a.patch, b.patch);
    }

    /**
     * 完整比较两个版本号（含预发布后缀），a 小于 b 时返回负数
     */
    private static int compare(SemVersion a, SemVersion b) {
        int number = compareNumber(a, b);
        if (number != 0) return number;
        if (a.suffixes.mj != b.suffixes.mj) return Integer.compare(a.suffixes.mj, b.suffixes.mj);
        return Integer.compare(a.suffixesBd, b.suffixesBd);
    }

    enum VersionSuffix {
        NONE(3),
        RC(2),
        BETA(1),
        ALPHA(0);

        private final int mj;

        VersionSuffix(int mj) {
            this.mj = mj;
        }
    }
}
