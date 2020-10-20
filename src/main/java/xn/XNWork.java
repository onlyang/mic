package xn;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 项目名称: ai-engine
 * 说明:
 * @version 4.0
 * @author 作者：yangwentao@xforceplus.com
 * 创建时间：Apr 24, 2020 7:36:35 PM 
 * @since JDK 1.8.0_202-b08
 */

public class XNWork {

	private static String name = "9-1";

	private static String src = "/Users/only/data/XN/"+ name + ".xlsx";
	public static String jsonFile = "/Users/only/data/XN/" + name + ".js";

	private static String detailFile = "/Users/only/data/XN/"+ name + "-detail.xlsx";

	private static String destFile = "/Users/only/data/XN/"+ name + "-result.xlsx";

	public static void calWorkTime() throws IOException {


		//数据清洗 转成json,如果未曾解析则解析，否则直接读取。
		File jFile = new File(jsonFile);
		if(!jFile.exists()) {
			DataListener listener = new DataListener(jFile);
			EasyExcel.read(src, SignWorkRecord.class, listener).sheet().doRead();
		}

		//从json读取清洗过的数据
		List<SignWorkRecord>  data = JSON.parseArray(FileUtils.readFileToString(new File(jsonFile),"utf-8"),SignWorkRecord.class);

        List<PersionSgin> list = new ArrayList<>();

        String first = data.get(0).name + data.get(0).signDate;
        PersionSgin ps = new PersionSgin();
		for(SignWorkRecord e : data) {

			String k = e.name + e.signDate;
			//同一个人同一天
			if(k.equals(first)) {
				ps.sginTimes.add(e.signTime);
				ps.name = e.name;
				ps.sginDate = e.signDate;

			}else {
				list.add(ps);
				ps = new PersionSgin();
				ps.sginTimes.add(e.signTime);
				ps.name = e.name;
				ps.sginDate = e.signDate;
				first = k;
			}
		}

		ArrayList<PersionSgin> result = new ArrayList<>();

		for (int j = list.size()-1; j>=0; j--){
			  PersionSgin e = list.get(j);
			  DateUtils.sort(e.sginTimes);
			  List<String> ls = e.sginTimes;
			  e.startSginTime = ls.get(0);
			  e.endSginTime = ls.get(ls.size()-1);
			  if(ls.size() >=2 ){

				  long times =  DateUtils.claMinute(e.startSginTime,e.endSginTime);
				  DecimalFormat    df   = new DecimalFormat("######0.0");

				  //换算
				  e.time = df.format(DateUtils.calDay(times));
				  e.jb = times;
				  if(times<=0){
				  	e.jb = 0;
				  	e.time = "0.0";
				  }
				  if(!e.time.equals("0.0")){
					  e.isJb = "是";
				  }
				  e.startSginTime = e.startSginTime.split(" ")[1];
				  e.endSginTime = e.endSginTime.split(" ")[1];
			  }else{
				  e.startSginTime = e.startSginTime.split(" ")[1];
				  e.endSginTime= "无打卡记录";
				  e.jb = 0;
				  e.isJb="-";
			   }



			  if(!filter.contains(e.name)) {
				 result.add(e);
			  }



		}

		//明细信息落地
		ExcelWriter excelWriter = EasyExcel.write(destFile, PersionSgin.class).build();
		WriteSheet writeSheet = EasyExcel.writerSheet("考勤").build();
		excelWriter.write(result, writeSheet);
		excelWriter.finish();


		//多维度汇总

        merger( result);



	}

	public static void merger(ArrayList<PersionSgin> data){
		//计算汇总周期
		List<String> datelist = new ArrayList<>();
		for (PersionSgin ps : data) {
			datelist.add(ps.sginDate);
		}
		DateUtils.sortforDay(datelist);
		String startDate = datelist.get(0);
		String endData = datelist.get(datelist.size()-1);

		System.out.println(startDate + "->"+endData);

		//周期
		Map<Integer,String> days = DateUtils.findDates(startDate,endData);

		//人员列表汇总
		Set<String> pers = new HashSet<>();
		for (PersionSgin ps : data) {
			pers.add(ps.name);
		}

		System.out.println(pers.toString());

		//开始汇总

		List<MegerResultItem> items = new ArrayList<>();


		for(String name : pers){
			MegerResultItem item = new MegerResultItem();
			item.setName(name);

			days.forEach((k,v) ->{
				System.out.print(k+"<==>");
				PersionSgin ps = find(v,name,data);
				if(ps != null){
					MegerSummer summer = new MegerSummer();
					summer.setJbTime(ps.getTime());
					item.getInfo().put(k,summer);
				}else{
					MegerSummer summer = new MegerSummer();
					item.getInfo().put(k,summer);
				}
			});
			System.out.println();

			items.add(item);

		}


		for(MegerResultItem it : items){
			System.out.println(it.getName());
			System.out.println(it.getInfo().toString());
		}


	}

	public static PersionSgin find(String date ,String name,ArrayList<PersionSgin> data){
		for (PersionSgin ps : data) {
			if(ps.sginDate.equals(date) && ps.name.equals(name)){
				return ps;
			}
		}
		return null;
	}

	
	public static void main(String[] args) {
		try {
			calWorkTime();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> filter = new HashSet<String>();
	static {
		filter.add("王研");
		filter.add("高汶林");
		filter.add("胡文声");
		filter.add("贺静");
	}

}
