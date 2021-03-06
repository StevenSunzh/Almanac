package com.almanac.lunar;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class AlmanacImpl implements Almanac {
	private AlmanacBean bean = null;
	private LunarCalendar lunarCalendar;
	private SunAndMoon sumAndMoon = null;
	private String[] areas = null;
	private Calendar calendar = null;
	private TimeBean dataBean = null;
	private Port port = null;

	public AlmanacImpl(TimeBean timeBean) {
		this.dataBean = timeBean;
		this.calendar = timeBean.getCalendar();
		this.port = new Port(Propt.getLatportProperties(), Propt.getLoogportProperties());
		lunarCalendar = new LunarCalendar(this.calendar);
		bean = lunarCalendar.getLunarDateClassObj();
		sumAndMoon = new SunAndMoon(timeBean, bean);
	};

	@Override
	public String getDate() {
		return TimeUtil.dateFormat(calendar, "yyyy年MM月dd日");
	}

	@Override
	public String getDateFormer() {
		return TimeUtil.dateFormat(calendar, "yyyy-MM-dd");
	}

	@Override
	public String getTime() {
		return TimeUtil.dateFormat(calendar, "HH时mm分ss秒SS毫秒");
	}

	@Override
	public String getTimeFormer() {
		return TimeUtil.dateFormat(calendar, "HH:mm:ss.SS");
	}

	@Override
	public String getWesternCalendar() {
		return TimeUtil.dateFormat(calendar, "yyyy-MM-dd HH:mm:ss.SS");
	};

	@Override
	public String getWesternCalendarCN() {
		/***
		 * G 年代标志符   y 年   M 月   d 日   h 时 在上午或下午 (1~12)   H 时 在一天中 (0~23)   m 分   s 秒  
		 * S 毫秒   E 星期   D 一年中的第几天   F 一月中第几个星期几   w 一年中第几个星期   W 一月中第几个星期   a 上午 / 下午
		 * 标记符   k 时 在一天中 (1~24)   K 时 在上午或下午 (0~11)   z 时区
		 */
		String timeStr = "G yyyy年MM月dd日 HH时mm分ss秒SSS毫秒 Eak时 Z时区 yyyy年第w周第D天 M月第W周第d天";
		return TimeUtil.dateFormat(calendar, timeStr);
	};

	@Override
	public String getWeek() {
		return TimeUtil.getWeek(calendar);
	}

	@Override
	public String getPosition() {
		// return areas[0] + " " + areas[1].substring(4, areas[1].length());
		String prov = dataBean.getProvince().replaceAll("省", "");// 字符替代
		String area = dataBean.getArea().replaceAll("市", "").replaceAll("区", "").replaceAll("县", "").replaceAll("镇", "")
				.replaceAll("乡", "");
		return prov + " " + area;
	}

	@Override
	public String getLongitude() {
		return bean.getLongitude();
	}

	@Override
	public String getLatitude() {
		return bean.getLatitude();
	}

	@Override
	public String getTimeZone() {
		String strDateFormat = TimeUtil.formatDateByFormat(calendar, "Z");// +0800

		String str2 = strDateFormat.substring(1, 3);
		int timeZoneInt = 0;
		int j = Integer.valueOf(str2.substring(0));
		if (j > 0) {
			timeZoneInt = Integer.valueOf(str2);
		} else {
			timeZoneInt = Integer.valueOf(str2.substring(1));
		}

		String[] timeZoneChese = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };

		if (strDateFormat.contains("-")) {
			return strDateFormat + " 西" + timeZoneChese[timeZoneInt - 1] + "区";
		} else {
			return strDateFormat + " 东" + timeZoneChese[timeZoneInt - 1] + "区";
		}
	}

	@Override
	public String getDiurnalTime() {
		return TimeUtil.getFormattingTime(bean.getDayTime());
	}

	@Override
	public String getNightTime() {
		return TimeUtil.getFormattingTime(bean.getEveningTime());
	}

	@Override
	public String getDawnTime() {
		return TimeUtil.getFormattingTime(bean.getDawnTime());
	}

	@Override
	public String getSunriseTime() {
		return TimeUtil.getFormattingTime(bean.getSunRise());
	}

	@Override
	public String getMidDayTime() {
		return TimeUtil.getFormattingTime(bean.getMidTime());
	}

	@Override
	public String getSunsetTime() {
		return TimeUtil.getFormattingTime(bean.getSunSet());
	}

	@Override
	public String getDarkTime() {
		return TimeUtil.getFormattingTime(bean.getNightTime());
	}

	@Override
	public String getMoonOutTime() {
		return TimeUtil.getFormattingTime(bean.getMoonRise());
	}

	@Override
	public String getMidMoonTime() {
		return TimeUtil.getFormattingTime(bean.getMoonMiddleTime());
	}

	@Override
	public String getMoonDownTime() {
		return TimeUtil.getFormattingTime(bean.getMoonSet());
	}

	@Override
	public String getPortName() {
		return port.getProtName(sumAndMoon);
	}

	@Override
	public String getJulianDay() {
		return String.valueOf(JulianCalendar.getJuLian(calendar));
	}

	@Override
	public String getChineseEraYear() {
		return bean.getChineseEra_Year();
	}

	@Override
	public String getChineseEraMonth() {
		return bean.getChineseEra_Month();
	}

	@Override
	public String getChineseEraDay() {
		return bean.getChineseEra_Day();
	}

	@Override
	public String getChineseEraTime() {
		return bean.getChineseEra_Time();
	}

	@Override
	public String getChronology() {
		return "开元" + bean.getLunar_king_Years() + "年";
	}

	@Override
	public String getZodiac() {
		return bean.getLunarDate_Animal_Year();
	}

	@Override
	public String getLunarYear() {
		return bean.getChineseEra_Year();
	}

	@Override
	public String getLunarMonth() {
		return bean.getLunar_MonthName() + "月";
	}

	@Override
	public String getLunarDay() {
		return bean.getLunar_DayName();
	}

	@Override
	public String getLunarTime() {
		String Hstr = null, Mstr = null;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		double sum = hour + 0.01 * minute;
		if (sum < 1 || sum >= 23) {
			Hstr = Annals.DIZHI[0] + "时" + "[三更]";
		}
		if (sum >= 1 && sum < 3) {
			Hstr = Annals.DIZHI[1] + "时" + "[四更]";
		}
		if (sum >= 3 && sum < 5) {
			Hstr = Annals.DIZHI[2] + "时" + "[五更]";
		}
		if (sum >= 5 && sum < 7) {
			Hstr = Annals.DIZHI[3] + "时";
		}
		if (sum >= 7 && sum < 9) {
			Hstr = Annals.DIZHI[4] + "时";
		}
		if (sum >= 9 && sum < 11) {
			Hstr = Annals.DIZHI[5] + "时";
		}
		if (sum >= 11 && sum < 13) {
			Hstr = Annals.DIZHI[6] + "时";
		}
		if (sum >= 13 && sum < 15) {
			Hstr = Annals.DIZHI[7] + "时";
		}
		if (sum >= 15 && sum < 17) {
			Hstr = Annals.DIZHI[8] + "时";
		}
		if (sum >= 17 && sum < 19) {
			Hstr = Annals.DIZHI[9] + "时";
		}
		if (sum >= 19 && sum < 21) {
			Hstr = Annals.DIZHI[10] + "时" + "[一更]";
		}
		if (sum >= 21 && sum < 23) {
			Hstr = Annals.DIZHI[11] + "时" + "[二更]";
		}
		if (minute >= 14 && minute <= 15) {
			Mstr = "一刻";
		} else if (minute >= 28 && minute <= 30) {
			Mstr = "二刻";
		} else if (minute >= 42 && minute <= 45) {
			Mstr = "三刻";
		} else if (minute >= 48 && minute <= 60) {
			Mstr = "四刻";
		} else {
			Mstr = "";
		}
		return Hstr + Mstr;
	}

	@Override
	public String getLunar() {
		return getLunarYear() + getZodiac() + "年" + getLunarMonth() + getLunarDay();
	}

	@Override
	public String getLunarDays() {
		return bean.getLunar_Days_OfMonth() + "天";
	}

	@Override
	public String isLunarBigMonth() {
		if (!(bean.getLunar_Days_OfMonth() > 29)) {
			return "否";
		} else {
			return "是";
		}
	}

	@Override
	public String isLeapMonth() {
		if (!"闰".equals(bean.getLunar_Lunar_isLeap())) {
			return "否";
		} else {
			return "是";
		}
	}

	@Override
	public String isLeapYear() {
		if (lunarCalendar.getQiShuoClassObj().leap > 0) {
			return "是";
		} else {
			return "否";
		}
	}

	@Override
	public int getIslamicYear() {
		return bean.getIslamic_Year();
	}

	@Override
	public int getIslamicMonth() {
		return bean.getIslamic_Month();
	}

	@Override
	public int getIslamicDay() {
		return bean.getIslamic_Day();
	}

	@Override
	public String getIslamic() {
		return getIslamicYear() + "年" + getIslamicMonth() + "月" + getIslamicDay() + "日";
	}

	@Override
	public String getConstellation() {
		return bean.getConstellation();
	}

	@Override
	public String getTianGan() {
		String tiangan = getBaZi();
		char c1 = tiangan.charAt(0);
		char c2 = tiangan.charAt(2);
		char c3 = tiangan.charAt(4);
		char c4 = tiangan.charAt(6);
		return String.valueOf(c1) + c2 + c3 + c4;
	}

	@Override
	public String getDiZhi() {
		String tiangan = getBaZi();
		char c1 = tiangan.charAt(1);
		char c2 = tiangan.charAt(3);
		char c3 = tiangan.charAt(5);
		char c4 = tiangan.charAt(7);
		return String.valueOf(c1) + c2 + c3 + c4;
	}

	@Override
	public String getBaZi() {
		return bean.getChineseEra_Year() + bean.getChineseEra_Month() + bean.getChineseEra_Day()
				+ bean.getChineseEra_Time();
	}

	@Override
	public String getHuangLi() {
		return bean.getChineseEra_Year() + "年" + bean.getChineseEra_Month() + "月" + bean.getChineseEra_Day() + "日"
				+ bean.getChineseEra_Time() + "时";
	}

	@Override
	public String getYearNumber() {
		return bean.getLunarDate_China_Era_Year_Number();
	}

	@Override
	public String getHolidayVacations() {
		String str = bean.getAllDay_Name();
		if (str == "") {
			return "无";
		} else if (str == null) {
			return "无";
		} else {
			return str;
		}
	}

	@Override
	public String getMoonPhase() {
		String str = bean.getMoon_PhaseName();
		if (str == "") {
			return "无";
		} else if (str == null) {
			return "无";
		} else {
			return str + " " + bean.getMoon_PhaseTimeStr();
		}
	}

	@Override
	public String getNextSolarTerm() {
		return lunarCalendar.getNextSolarTerm();
	}

	@Override
	public String getSolarTerm(String solarTerm) {
		return lunarCalendar.getSolarTermDate(solarTerm);
	}

	@Override
	public String[] getAllSolarTerm() {
		return lunarCalendar.getAllSolarTerm();
	}

	@Override
	public String getSolarTermDoc(String solarTerm) {
		return SolarTermUtil.solarTermsMap.get(solarTerm);
	}

}
