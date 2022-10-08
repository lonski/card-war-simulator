package pl.lonski.cws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardDeck {

	private List<Card> cards;

	public CardDeck(List<Card> cards) {
		this.cards = new ArrayList<>(cards);
	}

	public CardDeck() {
		this.cards = new ArrayList<>();
	}

	public void clear() {
		this.cards.clear();
	}

	public Card drawFromTop() {
		if (cards.isEmpty()) {
			return null;
		}
		return cards.remove(0);
	}

	public void putOnBottom(Card card) {
		cards.add(card);
	}

	public void putOnBottom(CardDeck deck) {
		var card = deck.drawFromTop();
		while (card != null) {
			cards.add(card);
			card = deck.drawFromTop();
		}
	}

	public void putOnTop(Card card) {
		cards.add(0, card);
	}

	public Card peek(int idx) {
		if (idx >= cards.size()) {
			return null;
		}
		return cards.get(idx);
	}

	public long size() {
		return cards.size();
	}

	public Tuple<CardDeck> splitToHalf() {
		var half = cards.subList(0, cards.size() / 2);
		var otherHalf = cards.stream().toList().subList(cards.size() / 2, cards.size());
		cards = new ArrayList<>();
		return new Tuple<>(new CardDeck(half), new CardDeck(otherHalf));
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	@Override
	public String toString() {
		return cards.stream().map(Card::toString).collect(Collectors.joining(","));
	}

	public static CardDeck createFullDeck() {
		var cards = new ArrayList<Card>();
		for (Card.Color color : Card.Color.values()) {
			for (Card.Rank rank : Card.Rank.values()) {
				cards.add(new Card(rank, color));
			}
		}
		var deck = new CardDeck(cards);
		deck.shuffle();
		return deck;
	}

}
