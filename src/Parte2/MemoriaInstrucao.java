package Parte2;

public class MemoriaInstrucao {
	private static MemoriaInstrucao instance;
	private String[] instrucoes;
	private int PC;
	
	public static MemoriaInstrucao getInstance(){
		if(instance == null){
			instance = new MemoriaInstrucao();
		}
		return instance;
	}
	
	private MemoriaInstrucao(){
		this.instrucoes = new String[10];
		this.PC = 0;
	}
	
	public void setPC(int pos){
		this.PC = pos;
	}
	
	public int getPC(){
		return this.PC;
	}
	
	public void inserirInstrucao(String assembly){
		if(PC == this.instrucoes.length-1){
			this.duplicarMemoria();
			this.instrucoes[this.PC] = assembly;
			this.PC++;
		}else{
			this.instrucoes[this.PC] = assembly;
			this.PC++;	
		}			
	}
	
	private void duplicarMemoria(){
		String[] memoriaDuplicada = new String[this.instrucoes.length * 2];
		if(this.instrucoes != null && this.instrucoes.length > 0){
			for(int i = 0; i < this.instrucoes.length; i++){
				memoriaDuplicada[i] = this.instrucoes[i];
			}
		}		
		this.instrucoes = memoriaDuplicada;
	}
	
	@Override
	public String toString() {
		String retorno = "";
		for(String a : this.instrucoes){
			if(a != null)
				retorno += a+"\n";
		}
		return retorno;
	}
	
	public static void clear(){
		instance = null;
	}
	
	public String[] getInstrucoes(){
		return this.instrucoes;
	}
	
	
	
}
