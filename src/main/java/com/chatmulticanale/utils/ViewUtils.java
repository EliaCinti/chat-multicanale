package com.chatmulticanale.utils;

/**
 * Classe di utilità per gestire la stampa a console in modo standardizzato.
 */
public class ViewUtils {


    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void printSeparator() {
        println("-----------------------------------------------------");
    }

    public static void clearScreen() {
        for (int i = 0; i < 50; ++i) {
            println("");
        }
    }
}