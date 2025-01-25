package com.flechazo.StareTillTheyGrowRb.Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 提供一个通用的日志记录器，用于在整个Mod中记录日志信息。
 * <p>
 * 该类提供了一个静态的Logger实例，可以通过 `ForgeLogger.LOGGER` 访问，
 * 从而方便地在Mod的不同部分记录调试、信息、警告和错误日志。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class ForgeLogger {
    /**
     * 静态的Logger实例，用于记录日志信息。
     */
    public static final Logger LOGGER = LogManager.getLogger();

    // 私有构造函数，防止实例化该工具类
    private ForgeLogger() {
        throw new IllegalStateException("Utility class");
    }
}