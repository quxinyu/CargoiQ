package com.efreight.base.module.one.record.neone.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UUIDTools 简单测试类（不依赖 JUnit）
 * 可以直接运行 main 方法进行测试
 */
public class UUIDToolsSimpleTest {

    public static void main(String[] args) {
        System.out.println("=== UUIDTools.generateSimpleUUID() 测试开始 ===\n");

        UUIDToolsSimpleTest tester = new UUIDToolsSimpleTest();

        try {
            // 1. 基本功能测试
            tester.testBasicFunction();

            // 2. 并发测试
            tester.testConcurrency();

            // 3. 压力测试
            tester.testStress();

        } catch (Exception e) {
            System.err.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== 所有测试完成 ===");
    }

    /**
     * 基本功能测试
     */
    public void testBasicFunction() {
        System.out.println("1. 基本功能测试:");

        String uuid = UUIDTools.generateSimpleUUID();
        System.out.println("   生成的UUID: " + uuid);
        System.out.println("   UUID长度: " + uuid.length());

        // 验证格式
        boolean isValid = uuid.length() == 36 &&
                         uuid.contains("-") &&
                         uuid.split("-").length == 5;

        System.out.println("   格式验证: " + (isValid ? "✅ 通过" : "❌ 失败"));
        System.out.println();
    }

    /**
     * 并发测试（每秒20个并发，持续3秒）
     */
    public void testConcurrency() throws InterruptedException {
        System.out.println("2. 并发测试（每秒20个并发，持续3秒）:");

        int concurrentPerSecond = 20;
        int testSeconds = 3;
        int totalRequests = concurrentPerSecond * testSeconds;

        Set<String> uuids = Collections.synchronizedSet(new HashSet<>());
        AtomicInteger duplicateCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (int second = 0; second < testSeconds; second++) {
            System.out.println("   第 " + (second + 1) + " 秒测试开始...");

            CountDownLatch latch = new CountDownLatch(concurrentPerSecond);

            for (int i = 0; i < concurrentPerSecond; i++) {
                final int requestId = second * concurrentPerSecond + i;

                executor.submit(() -> {
                    try {
                        String uuid = UUIDTools.generateSimpleUUID();

                        if (uuids.add(uuid)) {
                            successCount.incrementAndGet();
                        } else {
                            duplicateCount.incrementAndGet();
                            System.err.println("   ❌ 发现重复UUID #" + (requestId + 1) + ": " + uuid);
                        }

                    } catch (Exception e) {
                        System.err.println("   ❌ 生成异常 #" + (requestId + 1) + ": " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(2, TimeUnit.SECONDS);
            Thread.sleep(500); // 间隔0.5秒
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // 输出结果
        System.out.println("   总请求数: " + totalRequests);
        System.out.println("   成功生成: " + successCount.get());
        System.out.println("   重复数量: " + duplicateCount.get());
        System.out.println("   重复率: " + String.format("%.2f%%", duplicateCount.get() * 100.0 / totalRequests));

        if (duplicateCount.get() == 0) {
            System.out.println("   ✅ 并发测试通过！未发现重复UUID");
        } else {
            System.out.println("   ❌ 并发测试失败！发现 " + duplicateCount.get() + " 个重复UUID");
        }
        System.out.println();
    }

    /**
     * 压力测试（生成1000个UUID）
     */
    public void testStress() {
        System.out.println("3. 压力测试（生成1000个UUID）:");

        Set<String> uuids = new HashSet<>();
        int duplicateCount = 0;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            String uuid = UUIDTools.generateSimpleUUID();
            if (!uuids.add(uuid)) {
                duplicateCount++;
                System.err.println("   ❌ 压力测试发现重复UUID #" + (i + 1) + ": " + uuid);
            }

            if ((i + 1) % 100 == 0) {
                System.out.println("   已生成 " + (i + 1) + " 个UUID...");
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("   总生成数: 1000");
        System.out.println("   唯一数量: " + (1000 - duplicateCount));
        System.out.println("   重复数量: " + duplicateCount);
        System.out.println("   总耗时: " + duration + " ms");
        System.out.println("   平均速度: " + String.format("%.2f", 1000.0 / duration) + " UUID/ms");

        if (duplicateCount == 0) {
            System.out.println("   ✅ 压力测试通过！未发现重复UUID");
        } else {
            System.out.println("   ❌ 压力测试失败！发现 " + duplicateCount + " 个重复UUID");
        }
    }
}