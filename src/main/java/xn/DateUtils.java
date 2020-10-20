package xn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称: ai-engine
 * 说明:
 * @version 4.0
 * @author 作者：yangwentao@xforceplus.com
 * 创建时间：Apr 24, 2020 8:45:14 PM 
 * @since JDK 1.8.0_202-b08
 */

public class DateUtils {

	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
		System.out.println(claMinute("2020-04-22 08:05:09","2020-04-22 18:33:32"));
	}
	
	public static long claMinute(String start,String end) {
		long minute = -1;
		try {
			String s = end.split(" ")[0]+" 17:00:00";
			Date f = df.parse(s);
			Date t = df.parse(end);
			  minute =(t.getTime()-f.getTime())/(1000*60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return minute;
	}
	
	public static double  calDay(long time) {
		return time/(7.5*60);
	}
	
    public static void ListSort(List<SignWorkRecord> list) {
        Collections.sort(list, new Comparator<SignWorkRecord>() {
            @Override
            public int compare(SignWorkRecord o1, SignWorkRecord o2) {
                try {
                    Date dt1 = df.parse(o1.signTime);
                    Date dt2 = df.parse(o2.signTime);
                    if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }
    
    public static void sort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    Date dt1 = df.parse(o1.trim());
                    Date dt2 = df.parse(o2);
                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    public static void sortforDay(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    Date dt1 = df1.parse(o1.trim());
                    Date dt2 = df1.parse(o2);
                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }


    public static Map<Integer,String> findDates(String s, String e){

        Map<Integer, String> maps = new TreeMap<>();

        Date start = null;
        Date  end = null;
        try {
             start = df1.parse(s);
             end = df1.parse(e);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        maps.put(start.getDate(),s);


        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            maps.put(calBegin.getTime().getDate(),df1.format(calBegin.getTime()));
        }


        Map<Integer, String> result = maps.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));





        return result;
    }

}
