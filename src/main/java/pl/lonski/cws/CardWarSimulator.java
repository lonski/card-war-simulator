package pl.lonski.cws;

import java.util.stream.IntStream;

public class CardWarSimulator {

	public static void main(String[] args) {
		var turnTimeSec = 3;
		var gamesToPlay = 1000;
		System.out.println("Playing " + gamesToPlay + " games...");
		var gameTimes = IntStream.range(0, gamesToPlay)
				.mapToObj(i -> new Game(false).start().getTurn())
				.filter(turns -> turns > 0)
				.map(turns -> turns * turnTimeSec / 60.0)
				.sorted()
				.toList();

		System.out.println("Finished " + gameTimes.size() + " games");
		System.out.println("Assumed 3 seconds per turn");
		System.out.println("Fastest game: " + gameTimes.get(0) + " minutes");
		System.out.println("Longest game: " + gameTimes.get(gameTimes.size() - 1) + " minutes");

		var avg = gameTimes.stream().reduce(0.0, Double::sum) / gameTimes.size();
		System.out.println("Average : " + avg + " minutes");

	}

	private static class Game {

		private final CardDeck p1;
		private final CardDeck p2;
		private final CardDeck tableDeck = new CardDeck();
		private final long maxTurns = 1_000_000;
		private final boolean doLog;
		private long turn;

		Game(boolean enableLog) {
			var cards = CardDeck.createFullDeck().splitToHalf();
			p1 = cards.left();
			p2 = cards.right();
			turn = 0;
			doLog = enableLog;
		}

		long getTurn() {
			return turn;
		}

		Game start() {

			while (p1.size() != 0 && p2.size() != 0) {
				if (turn > maxTurns) {
					System.out.println("Infinite game! Breaking.");
					turn = 0;
					break;
				}
				turn++;
				logStatus();
				fight();
			}

			if (p1.size() == 0) {
				log("Player 2 wins the game");
			}
			if (p2.size() == 0) {
				log("Player 1 wins the game");
			}

			log("\nAssuming 3 seconds per turn the game took %f minutes", turn * 3.0 / 60.0);
			return this;
		}

		private void fight() {
			tableDeck.putOnTop(p1.drawFromTop());
			tableDeck.putOnTop(p2.drawFromTop());

			var p1card = tableDeck.peek(1);
			var p2card = tableDeck.peek(0);

			if (p1card == null || p2card == null) {
				return;
			}

			var cmp = p1card.compareTo(p2card);
			if (cmp > 0) {
				log("Player 1's %s beats player 2's %s", p1card, p2card);
				p1.putOnBottom(tableDeck);
			} else if (cmp < 0) {
				log("Player 2's %s beats player 1's %s", p2card, p1card);
				p2.putOnBottom(tableDeck);
			} else {
				log("War!\nPlayers put additional three cards on the deck and fight with fourth");
				playersPutThreeCards();
				fight();
			}
		}

		private void playersPutThreeCards() {
			if (p1.size() < 4) {
				log("Player 1 has too few cards to play war");
				p1.clear();
				return;
			}
			if (p2.size() < 4) {
				log("Player 2 has too few cards to play war");
				p2.clear();
				return;
			}

			IntStream.range(0, 3).forEach(i -> tableDeck.putOnTop(p1.drawFromTop()));
			IntStream.range(0, 3).forEach(i -> tableDeck.putOnTop(p2.drawFromTop()));
		}

		private void logStatus() {
			log("------------------------------------------------------");
			log("Turn " + turn);
			log("Player 1 cards (%d): %s", p1.size(), p1);
			log("Player 2 cards (%d): %s", p2.size(), p2);
		}

		private void log(String message, Object... args) {
			if (doLog) {
				System.out.printf(message + "\n", args);
			}
		}

	}

}
