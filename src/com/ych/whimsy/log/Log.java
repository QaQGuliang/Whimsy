package src.com.ych.whimsy.log;

public class Log {
    /**
     * 红色输出
     *
     * @param out 输出内容
     */
    public static void outRedGreenLn (Object out) {
        System.out.println("\033[31;46m" + out + "\033[m");
    }

    /**
     * 红色输出
     *
     * @param out 输出内容
     */
    public static void outRedLn (Object out) {
        System.out.println("\033[31m" + out + "\033[30m");
    }

    /**
     * 蓝色输出
     *
     * @param out 输出内容
     */
    public static void outBlueLn (Object out) {
        System.out.println("\033[34m" + out + "\033[30m");
    }

    /**
     * 红色输出
     *
     * @param out 输出内容
     */
    public static void outRedGreen (Object out) {
        System.out.print("\033[31;46m" + out + "\033[30m");
    }

    /**
     * 红色输出
     *
     * @param out 输出内容
     */
    public static void outRed (Object out) {
        System.out.print("\033[31m" + out + "\033[30m");
    }

    /**
     * 蓝色输出
     *
     * @param out 输出内容
     */
    public static void outBlue (Object out) {
        System.out.print("\033[34m" + out + "\033[30m");
    }

    /**
     * 紫色输出
     *
     * @param out 输出内容
     */
    public static void outPurpleLn (Object out) {
        System.out.println("\033[35m" + out + "\033[30m");

    }

    /**
     * 紫色输出
     *
     * @param out 输出内容
     */
    public static void outPurple (Object out) {
        System.out.print("\033[35m" + out + "\033[30m");

    }
}
