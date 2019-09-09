package Parte1;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import Erros.EnderecoInvalidoException;
import Erros.InvalidRegisterException;
import Parte2.BancoDeRegistradores;
import Parte2.MemoriaInstrucao;
import Parte3.MemoriaDados;

public class Interpretador {
	private ArrayList<String> assembly;
	private BancoDeRegistradores registradores;
	private MemoriaInstrucao memory;
	private MemoriaDados dataMemmory;

	public Interpretador(ArrayList<String> palavras){
		this.assembly = new ArrayList<String>();
		this.registradores = BancoDeRegistradores.getInstance();
		this.memory = MemoriaInstrucao.getInstance();
		this.dataMemmory = MemoriaDados.getInstance();
		this.palavrasAll(palavras);
	}

	private void palavrasAll(ArrayList<String> in){
		this.memory.setPC(0);
		for(String aux : in){
			this.traduzirPalavra();
		}
	}

	/*
	 * Metodo que converte uma palavra binaria em instrucao assembly
	 */
	private void traduzirPalavra(){
		String assembly = "";
		String[] inst = this.memory.getInstrucoes();
		int PClocal = this.memory.getPC();
		for(int i = 0; i <= inst.length-1; i++){
			if(i == PClocal && inst[i] != null){
				String palavra = inst[i];
				boolean jump = false;
				HashMap<String, Integer> reg = registradores.getRegistradores();
				Integer b,c;
				String opcode = palavra.substring(0, 6); //Opcode da funcao
				String rs = palavra.substring(6, 11); // Registrador de origem 1
				String rt = palavra.substring(11, 16); // Registrador de origem 2
				String rd = palavra.substring(16, 21); // Registrador de destino
				String sa = palavra.substring(21, 26); // Quantidade de deslocamento (shift amount)
				String imm = palavra.substring(16, 32); // Imediato
				String address = palavra.substring(6, 32); //endere�o completo
				BigInteger rsInt = new BigInteger(rs, 2); //Converte o valor do rs para inteiro
				BigInteger rtInt = new BigInteger(rt, 2); //Converte o valor do rt para inteiro
				BigInteger rdInt = new BigInteger(rd, 2); //Converte o valor do rd para inteiro
				BigInteger saInt = new BigInteger(sa, 2); //Converte o valor do sa para inteiro
				Integer high,low = new Integer(0);
				Integer rsValue = null, rtValue = null;
				try{
					rsValue = this.registradores.getValue("$"+rsInt.toString());
					rtValue = this.registradores.getValue("$"+rtInt.toString());
				}catch(InvalidRegisterException exc){
					JOptionPane.showMessageDialog(null, exc.getMessage());
				}
				high = null;
				low = null;

				String immInt = this.complementoDoisInt(imm);//Converte o valor do imm para inteiro em complemento a dois
				BigInteger addressInt = new BigInteger(address,2); // Converte o valor do address para inteiro
				int brenchAddressBin = this.branchString(imm);
				switch(opcode){
				case "000000": //CASE PARA TODAS AS INSTRUCOES TIPO R (OPCODE ZERADO)
					String function = palavra.substring(26, 32); //Extensao do opcode
					switch(function){
					case "100000":
						assembly += "ADD";
						assembly += " $"+rdInt.toString();
						assembly += ", $"+rsInt.toString();
						assembly += ", $"+rtInt.toString();
						b = reg.get("$"+rsInt.toString());
						c = reg.get("$"+rtInt.toString());
						try {
							registradores.setValue("$"+rdInt.toString(), b+c);
						} catch (InvalidRegisterException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
						break;
					case "100001":
						assembly += "ADDU";
						assembly += " $"+rdInt.toString();
						assembly += ", $"+rsInt.toString();
						assembly += ", $"+rtInt.toString();
						b = reg.get("$"+rsInt.toString());
						c = reg.get("$"+rtInt.toString());
						try {
							registradores.setValue("$"+rdInt.toString(), b+c);
						} catch (InvalidRegisterException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
						break;
					case "100100":
						assembly += "AND";
						assembly += " $"+rdInt.toString();
						assembly += ", $"+rsInt.toString();
						assembly += ", $"+rtInt.toString();
						b = reg.get("$"+rsInt.toString());
						c = reg.get("$"+rtInt.toString());
						try {
							registradores.setValue("$"+rdInt.toString(), b&c);
						} catch (InvalidRegisterException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
						break;
					case "001000":
						assembly += "JR";
						assembly += " $"+rsInt.toString();
						try {
							jump = true;
							this.memory.setPC(this.registradores.getValue("$"+rsInt.toString())/4);
						} catch (InvalidRegisterException e4) {
							JOptionPane.showMessageDialog(null, e4.getMessage());
						}
						break;
					case "010000":
						assembly += "MFHI";
						assembly += " $"+rdInt.toString();
						try {
							registradores.setValue("$"+rdInt.toString(), high);
						} catch (InvalidRegisterException e3) {
							JOptionPane.showMessageDialog(null, e3.getMessage());
						}
						break;
					case "011000":
						assembly += "MULT";
						assembly += " $"+rsInt.toString();
						assembly += ", $"+rtInt.toString();
						Integer multi1 = 0;
						try {
							multi1 = this.registradores.getValue("$"+rsInt)*this.registradores.getValue("$"+rtInt);
						} catch (InvalidRegisterException e2) {
							e2.printStackTrace();
						}
						break;
					case "011001":
						assembly += "MULTU";
						assembly += " $"+rsInt.toString();
						assembly += ", $"+rtInt.toString();
						Integer multi = 0;
						try {
							multi = this.registradores.getValue("$"+rsInt)*this.registradores.getValue("$"+rtInt);
						} catch (InvalidRegisterException e2) {
							e2.printStackTrace();
						}
						Integer hightemp = multi >> 16;
		Integer lowtemp = multi << 16;
		lowtemp = lowtemp >> 16;
		//				high = Integer.parseInt(hightemp.toString());
		//				low = Integer.parseInt(lowtemp.toString());
		//				BigInteger aux = new BigInteger(multi.toString());
		//				assembly += "\r\nMultiplicação = Decimal - " + multi.toString()+" | Binario = "+ aux.toString(2);
		//				BigInteger aux1 = new BigInteger(hightemp.toString());
		//				assembly += "\r\nHigh = " + hightemp.toString()+" | Binario = "+ aux1.toString(2);
		BigInteger aux2 = new BigInteger(multi.toString());
		//				Integer aux3 = Integer.parseInt(aux2.toString());
		//				assembly += "\r\nLow = " + new BigInteger(this.and(aux2.toString(2), "1111111111111111"), 2).toString() +" | Binario = "+ this.and(aux2.toString(2), "1111111111111111");
		high = Integer.parseInt(hightemp.toString());
		low = Integer.parseInt(new BigInteger(this.and(aux2.toString(2), "1111111111111111"), 2).toString());
		break;
		case "100111":
			assembly += "NOR";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = reg.get("$"+rtInt.toString());
			try {
				registradores.setValue("$"+rdInt.toString(), ~(b|c));
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "100101":
			assembly += "OR";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = reg.get("$"+rtInt.toString());
			try {
				registradores.setValue("$"+rdInt.toString(), b|c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "000000":
			assembly += "SLL";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", $"+saInt.toString();
			b = rtInt.intValue() << saInt.intValue();
			try {
				registradores.setValue("$"+rdInt.toString(), b);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "000100":
			assembly += "SLLV";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", $"+rsInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = rtInt.intValue() << b;
			try {
				registradores.setValue("$"+rdInt.toString(), c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "101010":
			assembly += "SLT";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = Integer.parseInt(rsInt.toString()) < Integer.parseInt(rtInt.toString()) ? 1 : 0;
			try {
				this.registradores.setValue("$"+rdInt.toString(), b);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			};
			break;
		case "000011":
			assembly += "SRA";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", "+saInt.toString();
			b = rtInt.intValue() >>> saInt.intValue();
			try {
				registradores.setValue("$"+rdInt.toString(), b);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "000111":
			assembly += "SRAV";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", $"+rsInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = rtInt.intValue() >>> b;
			try {
				registradores.setValue("$"+rdInt.toString(), c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "000010":
			assembly += "SRL";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", "+saInt.toString();
			b = rtInt.intValue() >>> saInt.intValue();
			try {
				registradores.setValue("$"+rdInt.toString(), b);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "000110":
			assembly += "SRLV";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rtInt.toString();
			assembly += ", $"+rsInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = rtInt.intValue() >>> b;
			try {
				registradores.setValue("$"+rdInt.toString(), c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "100010":
			assembly += "SUB";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = reg.get("$"+rtInt.toString());
			try {
				registradores.setValue("$"+rdInt.toString(), b-c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "100011":
			assembly += "SUBU";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = reg.get("$"+rtInt.toString());
			try {
				registradores.setValue("$"+rdInt.toString(), b-c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "001100":
			assembly += "SYSCALL";
			break;
		case "100110":
			assembly += "XOR";
			assembly += " $"+rdInt.toString();
			assembly += ", $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			b = reg.get("$"+rsInt.toString());
			c = reg.get("$"+rtInt.toString());
			try {
				registradores.setValue("$"+rdInt.toString(), b^c);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		case "011011":
			assembly += "DIVU";
			assembly += " $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			high = rsInt.intValue()%rtInt.intValue();
			low = rsInt.intValue()/rtInt.intValue();
			break;
		case "011010":
			assembly += "DIV";
			assembly += " $"+rsInt.toString();
			assembly += ", $"+rtInt.toString();
			high = rsInt.intValue()%rtInt.intValue();
			low = rsInt.intValue()/rtInt.intValue();
			break;
		case "010010":
			assembly += "MFLO";
			assembly += " $"+rdInt.toString();
			try {
				registradores.setValue("$"+rdInt.toString(), low);
			} catch (InvalidRegisterException e3) {
				JOptionPane.showMessageDialog(null, e3.getMessage());
			}
			break;
		default:
			System.out.println("ERRO");
					}
					break;
				case "001000":
					assembly += "ADDI";
					assembly += " $"+rtInt.toString();
					assembly += ", $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					b = reg.get("$"+rsInt.toString());
					c = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), b+c);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "001001":
					assembly += "ADDIU";
					assembly += " $"+rsInt.toString();
					assembly += ", $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					b = reg.get("$"+rsInt.toString());
					c = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), b+c);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "001100":
					assembly += "ANDI";
					assembly += " $"+rsInt.toString();
					assembly += ", $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					b = reg.get("$"+rsInt.toString());
					c = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), b&c);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "000001":
					assembly += "BLTZ";
					assembly += " $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					if(rsValue < 0){
						jump = true;
						this.memory.setPC(this.memory.getPC()+Integer.parseInt(immInt.toString())+1);
					}
					break;
				case "000011":
					assembly += "JAL";
					assembly += " "+addressInt.toString();
					try {
						jump = true;
						this.registradores.setValue("$31", (this.memory.getPC()+1)*4);
						this.memory.setPC(Integer.parseInt(addressInt.toString()));
					} catch (InvalidRegisterException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}

					break;
				case "000101":
					assembly += "BNE";
					assembly += " $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					if(rsValue != rtValue){
						jump = true;
						this.memory.setPC(this.memory.getPC()+Integer.parseInt(immInt.toString())+1);
					}
					break;
				case "000010":
					assembly += "J";
					assembly += " "+immInt.toString();
					jump = true;
					this.memory.setPC(Integer.parseInt(immInt.toString()));
					break;
				case "001111":
					assembly += "LUI";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					Integer a = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), (a << 16));
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "001101":
					assembly += "ORI";
					assembly += " $"+rtInt.toString();
					assembly += ", $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					b = reg.get("$"+rsInt.toString());
					c = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), b|c);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "001010":
					assembly += "SLTI";
					assembly += " $"+rdInt.toString();
					assembly += ", $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					b = Integer.parseInt(rsInt.toString()) < Integer.parseInt(immInt.toString()) ? 1 : 0;
					try {
						this.registradores.setValue("$"+rdInt.toString(), b);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "101011":
					assembly += "SW";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					assembly += "($"+rsInt.toString()+")";
					try {
						String addressMemmory = Integer.toString(rsValue+Integer.parseInt(immInt));
						this.dataMemmory.insertData(addressMemmory, this.registradores.getValue("$"+rtInt.toString()));
					} catch (InvalidRegisterException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					} 
					break;
				case "001110":
					assembly += "XORI";
					assembly += " $"+rtInt.toString();
					assembly += ", $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					b = reg.get("$"+rsInt.toString());
					c = Integer.parseInt(immInt.toString());
					try {
						registradores.setValue("$"+rtInt.toString(), b^c);
					} catch (InvalidRegisterException e3) {
						JOptionPane.showMessageDialog(null, e3.getMessage());
					}
					break;
				case "000100":
					assembly += "BEQ";
					assembly += " $"+rsInt.toString();
					assembly += ", "+immInt.toString();
					if(rsValue == rtValue){
						jump=true;
						this.memory.setPC(this.memory.getPC()+Integer.parseInt(immInt.toString())+1);
					}
					break;
				case "100000":
					assembly += "LB";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					assembly += "($"+rsInt.toString()+")";
					try {
						String posicaoMemoria1 = Integer.toString(Integer.parseInt(immInt)+this.registradores.getValue("$"+rsInt.toString()));
						this.registradores.setValue("$"+rtInt.toString(), this.signalExtend32(this.dataMemmory.getByteValue(posicaoMemoria1)));
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "Exception"+e2.getMessage());
					} catch (InvalidRegisterException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
					break;
				case "100100":
					assembly += "LBU";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					assembly += "($"+rsInt.toString()+")";
					try {
						String posicaoMemoria1 = Integer.toString(Integer.parseInt(immInt)+this.registradores.getValue("$"+rsInt.toString()));
						this.registradores.setValue("$"+rtInt.toString(), this.dataMemmory.getByteValue(posicaoMemoria1));
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "Exception"+e2.getMessage());
					} catch (InvalidRegisterException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
					break;
				case "101000":
					assembly += "SB";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt;
					assembly += "($"+rsInt.toString()+")";
					try {
						String posicaoMemoria = Integer.toString(Integer.parseInt(immInt)+this.registradores.getValue("$"+rsInt.toString()));
						this.dataMemmory.insertData(posicaoMemoria, this.registradores.getValue("$"+rtInt.toString()));
					} catch (InvalidRegisterException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					} 
					break;
				case "100011":
					assembly += "LW";
					assembly += " $"+rtInt.toString();
					assembly += ", "+immInt.toString();
					assembly += "($"+rsInt.toString()+")";
					try {
						rt = dataMemmory.getValue(Integer.toString(Integer.parseInt(immInt)+this.registradores.getValue("$"+rsInt.toString())));
						rt = rt.replaceAll("[^0123456789]", "");
						this.registradores.setValue("$"+rtInt.toString(), Integer.parseInt(rt)*-1);
					}catch (InvalidRegisterException e){
						JOptionPane.showMessageDialog(null, e.getMessage());
					}catch (EnderecoInvalidoException e) {
						JOptionPane.showMessageDialog(null, "Memmory address on \""+assembly+"\" is invalid (Address = "+immInt+rsInt+").");
					} 
					break;
				default:
					System.out.println("Erro");
				}
				assembly = assembly.toLowerCase()+"\r\n"+registradores.getRegistradores().toString();
				if(!jump){
					this.memory.setPC(this.memory.getPC()+1);
				}
//				assembly += "\r\n\r\n"+this.dataMemmory.toString();
				break;
			}
		}
		this.assembly.add(assembly);
	}

	//CONVERTE A STRING BINARIA EM COMPLEMENTO A DOIS EM INTEIRO COM SINAL	
	private String complementoDoisInt(String palavra){
		String resultado = "";
		if(palavra.charAt(0) == '1'){ // NUMEROS NEGATIVOS
			String aux = "";
			char[] a = new char[palavra.length()];
			a = palavra.toCharArray();
			resultado += "-";
			boolean apareceu = false;
			for(int i = a.length-1; i >= 1; i--){
				char temp = a[i];
				if(temp == '1' && !apareceu){
					aux = "1"+aux;
					apareceu = true;
				}else if(temp == '1' && apareceu){
					aux = "0"+aux;
				}else if(temp == '0' && apareceu){
					aux = "1"+aux;
				}else if(temp == '0'){
					aux = "0"+aux;
				}
			}
			resultado += new BigInteger(aux, 2).toString();
		}else{ // NUMEROS POSITIVOS
			resultado = new BigInteger(palavra.substring(1, palavra.length()), 2).toString();
		}
		return resultado;
	}

	private String and(String binary, String mask){
		String result = "";
		char[] array = binary.toCharArray();
		char[] arrayMask = mask.toCharArray();
		if(arrayMask.length < array.length){
			char[] tempArray = new char[array.length];
			int maskCount = 0;
			for(int i = 0;i <= tempArray.length-1; i++){
				if(i < (array.length - arrayMask.length)){
					tempArray[i] = '0';
				}else{
					tempArray[i] = arrayMask[maskCount];
					maskCount++;
				}
			}
			arrayMask = tempArray;
		}
		for(int i = 0; i <= array.length-1; i++){
			if(array[i] == '1' && arrayMask[i] == '1'){
				result += "1";
			}else if(array[i] == '0' && arrayMask[i] == '1'){
				result += "0";
			}else if(array[i] == '1' && arrayMask[i] == '0'){
				result += "0";
			}else if(array[i] == '0' && arrayMask[i] == '0'){
				result += "0";
			}
		}			
		return this.removeZerosLeft(result);
	}
	
	public int signalExtend32(int numero){
		String extended = "";
		char[] numeroChar = Integer.toBinaryString(numero).toCharArray();
		char signal = numeroChar[0];
		int tamanho = (32 - numeroChar.length)-1;
		for(int i = 0; i <= 31; i++){
			if(i <= tamanho){
				extended += signal;
			}
		}
		int decimalValue = 0;
		extended += Integer.toBinaryString(numero);
		if(signal == '1'){
			extended = extended.replace("0", " "); //temp replace 0s
			extended = extended.replace("1", "0"); //replace 1s with 0s
			extended = extended.replace(" ", "1");
			decimalValue = Integer.parseInt(extended, 2);
			decimalValue = (decimalValue + 1) * -1;
		}else{
			decimalValue = Integer.parseInt(extended, 2);
		}
		return decimalValue;
	}

	public int branchString(String bin){
		String result = "";
		int tam = 16-bin.length();
		for(int i = 0; i <= tam; i++){
			result += "0";
		}
		result+=bin+"00";
		String resutlTemp = "";
		char bit = result.charAt(0);
		for(int i = 0; i <= 13; i++){
			resutlTemp += bit;
		}
		return Integer.parseInt(resutlTemp+result, 2);
	}

	private String removeZerosLeft(String binary){
		String result = "";
		char[] array = binary.toCharArray();
		boolean achou = false;
		for(char a : array){
			if(a == '0' && !achou){
				result += "";
			}else if(a == '0' && achou){
				result += "0";
			}else if(a == '1'){
				achou = true;
				result += "1";
			}else if(achou){
				result += a;
			}
		}


		return result;
	}

	public ArrayList<String> getAssembly(){
		return this.assembly;
	}
}
