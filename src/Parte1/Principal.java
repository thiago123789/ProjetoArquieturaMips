package Parte1;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Principal {
	public static void main(String[] args) {
		FileIn f = FileIn.getInstance();
		ConvertToBinary cb = new ConvertToBinary(f.getInstrucoes());
		Interpretador in = new Interpretador(cb.getInstrucoesBinary());
		f.escreverSaida(in);
		JOptionPane.showMessageDialog(null, "Verifique o arquivo Saida.txt...\nCaso deseje ver a memória de dados verifique o arquivo dataMemmory.txt.");
		String path = System.getProperty("user.dir");
		try {
			Runtime.getRuntime().exec("notepad "+path+"\\saida.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
