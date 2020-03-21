package com.project.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 날짜 연산 유틸리티
 */
public class DateUtils {

    private String YYYMMDDHHMMSS =  "yyyyMMddHHmmss";
    private String YYYMMDD =  "yyyyMMdd";
    private String NOW_FORMAT = "yyyyMMddHHmmss";
    /**
     * 전달받은 값을 바탕으로 ZondeDatTime 값을 생성 하여 반환 ( 단순 날짜값만 계산시 사용 )
     * @param originalDate
     * @param originalTime
     * @param strZoneId
     * @return
     */
    private ZonedDateTime getZoneData(String originalDate, String originalTime , String strZoneId ) {



        String[] strDate= originalDate.split("-");
        int year = Integer.parseInt( strDate[0] );
        int month = Integer.parseInt( strDate[1] );
        int day = Integer.parseInt( strDate[2] );
        String[] strTime = originalTime.split(":");
        int hour = Integer.parseInt( strTime[0] );
        int minute = Integer.parseInt( strTime[1] );
        int second = Integer.parseInt( strTime[2] );
        int nano = 0 ;

        ZoneId zoneIdData = ZoneId.of(strZoneId);


        return   ZonedDateTime.of( year, month, day ,  hour, minute, second, nano, zoneIdData);
    }





    /**
     * 전달받은 값을 바탕으로 LocalData 값을 생성하여 변환 ( 시간 계산시 사용 )
     * @param originalDate
     * @param spritStr
     * @return
     */
    private LocalDate getLocalDate(String originalDate, String spritStr) {
        String[] strDate= originalDate.split(spritStr);
        int year = Integer.parseInt( strDate[0] );
        int month = Integer.parseInt( strDate[1] );
        int day = Integer.parseInt( strDate[2] );

        return IsoChronology.INSTANCE.date(year, month, day );
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 몇달  이후 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String afterMonth(String originalDate , String originalTime , long afterMonth, String spritStr ){




        LocalDate theDay = getLocalDate(originalDate, spritStr) ;

        LocalDate next = theDay.plusMonths(afterMonth);


        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);
        if( originalTime == null )   return next.toString();
        else  return next.toString()+" "+originalTime;

    }


    /**
     * 몇달 이후 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String afterMonth(String originalDate , long afterMonth ){



        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null ;
        if (   data.length == 2 ) orgTime = data[1] ;
        return afterMonth(originalDate, orgTime ,afterMonth , "-") ;

    }


    /**
     * 몇주  이후 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String afterWeeks(String originalDate, String originalTime , long afterWeek, String spritStr ){

        LocalDate theDay = getLocalDate(originalDate, spritStr) ;

        LocalDate next = theDay.plusWeeks(afterWeek);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);

        if( originalTime == null ) return next.toString();
        else return next.toString()+" "+originalTime;

    }





    /**
     * 몇주 이후 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String afterWeeks(String originalDate , long afterWeek ){
        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null ;
        if (   data.length == 2 ) orgTime = data[1] ;
        return afterWeeks(originalDate,  orgTime ,afterWeek , "-") ;

    }




    /**
     * 몇일  이후 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String afterDays(String originalDate, String originalTime , long afterDays, String spritStr ){




        LocalDate theDay = getLocalDate(originalDate, spritStr) ;
        LocalDate next = theDay.plusDays(afterDays);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);


        if( originalTime == null )    return next.toString();
        else  return next.toString()+" "+originalTime;

    }

    /**
     * 몇일 이후 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String afterDays(String originalDate , long afterDays ){
        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null;
        if (   data.length == 2 ) orgTime = data[1] ;
        return afterDays(orgDate, orgTime  ,afterDays , "-") ;

    }




    /**
     * 지정한 시간 이후 몇시간 이후 값 반환
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String afterHour(String strZoneId, String originalDate, String originalTime , long afterHours  ){
        String[] strDate= originalDate.split("-");
        String strFormater =YYYMMDDHHMMSS;
        ZoneId zoneIdData = ZoneId.of(strZoneId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime after  = thisTime.plusHours(afterHours);
        return  after.format(format);
    }
    /**
     * 지정한 시간 이후 몇시간 이후 값 반환
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String afterHour(String strZoneId, String originalFullDate  , long afterHours  ){
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return afterHour(strZoneId, originalDate ,originalTime, afterHours  );
    }

    /**
     * 지정한 시간 이후 몇시간 이후 값 반환 ( 국가 기본값 : 서울)
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String afterHour(String originalFullDate  , long afterHours  ){
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "Asia/Seoul";
        return afterHour(strZoneId, originalDate ,originalTime, afterHours  );
    }



    /**
     *  국가 시간 기준 몇분후 시간 반환
     *  strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String afterMinute(String strZoneId, String originalDate, String originalTime , long afterMinute){
        String strFormater =YYYMMDDHHMMSS;
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime after  = thisTime.plusMinutes(afterMinute);
        return  after.format(format);
    }

    /**
     *  국가 시간 기준 몇분후 시간 반환
     *  strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String afterMinute(String strZoneId , String originalFullDate, long afterMinute) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return afterMinute( strZoneId, originalDate,originalTime,afterMinute);
    }
    /**
     *  국가 시간 기준 몇분후 시간 반환( 한국 서울 기준 )
     *  strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String afterMinute(String originalFullDate, long afterMinute) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "Asia/Seoul";
        return afterMinute( strZoneId, originalDate,originalTime,afterMinute);
    }
    /**
     * 표준시간 기준 몇초후 시간 반환
     * strZoneId : ex) "UTC"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String afterSecond(String strZoneId, String originalDate, String originalTime , long afterSecond){

        String strFormater =YYYMMDDHHMMSS;
        ZoneId zoneIdData = ZoneId.of(strZoneId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime after  = thisTime.plusSeconds(afterSecond);
        return  after.format(format);
    }

    /**
     * 표준시간 기준 몇초후 시간 반환
     * strZoneId : ex) "UTC"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String afterSecond(String strZoneId, String originalFullDate, long afterSecond) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return afterSecond(strZoneId, originalDate, originalTime, afterSecond);
    }

    /**
     * 표준시간 기준 몇초후 시간 반환 ( UTC 기준 )
     * strZoneId : ex) "UTC"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String afterSecond(String originalFullDate, long afterSecond) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "UTC";
        return afterSecond(strZoneId, originalDate, originalTime, afterSecond);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 몇달  이전 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String beforeMonth(String originalDate , String originalTime , long afterMonth, String spritStr ){

        LocalDate theDay = getLocalDate(originalDate, spritStr) ;

        LocalDate next = theDay.minusMonths(afterMonth);


        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);
        if( originalTime == null )   return next.toString();
        else  return next.toString()+" "+originalTime;

    }


    /**
     * 몇달 이전 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String beforeMonth(String originalDate , long afterMonth ){
        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null ;
        if (   data.length == 2 ) orgTime = data[1] ;
        return beforeMonth(originalDate, orgTime ,afterMonth , "-") ;

    }

    /**
     * 몇주  이전 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String beforeWeeks(String originalDate, String originalTime , long afterWeek, String spritStr ){

        LocalDate theDay = getLocalDate(originalDate, spritStr) ;

        LocalDate next = theDay.minusWeeks(afterWeek);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);

        if( originalTime == null ) return next.toString();
        else return next.toString()+" "+originalTime;

    }


    /**
     * 몇주 이전 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String beforeWeeks(String originalDate , long afterWeek ){
        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null ;
        if (   data.length == 2 ) orgTime = data[1] ;
        return beforeWeeks(originalDate,  orgTime ,afterWeek , "-") ;

    }
    /**
     * 몇일  이전 구하기 ( 정해진 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @param spritStr
     * @return String
     */
    public String beforeDays(String originalDate , String originalTime, long afterDays, String spritStr ){

        LocalDate theDay = getLocalDate(originalDate, spritStr) ;

        LocalDate next = theDay.minusDays(afterDays);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYMMDD);

        if( originalTime == null )    return next.toString();
        else  return next.toString()+" "+originalTime;
    }

    /**
     * 몇일 이전 구하기 ( "-" 구분기호로 년월일 구분)
     * @param originalDate
     * @param afterDays
     * @return String
     */
    public String beforeDays(String originalDate , long afterDays ){
        String[] data = originalDate.split(" ");
        String orgDate = data[0];
        String orgTime = null;
        if (   data.length == 2 ) orgTime = data[1] ;
        return beforeDays(orgDate, orgTime , afterDays , "-") ;

    }

    /**
     * 지정한 시간 이전 몇시간 값 반환
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String beforeHour(String strZoneId, String originalDate, String originalTime , long beforeHours  ){
        String[] strDate= originalDate.split("-");
        String strFormater =YYYMMDDHHMMSS;
        ZoneId zoneIdData = ZoneId.of(strZoneId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime before  = thisTime.minusHours(beforeHours);

        return  before.format(format);
    }
    /**
     * 지정한 시간 이전 몇시간  값 반환
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String beforeHour(String strZoneId, String originalFullDate  , long afterHours  ){
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return beforeHour(strZoneId, originalDate ,originalTime, afterHours  );
    }

    /**
     * 지정한 시간 이전 몇시간 값 반환 ( 국가 기본값 : 서울)
     * strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterHours
     * @return String
     */
    public String beforeHour(String originalFullDate  , long afterHours  ){
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "Asia/Seoul";
        return beforeHour(strZoneId, originalDate ,originalTime, afterHours  );
    }



    /**
     *  국가 시간 기준 몇분이전 시간 반환
     *  strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String beforeMinute(String strZoneId, String originalDate, String originalTime , long afterMinute){
        String strFormater =YYYMMDDHHMMSS;
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime before  = thisTime.minusMinutes(afterMinute);
        return  before.format(format);
    }

    /**
     *  국가 시간 기준 몇분이전 시간 반환
     *  strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String beforeMinute(String strZoneId , String originalFullDate, long afterMinute) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return beforeMinute( strZoneId, originalDate,originalTime,afterMinute);
    }
    /**
     *  국가 시간 기준 몇분이전 시간 반환( 한국 서울 기준 )
     *   strZoneId : ex) "Asia/Seoul"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return String
     */
    public String beforeMinute(String originalFullDate, long afterMinute) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "Asia/Seoul";
        return beforeMinute( strZoneId, originalDate,originalTime,afterMinute);
    }
    /**
     * 표준시간 기준 몇초이전 시간 반환
     * * strZoneId : ex) "UTC"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String beforeSecond(String strZoneId, String originalDate, String originalTime , long afterSecond){


        String strFormater =YYYMMDDHHMMSS;
        ZoneId zoneIdData = ZoneId.of(strZoneId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(strFormater);
        ZonedDateTime thisTime = getZoneData(originalDate, originalTime, strZoneId);
        ZonedDateTime before  = thisTime.minusSeconds(afterSecond);
        return  before.format(format);
    }

    /**
     * 표준시간 기준 몇초이전 시간 반환
     *  strZoneId : ex) "UTC"
     * @param strZoneId
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String beforeSecond(String strZoneId, String originalFullDate, long afterSecond) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        return beforeSecond(strZoneId, originalDate, originalTime, afterSecond);
    }

    /**
     * 표준시간 기준 몇초 이전 시간 반환 ( UTC 기준 )
     * @param originalDate
     * @param originalTime
     * @param afterMinute
     * @return
     */
    public String beforeSecond(String originalFullDate, long afterSecond) {
        String[] data = originalFullDate.split(" ");
        String originalDate = data[0];
        String originalTime = data[1];
        String strZoneId = "UTC";
        return beforeSecond(strZoneId, originalDate, originalTime, afterSecond);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 지정한 날짜의 요일 정보
     * type : shor, full , narrow
     * locale : ex ) Locale.KOREA
     * @param originalDate
     * @param type
     * @param locale
     * @return String
     */
    public String getDayOfWeek(String originalDate , String type , Locale locale ){

        String[] strDate= originalDate.split("-");
        int year = Integer.parseInt( strDate[0] );
        int month = Integer.parseInt( strDate[1] );
        int day = Integer.parseInt( strDate[2] );
        LocalDate thisDays = LocalDate.of(year, month, day);

        DayOfWeek dayOfWeek = thisDays.getDayOfWeek();
        TextStyle strType ;

        switch ( type ) {
            case "full" : strType = TextStyle.FULL;
                break;
            case "narrow" : strType = TextStyle.NARROW ;
                break;
            default:  strType = TextStyle.SHORT ;
                break;
        }
        return dayOfWeek.getDisplayName(strType, locale );


    }

    /**
     * 두 날짜사이 기간 반환
     * @param startDate
     * @param endDate
     * @return
     */
    public int getBetWeenDate(String startDate , String endDate ){
        LocalDate stDay = getLocalDate(startDate, "-");
        LocalDate edDay = getLocalDate(endDate , "-");

        return edDay.compareTo(stDay);

    }



    /**
     * 두 날짜사이 기간 시간 반환
     * @param startDate
     * @param endDate
     * @return
     */
    public long getBetWeenTime(String startDate , String endDate , String getTimeType ){
        String[] strStart = startDate.split(" ");
        String[] endStart = endDate.split(" ");

        ZonedDateTime stDay = getZoneData(strStart[0], strStart[1], "Asia/Seoul");
        ZonedDateTime edDay =  getZoneData(endStart[0] , endStart[1] , "Asia/Seoul");

        Duration d = Duration.between( stDay , edDay );

        long toTime= 0 ;
        if(getTimeType.equals("min")) {

            return d.toMinutes();
        }
        else if(getTimeType.equals("hour")) {
            return d.toHours();

        }else if(getTimeType.equals("sec")) {
            return d.toMillis()/1000;
        }else {
            return d.toMinutes();
        }

    }

    /**
     * 현재 시간 가져오기
     * @return
     */
    public String getNow(String format){
        LocalDateTime curDate = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return  curDate.format(df).toString();
    }

    /**
     * 현재 시간 가져오기
     * @return
     */
    public String getSystemNow(String format){
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat(format);

        return dayTime.format(new Date(time));
    }

    public String getTimeZoneNow(String format) {
        String TIME_SERVER = "time server 주소";
        Date dTime = new Date();
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");

        SimpleDateFormat dayTime = new SimpleDateFormat(format);
        dayTime.setTimeZone(tz);

        return dayTime.format(dTime);
    }

    public static String getDateTimeFormat(String format) {
        String dateTime = null;
        try {
            if(format != null) {
                String dateInString = format.replaceFirst("T", " ");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(dateInString);
                dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);

            }

        }catch(ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 날짜 형식을 표준화 하여 LocalDate로 변환시켜서 가져옴
     * @param date
     * @return
     */
    public static LocalDate getLocalDate(String date) {
        String replaceDate = CommonUtils.getReplace(date, "-");
        return LocalDate.parse(replaceDate, DateTimeFormatter.BASIC_ISO_DATE);
    }
}