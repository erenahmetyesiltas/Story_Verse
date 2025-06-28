package com.sweproject.storyVerse.security.passwordValidation;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordValidation {
    public String isValidPassword(String password){

        String message = "";

        if(!isEnoughLength(password)){
            message+="The password must be at least 8 characters.\n";
        }
        if(!isIncludeAnyNumber(password)){
            message+="The password must be includes at least one number.";
        }

        return message;
    }

    public boolean isEnoughLength(String password){
        if(password.length() < 8){
            return false;
        }
        return true;
    }

    public boolean isIncludeAnyNumber(String password){

        char c;
        char[] numbers = {'0','1','2','3','4','5','6','7','8','9'};

        for (int i = 0; i < password.length(); i++) {
            c = password.charAt(i);

            for (int j = 0; j < numbers.length; j++) {
                if(c == numbers[j]){
                    return true;
                }
            }
        }
        return false;
    }
}
