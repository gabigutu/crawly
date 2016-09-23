/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author ReaderBench
 */
public class Orgnizare {
    public static void main(String[] args) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("Evidenta.txt"));
        File folder1 = new File("REVIEWS");
        for(File f : folder1.listFiles()){
            if(f.isFile()){
                pw.print(f.getName() + " ");
                Scanner sc = new Scanner(f);
                sc.nextLine();
                pw.print(sc.nextLine() + "\n");
                sc.close();
            }
        }
        
        File folder2 = new File("ReviewsMustSee1000");
        for(File f : folder2.listFiles()){
            if(f.isFile()){
                pw.print(f.getName() + " ");
                Scanner sc = new Scanner(f);
                sc.nextLine();
                pw.print(sc.nextLine() + "\n");
                sc.close();
            }
        }
        
        pw.close();
    }
}
