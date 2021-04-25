package com.mysteria.titles.enums;

import com.mysteria.customapi.rarity.Rarity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum Title {

	PUNK("Punk"),
	CAVEMAN("Caveman"),
	BLACKSMITH("Blacksmith"),
	ADVENTURER("Adventurer"),
	UNDEAD("Undead"),
	SAILOR("Sailor"),
	FRIENDLY("Friendly"),

	LEGIONNAIRE("Legionnaire", Rarity.UNCOMMON),
	HELL_PASSENGER("Hell Passenger", Rarity.UNCOMMON),
	VOID_FOLLOWER("Void Follower", Rarity.UNCOMMON),
	LIBRARIAN("Librarian", Rarity.UNCOMMON),
	MAN_AT_ARMS("Man-at-Arms", Rarity.UNCOMMON),
	ALCHEMIST("Alchemist", Rarity.UNCOMMON),
	MERCHANT("Merchant", Rarity.UNCOMMON),
	VAMPIRE("Vampire", Rarity.UNCOMMON),
	DUELLER("Dueller", Rarity.UNCOMMON),
	ARCHER("Archer", Rarity.UNCOMMON),
	PILLAGER("Pillager", Rarity.UNCOMMON),
	CHEATER("Cheater", Rarity.UNCOMMON),
	MAGNETIC("Magnetic", Rarity.UNCOMMON),
	TRAITOR("Traitor", Rarity.UNCOMMON),
	FAIRY("Fairy", Rarity.UNCOMMON),
	SOULLESS("Soulless", Rarity.UNCOMMON),

	BLESSED("Blessed", Rarity.RARE),
	ENCHANTER("Enchanter", Rarity.RARE),
	STARMIST("Starmist", Rarity.RARE),
	SNIPER("Sniper", Rarity.RARE),
	GORGEOUS("Gorgeous", Rarity.RARE),
	INTRUDER("Intruder", Rarity.RARE),
	BLACK_KNIGHT("Black Knight", Rarity.RARE),
	DEADMAN("Deadman", Rarity.RARE),
	POSTMORTAL("Postmortal", Rarity.RARE),
	ATLANTEAN("Atlantean", Rarity.RARE),
	PREDATOR("Predator", Rarity.RARE),
	TREASURE_HUNTER("Treasure Hunter", Rarity.RARE),
	TOURIST("Tourist", Rarity.RARE),
	FEARLESS("Fearless", Rarity.RARE),

	THE_GUIDE("The Guide", Rarity.EPIC),
	DIAMOND_GUY("Diamond Guy", Rarity.EPIC),
	TRAVELER("Traveler", Rarity.EPIC),
	UNFORGIVEN("Unforgiven", Rarity.EPIC),
	PATHFINDER("Pathfinder", Rarity.EPIC),
	ASCENDED("Ascended", Rarity.EPIC),

	INTERDIMENSIONAL_EXPLORER("Interdimensional Explorer", Rarity.HEAVENLY),
	DEMON_LORD("Demon Lord", Rarity.HEAVENLY),
	INSOMNIAC("Insomniac", Rarity.HEAVENLY),

	DRAGON_SLAYER("Dragon Slayer", Rarity.LEGENDARY),
	WORTHY("Worthy", Rarity.LEGENDARY),

	BEATER("Beater", Rarity.SPECIAL),
	MYSTERIAN("Mysterian", Rarity.PREMIUM),
	;


	private final String displayName;
	private final Rarity rarity;

	Title(@Nonnull String displayName, @Nonnull Rarity rarity) {
		this.displayName = displayName;
		this.rarity = rarity;
	}

	Title(@Nonnull String displayName) {
		this(displayName, Rarity.COMMON);
	}



	@Nonnull
	public String getDisplayName() {
		return displayName;
	}

	@Nonnull
	public Rarity getRarity() {
		return rarity;
	}

	@Nullable
	public static Title getFromDisplayName(@Nonnull String displayName) {
		for (Title title : Title.values()) {
			if (title.getDisplayName().equalsIgnoreCase(displayName)) {
				return title;
			}
		}
		return null;
	}


}
