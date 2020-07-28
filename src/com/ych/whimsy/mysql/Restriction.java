package src.com.ych.whimsy.mysql;

public interface Restriction {

    /**
     * 自增长
     */
    String AUTO_INCREMENT = " auto_increment ";

    /**
     * 非空约束
     */
    String NOT_NULL = " not null ";

    /**
     * 唯一约束
     */
    String UNIQUE = " unique ";

    /**
     * 主键约束
     */
    String PRIMARY_KEY = " primary key ";


}
