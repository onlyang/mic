package xn;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称: ai-engine
 * 说明:
 * @version 4.0
 * @author 作者：yangwentao@xforceplus.com
 * 创建时间：Apr 24, 2020 9:32:34 PM 
 * @since JDK 1.8.0_202-b08
 */
@Data
public class PersionSgin {

	@ExcelProperty("姓名")
	public String name;
	@ExcelProperty("考勤日期")
	public String sginDate;
	@ExcelIgnore
	public List<String> sginTimes = new ArrayList<>();
	@ExcelProperty("上班打卡")
	public String startSginTime;
	@ExcelProperty("下班打卡")
	public String endSginTime;
	@ExcelProperty("加班时长(分钟)")
	public long jb;
	@ExcelProperty("加班时长(0.0)")
	public String time;
	@ExcelProperty("是否加班")
	public String isJb = "否";

}
