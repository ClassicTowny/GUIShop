package us.alreadycoded.shop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FileUtil
{
  public static void mkdirs()
  {
    Shop.shop.getDataFolder().mkdirs();
  }
  
  public static void saveFile(String fileName, ArrayList<String> content)
  {
    try
    {
      File file = new File(Shop.shop.getDataFolder() + "/" + fileName);
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      
      for (String s : content)
      {
        out.write(s.trim());
        out.newLine();
      }
      
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static ArrayList<String> readFile(String fileName) {
    File file = new File(Shop.shop.getDataFolder() + "/" + fileName);
    ArrayList<String> contents = new ArrayList<String>();
    try
    {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String inputLine = " ";
      while ((inputLine = in.readLine()) != null) {
        contents.add(inputLine.trim());
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace(); }
    return contents;
  }
}
