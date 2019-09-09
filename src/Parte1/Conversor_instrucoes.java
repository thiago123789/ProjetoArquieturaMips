package Parte1;

import java.util.ArrayList;

public class Conversor_instrucoes {
	ArrayList<String> instr;
	ArrayList<String> instr_convertidas;
	
	
	public Conversor_instrucoes(ArrayList<String> instrucoes) {
		this.instr = instrucoes;
		this.instr_convertidas = new ArrayList<String>(); 
		hexToBin();
	}	

	private void hexToBin() {
		for(String s2 : this.instr){
			int aux = Integer.parseInt(s2,16);
			instr_convertidas.add(Integer.toBinaryString(aux));
		}
	
	}
}
