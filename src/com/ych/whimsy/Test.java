package src.com.ych.whimsy;

import src.com.ych.whimsy.log.Log;

public class Test {
    public static void main (String[] args) {

        Log.outRedLn(Type.STRING);
        Type.STRING.setLength(2);
        Log.outRedLn(Type.STRING);

    }

    private abstract class TypeLength {
        /**
         * 设置类型长度
         */
        public abstract void setLength (int length);
    }

    public enum Type {
        INT("int") {
            @Override
            public void setLength (int length) {

            }
        },
        STRING("varchar") {
            public void setLength (int length) {
                STRING.type = STRING.type + "(" + length + ")";
            }
        };

        private String type;

        Type (String type) {
            this.type = type;
        }

        public abstract void setLength (int length);

        public String toString ( ) {
            return this.type;
        }


    }

}