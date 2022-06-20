package br.com.gerenciamentoDeCervejas.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerType {
	
	LAGER("Lager"),
	MALZBIER("Malzbier"),
	WITBIER("Witbier"),
	WEISS("Weiss"),
	ALE("Ale"),
	IPA("IPA"),
	STOUT("Stout");
	
	private String descriptio;
	
	BeerType(String description) {
		this.descriptio = description;
	}

	public String getDescriptio() {
		return descriptio;
	}

	public void setDescriptio(String descriptio) {
		this.descriptio = descriptio;
	}
}