package tests;

import java.io.IOException;

import org.junit.Test;

import crawlers.Metacritic;

/**
 * @author Gabriel Gutu
 *
 */
public class Tests {

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
