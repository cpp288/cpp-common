package com.cpp.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 前提条件工具类
 *
 * @author chenjian
 * @date 2019-01-11 15:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PreconditionUtils {

    /**
     * 检查参数
     *
     * @param expression 条件
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 检查参数
     *
     * @param expression   条件
     * @param errorMessage 错误信息
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
