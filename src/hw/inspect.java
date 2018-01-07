package hw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class inspect {
	public static void main(String[] args) {
		File f = new File(args[0]);
		FileReader fileReader;
		Map<String, Integer> map = new HashMap<>();
		try {
			fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			boolean isTitle = true;
			while ((line = bufferedReader.readLine()) != null) {
				if(!isTitle) {
					String[] curarr = line.split(",");
					String classification = curarr[curarr.length - 1];
					if (map.containsKey(classification)) {
						map.put(classification, map.get(classification) + 1);
					} else {
						map.put(classification, 1);
					}
				}
				isTitle = false;
			}
			double entropy = 0.0;
			int max = 0;
			
			System.out.println(map);
			for (String str: map.keySet()){
				entropy += map.get(str) * 1.0 / map.size() * Math.log10(map.size()*1.0/map.get(str)) / Math.log10(2.0);
				if (map.get(str) > max) {
					max = map.get(str);
				}
			}
			System.out.println("entropy:" + entropy);
			System.out.println("error:" + (map.size() - max) * 1.0 / map.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
