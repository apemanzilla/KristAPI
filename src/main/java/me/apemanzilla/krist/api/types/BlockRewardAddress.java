package me.apemanzilla.krist.api.types;

class BlockRewardAddress extends SpecialAddress {

	BlockRewardAddress() {
		
	}
	
	@Override
	public String getAddress() {
		return "N/A(Mined)";
	}

}
