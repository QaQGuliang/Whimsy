package src.com.ych.whimsy;

import src.com.ych.whimsy.log.Log;
import src.com.ych.whimsy.mysql.Field;
import src.com.ych.whimsy.mysql.Type;

public class Test {
    public static void main (String[] args) {

        Field field = new Field();
        field.setName("name");
        field.setType(Type.INT.getType(5, 1));
        Log.outBlueLn(field);
        Log.outPurpleLn(field.getType().getDefaultValue());

    }

}