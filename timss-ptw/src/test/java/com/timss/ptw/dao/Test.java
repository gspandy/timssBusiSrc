package com.timss.ptw.dao;

import java.text.DecimalFormat;

public class Test {
    public static void main(String[] args) {
        String fileName = "Jellyfish.jpg";
        System.out.println(fileName.substring( 0,fileName.lastIndexOf( "." )));
        DecimalFormat df = new DecimalFormat("0000");
        System.out.println(df.format( 123 ));
        System.out.println(df.format( 12345 ));
    }
}
