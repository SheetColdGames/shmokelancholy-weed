/**
 * Copyright 2015 Rafael Giordanno <rafael_giordanno@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.sheetcoldgames.shmokeprototype.controller;

import java.util.HashMap;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
	
	public boolean[] buttons;
	
	public static final int SHOOT = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int ESCAPE = 15;
	public static final int FULLSCREEN = 14;
	
	// primary keys
	public static HashMap<String, Integer> keys;
	public static String P_KEY_SHOOT = "P_SHOOT";
	public static String P_KEY_UP    = "P_UP";
	public static String P_KEY_DOWN  = "P_DOWN";
	public static String P_KEY_LEFT  = "P_LEFT";
	public static String P_KEY_RIGHT = "P_RIGHT";
	
	
	// secondary keys
	public static String S_KEY_SHOOT = "S_SHOOT";
	public static String S_KEY_UP    = "S_UP";
	public static String S_KEY_DOWN  = "S_DOWN";
	public static String S_KEY_LEFT  = "S_LEFT";
	public static String S_KEY_RIGHT = "S_RIGHT";
	
	// Utility keys
	public static String KEY_ESCAPE = "ESCAPE";
	public static String KEY_FULLSCREEN = "FULLSCREEN";
	
	public Input() {
		buttons = new boolean[16];
		keys = new HashMap<String, Integer>();
		defaultKeys();
	}
	
	public void defaultKeys() {
		// primary keys
		keys.put("P_SHOOT", Keys.SPACE);
		keys.put("P_UP", Keys.UP);
		keys.put("P_DOWN", Keys.DOWN);
		keys.put("P_LEFT", Keys.LEFT);
		keys.put("P_RIGHT", Keys.RIGHT);
		
		// secondary keys
		keys.put("S_SHOOT", Keys.J);
		keys.put("S_UP", Keys.W);
		keys.put("S_DOWN", Keys.S);
		keys.put("S_LEFT", Keys.A);
		keys.put("S_RIGHT", Keys.D);
		
		// utility keys
		keys.put("FULLSCREEN", Keys.F7);
		keys.put("ESCAPE", Keys.ESCAPE);
	}
	
	public void setKey() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == keys.get(P_KEY_SHOOT)
				|| keycode == keys.get(S_KEY_SHOOT)) { buttons[SHOOT] = true; }
		else if (keycode == keys.get(P_KEY_UP)
				|| keycode == keys.get(S_KEY_UP)) { buttons[UP] = true; }
		else if (keycode == keys.get(P_KEY_DOWN)
				|| keycode == keys.get(S_KEY_DOWN)) { buttons[DOWN] = true; }
		else if (keycode == keys.get(P_KEY_LEFT)
				|| keycode == keys.get(S_KEY_LEFT)) { buttons[LEFT] = true; }
		else if (keycode == keys.get(P_KEY_RIGHT)
				|| keycode == keys.get(S_KEY_RIGHT)) { buttons[RIGHT] = true; }
		
		else if (keycode == keys.get(KEY_FULLSCREEN)) { buttons[FULLSCREEN] = true; }
		else if (keycode == keys.get(KEY_ESCAPE)) { buttons[ESCAPE] = true; }
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == keys.get(P_KEY_SHOOT)
				|| keycode == keys.get(S_KEY_SHOOT)) { buttons[SHOOT] = false; }
		else if (keycode == keys.get(P_KEY_UP)
				|| keycode == keys.get(S_KEY_UP)) { buttons[UP] = false; }
		else if (keycode == keys.get(P_KEY_DOWN)
				|| keycode == keys.get(S_KEY_DOWN)) { buttons[DOWN] = false; }
		else if (keycode == keys.get(P_KEY_LEFT)
				|| keycode == keys.get(S_KEY_LEFT)) { buttons[LEFT] = false; }
		else if (keycode == keys.get(P_KEY_RIGHT)
				|| keycode == keys.get(S_KEY_RIGHT)) { buttons[RIGHT] = false; }
		
		else if (keycode == keys.get(KEY_FULLSCREEN)) { buttons[FULLSCREEN] = false; }
		else if (keycode == keys.get(KEY_ESCAPE)) { buttons[ESCAPE] = false; }
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
