package com.theost.wavenote.utils;

/*
 *  misc. string utilities
 *  added 01-Apr-2013 by Nick Bradbury
 */

import android.text.Spanned;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.theost.wavenote.models.Note.SPACE;

public class StrUtils {
    // suppress default constructor for non-instantiability
    private StrUtils() {
        throw new AssertionError();
    }

    // returns true if the passed string is null or empty
    public static boolean isBlankStr(String str) {
        return (str == null || str.length() == 0);
    }

    // returns true if the passed string is null, empty or nothing but whitespace
    public static boolean isBlankTrimStr(String str) {
        return (str == null) || (str.length() == 0) || (str.trim().length() == 0);
    }

    // returns "" if the passed string is null, otherwise returns the passed string
    public static String notNullStr(final String value) {
        if (value == null)
            return "";
        return value;
    }

    // check is text empty
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    // exception-less conversion of string to int
    public static int strToInt(final String value) {
        return strToInt(value, 0);
    }

    public static int strToInt(String value, int defaultInt) {
        if (value == null)
            return defaultInt;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultInt;
        }
    }

    // exception-less conversion of string to long
    public static long strToLong(final String value) {
        return strToLong(value, 0);
    }

    public static long strToLong(String value, long defaultLong) {
        if (value == null)
            return defaultLong;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultLong;
        }
    }

    // exception-less conversion of string to float
    public static float strToFloat(String value) {
        return strToFloat(value, 0f);
    }

    public static float strToFloat(String value, float defaultFloat) {
        if (value == null)
            return defaultFloat;
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultFloat;
        }
    }

    public static String urlEncode(final String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should never get here
            return "";
        }
    }

    public static String urlDecode(final String text) {
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should never get here
            return "";
        }
    }

    // returns true if passed strings are the same, handles null so caller can skip null check before comparison
    public static boolean isSameStr(String s1, String s2) {
        if (s1 == null || s2 == null)
            return (s1 == null && s2 == null);
        return s1.equals(s2);
    }

    // removes html from the passed string
    public static String stripHtml(final String html) {
        if (html == null)
            return "";

        // HtmlCompat.fromHtml().toString() is not high-performance, so skip whenever possible
        if (!html.contains("<") && !html.contains("&"))
            return html;

        // http://stackoverflow.com/a/7389663/1673548
        return HtmlCompat.fromHtml(html).toString().trim();
    }

    public static Spanned setTextToUpperCaseAndBold(String originalString) {
        if (TextUtils.isEmpty(originalString)) {
            return HtmlCompat.fromHtml("");
        }

        return HtmlCompat.fromHtml("<strong>" + originalString.toUpperCase() + "</strong>");
    }

    // Write string x times
    public static String repeat(String val, int count) {
        StringBuilder buf = new StringBuilder(val.length() * count);
        while (count-- > 0) {
            buf.append(val);
        }
        return buf.toString();
    }

    public static String formatFilename(String name) {
        return name.replaceAll("\\s+", "_").replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    public static String getFileName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    public static String getFileExtention(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    public static String convertToRequestFormat(String words, String summer) {
        return words.trim().toLowerCase().replaceAll("\\s+", SPACE).replaceAll(SPACE, summer);
    }

    public static String firstToUppercase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static boolean isStringEnglish(String text) {
        return text.matches("[a-zA-Z]+");
    }

}
