package Erros;

public class EnderecoInvalidoException extends Exception{
	public EnderecoInvalidoException(String address){
		super("Enderço de memória ("+address+") inválido!!");
	}
}
