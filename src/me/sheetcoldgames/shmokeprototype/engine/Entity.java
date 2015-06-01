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

import com.badlogic.gdx.math.Vector2;

public class Entity {
	public Vector2 pos;
	public Vector2 vel;
	public boolean hit;
	
	public float radius;
	
	public enum ENTITY_STATE {ALIVE, DEAD};
	public ENTITY_STATE state;
	
	public float aliveStateTime = 0f;
	public float deadStateTime = 0f;
	
	public Entity(float x, float y, float r) {
		pos = new Vector2(x, y);
		vel = new Vector2();
		
		radius = r;
		state = ENTITY_STATE.ALIVE;
	}
	
	public boolean overlaps(Entity ent) {
		return overlaps(ent.pos.x, ent.pos.y, ent.radius);
	}
	
	public boolean overlaps(float x, float y, float r) {
		float r2 = (r - radius) * (r - radius);
		float x2 = (x - pos.x) * (x - pos.x);
		float y2 = (y - pos.y) * (y - pos.y);
		return (r2 >= (x2 + y2));
	}
}
