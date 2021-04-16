package com.company;
import Exceptions.WordAlreadyExists;
import Exceptions.WordNotFound;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public synchronized ArrayList<String> QueryDictionary(String query) throws WordNotFound
    {
        ArrayList<String> meanings = this.dictionary.get(query);
        if(meanings == null){
            throw new WordNotFound("The word you are looking for can not be found. Please insert the word," +
                    "or query another word.");
        }else{
            return meanings;
        }
    }
    public synchronized ArrayList<String> InsertWord(JSONObject insertObject) throws WordAlreadyExists
    {
        // Add all meanings into a List
        ArrayList<String> meaningList = new ArrayList<>();
        JSONArray meanings = insertObject.getJSONArray("Meanings");
        for( Object meaning : meanings){
            meaningList.add(meaning.toString());
        }
        ArrayList<String> oldMeanings = dictionary.get(insertObject.get("Word").toString());
        if(oldMeanings == null){
            dictionary.put(insertObject.get("Word").toString(), meaningList);
        }
        else{
            throw new WordAlreadyExists("The word you are trying to Insert already exists in the dictionary. " +
                    "Please use the update functionality if you wish to overwrite it's current meanings.");
        }
        return meaningList;
    }

    public synchronized ArrayList<String> DeleteWord(String word) throws WordNotFound
    {
        ArrayList<String> deletedMeanings = this.dictionary.remove(word);

        if(deletedMeanings == null){
            throw new WordNotFound("The word you are trying to delete could not be found.");
        }
        return deletedMeanings;
    }
    public synchronized ArrayList<String> UpdateMeaning(JSONObject updateObject) throws WordNotFound
    {
        // Replace current list of meanings with new one specified
        ArrayList<String> meaningList = new ArrayList<>();
        JSONArray meanings = updateObject.getJSONArray("Meanings");
        for( Object meaning : meanings){
            meaningList.add(meaning.toString());
        }

        ArrayList<String> oldMeanings = this.dictionary.replace(updateObject.get("Word").toString(), meaningList);
        if(oldMeanings == null){
            throw new WordNotFound("The word you are trying to update does not exist.");
        }
        else {
            return oldMeanings;
        }
    }
}
