package pl.lonski.cws;

public record Card(Rank rank, Color color) implements Comparable<Card> {

	@Override
	public int compareTo(Card other) {
		if (other.rank.value < this.rank.value) {
			return 1;
		} else if (other.rank.value > this.rank.value) {
			return -1;
		}

		return 0;
	}

	@Override
	public String toString() {
		return color.symbol + rank.symbol;
	}

	public enum Rank {
		TWO(2, "2"),
		THREE(3, "3"),
		FOUR(4, "4"),
		FIVE(5, "5"),
		SIX(6, "6"),
		SEVEN(7, "7"),
		EIGHT(8, "8"),
		NINE(9, "9"),
		TEN(10, "10"),
		JACK(11, "J"),
		QUEEN(12, "Q"),
		KING(13, "K"),
		ACE(14, "A");

		private int value;
		private String symbol;

		Rank(int value, String symbol) {
			this.value = value;
			this.symbol = symbol;
		}
	}

	public enum Color {
		HEART("♥"),
		DIAMOND("♦"),
		CLUB("♣"),
		SPADE("♠");

		private String symbol;

		Color(String symbol){
			this.symbol = symbol;
		}
	}
}
