
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class partA {
	private static void parseline(String[] res, String line) {
		//Gender
		int curidx = line.indexOf("Gender") + 7;
		if (res[0] == null) {
			res[0] = line.charAt(curidx) == 'F' ? "Female" : "Male";
		} else if (line.charAt(curidx) != res[0].charAt(0)) {
			res[0] = "?";
		}
		
		//Age
		curidx = line.indexOf("Age") + 4;
		if (res[1] == null) {
			res[1] = line.charAt(curidx) == 'Y' ? "Young" : "Old";
		} else if (line.charAt(curidx) != res[1].charAt(0)) {
			res[1] = "?";
		}
		
		//Student?
		curidx = line.indexOf("Student?") + 9;
		if (res[2] == null) {
			res[2] = line.charAt(curidx) == 'Y' ? "Yes" : "No";
		} else if (line.charAt(curidx) != res[2].charAt(0)) {
			res[2] = "?";
		}
		
		//PreviouslyDeclined?
		curidx = line.indexOf("PreviouslyDeclined?") + 20;
		if (res[3] == null) {
			res[3] = line.charAt(curidx) == 'Y' ? "Yes" : "No";
		} else if (line.charAt(curidx) != res[3].charAt(0)) {
			res[3] = "?";
		}
		
		//HairLength
		curidx = line.indexOf("HairLength") + 11;
		if (res[4] == null) {
			res[4] = line.charAt(curidx) == 'L' ? "Long" : "Short";
		} else if (line.charAt(curidx) != res[4].charAt(0)) {
			res[4] = "?";
		}
		
		//Employed?
		curidx = line.indexOf("Employed?") + 10;
		if (res[5] == null) {
			res[5] = line.charAt(curidx) == 'Y' ? "Yes" : "No";
		} else if (line.charAt(curidx) != res[5].charAt(0)) {
			res[5] = "?";
		}
		
		//TypeOfColateral
		curidx = line.indexOf("TypeOfColateral") + 16;
		if (res[6] == null) {
			res[6] = line.charAt(curidx) == 'H' ? "House" : "Car";
		} else if (line.charAt(curidx) != res[6].charAt(0)) {
			res[6] = "?";
		}
		
		//FirstLoan
		curidx = line.indexOf("FirstLoan") + 10;
		if (res[7] == null) {
			res[7] = line.charAt(curidx) == 'Y' ? "Yes" : "No";
		} else if (line.charAt(curidx) != res[7].charAt(0)) {
			res[7] = "?";
		}
		
		//LifeInsurance
		curidx = line.indexOf("LifeInsurance") + 14;
		if (res[8] == null) {
			res[8] = line.charAt(curidx) == 'Y' ? "Yes" : "No";
		} else if (line.charAt(curidx) != res[8].charAt(0)) {
			res[8] = "?";
		}
	}
	
	public static void main(String[] args) throws IOException {
		//P1 - P5
		System.out.println(512);
		System.out.println(155);
		System.out.println(19684);
		System.out.println(59050);
		System.out.println(26245);
		
		//P6
		//initialization : h0
		String[] res = {null, null, null, null, null, null, null, null, null};
		String line;
		try {
			FileReader fileReader = new FileReader("9Cat-Train.labeled");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			File fout = new File("partA6.txt");
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 
			try {
				int idx = 0;
				while((line = bufferedReader.readLine()) != null) {
					int riskidx = line.indexOf("Risk") + 5;
					if (line.charAt(riskidx) == 'h') {
						parseline(res, line);
					}
					if (idx % 30 == 29) {
						StringBuilder sb = new StringBuilder();
						sb.append(res[0]);
						for(int i = 1; i < res.length; i ++) {
							sb.append("\t");
							sb.append(res[i]);
						}
						bw.write(sb.toString());
						bw.newLine();
					}
				    idx ++;
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//P7
		FileReader fileReader;
		try {
			fileReader = new FileReader("9Cat-Dev.labeled");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int count = 0;
			int miss = 0;
			try {
				while((line = bufferedReader.readLine()) != null) {
					count ++;
					int riskidx = line.indexOf("Risk") + 5;
					boolean positive = line.charAt(riskidx) == 'h';
					String[] curres = {null, null, null, null, null, null, null, null, null};
					parseline(curres,line);
					for (int i = 0; i < res.length; i ++) {
						if (res[i].charAt(0) != '?') {
							if ((positive && res[i] != curres[i]) || (!positive && res[i] == curres[i])) {
								miss ++;
							}
						}
					}
				}
				System.out.println(1.0 * miss / count);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//P8
		try {
			fileReader = new FileReader(args[0]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					String[] curres = {null, null, null, null, null, null, null, null, null};
					parseline(curres,line);
					for (int i = 0; i < res.length; i ++) {
						if (res[i].charAt(0) != '?' && res[i] != curres[i]) {
							System.out.println("low");
						} else {
							System.out.println("high");
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
