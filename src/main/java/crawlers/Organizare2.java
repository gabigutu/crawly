/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author ReaderBench
 */
public class Organizare2 {
    public static void main(String[] args) throws FileNotFoundException{
        HashMap<String, ArrayList<String>> evidenta = new HashMap<String, ArrayList<String>>();
        Scanner sc = new Scanner(new File("Evidenta.txt"));
        String line = ""; 
        while(sc.hasNextLine() && (line = sc.nextLine())!=null){
            StringTokenizer st = new StringTokenizer(line,"<>",false);
            String file = st.nextToken();
            st.nextToken();
            String title = st.nextToken();
            if(evidenta.containsKey(title)){
                evidenta.get(title).add(file);
            }else{
                ArrayList<String> aux = new ArrayList<String>();
                aux.add(file);
                evidenta.put(title, aux);
            }
        }
        
        PrintWriter pw = new PrintWriter(new File("Evidenta3.txt"));
        for(Map.Entry<String,ArrayList<String>> pair : evidenta.entrySet()){
            if(pair.getValue().size() == 1){
                continue;
            }
            pw.print(pair.getKey() + " ");
            for(String s : pair.getValue()){
                pw.print(s + " ");
            }
            pw.println();
        }
        pw.close();
    }
}
