package test.java.crawlers.metacritic;

import java.io.IOException;
import main.java.crawlers.Metacritic;


import org.junit.Test;

/**
 * @author Gabriel Gutu
 *
 */
public class MetacriticTest {

	@Test
	public void metacriticParser() {
		
		Metacritic metacritic = new Metacritic();
		try {
			metacritic.parse("http://www.metacritic.com/game/playstation-4/assassins-creed-syndicate/critic-reviews");
		} catch (IOException e) {
			System.err.println("Eroare parsare URL: ");
			e.printStackTrace();
		}
		System.out.println(metacritic);
		
	}
	
}
