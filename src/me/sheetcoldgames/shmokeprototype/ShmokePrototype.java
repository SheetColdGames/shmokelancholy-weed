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
import me.sheetcoldgames.shmokeprototype.controller.Input;
import me.sheetcoldgames.shmokeprototype.engine.ShmokeCamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShmokePrototype extends ApplicationAdapter {
	public static final int WINDOW_WIDTH = 320;
	public static final int WINDOW_HEIGHT = 480;
	
	ShmokeCamera camera;
	ShapeRenderer sr;
	SpriteBatch sb;
	
	GameController controller;
	GameRenderer renderer;
	
	Input input;
	
	public void create() {
		camera = new ShmokeCamera(Gdx.graphics.getWidth()/16f, Gdx.graphics.getHeight()/16f);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0f);
		camera.update();
		sr = new ShapeRenderer();
		sb = new SpriteBatch();
		
		controller = new GameController(camera);
		renderer = new GameRenderer(controller);
		
		input = new Input();
		Gdx.input.setInputProcessor(input);
	}
	
	public void dispose() {
		sr.dispose();
		sb.dispose();
	}
	
	public void render() {
		handleInput();
		controller.update(camera, input, Gdx.graphics.getDeltaTime());
		renderer.render(camera, sr, sb);
	}
	
	boolean fullscreenPressed = false;
	
	private void handleInput() {
		if (input.buttons[Input.FULLSCREEN]) {
			fullscreenPressed = true;
		} else if (fullscreenPressed) {
			fullscreenPressed= false;
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
			} else {
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
			}
		}
	}
	
	public void resize(int width, int height) {
		controller.viewport.update(width, height, false);
	}
}
