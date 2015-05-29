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
package me.sheetcoldgames.shmokeprototype.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MenuItem {
	public Vector2 pos;
	public Texture sprite;
	public float rotation;
	float initialStateTime;
	public float stateTime;
	public float scaleLimit;
	
	public Vector2 onScreenPosition;
	public Vector2 offScreenPosition;
	
	public MenuItem(float onX, float onY, float offX, float offY, String filename, float stateTimeOffset) {
		pos = new Vector2(offX, offY);
		onScreenPosition = new Vector2(onX, onY);
		offScreenPosition = new Vector2(offX, offY);
		sprite = new Texture(filename);
		rotation = 0f;
		initialStateTime = stateTimeOffset;
		stateTime = initialStateTime;
		scaleLimit = 8f;
	}
	
	public void render(SpriteBatch sb, float scale) {
		float size = 0f;
		if (MathUtils.sin(stateTime*2f) > 0f) {
			size = MathUtils.sin(stateTime*2f);
		}
		float w = sprite.getWidth();
		float h = sprite.getHeight();
		
		float x = pos.x - w / 2f;
		float y = pos.y - h / 2f;
		float oX = w / 2f;
		float oY = h / 2f;
		float sx = 1f/scale + size/scaleLimit;
		float sy = 1f/scale + size/scaleLimit;
		int srcX = 0;
		int srcY = 0;
		int srcW = (int) w;
		int srcH = (int) h;
		boolean flip = false;
		
//		sb.draw(sprite,
//				pos.x - sprite.getWidth()/(scale*2),
//				pos.y - sprite.getHeight()/(scale*2f), 
//				sprite.getWidth()/scale, sprite.getHeight()/scale);
		
		sb.draw(sprite, x, y, oX, oY, w, h, sx, sy, rotation, srcX, srcY, srcW, srcH, flip, flip);
	}
	
	public void update(float dt, boolean onScreen) {
		if (onScreen) {
			stateTime += dt;
			
			rotation = MathUtils.sin(stateTime) * 7f;
			
			pos.set(MathUtils.lerp(pos.x, onScreenPosition.x, .1f), 
					MathUtils.lerp(pos.y, onScreenPosition.y, .1f));
		} else {
			stateTime = initialStateTime;
			
			rotation += 23f;
			
			pos.set(MathUtils.lerp(pos.x, offScreenPosition.x, .03f), 
					MathUtils.lerp(pos.y, offScreenPosition.y, .03f));
			
		}
	}
}
