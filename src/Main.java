import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

	public static void main(String[] args) {
//		if (args.length == 0) {
//			System.out.println("Please a command line argument");
//			return;
//		}
		try {
			// read command line arguments
			int algoId = Integer.parseInt(args[0]);
			System.out.println("algoId:"+algoId);
			File queryFile = new File(args[1]);
			File dataFile = new File(args[2]);
			File alphabetFile = new File(args[3]);
			File scoringMatrixFile = new File(args[4]);
			int topK = Integer.parseInt(args[5]);
			float gapPenalty = Float.parseFloat(args[6]);
			
			List<String> queries = parseSequence(queryFile);
			List<String> database = parseSequence(dataFile);
			
			BufferedReader br = new BufferedReader(new FileReader(alphabetFile));
			String alphabets = br.readLine();
			ArrayList<ArrayList<Float>> scoringMatrix = parseScoringMatrix(scoringMatrixFile);
			
			SequenceAlignment sa = new SequenceAlignment(gapPenalty, scoringMatrix, alphabets);
			System.out.println(sa.dovetailAlignment(queries.get(0), database.get(0)));
			
			ArrayList<Float> result = new ArrayList<Float>();
			int count = 0;
			for (int i = 0; i < queries.size(); i++) {
				for (int j = 0; j < database.size(); j++) {
					if (algoId == 1) {
						result.add(sa.globalAlignment(queries.get(i), database.get(j)));
					} else if (algoId == 2) {
						result.add(sa.localAlignment(queries.get(i), database.get(j)));
					} else if (algoId == 3) {
						result.add(sa.dovetailAlignment(queries.get(i), database.get(j)));
					}
					count++;
				}
			}
			
			Collections.sort(result, Collections.reverseOrder());
			for (int i = 0; i < topK; i++)
				System.out.println(result.get(i));
			
		} catch (Exception e) {
			System.out.println("Unknown error occured");
			e.printStackTrace();
		}
		

	}
	
	static List<String> parseSequence(File file) {
		List<String> result = new LinkedList<>();
		try {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			if(line.charAt(0) == '>') {
				result.add(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(line);
			}
		}
		result.remove(0);
		br.close();
		} catch (IOException e) {
			System.out.println("File not found");
		}
		return result;
	}
	
	static ArrayList<ArrayList<Float>> parseScoringMatrix(File file) {
		ArrayList<ArrayList<Float>> res = new ArrayList<>(); 
		String line;
		try {
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
			String[] scores = line.split("\\s+");
			ArrayList<Float> temp = new ArrayList<>();
			for (String score : scores) {
				temp.add(Float.parseFloat(score));
			}
			res.add(new ArrayList<Float>(temp));
		}
		br.close();
		} catch (IOException e) {
			System.out.println("IOException!");
		}
		return res;
	}
	
	
}
