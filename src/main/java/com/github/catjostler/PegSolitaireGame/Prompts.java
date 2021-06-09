package com.github.catjostler.PegSolitaireGame;

import java.util.HashMap;
import java.util.Map;

public class Prompts {
	
	private static final Prompts instance = new Prompts();
	
	private Prompts() {
		Map<String, String> userPrompts = new HashMap<String, String>();
		userPrompts.put("boardTypePrompt", "Enter the number of the board you wish to play as an integer between 1 and 4:");
		userPrompts.put("pegColumnPrompt", "Choose the COLUMN of the peg you wish to move:");
		userPrompts.put("pegRowPrompt", "Choose the ROW of the peg you wish to move:");
		userPrompts.put("directionChoicePrompt", "Choose a DIRECTION to move the peg - 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT:");
		
		this.prompts = userPrompts;
	}
	
	public Map<String, String> prompts;
	
	public static Prompts getInstance() {
		return instance;
	}

}
