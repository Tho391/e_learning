package com.example.dardan.elearning;

import android.net.Uri;

public class Ulti {
    public static Uri getUrl(int res){
        return Uri.parse("android.resource://com.example.project/" + res);
    }
}
