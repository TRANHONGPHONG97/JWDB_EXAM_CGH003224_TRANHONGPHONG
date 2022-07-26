package com.codegym.jwdb_exam_tranhongphong.utils;

import java.util.regex.Pattern;

public class ValidateUtils {

        public static final String NUMBER_REGEX = "\\d+";
        public static final String LETTER_WITHOUT_NUMBER_REGEX = "^([A-Z]+[a-z]*[ ]?)+$";
        public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
//        public static final String PHONE_REGEX ="^[0][1-9][0-9]{8}$|^[+84][1-9][0-9]{10}$";
    public static final String PHONE_REGEX ="(84|0[1|2|3|4|5|6|7|8|9])+([0-9]{8})\\b";
        public static final String QUANTITY_REGEX = "^[1-9][0-9]{0,2}";
        // 1-999
        public static final String PRICE_REGEX = "^[1-9][0-9]{1,10}";
//10 - 99 999 999 999
        public static final String PASSWORD_REGEX = "^[A-Z][a-z0-9]{8,24}";
    public static final String USER_NAME_REGEX = "^([A-Z]+[a-z]*[ ]?)+$";

        public static boolean isPriceValid(String price) {
            return Pattern.compile(PRICE_REGEX).matcher(price).matches();
        }

        public static boolean isQuantityValid(String quantity) {
            return Pattern.compile(QUANTITY_REGEX).matcher(quantity).matches();
        }

        public static boolean isPhoneValid(String phone) {
            return Pattern.compile(PHONE_REGEX).matcher(phone).matches();
        }

        public static boolean isNumberValid(String number) {
            return Pattern.compile(NUMBER_REGEX).matcher(number).matches();
        }

        public static boolean isLetterWithoutNumberValid(String name) {
            return Pattern.compile(LETTER_WITHOUT_NUMBER_REGEX).matcher(name).matches();
        }

        public static boolean isEmailValid(String email) {
            return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
        }
    public static boolean isPasswordValid(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
    public static boolean isUserNameValid(String email) {
        return Pattern.compile(USER_NAME_REGEX).matcher(email).matches();
    }
    }
