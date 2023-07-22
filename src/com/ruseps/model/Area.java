package com.ruseps.model;

import java.util.Random;

public class Area {

	private Position minPos;
	private Position maxPos;

	public Area(Position minPos, Position maxPos) {
		this.minPos = minPos;
		this.maxPos = maxPos;
	}

	public Position getMinPos() {
		return this.minPos;
	}

	public Position getMaxPos() {
		return this.maxPos;
	}

	public Position getRandomLocation() {
		int possibleX = this.getMaxPos().getX() - this.getMinPos().getX();
		int possibleY = this.getMaxPos().getY() - this.getMinPos().getY();

		Random rand = new Random();
		possibleX = rand.nextInt(possibleX) + this.getMinPos().getX();
		possibleY = rand.nextInt(possibleY) + this.getMinPos().getY();
		return new Position(possibleX, possibleY, this.getMaxPos().getZ());
	}

}
