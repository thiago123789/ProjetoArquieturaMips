package Parte2;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import Erros.InvalidRegisterException;

public class BancoDeRegistradores {
	private static BancoDeRegistradores instance;
	private LinkedHashMap<String, Integer> registradores = new LinkedHashMap<>();
	
	public static BancoDeRegistradores getInstance(){
		if(instance == null){
			instance = new BancoDeRegistradores();
		}
		return instance;
	}
	
	private BancoDeRegistradores(){
		registradores.put("$0", 0);
		registradores.put("$1", 0);
		registradores.put("$2", 0);
		registradores.put("$3", 0);
		registradores.put("$4", 0);
		registradores.put("$5", 0);
		registradores.put("$6", 0);
		registradores.put("$7", 0);
		registradores.put("$8", 0);
		registradores.put("$9", 0);
		registradores.put("$10", 0);
		registradores.put("$11", 0);
		registradores.put("$12", 0);
		registradores.put("$13", 0);
		registradores.put("$14", 0);
		registradores.put("$15", 0);
		registradores.put("$16", 0);
		registradores.put("$17", 0);
		registradores.put("$18", 0);
		registradores.put("$19", 0);
		registradores.put("$20", 0);
		registradores.put("$21", 0);
		registradores.put("$22", 0);
		registradores.put("$23", 0);
		registradores.put("$24", 0);
		registradores.put("$25", 0);
		registradores.put("$26", 0);
		registradores.put("$27", 0);
		registradores.put("$28", 0);
		registradores.put("$29", 0);
		registradores.put("$30", 0);
		registradores.put("$31", 0);
	}

	public HashMap<String, Integer> getRegistradores(){
		return (HashMap<String, Integer>) this.registradores;
	}

	public void setValue(String reg, Integer value) throws InvalidRegisterException{
		if(this.registradores.containsKey(reg)){
			if(reg.equals("$0")){
				value = 0;
				this.registradores.put(reg, value);
			}else{
				this.registradores.put(reg, value);
			}
		}else{
			throw new InvalidRegisterException(reg);
		}
	}
	
	public Integer getValue(String reg) throws InvalidRegisterException{
		Integer value = null;
		if(this.registradores.containsKey(reg)){
			for (Map.Entry<String,Integer> entry : this.registradores.entrySet()){
				if (entry.getKey().equals(reg)){
					value = entry.getValue();
					break;
				}
				
			}
		}else{
			throw new InvalidRegisterException(reg);
		}
		return value;
	}
	
	
}
