package Parte3;
import Erros.EnderecoInvalidoException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoriaDados {
	private static MemoriaDados instance;
	private LinkedHashMap<String, PosicaoMemoria> mem = new LinkedHashMap<>();
	private int counter;

	public static MemoriaDados getInstance(){
		if(instance == null){
			instance = new MemoriaDados();
		}
		return instance;
	}

	private MemoriaDados(){
		this.counter = 8192;
		mountMemmory();
	}

	private void mountMemmory(){
		for(int i = 0; i <= 255; i++){
			PosicaoMemoria aux = new PosicaoMemoria();
			this.mem.put("&"+this.counterConverter(counter), aux);
			this.counter += 4;
		}
	}

	private String counterConverter(int counter){
		return Integer.toString(counter, 16);
	}

	public void insertData(String address, int data){
		int address2 = Integer.parseInt(this.counterConverter(Integer.parseInt(address)))-2000;
		int count = address2 / 4;
		address = "&"+this.counterConverter(Integer.parseInt(address));
		for (Map.Entry<String,PosicaoMemoria> entry : this.mem.entrySet()){
			if (entry.getKey().equals("&"+Integer.toString(2000+(count*4)))){
				int contador = 0;
				address2 = address2 - count*4;
				if(address2 == 0){ 
					contador = address2;
					data = data << contador*8;
					entry.getValue().conteudo = this.and32(entry.getValue().conteudo, data, 4);
				}else if (address2 == 1){
					contador = address2;
					data = data << contador*8;
					entry.getValue().conteudo = this.and32(entry.getValue().conteudo, data, 3);
				}else if (address2 == 2){
					contador = address2;
					data = data << contador*8;
					entry.getValue().conteudo = this.and32(entry.getValue().conteudo, data, 2);
				}else if (address2 == 3){
					contador = address2;
					data = data << contador*8;
					entry.getValue().conteudo = this.and32(entry.getValue().conteudo, data, 1);
				}
				break;
			}
		}
	}

	public int and32(int numberInMemmory, int numberToInsert, int byteToZero){
		String result = "";
		String numeroString = Integer.toBinaryString(numberInMemmory);
		String numero2String = Integer.toBinaryString(numberToInsert);

		while(numeroString.length() < 32){
			numeroString = this.insertZerosLeft(numeroString);
		}

		while(numero2String.length() < 32){
			numero2String = this.insertZerosLeft(numero2String);
		}
		char[] array = numeroString.toCharArray();
		char[] array2 = numero2String.toCharArray();

		if(byteToZero == 1){
			int k = (0+8)-1;
			for(int i = 0; i <= k; i++){
				array[i] = '0';
			}
		}else if(byteToZero == 2){
			int k = (8+8)-1;
			for(int i = 8; i <= k; i++){
				array[i] = '0';
			}
		}else if(byteToZero == 3){
			int k = (16+8)-1;
			for(int i = 16; i <= k; i++){
				array[i] = '0';
			}
		}else if(byteToZero == 4){
			int k = (24+8)-1;
			for(int i = 24; i <= k; i++){
				array[i] = '0';
			}
		}

		for(int i = 0; i <= array.length-1; i++){
			if(array[i] == '1' || array2[i] == '1'){
				result += "1";
			}else{
				result += "0";
			}
		}
		return Integer.parseUnsignedInt(result, 2);
	}


	private String insertZerosLeft(String binary){
		String result = "";
		char[] array = binary.toCharArray();
		//		char[] arrayBin = new char[32];
		int i = 32-array.length;
		for(int j = 0; j <= i-1; j++){
			result += '0';
		}
		boolean achou = false;
		for(char a : array){
			if(a == '0' && !achou){
				result += "0";
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

	public LinkedHashMap<String, PosicaoMemoria> getMemoria(){
		return (LinkedHashMap<String, PosicaoMemoria>) this.mem;
	}

	public String getValue(String address) throws EnderecoInvalidoException{
		String value = "";
		address = "&"+this.counterConverter(Integer.parseInt(address));
		if(this.mem.containsKey(address)){
			for (Map.Entry<String,PosicaoMemoria> entry : this.mem.entrySet()){
				if (entry.getKey().equals(address)){
					value = entry.getValue().toString();
				}
			}
		}else{
			throw new EnderecoInvalidoException(address);
		}
		return value;
	}


	public int getByteValue(String address) {
		int value = 0;
		int address2 = Integer.parseInt(this.counterConverter(Integer.parseInt(address)))-2000;
		int count = address2 / 4;
		address = "&"+this.counterConverter(Integer.parseInt(address));
		for (Map.Entry<String,PosicaoMemoria> entry : this.mem.entrySet()){
			if (entry.getKey().equals("&"+Integer.toString(2000+(count*4)))){
				int data = entry.getValue().conteudo;
				int contador = 0;
				address2 = address2 - count*4;
				if(address2 == 0){ 
					contador = address2;
					data = data << ((4-address2)-1)*8;
					data = data >>> 24;
					value = data;
				}else if (address2 == 1){
					contador = address2;
					data = data << ((4-address2)-1)*8;
					data = data >>> 24;
					value = data;
				}else if (address2 == 2){
					contador = address2;
					data = data << ((4-address2)-1)*8;
					data = data >>> 24;
					value = data;
				}else if (address2 == 3){
					contador = address2;
					data = data << ((4-address2)-1)*8;
					data = data >>> 24;
					value = data;
				}
				break;
			}
		}
		return value;
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
		return result;
	}


	@Override
	public String toString(){
		int count = 1;
		String result = "";
		for (Map.Entry<String, PosicaoMemoria> entry : mem.entrySet()) {
			String key = entry.getKey();
			PosicaoMemoria value = entry.getValue();
			result += "| "+key +" = "+ value.toString() +" |";
			if( count % 8 == 0){
				result += "\r\n";
			}
			count++;
		}

		return result;
	}




}
