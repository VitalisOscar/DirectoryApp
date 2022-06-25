package com.directoryapp.helpers;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Utils {

    public static void alert(Context context, String title, String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static ArrayList<String> generateSearchIndex(ArrayList<String> currentIndex, String... keywords){
        for(String keyword:keywords) {

            String[] splited = keyword.split(" ");
            String[] all = Arrays.copyOf(splited, splited.length + 1);
            all[splited.length] = keyword;

            String temp;

            for (String s : all) {
                temp = s;
                temp = temp.replace(" ", "");
                temp = temp.toLowerCase();

                for (int i = 0; i < temp.length(); i++) {
                    String searchTerm = "";
                    for (int j = 0; j < i; j++) {
                        searchTerm += temp.charAt(j);
                    }

                    searchTerm += temp.charAt(i);

                    if (!currentIndex.contains(searchTerm)) {
                        currentIndex.add(searchTerm);
                    }
                }
            }

        }

        return currentIndex;
    }

    public static String numberFormat(double n){
        double decimalPart = n - Math.floor(n);
        int num = (int) Math.floor(n);

        String str = String.valueOf(num);
        String fmt = "";

        int c = 0;
        for(int i = str.length(); i > 0; i--){
            if(c == 3){
                fmt = "," + fmt;
                c = 0;
            }

            fmt = str.charAt(i - 1) + fmt;
            c++;
        }

        return fmt + (decimalPart > 0 ? ("." + decimalPart):"");
    }

    public static String getTodayDate(){
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) + "-" +
                ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0":"") + (calendar.get(Calendar.MONTH) + 1) + "-" +
                (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0":"") + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();

        return (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0":"") + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                (calendar.get(Calendar.MINUTE) < 10 ? "0":"") + calendar.get(Calendar.MINUTE);
    }

    public static String getCurrentTimestamp(){
        return getTodayDate() + " " + getCurrentTime();
    }
}
