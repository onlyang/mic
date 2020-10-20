package xn;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称: ai-engine
 * 说明:
 * @version 4.0
 * @author 作者：yangwentao@xforceplus.com
 * 创建时间：Apr 24, 2020 8:01:49 PM 
 * @since JDK 1.8.0_202-b08
 */

public class DataListener extends  AnalysisEventListener<SignWorkRecord>{

	private  File dest;

	public DataListener(File dest){
		this.dest = dest;
	}
	
	int i = 0;
	List<SignWorkRecord> finalData = new ArrayList<SignWorkRecord>();
	@Override
	public void invoke(SignWorkRecord data, AnalysisContext context) {
		System.out.println(data.toString());
		data.ID = i;
		if(data.getSignMachine()==null || data.getSignMachine().equals("")) {
			System.out.println("-------");
		}else {
			System.out.println(data.getSignMachine());
		}
		if(data.getSignMachine().endsWith("考勤机")) {
			if(data.entryDate != null){
				data.entryDate = data.entryDate.split(" ")[0];
			}
		    data.signDate = data.signDate.split(" ")[0];
		    data.signTime  = data.signDate + " " + data.getSignTime().split(" ")[1];
		    i++;
		    finalData.add(data);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		try {
			FileUtils.write(dest, JSON.toJSONString(finalData),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
