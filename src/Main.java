import java.io.File;
import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
//		if (args.length == 0) {
//			System.out.println("Please a command line argument");
//			return;
//		}
		try {
			// read command line arguments
//			int algoId = Integer.parseInt(args[0]);
//			File queryFile = new File(args[1]);
//			File dataFile = new File(args[2]);
//			File alphabetFile = new File(args[3]);
//			File scoringMatrixFile = new File(args[4]);
//			int topK = Integer.parseInt(args[5]);
//			float gapPenalty = Float.parseFloat(args[6]);
			
			float gapPenalty = -1;
			float[][] scoringMatrix = {{1,-1,-1,-1}, {-1,1,-1,-1}, {-1,-1,1,-1}, {-1,-1,-1,1}};
			String alphabets = "ACGT";
			SequenceAlignment sa = new SequenceAlignment(gapPenalty, scoringMatrix, alphabets);
			System.out.println(sa.dovetailAlignment("AAAATTTT", "TTTTGGGG"));
			
			
			
		} catch (Exception e) {
			System.out.println("Unknown error occured");
			e.printStackTrace();
		}
		

	}
	

}
