package com.github.syuchan1005.pokego;

/**
 * Created by syuchan on 2016/07/25.
 */
public enum PokemonType {
	NORMAL(0),
	FIRE(1),
	WATER(2),
	ELECTRIC(3),
	GRASS(4),
	ICE(5),
	FIGHTING(6),
	POISON(7),
	GROUND(8),
	FLYING(9),
	PSYCHIC(10),
	BUG(11),
	ROCK(12),
	GHOST(13),
	DRAGON(14),
	DARK(15),
	STEEL(16),
	FAIRY(17),
	Unimplemented(18);

	private final int id;

	PokemonType(int id) {
		this.id = id;
	}

	public static String ArraytoString(PokemonType... types) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < types.length; i++) {
			builder.append(types[i].name());
			if(i != types.length -1) builder.append(", ");
		}
		return builder.toString();
	}
}
