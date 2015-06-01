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

import java.util.ArrayList;

import me.sheetcoldgames.shmokeprototype.engine.Bullet;
import me.sheetcoldgames.shmokeprototype.engine.Entity;
import me.sheetcoldgames.shmokeprototype.engine.Entity.ENTITY_STATE;
import me.sheetcoldgames.shmokeprototype.engine.MenuItem;
import me.sheetcoldgames.shmokeprototype.engine.ShmokeCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The game cycle usually is:
 * MENU -> GAME/PAUSE -> WIN or LOSE -> MENU (repeat)
 * @author Rafael Giordanno
 *
 */
public class GameController {
	final float camWidth;
	final float camHeight;
	
	public Vector2[] stars;
	
	public Viewport viewport;
	
	public Entity playerShip;
	public ArrayList<Entity> enemies;
	public ArrayList<Bullet> playerBullets;
	public ArrayList<Bullet> enemyBullets;
	
	public enum GAME_STATES {MENU, GAME, WIN, LOSE, PAUSE};
	public GAME_STATES state = GAME_STATES.MENU;
	
	// Menu items
	public MenuItem snoopPortrait;
	public MenuItem gameTitle;
	public MenuItem gameInstructions;
	public MenuItem pauseTitle;
	public MenuItem pauseEscape;
	public MenuItem pauseReturn;
	
	public GameController(ShmokeCamera camera) {
		camWidth = camera.viewportWidth;
		camHeight = camera.viewportHeight;
		initStars(camera, 177);
		
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		
		initMenuItems(camera);
		initEntities(camera.position.x, camera.position.y);
	}
	
	private void initMenuItems(ShmokeCamera camera) {
		float cx = camera.position.x;
		float cy = camera.position.y;
		snoopPortrait = new MenuItem(cx, cy-2f, cx, 50f, "menu/snoop.png", .7f);
		snoopPortrait.scaleLimit = 8f;
		gameTitle = new MenuItem(cx, cy+10f, cx, 50f, "menu/title.png", 1.4f);
		gameTitle.scaleLimit = 128f;
		gameInstructions = new MenuItem(cx, cy-12f, cx, -20f, "menu/instructions.png", 0f);
		gameInstructions.scaleLimit = 64f;
		 
		// pause stuff
		pauseTitle = new MenuItem(cx, cy+7f, cx, cy+42f, "pause/title.png", 1.4f);
		pauseTitle.scaleLimit = 16f;
		pauseReturn = new MenuItem(cx, cy+3f, cx-42f, cy+4f, "pause/return.png", .7f);
		pauseReturn.scaleLimit = 64f;
		pauseEscape = new MenuItem(cx, cy-1f, cx+42f, cy, "pause/esc.png", 0f);
		pauseEscape.scaleLimit = 64f;
	}
	
	private void initStars(ShmokeCamera camera, int size) {
		stars = new Vector2[size];
		for (int k = 0; k < stars.length; k++) {
			float x = MathUtils.random() * camWidth*3f + (camera.position.x - camWidth);
			float y = MathUtils.random() * camHeight*3f + (camera.position.y - camHeight);
			stars[k] = new Vector2(x, y);
		}
	}
	
	private void initEntities(float centerX, float centerY) {
		playerShip = new Entity(centerX, centerY, 1f);
		playerBullets = new ArrayList<Bullet>();
		
		enemies = new ArrayList<Entity>();
		enemyBullets = new ArrayList<Bullet>();
	}
	
	float verticalSpeed = .2f;
	float stateTime;
	
	public void update(ShmokeCamera camera, Input input, float dt) {
		stateTime += dt;
		
		handleInput(playerShip, camera, input, dt);
		updateBullets(dt, camera, state == GAME_STATES.PAUSE);
		if (state == GAME_STATES.MENU) {
			// From MENU, we can call GAME
			resetGame();
			moveMenu(true, dt);
			movePause(false, dt);
			updateBackground(camera, .3f);
			updateEntities(camera, dt);
		} else if (state == GAME_STATES.GAME) {
			// from GAME, we can call PAUSE, WIN or LOSE
			// move the menu away
			moveMenu(false, dt);
			movePause(false, dt);
			updateEntities(camera, dt);
			updateBackground(camera, .3f);
		} else if (state == GAME_STATES.PAUSE) {
			// from PAUSE, we can only call GAME
			moveMenu(false, dt);
			movePause(true, dt);
		} else if (state == GAME_STATES.LOSE) {
			// from LOSE, we can only call MENU
		} else if (state == GAME_STATES.WIN) {
			// from WIN, we can only call MENU
		}
		camera.update();
	}
	
	private void resetGame() {
		// set all the variables to false
		shootPressed = false;
		escapePressed = false;
		fireBullets = false;
		
		// killing the player
		playerShip.state = ENTITY_STATE.DEAD;
		
		// let's set all the player's bullet to hit
		for (int k = 0; k < playerBullets.size(); k++) {
			playerBullets.get(k).hit = true;
		}
		// let's set all the enemies bullets to hit
		for (int k = 0; k < enemyBullets.size(); k++) {
			enemyBullets.get(k).hit = true;
		}
	}
	
	boolean shootPressed = false;
	boolean toggleMenu = true;
	boolean escapePressed = false;
	
	private void handleInput(Entity player, ShmokeCamera camera, Input input, float dt) {
		if (state == GAME_STATES.MENU) {
			menuInput(input);
		} else if (state == GAME_STATES.GAME) {
			gameInput(player, input, dt);
		} else if (state == GAME_STATES.PAUSE) {
			pauseInput(input, dt);
		}
	}
	
	private void menuInput(Input input) {
		if (input.buttons[Input.SHOOT] && !shootPressed) {
			// let's go to the game
			state = GAME_STATES.GAME;
			// create the player
			playerShip.state = ENTITY_STATE.ALIVE;
			playerShip.deadStateTime = 0f;
			
			shootPressed = true;
		} else if (!input.buttons[Input.SHOOT]) {
			shootPressed = false;
		}
	}
	
	float playerAcc = 2.3f;
	boolean fireBullets;
	
	private void gameInput(Entity player, Input input, float dt) {
		if (player.aliveStateTime > 1f) {
			playerShipInput(player, input, dt);
		}
		
		
		if (input.buttons[Input.ESCAPE] && !escapePressed) {
			escapePressed = true;
			state = GAME_STATES.PAUSE;
		} else if (!input.buttons[Input.ESCAPE]) {
			escapePressed = false;
		}
	}
	
	private void playerShipInput(Entity player, Input input, float dt) {
		if (input.buttons[Input.UP]) {
			player.vel.y += playerAcc * dt;
		} else if (input.buttons[Input.DOWN]) {
			player.vel.y -= playerAcc * dt;
		} else {
			player.vel.y = MathUtils.lerp(player.vel.y, 0f, .23f);
		}
		
		if (input.buttons[Input.RIGHT]) {
			player.vel.x += playerAcc * dt;
		} else if (input.buttons[Input.LEFT]) {
			player.vel.x -= playerAcc * dt;
		} else {
			player.vel.x = MathUtils.lerp(player.vel.x, 0f, .23f);
		}
		
		if (input.buttons[Input.SHOOT] && !shootPressed) {
			shootPressed = true;
			fireBullets = true;
		} else if (!input.buttons[Input.SHOOT]) {
			shootPressed = false;
			fireBullets = false;
		}
	}
	
	private void pauseInput(Input input, float dt) {
		// go to main menu
		if (input.buttons[Input.ESCAPE] && !escapePressed) {
			escapePressed = true;
			state = GAME_STATES.MENU;
		} else if (!input.buttons[Input.ESCAPE]) {
			escapePressed = false;
		}
		
		// get back to game
		if (input.buttons[Input.SHOOT] && !shootPressed) {
			shootPressed = true;
			state = GAME_STATES.GAME;
		} else if (!input.buttons[Input.SHOOT]) {
			shootPressed = false;
		}
	}
	
	private void moveMenu(boolean showMenu, float dt) {
		if (showMenu) {
			snoopPortrait.update(dt, true);
			gameTitle.update(dt, true);
			gameInstructions.update(dt, true);
		} else {
			snoopPortrait.update(dt, false);
			gameTitle.update(dt, false);
			gameInstructions.update(dt, false);
		}
	}
	
	private void movePause(boolean showPause, float dt) {
		if (showPause) {
			pauseTitle.update(dt, true);
			pauseReturn.update(dt, true);
			pauseEscape.update(dt, true);
		} else {
			pauseTitle.update(dt, false);
			pauseReturn.update(dt, false);
			pauseEscape.update(dt, false);
		}
	}
	
	float maxSpeed = .32f;
	
	private void updateEntities(ShmokeCamera camera, float dt) {
		if (playerShip.state == ENTITY_STATE.ALIVE) {
			playerShip.aliveStateTime += dt;
			// The player can only move after it appears on screen
			// Make this code look prettier
			if (playerShip.aliveStateTime < 1f) {
				playerShip.pos.x = camera.position.x;
				playerShip.pos.y = MathUtils.lerp(playerShip.pos.y, camera.position.y - camHeight/2f + playerShip.radius + 1f, .1f);
			} else {
				// clamping the velocity
				playerShip.vel.x = MathUtils.clamp(playerShip.vel.x, -maxSpeed, maxSpeed);
				playerShip.vel.y = MathUtils.clamp(playerShip.vel.y, -maxSpeed, maxSpeed);
				
				playerShip.pos.x += playerShip.vel.x;
				playerShip.pos.y += playerShip.vel.y;
				
				playerShip.pos.x = MathUtils.clamp(playerShip.pos.x, 
						camera.position.x-camWidth/2f+playerShip.radius, 
						camera.position.x+camWidth/2f-playerShip.radius);
				playerShip.pos.y = MathUtils.clamp(playerShip.pos.y, 
						camera.position.y-camHeight/2f+playerShip.radius, 
						camera.position.y+camHeight/2f-playerShip.radius);
			}
		} else if (playerShip.state == ENTITY_STATE.DEAD) {
			playerShip.aliveStateTime = 0f;
			playerShip.deadStateTime += dt;
			if (playerShip.deadStateTime > 1f) {
				playerShip.pos.set(camera.position.x, camera.position.y - camHeight);
			}
		}
		
	}
	
	float playerBulletStateTime;
	
	private void updateBullets(float dt, ShmokeCamera camera, boolean paused) {
		playerBulletStateTime += dt;
		
		if (fireBullets && playerBulletStateTime > .07f && !paused) {
			playerBullets.add(new Bullet(playerShip.pos.x, playerShip.pos.y+playerShip.radius, 0f, .7f, .5f, 1));
			playerBulletStateTime = 0f;
		}
		
		for (int k = 0; k < playerBullets.size(); k++) {
			if (!paused) {
				playerBullets.get(k).update(dt, enemies);
			}
			
			if (playerBullets.get(k).damageOffset > 1f || (playerBullets.get(k).pos.y > camera.position.y + camHeight/2f)) {
				playerBullets.remove(k);
				if (playerBullets.isEmpty()) { break; }
				--k;
			}
		}
	}
	
	private void updateBackground(ShmokeCamera camera, float amnt) {
		for (int k = 0; k < stars.length; k++) {
			stars[k].set(stars[k].x, stars[k].y-amnt);
		}
	}
	
	public void randomizeStarsPosition(ShmokeCamera camera) {
		for (int k = 0; k < stars.length; k++) {
			float x = MathUtils.random() * camWidth * 3f + (camera.position.x - camWidth);
			float y = MathUtils.random() * camHeight * 3f + (camera.position.y - camHeight);
			stars[k].set(x, y);
		}
	}
}
