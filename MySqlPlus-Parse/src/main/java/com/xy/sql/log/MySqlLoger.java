package com.xy.sql.log;

/**
 * 日志
 */
public class MySqlLoger {
    /**
     * 记录日志
     *
     * @param message
     */
    public static void error(String message) {
        //TODO 分等级
        log(message, null);
    }

    /**
     * 记录日志
     *
     * @param message
     */
    public static void info(String message) {
        //TODO 分等级
        log(message, null);
    }

    /**
     * 记录日志
     *
     * @param message
     */
    public static void debug(String message) {
        //TODO 分等级
        log(message, null);
    }

    /**
     * 记录日志
     *
     * @param ex
     */
    public static void error(Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param ex
     */
    public static void info(Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param ex
     */
    public static void debug(Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param message
     * @param ex
     */
    public static void error(String message, Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param message
     * @param ex
     */
    public static void info(String message, Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param message
     * @param ex
     */
    public static void debug(String message, Exception ex) {
        //TODO 分等级
        log(null, ex);
    }

    /**
     * 记录日志
     *
     * @param message
     * @param ex
     */
    private static void log(String message, Exception ex) {
        if (message != null) {
            System.out.println(message);
        }
        if (ex != null) {
            ex.printStackTrace();
        }
    }
}
