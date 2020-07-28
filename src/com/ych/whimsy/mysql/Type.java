package src.com.ych.whimsy.mysql;

import com.mysql.cj.exceptions.NumberOutOfRange;

import java.util.Objects;

/**
 * 该类是一个枚举类，每个枚举实例代表一个数据类型，每个数据类型实例可以调用它的<br/>
 * getType(...)方法获取类型并指定该类型的长度或者宽度
 * <p>目前支持类型只有两个:int、varchar<p/>
 */
public enum Type {
    /**
     * int 类型<br/>
     * 如果没有使用getType方法获取类型并指定宽度则使用该实例的<br/>
     * 默认int类型,它的宽度为11位
     */
    INT("int") {
        /**
         * 获取INT类型并设置int类型的宽度，不指定宽度或者宽度指定为零会使用默认宽度
         * @param width int类型的宽度
         * @NumberOutOfRange 当前类型的宽度超出的范围
         * @return 返回设置过宽度的int类型
         */
        @Override
        public Type getType (int width) throws NumberOutOfRange {
            // 如果宽度超出了范围
            if (width < 0 || width > 10) {
                throw new NumberOutOfRange("宽度超出了范围");
            }
            INT.type = INT.type + "(" + width + ")";
            return this;
        }

        /**
         * 给int类型设置宽度和默认值后返回当前类型
         * @param width            int类型的宽度
         * @param defaultValue int类型默认值
         * @NumberOutOfRange 当前类型的宽度超出的范围, 或者默认值超出的限定范围
         * @NullPointerException 默认值对象为空了
         * @ClassCastException 默认值不是数字
         * @return 返回设置好的int类型
         */
        @Override
        public Type getType (int width, Object defaultValue) throws NumberOutOfRange, NullPointerException, ClassCastException {
            // 判断默认值是否为空为空了会抛出异常
            if (Objects.isNull(defaultValue) || defaultValue.toString().isEmpty()) {
                throw new NullPointerException("整数型默认值为空了");
            }
            // 判断默认值是否是数字
            if (!defaultValue.toString().matches("\\d*")) {
                throw new ClassCastException("默认值不是数组无法转成数字");
            }
            // 初始化宽度
            this.getType(width);

            // 判断默认值的位宽是否超出限定范围
            int length = String.valueOf(defaultValue).length();
            if (width == 0) {
                if (length > 10) {
                    throw new NumberOutOfRange("默认值位数超出了限定范围");
                }
            } else if (length > width) {
                throw new NumberOutOfRange("默认值位数超出了限定范围");
            }
            INT.defaulteValue = defaultValue.toString();
            return this;
        }
    },
    /**
     * 可变长度的字符串 长度区间:0~65535<br/>
     * 如果该实例没有使用getType获取类型并指定长度,<br/>
     * 使用该实例默认的255长度.
     */
    VARCHAR("varchar(255)") {
        /**
         * 获取VARCHAR类型并指定长度
         * @param length varchar长度\
         * @NumberOutOfRange 当前类型的长度超出的范围
         * @return 返回设置过长度的VARCHAR
         */
        @Override
        public Type getType (int length) throws NumberOutOfRange {
            // 判断length的取值范围,范围超出抛出异常
            if (length < 0 || length > 65535) {
                throw new NumberOutOfRange("");
            }
            VARCHAR.type = "varchar(" + length + ")";
            return this;
        }

        /**
         * 设置字符类型的长度和默认值后返回当前类型实例
         * @param length 字符长度
         * @param defaultValue 类型默认值
         * @NumberOutOfRange 当前类型的长度超出的范围, 或者默认值超出的限定范围
         * @return 返回设置好长度的字符类型
         */
        @Override
        public Type getType (int length, Object defaultValue) {
//                // 判断默认值是否为空为空了会抛出异常
//                Type.isNull(defaultValue);
            // 初始化宽度
            this.getType(length);
            if (!Objects.isNull(defaultValue)) {
                // 判断默认值长度是否超出范围
                int l = defaultValue.toString().length();
                if (l > length) {
                    throw new NumberOutOfRange("字符默认值超出范围");
                }
                if (defaultValue.toString().isEmpty()) {
                    VARCHAR.defaulteValue = "''";
                } else
                    VARCHAR.defaulteValue = "'" + defaultValue + "'";
            } else
                VARCHAR.defaulteValue = "" + defaultValue;
            return this;
        }
    };
    /**
     * 数据类型
     */
    private String type;

    /**
     * 类型默认值
     */
    private String defaulteValue;

    /**
     * 初始化数据类型
     *
     * @param type 类型名
     */
    Type (String type) {
        this.type = type;
    }

    /**
     * 该方法用来指定数据类型行的精度,是字符型指长度,是整形指宽度
     *
     * @param i 长度或者宽度
     * @return 返回当前设置好的类型
     */
    public abstract Type getType (int i);

    /**
     * 该方法用来指定数据类型行的精度,是字符型指长度,是整形指宽度<br/>
     * ;并为该类型指定一个默认值.
     *
     * @param i            长度或者宽度
     * @param defaultValue 类型默认值
     * @return 返回当前设置好的类型
     */
    public abstract Type getType (int i, Object defaultValue);


    /**
     * 当前类的默认值
     *
     * @return 返回默认值
     */
    public String getDefaultValue ( ) {
        if (Objects.isNull(defaulteValue))
            return "";
        return "default " + defaulteValue;
    }

    /**
     * 返回默认字段类型信息
     *
     * @return 返回默认字段类型
     */
    public String toString ( ) {
        return Objects.isNull(defaulteValue) ? this.type : this.type + " " + this.getDefaultValue();
    }


}