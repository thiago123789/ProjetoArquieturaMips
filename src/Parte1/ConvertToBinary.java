package Parte1;
import java.math.BigInteger;
import java.util.ArrayList;

import Parte2.MemoriaInstrucao;

public class ConvertToBinary {
	private ArrayList<String> instrucoesBinary;
	private ArrayList<String> instrucoesBinary32;
	private MemoriaInstrucao memmory;
	public ConvertToBinary(String[] instruct){
		this.instrucoesBinary32 = new ArrayList<String>();
		this.instrucoesBinary = new ArrayList<String>();
		this.memmory = MemoriaInstrucao.getInstance();
		this.hexaToBinary(instruct); 
		this.convertArrayEm32Bits();
	}

	private void hexaToBinary(String[] instruct){
		for(String aux : instruct){ //Corre o array list de entrada
			if(aux != null){
				BigInteger a = new BigInteger(aux, 16); //Converte em inteiro grande (entrada base 16 - Hexa)
				this.instrucoesBinary.add(a.toString(2)); //Converte o bigInteger em string com base 2;
				
			}
		}
	}

	private String to32Bits(String bin){
		String retorno = "";
		if(bin.length() < 32){
			while(bin.length() < 32){
				bin = "0"+bin;
			}
			retorno = bin;
		}else{
			retorno = bin;
		}
		return retorno;
	}

	private void convertArrayEm32Bits(){
		for(String aux : this.instrucoesBinary){
			this.instrucoesBinary32.add(this.to32Bits(aux));
			this.memmory.inserirInstrucao(this.to32Bits(aux));
		}
	}

	public ArrayList<String> getInstrucoesBinary(){
		return this.instrucoesBinary32;
	}

}
