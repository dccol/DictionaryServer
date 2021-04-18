package com.company;
import Exceptions.InvalidCommand;
import Exceptions.MissingMeaning;
import Exceptions.WordAlreadyExists;
import Exceptions.WordNotFound;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public final class DictionaryDataAccess {

    private static DictionaryDataAccess instance = null;

    private final HashMap<String, ArrayList<String>> dictionary;

    private DictionaryDataAccess(String fileName){
        this.dictionary = createDictionary(fileName);
    }
    public static synchronized DictionaryDataAccess getInstance(String fileName) {
        if(instance == null){
            instance = new DictionaryDataAccess(fileName);
        }
        return instance;
    }
    private static HashMap<String, ArrayList<String>> createDictionary(String fileName)
    {
        try {
            HashMap<String, ArrayList<String>> dictionary = new HashMap<>();
            String txt = readDictionaryTextFile(fileName);
            String[] lines = txt.split(System.getProperty("line.separator"));
            for (String line : lines) {
                String[] split = line.split("\\t");
                String word = split[0];
                String meaning = split[1];

                // Check if the word already has a meaning.
                // If no -> create new meaningList
                // If yes -> update meaningList
                ArrayList<String> meanings = dictionary.get(word);
                if (meanings == null) {
                    meanings = new ArrayList<>();
                    meanings.add(meaning);
                } else {
                    meanings.add(meaning);
                }
                dictionary.put(word, meanings);
            }
            return dictionary;
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Error: the dictionary file does not match required format.\n" +
                    "Please ensure the specified file is tab delimited in format:\n" +
                    "<Word>\t<Meaning>\n" +
                    "<Word2>\t<Meaning2>\n");
            System.exit(0);
            return null;
        }
    }
    private static String readDictionaryTextFile(String fileName)
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
            System.out.println("An error occurred. Please ensure the dictionary file path and name are correct.");
            System.exit(0);
        }
        return txt;
    }

    public void saveDictionaryToFile(String filename)
    {
        try {
            FileWriter dictionaryWriter = new FileWriter(filename);
            for (Map.Entry<String, ArrayList<String>> entry : dictionary.entrySet()) {
                String word = entry.getKey();
                ArrayList<String> meanings = entry.getValue();
                for(String meaning : meanings){
                    dictionaryWriter.write(word + "\t" + meaning + "\n");
                }
            }
            dictionaryWriter.close();
            System.out.println("Successfully wrote to the file");
        } catch ( IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public synchronized String queryDictionary(String query) throws InvalidCommand
    {
        // Validate query
        if(query.length() == 0){
            throw new InvalidCommand("Error: You must enter a word with at least 1 character");
        }

        ArrayList<String> meanings = this.dictionary.get(query);
        if(meanings == null){
            return "Word Not Found";
//            throw new WordNotFound("The word you are looking for can not be found. Please insert the word," +
//                    "or query another word.");
        }else{
            return meanings.toString();
        }
    }
    public synchronized String insertWord(JSONObject insertObject) throws InvalidCommand
    {
        // Add all meanings into a List
        ArrayList<String> meaningList = new ArrayList<>();
        JSONArray meanings = insertObject.getJSONArray("Meanings");
        for( Object meaning : meanings){
            if(!meaning.equals(""))
            meaningList.add(meaning.toString());
        }
        if(insertObject.get("Word").toString().length() == 0){
            throw new InvalidCommand("Error: You must enter a word with at least 1 character");
        }
        if(meaningList.size() == 0){
            throw new InvalidCommand("Error: You must attach at least one meaning to the word you are trying to " +
                    "insert into the dictionary");
        }


        ArrayList<String> oldMeanings = dictionary.get(insertObject.get("Word").toString());
        if(oldMeanings == null){
            dictionary.put(insertObject.get("Word").toString(), meaningList);
            return "Success";
        }
        else{
            return "Word Already Exists";
//            throw new WordAlreadyExists("The word you are trying to Insert already exists in the dictionary. " +
//                    "Please use the update functionality if you wish to overwrite it's current meanings.");
        }
    }

    public synchronized String deleteWord(String word) throws InvalidCommand
    {
        // Validate query
        if(word.length() == 0){
            throw new InvalidCommand("Error: You must enter a word with at least 1 character");
        }

        ArrayList<String> deletedMeanings = this.dictionary.remove(word);

        if(deletedMeanings == null){
            return "Word Not Found";
            //throw new WordNotFound("The word you are trying to delete could not be found.");
        }
        return "Success";
    }
    public synchronized String updateMeaning(JSONObject updateObject) throws InvalidCommand
    {
        // Replace current list of meanings with new one specified
        ArrayList<String> meaningList = new ArrayList<>();
        JSONArray meanings = updateObject.getJSONArray("Meanings");
        for( Object meaning : meanings){
            if(!meaning.equals("")) {
                meaningList.add(meaning.toString());
            }
        }

        if(updateObject.get("Word").toString().length() == 0){
            throw new InvalidCommand("Error: You must enter a word with at least 1 character");
        }
        if(meaningList.size() == 0){
            throw new InvalidCommand("Error: You must attach at least one meaning to the word you are trying to " +
                    "insert into the dictionary");
        }

        ArrayList<String> oldMeanings = this.dictionary.replace(updateObject.get("Word").toString(), meaningList);
        if(oldMeanings == null){
            return "Word Not Found";
            //throw new WordNotFound("The word you are trying to update does not exist.");
        }
        else {
            return "Success";
        }
    }
}
