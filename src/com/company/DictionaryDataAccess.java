package com.company;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public final class DictionaryDataAccess {

    private static DictionaryDataAccess instance = null;

    private final ConcurrentHashMap<String, String> dictionary;

    private DictionaryDataAccess(String fileName){
        this.dictionary = CreateDictionary(fileName);
    }
    public static synchronized DictionaryDataAccess getInstance(String fileName) {
        if(instance == null){
            instance = new DictionaryDataAccess(fileName);
        }
        return instance;
    }
    private static ConcurrentHashMap<String, String> CreateDictionary(String fileName)
    {
        ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>();
        String txt = ReadDictionaryTextFile(fileName);
        String[] lines = txt.split(System.getProperty("line.separator"));
        for(String line : lines){
            String[] split = line.split("\\t");
            dictionary.putIfAbsent(split[0], split[1]);
            //System.out.println(dictionary.get(split[0]));
        }
        return dictionary;
    }
    private static String ReadDictionaryTextFile(String fileName)
    {
        // Open Text File
        String txt = "";
        try {
            File dictionaryFile = new File(fileName);
            Scanner dictionaryReader = new Scanner(dictionaryFile);
            while (dictionaryReader.hasNextLine()) {
                txt += dictionaryReader.nextLine();
                txt += "\r\n";
            }
            dictionaryReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. Please ensure the file path and name are correct.");
            e.printStackTrace();
        }
        //System.out.println(txt);
        return txt;
    }
    private String QueryDictionary(String query)
    {
        return this.dictionary.get(query);
    }
    private String InsertWord(JSObject word)
    {
        return this.dictionary.putIfAbsent(word.getMember("Key").toString(), word.getMember("Value").toString());
    }
    private String DeleteWord(String word)
    {
        return this.dictionary.remove(word);
    }
    private String UpdateWord(JSObject word)
    {
        return this.dictionary.replace(word.getMember("Key").toString(), word.getMember("Value").toString());
    }
}
