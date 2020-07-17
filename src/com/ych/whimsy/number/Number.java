package com.ych.whimsy.number;

/**
 * 2020年1月5日下午7:10:40<br>
 * 
 * @version V-2.0
 * @author QAQ
 *         <h3>简介</h3>
 *         <h3>该类是一个对数字操作的类，目前该类有一个判断一个字符串的组成是否是数字的方法</h3>
 */
public final class Number {

	/**
	 * 该类不允许有实例所以该类的构造为空
	 */
	private Number() {
	}

	/**
	 * 该方法是一个判断特定字符串的元素组成是否是正整数、整、,负数、负整数。
	 * 
	 * @param message 特定的字符串，判断该字符串组成是否是数
	 * @param flag    该标识表示判断模式:
	 *                <p>
	 *                flag：为2时表示只判断字符串组成是否是整数。<br>
	 *                flag：为1的时候标识只判断字符串的组成是否是正整数。 <br>
	 *                flag：为0时表示只判断字符串组成是否是数即可。 <br>
	 *                flag：为-1时表示判断字符串组成是负整数。<br>
	 *                flag：为-2时表示判断字符串组成是负数即可。
	 * @return 返回类型是boolean类型，根据判断模式进行判断，符合每种模式要求<br>
	 *         后返回true反之false
	 */
	public static boolean isNumber(String message, int flag) {
		// 判断字符串是否为空
		if (message != null && !message.equals("")) {
			if (flag >= -2 && flag <= 2) {
				// 选择判断条件
				switch (flag) {
				case -2:// 判断是否是负数
					return isNegative(message, flag);
				case -1:// 判断是否是负整数
					return isNegative(message, flag);
				case 0:// 判断是否是数
					return isNegative(message, flag) || isPositive(message, flag) ? true : false;
				case 1:// 判断是是否时正整数
					return isPositive(message, flag);
				case 2:// 判断是否是整数
					return isPositive(message, flag);
				}
			} else
				System.out.println("判断模式出错 ! ! !");
		} else
			System.out.println("被判断的字符串是空的! ! ! !");

		return false;
	}

	/**
	 * 这个方法时isNumber(String message, int flag);的工具方法，这个方法用于作为<br>
	 * isNumber(String message, int flag);方法的返回值。<br>
	 * 该方法主要用于判断传进来的字符串是否是正方向的整数或小数。
	 * <p>
	 * 我们首先把字符串message转成一个char类型的数组，然后分别判断数组 的<br>
	 * 每个元素是否是数字，是否是小数点然后把他们统计起来，然后把统计起来<br>
	 * 的数据交给辅助方法returnValue(int nums, int points, int mesggLegth, int flag)<br>
	 * 处理。
	 * 
	 * @param message 外部传进来的，被判断处理的字符串。
	 * @param flag    判断模式。
	 * @return 返回值是一个boolean类型的值<br>
	 *         该返回值是returnValue(int nums, int points, int mesggLegth, int flag);<br>
	 *         方法的返回值。
	 * 
	 */
	private static boolean isPositive(String message, int flag) {
		// 是数字的个数
		int nums = 0;
		// 小数点的个数
		int points = 0;
		// 讲传进来的字符串转换换成它对应元素组成的字符数组
		char[] numbers = message.toCharArray();
		// 遍历数字字符数组
		for (char num : numbers) {
			// 判断该数组的每一位元素是否是数字
			if (num >= 48 && num <= 57) {
				// 如果是数字,数字(nums)的数量加1
				nums++;
			}
			// 判断该数组的每一位元素是否是"."
			if (num == 46) {
				// 如果是点(points)的数量加1
				points++;
			}
		}
		return returnValue(nums, points, message.length(), flag);
	}

	/**
	 * 该方法是isPositive(String message, int flag);方法的工具方法。<br>
	 * 当前方法用于作为isPositive(String message, int flag);方法的返回值。
	 * 
	 * @param nums       数字的数量
	 * @param points     小数点的数量
	 * @param mesggLegth 字符串的长度
	 * @param flag       判断模式
	 * @return 返回值是一个boolean类型的值:<br>
	 *         当判断模式flag为1或者-1时，这个时候说明数字的数量nums和传进 来的 字<br>
	 *         符串的元素个数mesgLength相同，这个时候返回nums==mesgLength的值。
	 *         <p>
	 *         当判断模式flag为-2、2、0时，这个时候说明只要合法的数字即可；是一个<br>
	 *         合法的数字需要具备小数的点的个数points小于等于1，然后小数点的个数加<br>
	 *         数字的个数nums等于字符串的长度，如果符合这个条件返回true反之false。
	 */
	private static boolean returnValue(int nums, int points, int mesggLegth, int flag) {
		// 判断查询条件
		if (flag == 1 || flag == -1) {
			// 如果查询条件等于1或者-1表示数字的个数等于字符串的长度
			return nums == mesggLegth;
		} else if (flag == 2 || flag == -2 || flag == 0) {
			// 如果查询条件 等于2,-2或者0表示字符串中的小数点的个数大于小于1,其他字符必须都是数字,且数字数量加小数点数量等于字符串的长度
			return points <= 1 && (nums + points) == mesggLegth;
		}
		return false;
	}

	/**
	 * 这个方法时isNumber(String message, int flag);的工具方法，这个方法用于作为<br>
	 * isNumber(String message, int flag);方法的返回值。该方法是判断传进来的字符<br>
	 * 串是否是负数或负整数。
	 * 
	 * @param message 外部传进来的，被判断处理的字符串。
	 * @param flag    判断模式。
	 * @return 返回值一个boolean值:<br>
	 *         当我们被判断的字符串元素第一位是"-"号是，说明它是一个负数，然后对<br>
	 *         "-" 后的所有字符元素进行判断，是否是小数是否是整数,如果是返回true<br>
	 *         反之false。
	 *         <p>
	 *         如果字符串的元素第一位不是"-"号，直接返回false。
	 */
	private static boolean isNegative(String message, int flag) {
		// 不为空，判断第一位字符是否是‘-’号
		if (message.startsWith("-")) {
			// 第一位是‘-’号，拿到‘-’好后一位到结束的所有字符
			String numbers = message.substring(message.indexOf('-') + 1);
			// 判断拿到的字符集是否是数字组成或是小数组成的部分
			// 是小数或是数字组成返回true
			return isPositive(numbers, flag);
		}
		return false;
	}

}
