package sne.bcs.util;

import android.content.SharedPreferences;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.Locale;

/**
 * Collection of utilities related to barcode.
 */
public class BarcodeUtils
{
    /**
     * Validates checksum of barcode.
     *
     * @param barcode EAN-13 or ISBN-13 or EAN-8 or UPC-12 barcode
     * @return {@code true} if {@code barcode} is valid
     */
    public static boolean validateBarcode(String barcode)
    {
        if (barcode == null) return false;
        //if (barcode.length() != 13 && barcode.length() != 8) return false;

        int sum = 0;
        for (int i = barcode.length() - 1, j = 0; i >= 0; --i, ++j)
        {
            char c = barcode.charAt(i);
            if (!Character.isDigit(c)) return false;

            int d = c - '0';
            if (j % 2 == 0)
            {
                sum += d;
            }
            else
            {
                sum += 3 * d;
            }
        }

        return sum % 10 == 0;
    }

    public static String barcodeValueFormat(int valueFormat)
    {
        switch (valueFormat)
        {
            case Barcode.CALENDAR_EVENT:
                return "CALENDAR_EVENT";
            case Barcode.CONTACT_INFO:
                return "CONTACT_INFO";
            case Barcode.DRIVER_LICENSE:
                return "DRIVER_LICENSE";
            case Barcode.EMAIL:
                return "EMAIL";
            case Barcode.GEO:
                return "GEO";
            case Barcode.ISBN:
                return "ISBN";
            case Barcode.PHONE:
                return "PHONE";
            case Barcode.PRODUCT:
                return "PRODUCT";
            case Barcode.SMS:
                return "SMS";
            case Barcode.TEXT:
                return "TEXT";
            case Barcode.URL:
                return "URL";
            case Barcode.WIFI:
                return "WIFI";

            default:
                return String.valueOf(valueFormat);
        }
    }

    public static String barcodeFormat(int format)
    {
        switch (format)
        {
            case Barcode.CODABAR:
                return "CODABAR";
            case Barcode.CODE_128:
                return "CODE_128";
            case Barcode.CODE_39:
                return "CODE_39";
            case Barcode.CODE_93:
                return "CODE_93";
            case Barcode.DATA_MATRIX:
                return "DATA_MATRIX";
            case Barcode.EAN_13:
                return "EAN_13";
            case Barcode.EAN_8:
                return "EAN_8";
            case Barcode.ITF:
                return "ITF";
            case Barcode.PDF417:
                return "PDF417";
            case Barcode.QR_CODE:
                return "QR_CODE";
            case Barcode.UPC_A:
                return "UPC_A";
            case Barcode.UPC_E:
                return "UPC_E";

            default:
                return String.valueOf(format);
        }
    }

    /**
     * Returns country name according to GS1 prefix list.
     * Note that since GS1 member companies can manufacture
     * products anywhere in the world, GS1 prefixes do not
     * identify the country of origin for a given product.
     * <br/>
     * From <a href="http://www.gs1.org/company-prefix">GS1 prefix list</a>.
     *
     * @param barcode barcode string
     * @param language country language, such as "en", "ru"
     * @return country name according to GS1 prefix list and language, or
     * {@code null} if country cannot be identified.
     * If language cannot be identified, then english is used.
     */
    public static String barcodeGS1Country(String barcode, String language)
    {
        if (barcode == null || barcode.length() < 3)
        {
            return null;
        }

        String prefix = barcode.substring(0, 3);
        int code;
        try
        {
            code = Integer.valueOf(prefix);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        final int[] code_min = new int[]{
                0, 30, 50,
                300, 380, 383, 385, 387, 389,
                400, 450, 490, 460, 470, 471,
                474, 475, 476, 477, 478, 479,
                480, 481, 482, 483, 484, 485,
                486, 487, 488, 489,
                500, 520, 528, 529,
                530, 531, 535, 539,
                540, 560, 569, 570,
                590, 594, 599,
                600, 603, 604, 608, 609,
                611, 613, 615, 616, 618, 619,
                620, 621, 622, 623, 624,
                625, 626, 627, 628, 629,
                640, 690,
                700, 729, 730, 740, 741,
                742, 743, 744, 745, 746,
                750, 754, 759, 760,
                770, 773, 775, 777, 778,
                780, 784, 786, 789,
                800, 840, 850, 858, 859,
                860, 865, 867, 868,
                870, 880, 884, 885, 888,
                890, 893, 896, 899,
                900, 930, 940,
                955, 958
        };
        final int[] code_max = new int[]{
                19, 39, 139,
                379, 380, 383, 385, 387, 389,
                440, 459, 499, 469, 470, 471,
                474, 475, 476, 477, 478, 479,
                480, 481, 482, 483, 484, 485,
                486, 487, 488, 489,
                509, 521, 528, 529,
                530, 531, 535, 539,
                549, 560, 569, 579,
                590, 594, 599,
                601, 603, 604, 608, 609,
                611, 613, 615, 616, 618, 619,
                620, 621, 622, 623, 624,
                625, 626, 627, 628, 629,
                649, 699,
                709, 729, 739, 740, 741,
                742, 743, 744, 745, 746,
                750, 755, 759, 769,
                771, 773, 775, 777, 779,
                780, 784, 786, 790,
                839, 849, 850, 858, 859,
                860, 865, 867, 869,
                879, 880, 884, 885, 888,
                890, 893, 896, 899,
                919, 939, 949,
                955, 958
        };
        final String[] countries_en = new String[]{
                "US", "US", "US",
                "France", "Bulgaria", "Slovenija", "Croatia", "Bosnia-Herzegovina", "Montenegro",
                "Germany", "Japan", "Japan", "Russia", "Kyrgyzstan", "Taiwan",
                "Estonia", "Latvia", "Azerbaijan", "Lithuania", "Uzbekistan", "Sri Lanka",
                "Philippines", "Belarus", "Ukraine", "Turkmenistan", "Moldova", "Armenia",
                "Georgia", "Kazakstan", "Tajikistan", "Hong Kong",
                "UK", "Greece", "Lebanon", "Cyprus",
                "Albania", "Macedonia", "Malta", "Ireland",
                "Belgium & Luxembourg", "Portugal", "Iceland", "Denmark",
                "Poland", "Romania", "Hungary",
                "South Africa", "Ghana", "Senegal", "Bahrain", "Mauritius",
                "Morocco", "Algeria", "Nigeria", "Kenya", "Ivory Coast", "Tunisia",
                "Tanzania", "Syria", "Egypt", "Brunei", "Libya",
                "Jordan", "Iran", "Kuwait", "Saudi Arabia", "Emirates",
                "Finland", "China",
                "Norway", "Israel", "Sweden", "Guatemala", "El Salvador",
                "Honduras", "Nicaragua", "Costa Rica", "Panama", "Republica Dominicana",
                "Mexico", "Canada", "Venezuela", "Schweiz, Suisse, Svizzera",
                "Colombia", "Uruguay", "Peru", "Bolivia", "Argentina",
                "Chile", "Paraguay", "Ecuador", "Brasil",
                "Italy", "Spain", "Cuba", "Slovakia", "Czech",
                "Serbia", "Mongolia", "North Korea", "Turkey",
                "Netherlands", "South Korea", "Cambodia", "Thailand", "Singapore",
                "India", "Vietnam", "Pakistan", "Indonesia",
                "Austria", "Australia", "New Zealand",
                "Malaysia", "Macau"
        };
        final String[] countries_ru = new String[]{
                "США", "США", "США",
                "Франция", "Болгария", "Словения", "Хорватия", "Босния-Герцеговина", "Черногория",
                "Германия", "Япония", "Япония", "Россия", "Кыргызстан", "Тайвань",
                "Эстония", "Латвия", "Азербайджан", "Литва", "Узбекистан", "Шри Ланка",
                "Филиппины", "Белоруссия", "Украина", "Туркменистан", "Молдавия", "Армения",
                "Грузия", "Казахстан", "Таджикистан", "Гон Конг",
                "Великобритания", "Греция", "Ливан", "Кипр",
                "Албания", "Македония", "Мальта", "Ирландия",
                "Бельгия & Люксембург", "Португалия", "Исландия", "Дания",
                "Польша", "Румыния", "Венгрия",
                "ЮАР", "Гана", "Сенегал", "Бахрейн", "Мавритания",
                "Morocco", "Algeria", "Nigeria", "Kenya", "Ivory Coast", "Tunisia",
                "Tanzania", "Сирия", "Египет", "Brunei", "Libya",
                "Jordan", "Иран", "Kuwait", "Saudi Arabia", "Emirates",
                "Финляндия", "Китай",
                "Норвегия", "Израиль", "Швеция", "Guatemala", "El Salvador",
                "Honduras", "Nicaragua", "Costa Rica", "Panama", "Republica Dominicana",
                "Мексика", "Канада", "Венесуэла", "Швейцария",
                "Colombia", "Уругвай", "Перу", "Боливия", "Аргентина",
                "Чили", "Paraguay", "Ecuador", "Бразилия",
                "Италия", "Испания", "Куба", "Словакия", "Чехия",
                "Сербия", "Монголия", "КНДР", "Турция",
                "Нидерланды", "Южная Корея", "Cambodia", "Тайланд", "Singapore",
                "Индия", "Вьетнам", "Пакистан", "Индонезия",
                "Австрия", "Австралия", "Новая Зеландия",
                "Malaysia", "Macau"
        };

        String[] countries = countries_en;
        if ("ru".equals(language))
        {
            countries = countries_ru;
        }

        for (int i = 0; i < countries.length; ++i)
        {
            if (code >= code_min[i] && code <= code_max[i])
            {
                return countries[i];
            }
        }

        return null;
    }

    /**
     *
     * @param prefs preferences needed to define language
     * @param barcodeValue
     * @return {@code barcodeValue (country)} where country is displayed according to preferences.
     */
    public static String showBarcodeWithCountry(SharedPreferences prefs, String barcodeValue)
    {
        String language = Locale.getDefault().getCountry().toLowerCase();
        String country = barcodeGS1Country(barcodeValue, language);
        if (country == null)
        {
            country = "";
        }
        else
        {
            country = " ("+country+")";
        }
        return barcodeValue + country;
    }

    public static String barcodeData(Barcode barcode)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Format ").append(barcodeFormat(barcode.format)).append('\n');
        sb.append("Value Format ").append(barcodeValueFormat(barcode.valueFormat)).append('\n');
        sb.append(barcode.displayValue).append('\n');

        switch (barcode.valueFormat)
        {
            case Barcode.CALENDAR_EVENT:
                appendCalendarEvent(sb, barcode);
                break;
            case Barcode.CONTACT_INFO:
                appendContactInfo(sb, barcode);
                break;
            case Barcode.DRIVER_LICENSE:
                appendDriverLicense(sb, barcode);
                break;
            case Barcode.GEO:
                appendGeoPoint(sb, barcode);
                break;
            case Barcode.PHONE:
                appendPhone(sb, barcode);
                break;
            case Barcode.EMAIL:
                appendEmail(sb, barcode);
                break;
            case Barcode.URL:
                appendUrl(sb, barcode);
                break;
            case Barcode.SMS:
                appendSMS(sb, barcode);
                break;
            case Barcode.WIFI:
                appendWifi(sb, barcode);
                break;
        }
        return sb.toString();
    }

    private static void appendContactInfo(StringBuilder sb, Barcode barcode)
    {
        Barcode.ContactInfo info = barcode.contactInfo;
        sb.append("Person Name ").append(info.name).append('\n');
        sb.append("Organization ").append(info.organization).append('\n');
        sb.append("Title ").append(info.title).append('\n');

        if (info.phones != null && info.phones.length > 0)
        {
            sb.append("Phones:").append('\n');
            for (Barcode.Phone ph : info.phones)
            {
                sb.append("   ").append(phoneToString(ph)).append('\n');
            }
        }

        if (info.emails != null && info.emails.length > 0)
        {
            sb.append("e-mail:").append('\n');
            for (Barcode.Email em : info.emails)
            {
                sb.append("   ").append(emailToString(em)).append('\n');
            }
        }

        if (info.urls != null && info.urls.length > 0)
        {
            sb.append("URL:").append('\n');
            for (String u : info.urls)
            {
                sb.append("   ").append(u).append('\n');
            }
        }

        if (info.addresses != null && info.addresses.length > 0)
        {
            sb.append("Addresses:").append('\n');
            for (Barcode.Address a : info.addresses)
            {
                sb.append("   ").append(addressToString(a)).append('\n');
            }
        }
    }

    private static void appendCalendarEvent(StringBuilder sb, Barcode barcode)
    {
        Barcode.CalendarEvent info = barcode.calendarEvent;
        // TODO
    }

    private static void appendDriverLicense(StringBuilder sb, Barcode barcode)
    {
        Barcode.DriverLicense info = barcode.driverLicense;
        sb.append("Document Type ").append(info.documentType).append('\n');
        sb.append("First Name ").append(info.firstName).append('\n');
        sb.append("Middle Name ").append(info.middleName).append('\n');
        sb.append("Last Name ").append(info.lastName).append('\n');
        sb.append("Gender ").append(info.gender).append('\n');
        sb.append("Address Street ").append(info.addressStreet).append('\n');
        sb.append("Address City ").append(info.addressCity).append('\n');
        sb.append("Address State ").append(info.addressState).append('\n');
        sb.append("Address Zip ").append(info.addressZip).append('\n');
        sb.append("License Number ").append(info.licenseNumber).append('\n');
        sb.append("Issue Date ").append(info.issueDate).append('\n');
        sb.append("Expiry Date ").append(info.expiryDate).append('\n');
        sb.append("Birth Date ").append(info.birthDate).append('\n');
        sb.append("Issuing Country ").append(info.issuingCountry).append('\n');
    }

    private static void appendGeoPoint(StringBuilder sb, Barcode barcode)
    {
        Barcode.GeoPoint info = barcode.geoPoint;
        sb.append("Geo Point (").append(info.lat).append(", ").append(info.lng).append(")\n");
    }

    private static void appendPhone(StringBuilder sb, Barcode barcode)
    {
        Barcode.Phone info = barcode.phone;
        sb.append("Phone ").append(phoneToString(info)).append('\n');
    }

    private static void appendEmail(StringBuilder sb, Barcode barcode)
    {
        Barcode.Email info = barcode.email;
        sb.append("e-mail ").append(emailToString(info)).append('\n');
    }

    private static void appendUrl(StringBuilder sb, Barcode barcode)
    {
        Barcode.UrlBookmark info = barcode.url;
        sb.append("URL ").append(info.title).append(": ").append(info.url).append('\n');
    }

    private static void appendSMS(StringBuilder sb, Barcode barcode)
    {
        Barcode.Sms info = barcode.sms;
        // TODO
    }

    private static void appendWifi(StringBuilder sb, Barcode barcode)
    {
        Barcode.WiFi info = barcode.wifi;
        // TODO
    }

    private static String phoneType(int phoneType)
    {
        switch (phoneType)
        {
            case Barcode.Phone.WORK: return "Work";
            case Barcode.Phone.HOME: return "Home";
            case Barcode.Phone.FAX: return "Fax";
            case Barcode.Phone.MOBILE: return "Mobile";
            default: return "";
        }
    }

    private static String phoneToString(Barcode.Phone phone)
    {
        return phoneType(phone.type)+" "+phone.number;
    }

    private static String emailType(int emailType)
    {
        switch (emailType)
        {
            case Barcode.Email.WORK: return "Work";
            case Barcode.Email.HOME: return "Home";
            default: return "";
        }
    }

    private static String emailToString(Barcode.Email email)
    {
        return emailType(email.type)+" To: "+email.address + "\n" +
                "Subject: "+email.subject+'\n'+
                email.body;
    }

    private static String addressType(int addressType)
    {
        switch (addressType)
        {
            case Barcode.Address.WORK: return "Work";
            case Barcode.Address.HOME: return "Home";
            default: return "";
        }
    }

    private static String addressToString(Barcode.Address addr)
    {
        String prefix = addressType(addr.type) + " ";
        StringBuilder sb = new StringBuilder(prefix);
        String offset = Mix.spaces(prefix.length());
        for (int i = 0; i < addr.addressLines.length; ++i)
        {
            String a = addr.addressLines[i];
            if (i == 0)
            {
                sb.append(a).append('\n');
            }
            else
            {
                sb.append(offset).append(a).append('\n');
            }
        }
        return sb.toString();
    }
}
