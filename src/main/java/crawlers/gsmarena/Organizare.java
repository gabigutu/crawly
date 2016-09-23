/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.gsmarena;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author ReaderBench
 */
public class Organizare {
    public static void main(String[] args) throws FileNotFoundException{
        File folder = new File("GsmArenaReviews/Good");
        int count = 0;
        System.out.println(folder.listFiles().length);
        for(File f : folder.listFiles()){
            if(f.isDirectory() || f.isHidden()){
                continue;
            }
            Scanner sc = new Scanner(f);
            PrintWriter pw = new PrintWriter(new File("Review"+count+".txt"));
            String line = "";
            while(sc.hasNextLine() && (line = sc.nextLine())!=null){
                pw.println(line);
            }
            count++;
            pw.close();
            sc.close();
        }
    }
}
