/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebias;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mostafa
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Calendar cal = Calendar.getInstance();
       // cal.setTime(date);
       // cal.add(Calendar.MINUTE, 5);
        Date newDate = cal.getTime();
        System.out.println(newDate.toString());
    }

}
