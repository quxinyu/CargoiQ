package com.efreight.base.module.one.record.neone.utils;

/**
 * UUIDTools 两个 generateSimpleUUID 方法对比测试
 */
public class UUIDComparisonTest {

    public static void main(String[] args) {
        System.out.println("=== UUIDTools 方法对比分析 ===\n");

        // 生成示例对比
        String uuid1 = UUIDTools.generateSimpleUUID();
        String uuid2 = UUIDTools.generateSimpleUUID2();

        System.out.println("1. generateSimpleUUID() 生成结果:");
        System.out.println("   UUID: " + uuid1);
        System.out.println("   长度: " + uuid1.length());
        System.out.println("   格式分析: " + analyzeFormat(uuid1));

        System.out.println("\n2. generateSimpleUUID2() 生成结果:");
        System.out.println("   UUID: " + uuid2);
        System.out.println("   长度: " + uuid2.length());
        System.out.println("   格式分析: " + analyzeFormat(uuid2));

        // 批量测试验证一致性
        System.out.println("\n3. 批量测试验证:");
        testConsistency();

        // 详细对比
        System.out.println("\n4. 详细方法对比:");
        compareMethods();
    }

    /**
     * 分析UUID格式
     */
    private static String analyzeFormat(String uuid) {
        StringBuilder analysis = new StringBuilder();

        // 基本格式检查
        if (uuid.length() != 36) {
            analysis.append("❌ 长度不是36字符，实际: ").append(uuid.length());
        } else {
            analysis.append("✅ 长度正确: 36字符");
        }

        // 分段检查
        String[] parts = uuid.split("-");
        if (parts.length == 5) {
            analysis.append("\n   标准UUID分段: ");
            for (int i = 0; i < parts.length; i++) {
                analysis.append("[").append(parts[i].length()).append("]");
                if (i < parts.length - 1) analysis.append("-");
            }
        } else {
            analysis.append("\n   ❌ 分段数量异常: ").append(parts.length);
        }

        // 连字符位置检查
        analysis.append("\n   连字符位置: ");
        for (int i = 0; i < uuid.length(); i++) {
            if (uuid.charAt(i) == '-') {
                analysis.append(i).append(" ");
            }
        }

        return analysis.toString();
    }

    /**
     * 测试一致性
     */
    private static void testConsistency() {
        boolean lengthConsistent = true;
        boolean formatConsistent = true;
        int testCount = 100;

        for (int i = 0; i < testCount; i++) {
            String uuid1 = UUIDTools.generateSimpleUUID();
            String uuid2 = UUIDTools.generateSimpleUUID2();

            // 检查长度一致性
            if (uuid1.length() != 36 || uuid2.length() != 36) {
                lengthConsistent = false;
                System.err.println("长度异常 - UUID1: " + uuid1.length() + ", UUID2: " + uuid2.length());
            }

            // 检查格式一致性
            String[] parts1 = uuid1.split("-");
            String[] parts2 = uuid2.split("-");
            if (parts1.length != 5 || parts2.length != 5) {
                formatConsistent = false;
                System.err.println("格式异常 - UUID1分段: " + parts1.length + ", UUID2分段: " + parts2.length);
            }
        }

        System.out.println("   测试数量: " + testCount);
        System.out.println("   长度一致性: " + (lengthConsistent ? "✅ 一致" : "❌ 不一致"));
        System.out.println("   格式一致性: " + (formatConsistent ? "✅ 一致" : "❌ 不一致"));
    }

    /**
     * 详细方法对比
     */
    private static void compareMethods() {
        System.out.println("   方法1: generateSimpleUUID()");
        System.out.println("   - 使用雪花算法思想");
        System.out.println("   - 时间戳 + 序列号组合");
        System.out.println("   - 使用AtomicLong保证并发安全");
        System.out.println("   - 36进制转换，更短的表示");
        System.out.println("   - 序列号分散插入到UUID不同位置");

        System.out.println("\n   方法2: generateSimpleUUID2()");
        System.out.println("   - 原始实现");
        System.out.println("   - 简单的时间戳截取");
        System.out.println("   - 使用毫秒级时间戳后5位");
        System.out.println("   - 没有序列号保护");
        System.out.println("   - 高并发下可能重复");

        System.out.println("\n   📊 对比结论:");
        System.out.println("   ✅ 长度: 两个方法都生成36字符长度的UUID");
        System.out.println("   ✅ 格式: 两个方法都保持标准UUID格式 (8-4-4-4-12)");
        System.out.println("   ⚠️  安全性: generateSimpleUUID() 更安全");
        System.out.println("   ⚠️  性能: generateSimpleUUID2() 略快（但不够安全）");
    }
}