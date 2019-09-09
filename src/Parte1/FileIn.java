package Parte1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Parte2.MemoriaInstrucao;
import Parte3.MemoriaDados;
/*
 * CLASSE RESPONSÃ�VEL PELA LEITURA DO ARQUIVO DE ENTRADA
 * LER TODAS AS LINHAS DO ARQUIVO E SALVA EM UM ARRAYLIST DE STRING
 */
public class FileIn {
	private static FileIn instance;
	private MemoriaInstrucao instrucoes;
	private String[] tempInstructions;
	
	public static FileIn getInstance(){
		if(instance == null){
			instance = new FileIn();
		}
		return instance;
	}
	
	private FileIn(){
		this.instrucoes = MemoriaInstrucao.getInstance();
		this.lerArquivo();
	}

	private void lerArquivo(){
		try{
			FileReader arq = new FileReader("entrada.txt");
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine();
			while (linha != null) {
				if(linha.contains("0x")){
					this.instrucoes.inserirInstrucao(linha.substring(2, 10));
					linha = lerArq.readLine(); // lÃª da segunda atÃ© a Ãºltima linha
				}else{
					this.instrucoes.inserirInstrucao(linha);
					linha = lerArq.readLine();
				}
				
			}
			arq.close();
		}catch(IOException e){
			e.getMessage();
		}
	}
	
	public void escreverSaida(Interpretador resultado){
		try{
			FileWriter fw = new FileWriter("saida.txt");
			BufferedWriter bw = new BufferedWriter(fw);  
			 
			for(String b : resultado.getAssembly()){
				bw.write(b+"\r\n");
			}			
			bw.flush();  
			bw.close();
			this.saveMemmory();
		}catch(IOException e){
			e.getMessage();
		}
	}
	
	private void saveMemmory(){
		String memo = MemoriaDados.getInstance().toString();
		try{
			FileWriter fw = new FileWriter("dataMemmory.txt");
			BufferedWriter bw = new BufferedWriter(fw);  
						
			bw.write(memo);
					
			bw.flush();  
			bw.close();  
		}catch(IOException e){
			e.getMessage();
		}
	}
	
	public String[] getInstrucoes(){
		this.tempInstructions = this.instrucoes.getInstrucoes();
		MemoriaInstrucao.clear();
		return this.tempInstructions;
	}
	
	

}
