package com.microsoft.helpit.util;

import java.util.Calendar;
import java.util.Date;

public class TimeProcessor {

    public static Date[] getInterval(String interval){
        Date[] res = new Date[2];
        Calendar scal = Calendar.getInstance();
        scal.setTime(new Date());
        scal.set(Calendar.HOUR_OF_DAY, 0);
        scal.set(Calendar.MINUTE, 0);
        scal.set(Calendar.SECOND, 0);
        scal.set(Calendar.MILLISECOND, 0);

        Calendar ecal = Calendar.getInstance();
        ecal.setTime(new Date());
        ecal.set(Calendar.HOUR_OF_DAY, 0);
        ecal.set(Calendar.MINUTE, 0);
        ecal.set(Calendar.SECOND, 0);
        ecal.set(Calendar.MILLISECOND, 0);

        if(interval.equals("yesterday")) {
            scal.add(Calendar.DATE,-1);
        }else if(interval.equals("week")){
//            int dayWeek = scal.get(Calendar.DAY_OF_WEEK);
//            if (1 == dayWeek) {
//                scal.add(Calendar.DAY_OF_MONTH, -1);
//            }

            scal.setFirstDayOfWeek(Calendar.MONDAY);
            int weekDay = scal.get(Calendar.DAY_OF_WEEK);
            scal.add(Calendar.DATE, scal.getFirstDayOfWeek() - weekDay - 7);

            ecal.add(Calendar.DATE, scal.getFirstDayOfWeek() - weekDay);
        }else if(interval.equals("month")){
            scal.add(Calendar.MONTH, -1);
            scal.set(Calendar.DAY_OF_MONTH,1);
            ecal.set(Calendar.DAY_OF_MONTH,1);

        }else if(interval.equals("year")){
            scal.add(Calendar.YEAR,-1);
            scal.set(Calendar.MONTH,0);
            scal.set(Calendar.DAY_OF_MONTH,1);

            ecal.set(Calendar.MONTH,0);
            ecal.set(Calendar.DAY_OF_MONTH,1);

        }else
            return null;


        res[0] = scal.getTime();
        res[1] = ecal.getTime();

        return res;
    }
}
