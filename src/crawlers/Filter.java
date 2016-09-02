package crawlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

public class Filter {
	
	public static void main(String[] args) throws FileNotFoundException {
		HashSet<String> links = new HashSet<String>();
		Scanner sc1 = new Scanner(new File("Top250IMDB/Top250IMDB.txt"));
		Scanner sc2 = new Scanner(new File("MostReviewed50ByEachGenreIMDB/MostReviewed50ByEachGenreIMDB.txt"));
		Scanner sc3 = new Scanner(new File("First50ByEachGenreIMDB/First50ByEachGenreIMDB.txt"));
		
		String link = "";
		
		while(sc1.hasNextLine() && (link = sc1.nextLine()) != null){
			links.add(link);
		}
		sc1.close();
		
		while(sc2.hasNextLine() && (link = sc2.nextLine()) != null){
			links.add(link);
		}
		sc2.close();
		
		while(sc3.hasNextLine() && (link = sc3.nextLine()) != null){
			links.add(link);
		}
		sc3.close();
		
		PrintWriter pw = new PrintWriter(new File("LinksToCrawlIMDB.txt"));
		
		for(String line : links){
			pw.println(line);
		}
		
		pw.close();
	}
}
