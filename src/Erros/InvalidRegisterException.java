package Erros;

public class InvalidRegisterException extends Exception{
	public InvalidRegisterException(String reg) {
		super(reg + " register is invalid.");
	}
}
