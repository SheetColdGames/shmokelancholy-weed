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
package me.sheetcoldgames.shmokeprototype;

import me.sheetcoldgames.shmokeprototype.controller.GameController;
import me.sheetcoldgames.shmokeprototype.controller.GameController.GAME_STATES;
import me.sheetcoldgames.shmokeprototype.engine.ShmokeCamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameRenderer {
	GameController con;
	
	// Colors
	public static final Color BACKGROUND_COLOR = new Color(23/255f, 42/255f, 56/255f, 1f);
	public static final Color STAR_COLOR = new Color(1f, 1f, 1f, 1f);
	
	public GameRenderer(GameController c) {
		this.con = c;
	}
	
	public void render(ShmokeCamera camera, ShapeRenderer sr, SpriteBatch sb) {
		sr.setProjectionMatrix(camera.combined);
		clearScreen();
		sr.begin(ShapeType.Filled);
		drawStars(sr, camera);
		drawShips(sr, camera);
		drawBullets(sr, camera);
		sr.end();
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		con.snoopPortrait.render(sb, 4);
		con.gameTitle.render(sb, 16);
		con.gameInstructions.render(sb, 16);
		con.pauseTitle.render(sb, 16);
		con.pauseReturn.render(sb, 24);
		con.pauseEscape.render(sb, 24);
//		sb.draw(snoopMenu, 
//				con.snoopMenuPosition.x-snoopMenu.getWidth()/8f, con.snoopMenuPosition.y-snoopMenu.getHeight()/8f, 
//				snoopMenu.getWidth()/4f, snoopMenu.getHeight()/4f);
//		sb.draw(titleMenu, 
//				con.titleMenuPosition.x-titleMenu.getWidth()/8f, con.titleMenuPosition.y-titleMenu.getHeight()/8f, 
//				titleMenu.getWidth()/4f, titleMenu.getHeight()/4f);
//		sb.draw(instructionsMenu, 
//				con.instructionsMenuPosition.x-instructionsMenu.getWidth()/8f, con.instructionsMenuPosition.y-instructionsMenu.getHeight()/8f,
//				instructionsMenu.getWidth()/4f, instructionsMenu.getHeight()/4f);
		sb.end();
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	/** Usado pelo método {@link renderStars()} */
	Color currentStarColor = new Color(STAR_COLOR);
	/** Usado pelo método {@link renderStars()} */
	boolean fadingOut = true;
	
	private void drawStars(ShapeRenderer sr, ShmokeCamera cam) {
		// Just a quick and dirty way to get things going
		if (currentStarColor.r < BACKGROUND_COLOR.r + .03f &&
			currentStarColor.g < BACKGROUND_COLOR.g + .03f &&
			currentStarColor.b < BACKGROUND_COLOR.b + .03f) {
			fadingOut = false;
			con.randomizeStarsPosition(cam);
		} else if (currentStarColor.r > STAR_COLOR.r - .07f &&
				currentStarColor.g > STAR_COLOR.g - .07f &&
				currentStarColor.b > STAR_COLOR.b - .07f) {
			fadingOut = true;
		}
		if (con.state != GAME_STATES.PAUSE) {
			if (fadingOut) {
				currentStarColor.lerp(BACKGROUND_COLOR, .04f);
			} else {
				currentStarColor.lerp(STAR_COLOR, .04f);
			}
		}
		sr.setColor(currentStarColor);
		for (int k = 0; k < con.stars.length; k++) {
			sr.rect(con.stars[k].x, con.stars[k].y, .2f, .2f);
		}
	}
	
	public static final Color PLAYER_COLOR = new Color(107f/255f, 146f/255f, 1f, 1f);
	
	private void drawShips(ShapeRenderer sr, ShmokeCamera camera) {
		sr.setColor(PLAYER_COLOR);
		sr.circle(con.playerShip.pos.x, con.playerShip.pos.y, con.playerShip.radius, 16);
	}
	
	private void drawBullets(ShapeRenderer sr, ShmokeCamera camera) {
		sr.setColor(Color.WHITE);
		System.out.println(con.playerBullets.size());
		for (int k = 0; k < con.playerBullets.size(); k++) {
			sr.circle(con.playerBullets.get(k).pos.x, con.playerBullets.get(k).pos.y, con.playerBullets.get(k).radius, 16);
		}
	}
}
