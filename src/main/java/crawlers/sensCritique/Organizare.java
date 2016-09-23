/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.sensCritique;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author ReaderBench
 */
public class Organizare {
    public static void main(String[] args) throws FileNotFoundException{
        File folder = new File("SensCritique/Series");
        if(folder.isDirectory()){
            PrintWriter pw = new PrintWriter(new File("SensCritique/Series.txt"));
            HashSet<String> links = new HashSet<String>();
            for(File f : folder.listFiles()){
                if(f.isDirectory() || f.isHidden()){
                    continue;
                }
                Scanner sc = new Scanner(f);
                String line = "";
                while(sc.hasNextLine() && (line = sc.nextLine())!=null){
                    links.add(line);
                }
                sc.close();
            }
            
            links.stream().forEach((String link) -> {
                pw.println(link);
            });
            
            pw.close();
        }
    }
}
