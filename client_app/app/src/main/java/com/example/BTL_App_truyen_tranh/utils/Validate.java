package com.example.BTL_App_truyen_tranh.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
                + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
