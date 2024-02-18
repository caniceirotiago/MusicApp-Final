package src.RockStar;

public class Validation {
    /**
     *Classe utilizada para organizar alguns métodos de validação
     */
    public static boolean isAnNumber(String pin){
        boolean isANumber = false;
        for (int i = 0; i < pin.length(); i++){
            if (pin.charAt(i) >= '0' && pin.charAt(i) <= '9') {
                isANumber = true;
                break;
            }
        }
        return isANumber;
    }
    public static boolean pinSizeIsCorrect(String pin){
        boolean correctSize = true;
        if (pin.length() < 4 || pin.length() > 8){
            correctSize = false;
        }
        return correctSize;
    }
    public static boolean hasNameOnlyLetters(String name){
        boolean isAValidName = true;
        for (int i = 0; i < name.length(); i++){
            char c = name.charAt(i);
            if(!(Character.isLetter(c) || c == ' ')) {
                isAValidName =false;
            }
        }
        return isAValidName;
    }
    public static boolean nameHasRightLenght(String name){
        boolean isAValidName = true;
        if (name.length() < 3 || name.length() > 30){
            isAValidName = false;
        }
        return isAValidName;
    }
    public static boolean usernameOrPassWordHasRightLenght(String username){
        boolean isAValidTerm = true;
        if (username.length() < 3 || username.length() > 20){
            isAValidTerm = false;
        }
        return isAValidTerm;
    }
    public static boolean emailValidation(String email){
        boolean validEmail = true;
        if(email == null || email.isEmpty()){
            validEmail = false;
        }
        int atCount = email.length() - email.replace("@","").length();
        if(atCount != 1){
            validEmail = false;
        }
        String[] emailDivision = email.split("@");
        if(emailDivision.length == 2){
            String beforeAt = emailDivision[0];
            String afterAt = emailDivision[1];
            if(beforeAt.isEmpty() || afterAt.isEmpty()){
                validEmail = false;
            }
            if(!afterAt.contains(".")){
                validEmail = false;
            }
            if(beforeAt.startsWith(".") || beforeAt.endsWith(".")){
                validEmail = false;
            }
            if(email.contains("..")) validEmail = false;
        }else validEmail = false;
        return validEmail;
    }
    public static boolean validMusicName(String name){
        boolean validName = true;
        if(name.isEmpty() || name.length() > 20) {
            validName = false;
        }
        return validName;
    }
}
