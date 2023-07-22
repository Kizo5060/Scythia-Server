package com.ruseps.world.content.scratchcards;

public enum ScratchCardData {

	IndanHelmet(19992),IndanBody(19992),IndanLegs(19992),IndanBoots(19994),IndanBow(19994),staff(19994),muske(19990),muske1(19990)
	,muske2(995),muske3(995),muske4(995);
	private int displayId;


	ScratchCardData(int displayId) {
		this.displayId = displayId;
	}

	public int getDisplayId() {
		return displayId;
	}

	public void setDisplayId(int displayId) {
		this.displayId = displayId;
	}

}