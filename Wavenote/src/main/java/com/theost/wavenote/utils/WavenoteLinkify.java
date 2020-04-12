package com.theost.wavenote.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

public class WavenoteLinkify {

    static public final String WAVENOTE_SCHEME = "wavenote";
    static public final Pattern WAVENOTE_LINK_PATTERN = Pattern.compile("wavenote://preferences/[^\\s]*");

    // Works the same as Linkify.addLinks, but doesn't set movement method
    public static boolean addLinks(TextView text, int mask) {
        if (mask == 0) {
            return false;
        }

        CharSequence t = text.getText();

        if (t instanceof Spannable) {
            boolean linked = Linkify.addLinks((Spannable) t, mask);
            Linkify.addLinks((Spannable) t, WAVENOTE_LINK_PATTERN, WAVENOTE_SCHEME);

            return linked;
        } else {
            SpannableString s = SpannableString.valueOf(t);

            if (Linkify.addLinks(s, mask)) {
                text.setText(s);
                return true;
            }

            return false;
        }
    }
}
