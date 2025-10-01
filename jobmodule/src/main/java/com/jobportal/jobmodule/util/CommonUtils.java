package com.jobportal.jobmodule.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.jobmodule.data.pojo.MyPair;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.security.MessageDigest;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtils {
    private final ObjectMapper objectMapper;
    public static final char CHAR_SEVEN = '7';
    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú',
            'ý', 'ỹ', 'ỳ', 'ỵ', 'ỷ', 'Ý', 'Ỹ', 'Ỳ', 'Ỵ', 'Ỷ',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};
    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E', 'E',
            'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'a', 'a', 'a',
            'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'y', 'y', 'y', 'y', 'Y', 'Y', 'Y', 'Y', 'Y',
            'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E',
            'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
            'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U',
            'u', 'U', 'u',};
    private static final String tenDigitNumber = "^\\d{10}$";
    private static final String tenDigitsNumberWhitespacesDotHyphen = "^(\\d{3}[- .]?){2}\\d{4}$";
    private static final String tenDigitsNumberParenthesis = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
    private static final String tenDigitNumberInternationalPrefix = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
    private static final String pattern = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    private static final String phoneValidate = "(?:([+]\\d{1,4})[-.\\s]?)?(?:[(](\\d{1,3})[)][-.\\s]?)?(\\d{1,4})[-.\\s]?(\\d{1,4})[-.\\s]?(\\d{1,9})";


    static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {

        }
    }

    public CommonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Kiểm tra số điện thoại có hợp lệ
     */
    public static Boolean validatePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        String patterns = String.format("%s|%s|%s|%s|%s|%s", tenDigitNumber,
                tenDigitsNumberParenthesis, tenDigitsNumberWhitespacesDotHyphen, tenDigitNumberInternationalPrefix, pattern, phoneValidate);
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phone);
        System.out.println(patterns);
        return matcher.matches();
    }

    /**
     * Kiểm tra tính hợp lệ của email
     */
    public static Boolean validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher m = pattern.matcher(email);
        return m.find();
    }

    /**
     * Kiểm tra độ dài tối thiểu
     */
    public static Boolean validateMinLength(String content, int min) {
        if (StringUtils.isBlank(content)) {
            return 0 >= min;
        }

        content = content.trim();
        return content.length() >= min;
    }

    /**
     * Kiểm tra độ dài tối đa
     */
    public static Boolean validateMaxLength(String content, int max) {
        if (StringUtils.isBlank(content)) {
            return 0 <= max;
        }

        content = content.trim();
        return content.length() <= max;
    }

    /**
     * Kiểm tra tính hợp lệ của mật khẩu
     */
    public static Boolean validatePassword(String password) {
        if (!validateMinLength(password, 6)) {
            return false;
        }

        //Trường hợp đặc cách nếu dùng số đt (...)
        password = password.trim();
        if (password.length() == 10 || password.length() == 11) {
            return true;
        }

        Pattern pattern1 = Pattern.compile("[a-zA-Z]");
        Matcher m1 = pattern1.matcher(password);

        Pattern pattern2 = Pattern.compile("[0-9]");
        Matcher m2 = pattern2.matcher(password);
        return (m1.find() && m2.find());
    }

    /**
     * Kiểm tra tính hợp lệ của link (Khi nhập link youtube, ...)
     */
    public static Boolean validateURL(String link) {
        if (StringUtils.isBlank(link)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:\\/[\\+~%\\/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!\\/\\\\\\w]*))?)");
        Matcher matcher = pattern.matcher(link);
        return matcher.find();
    }

    public static Boolean validateDomain(String domain) {
        if (StringUtils.isBlank(domain)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\\\.)+[A-Za-z]{2,6}$");
        Matcher matcher = pattern.matcher(domain);
        return matcher.find();
    }

    public static String getDomain(String link) {
        String domainName = "";
        try {
            URI uri = new URI(link);
            String host = uri.getHost();
            domainName = host.startsWith("www.") ? host.substring(4) : host;
        } catch (Exception ex) {

        }

        return domainName;
    }

    /**
     * Kiểm tra tính hợp lệ của ngày (dd/mm/yyyy hoặc dd-mm-yyyy hoặc dd.mm.yyyy)
     */
    public static Boolean validateDate(String date) {
        if (StringUtils.isBlank(date)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        Matcher matcher = pattern.matcher(date);
        return matcher.find();
    }

    /**
     * Kiểm tra tính hợp lệ của thời gian (HH:ii)
     */
    public static Boolean validateTime(String time) {
        if (StringUtils.isBlank(time)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^$|^(([01][0-9])|(2[0-3])):[0-5][0-9]$");
        Matcher matcher = pattern.matcher(time);
        return matcher.find();
    }

    public static Timestamp convertStringToStartTimesDMY(String strDate) {
        return convertStringToTimestamp(strDate.concat(" 00:00:00"), "dd/MM/yyyy hh:mm:ss");
    }

    public static Timestamp convertStringToEndTimesDMY(String strDate) {
        return convertStringToTimestamp(strDate.concat(" 23:59:59"), "dd/MM/yyyy hh:mm:ss");
    }

    public static Timestamp convertStringToTimestamp(String strDate, String format) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        if (StringUtils.isEmpty(format)) {
            format = "dd/MM/yyyy";
        }

        try {
            DateFormat formatter = new SimpleDateFormat(format);

            // you can change format of date
            Date date = formatter.parse(strDate);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            return timeStampDate;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }


    /**
     * Lấy khoảng cách theo ngày
     */
    public static Long diffTwoDays(Timestamp t1, Timestamp t2) {
        final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
        long time1 = t1.getTime();
        long time2 = t2.getTime();

        // Set both times to 0:00:00
        time1 -= time1 % MILLIS_PER_DAY;
        time2 -= time2 % MILLIS_PER_DAY;

        return TimeUnit.DAYS.convert(time1 - time2, TimeUnit.MILLISECONDS);
    }

    public static Date convertStringToDate(String strDate, String format) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        if (StringUtils.isEmpty(format)) {
            format = "dd/MM/yyyy";
        }

        try {
            DateFormat formatter = new SimpleDateFormat(format);

            // you can change format of date
            Date date = formatter.parse(strDate);

            return date;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }


    /**
     * Chuyển sang thời gian, dựa trên chuỗi thời gian
     */
    public static Time convertStringToTime(String strTime, String... formats) {
        if (StringUtils.isEmpty(strTime)) {
            return null;
        }

        String format = "";
        if (formats != null && formats.length > 0) {
            format = formats[0];
        }

        if (StringUtils.isEmpty(format)) {
            format = "HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long ms = sdf.parse(strTime).getTime();
            Time time = new Time(ms);

            System.out.println("Converted Time: " + time);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Timestamp convertStringAutoDetectFormat(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        Timestamp ts = null;

        try {
            ts = new Timestamp(Long.parseLong(strDate));
        } catch (Exception e) {
            try {
                //Thử thêm 1 định dạng khác
                String format = "MMM d, yyyy HH:mm:ss a"; //May 2, 2024 12:00:00 AM
                DateFormat formatter = new SimpleDateFormat(format);
                Date date = formatter.parse(strDate);
                ts = new Timestamp(date.getTime());
            } catch (Exception ex) {
                return null;
            }
        }

        return ts;
    }

    /**
     * Trả về ngày tối đa của tháng đó
     */
    public static Integer getLastDayOfMonth(Integer month, Integer year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                //Năm nhuận thì có 29 ngày
                //Năm nhuận là năm chia hết cho 4 nhưng không chia hết cho 100
                Boolean isLeapYear = (year % 4 == 0) && (year % 100 > 0);
                if (isLeapYear) {
                    return 29;
                }

                return 28;
            default:
                return 30;
        }
    }

    public static Integer getDayOfWeek(Integer day, Integer month, Integer year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); //month based 0
        //CN = 1, T2 = 2, ..., T7 = 7
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * Lấy số ngày lớn nhất của năm/tháng
     */
    public static Integer getMaxDayOfMonth(Integer month, Integer year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                //Tính năm nhuận
                if (CommonUtils.NVL(year) > 0) {
                    if (year % 400 == 0) {
                        return 29;
                    }
                    if (year % 100 == 0) {
                        return 28;
                    }
                    if (year % 4 == 0) {
                        return 29;
                    }
                    return 28;
                }
                return 28;
            default:
                return 30;
        }
    }

    /**
     * So sánh 2 mốc thời gian (timestamp - ngày:giờ), t2: ngày ==> Chỉ so sánh theo ngày
     */
    public static boolean compareTwoDate(Timestamp t1, Date t2) {
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(t1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTime(t2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        return cal1.getTime().equals(cal2.getTime());
    }

    public static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String convertTimestampToString(Timestamp ts, String format) {
        if (StringUtils.isBlank(format)) {
            format = "dd/MM/yyyy";
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate = dateFormat.format(ts);
        return strDate;
    }

    public static String convertToAsiaTime(Timestamp ts, String format) {
        if (format == null || format.trim().isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        // Chuyển đổi Timestamp thành giờ của server (GMT+7)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.of("Asia/Bangkok"));  // GMT+7

        return formatter.format(ts.toInstant());
    }

    public static String convertDateToString(Date date, String format) {
        if (StringUtils.isBlank(format)) {
            format = "dd/MM/yyyy";
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static Long NVL(Long l) {
        return NVL(l, 0l);
    }

    public static Boolean NVL(Boolean l) {
        if (l == null) {
            return false;
        }

        return l;
    }

    public static Double NVL(Double l) {
        return NVL(l, 0.0);
    }

    public static Long NVL(Long l, Long defaultVal) {
        return (l == null ? defaultVal : l);
    }

    public static Integer NVL(Integer t) {
        return NVL(t, 0);
    }

    public static Integer NVL(Integer t, Integer defaultVal) {
        return (t == null ? defaultVal : t);
    }

    public static Double NVL(Double t, Double defaultVal) {
        return (t == null ? defaultVal : t);
    }

    public static Float NVL(Float t, Float defaultVal) {
        return (t == null ? defaultVal : t);
    }

    public static <T> Boolean isEmpty(List<T> lst) {
        if (lst == null || lst.isEmpty()) {
            return true;
        }

        return false;
    }

    public static <T> List<T> NVL(List<T> lst) {
        if (lst == null || lst.isEmpty()) {
            return new ArrayList<>();
        }

        return lst;
    }

    public static String NVL(String l) {
        if (StringUtils.isBlank(l)) {
            return "";
        }

        return l.trim();
    }

    public static String NVL(String l, String defaultVal) {
        return l == null ? defaultVal : l.trim();
    }

    public static String getRandomPassword() {
        String storeAlphabet = "abcdefghijklmnpqrstuvwxyz";
        String storeAlphabetUpper = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
        String storeNumber = "0123456789";

        Integer alphabetUpperLength = (int) Math.floor(Math.random() * 2);
        if (alphabetUpperLength == 0) {
            alphabetUpperLength = 1;
        }

        Integer numberLength = (int) Math.floor(Math.random() * 2);
        if (numberLength == 0) {
            numberLength = 1;
        }

        Integer alphabetLength = 6 - alphabetUpperLength - numberLength;

        String passwordAlphabet = "";
        for (int i = 0; i < alphabetLength; i++) {
            int pos = (int) Math.floor(Math.random() * storeAlphabet.length());
            passwordAlphabet += storeAlphabet.charAt(pos);
        }

        String passwordAlphabetUpper = "";
        for (int i = 0; i < alphabetUpperLength; i++) {
            int pos = (int) Math.floor(Math.random() * storeAlphabetUpper.length());
            passwordAlphabetUpper += storeAlphabetUpper.charAt(pos);
        }

        String passwordNumber = "";
        for (int i = 0; i < numberLength; i++) {
            int pos = (int) Math.floor(Math.random() * storeNumber.length());
            passwordNumber += storeNumber.charAt(pos);
        }

        String password = passwordAlphabet + passwordAlphabetUpper + passwordNumber;
        return swapPair(password);
    }

    public static String swapPair(String str) {

        if (str == null || str.isEmpty())
            return str;

        char[] ch = str.toCharArray();

        for (int i = 0; i < ch.length - 1; i += 2) {

            char temp = ch[i];
            ch[i] = ch[i + 1];
            ch[i + 1] = temp;
        }

        return new String(ch);
    }


    public static String getMobileOperatingSystem(String userAgent) {
        // Windows Phone must come first because its UA also contains "Android"
        Pattern pattern = Pattern.compile("/windows phone/", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userAgent.toLowerCase());

        if (matcher.find()) {
            return "Windows Phone";
        }

        pattern = Pattern.compile("android", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(userAgent.toLowerCase());
        if (matcher.find()) {
            return "Android";
        }

        // iOS detection from: http://stackoverflow.com/a/9039885/177710
        pattern = Pattern.compile("/iPad|iPhone|iPod/");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            return "iOS";
        }

        return "unknown";
    }

    public static String getYYYYMMDD(long publishedTime) {
        Timestamp ts = new Timestamp(publishedTime);
        return new SimpleDateFormat("yyyyMMdd").format(ts);
    }

    public static char removeAccent(char ch) {
        int index = ArrayUtils.indexOf(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * chuyen doi so nguyen n sang he co so b
     */
    public static String convertNumber(int n, int b) {
        if (n < 0 || b < 2 || b > 36) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int m;
        int remainder = n;

        while (remainder > 0) {
            if (b > 10) {
                m = remainder % b;
                if (m >= 10) {
                    sb.append((char) (CHAR_SEVEN + m));
                } else {
                    sb.append(m);
                }
            } else {
                sb.append(remainder % b);
            }
            remainder = remainder / b;
        }
        return sb.reverse().toString();
    }

    /**
     * Tạo ra chuỗi ký tự ngẫu nhiên làm khóa mật
     */
    public static String getRandomClientKey(Integer size) {
        String storeAlphabet = "abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ0123456789";

        String clientKey = "";
        for (int i = 0; i < size; i++) {
            int pos = (int) Math.floor(Math.random() * size);
            clientKey += storeAlphabet.charAt(pos);
        }

        return swapPair(clientKey);
    }

    /**
     * Chuyển đổi từ đối tượng nguồn sang đối tượng đích, theo một luật đuợc quy định
     *
     * @param jsonSource
     * @param jsonMapper
     * @return
     */
    @SneakyThrows
    public static MyPair<String, JSONObject> convertToTargetObject(JSONObject jsonSource, JSONObject jsonMapper) {
        JSONObject jsonTarget = new JSONObject();
        Class<?> classObject = jsonSource.getClass();
        Field[] fields = classObject.getDeclaredFields();
        List<String> lstField = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(jsonSource);
            String type = field.getType().getSimpleName();
            if (type.equals("Map")) {
                HashMap<String, Object> hashedFieldValue = (HashMap<String, Object>) fieldValue;

                //Lặp trên khóa và giá trị
                for (Map.Entry<String, Object> entry : hashedFieldValue.entrySet()) {
                    Object value = entry.getValue();

                    //Nếu nằm trong danh sách field thì thêm vào đối tượng đích
                    //Trường hợp ánh xạ 1 - nhiều chưa giải
                    String targetFieldName = jsonMapper.optString(entry.getKey());

                    if (!StringUtils.isBlank(targetFieldName)) {
                        //Trường hợp value là kiểu time thì đổi sang chuỗi để giữ nguyên
                        if (value instanceof Timestamp
                                || value instanceof Date
                                || value instanceof java.sql.Date) {
                            System.out.println("Type Date =>");
                            System.out.println(value);
                        } else {
                            jsonTarget.put(targetFieldName, value);

                            //Ngoại trừ trường ngày/tháng
                            lstField.add(targetFieldName);
                        }
                    }
                }
            }
        }

        return new MyPair<>(String.join(",", lstField), jsonTarget);
    }

    /**
     * Kiểm tra có phải số nguyên
     */
    public static boolean isInteger(String str) {
        return str != null && str.matches("-?\\d+");
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        //Bỏ qua field không mapping được
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //Không fail nếu null cho primitive (int, long,...)
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        return mapper;
    }

    public static String nextUID() {
        return UUID.randomUUID().toString();
    }

    public static String getJSON(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }


}
