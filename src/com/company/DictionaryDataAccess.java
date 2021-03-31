package com.company;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public final class DictionaryDataAccess {

    private static DictionaryDataAccess instance = null;

    private final HashMap<String, ArrayList<String>> dictionary;

    private DictionaryDataAccess(String fileName){
        this.dictionary = CreateDictionary(fileName);
    }
    public static synchronized DictionaryDataAccess getInstance(String fileName) {
        if(instance == null){
            instance = new DictionaryDataAccess(fileName);
        }
        return instance;
    }
    private static HashMap<String, ArrayList<String>> CreateDictionary(String fileName)
    {
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();
        String txt = ReadDictionaryTextFile(fileName);
        String[] lines = txt.split(System.getProperty("line.separator"));
        for(String line : lines){
            String[] split = line.split("\\t");
            String word = split[0];
            String meaning = split[1];

            // Check if the word already has a meaning.
            // If no -> create new meaningList
            // If yes -> update meaningList
            ArrayList<String> meanings = dictionary.get(word);
            if(meanings == null){
                meanings = new ArrayList<>();
                meanings.add(meaning);
            }
            else{
                meanings.add(meaning);
            }
            dictionary.put(word, meanings);
            System.out.println(dictionary.get(word));
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
    private synchronized ArrayList<String> QueryDictionary(String query)
    {
        return this.dictionary.get(query);
    }
    private synchronized ArrayList<String> InsertWord(JSObject word)
    {
        // Add all meanings into a List
        ArrayList<String> meanings = new ArrayList<>();
        meanings.add(word.getMember("Value").toString());

        return this.dictionary.putIfAbsent(word.getMember("Key").toString(), meanings);
    }
    private synchronized ArrayList<String> DeleteWord(String word)
    {
        return this.dictionary.remove(word);
    }
    private synchronized ArrayList<String> UpdateMeaning(JSObject word)
    {
        // Replace current list of meanings with new one specified
        ArrayList<String> meanings = new ArrayList<>();
        meanings.add(word.getMember("Value").toString());
        return this.dictionary.replace(word.getMember("Key").toString(), meanings);
    }
}
