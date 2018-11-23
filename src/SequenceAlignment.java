import java.util.*;
public class SequenceAlignment {
	
	float gapPenalty;
	float[][] scoringMatrix;
	String alphabets;
	
	SequenceAlignment(float gapPenalty, float[][] scoringMatrix, String alphabets) {
		this.gapPenalty = gapPenalty;
		this.scoringMatrix = scoringMatrix;
		this.alphabets = alphabets;
	}
	
	public float globalAlignment(String str1, String str2) {
		float[][] dp = new float[str1.length() + 1][str2.length() + 1];
		int rows = dp.length;
		int cols = dp[0].length;
		
		for (int j = 0; j < cols; j ++)
			dp[0][j] = j*gapPenalty;
		
		for (int i = 0; i < rows; i ++)
			dp[i][0] = i*gapPenalty;
		
		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < cols; j++) {
				dp[i][j] = Math.max(dp[i-1][j] + this.gapPenalty,
							Math.max(
									dp[i][j-1] + this.gapPenalty, dp[i-1][j-1] + replacementPenalty(str1.charAt(i-1), str2.charAt(j-1)) 
									)
						);
			}
		}
		findGlobalAlignment(str1, str2, dp);
		return dp[rows-1][cols-1];
	}
	
	public float localAlignment(String str1, String str2) {
		float[][] dp = new float[str1.length() + 1][str2.length() + 1];
		int rows = dp.length;
		int cols = dp[0].length;
		
		for (int j = 0; j < cols; j ++)
			dp[0][j] = 0;
		
		for (int i = 0; i < rows; i ++)
			dp[i][0] = 0;
		
		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < cols; j++) {
				dp[i][j] = Math.max(0, Math.max(dp[i-1][j] + this.gapPenalty,
							Math.max(
									dp[i][j-1] + this.gapPenalty, dp[i-1][j-1] + replacementPenalty(str1.charAt(i-1), str2.charAt(j-1)) 
									)
						));
			}
		}
		
		
		float maxScore = Float.MIN_VALUE;
		int startingRow = -1;
		int startingCol = -1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (dp[i][j] > maxScore) {
					maxScore = dp[i][j];
					startingRow = i;
					startingCol = j;
				}
			}
		}
		findLocalAlignment(str1, str2, dp, startingRow, startingCol);
		return maxScore;
	}
	
	public float dovetailAlignment(String str1, String str2) {
		float[][] dp = new float[str1.length() + 1][str2.length() + 1];
		int rows = dp.length;
		int cols = dp[0].length;
		
		for (int j = 0; j < cols; j ++)
			dp[0][j] = 0;
		
		for (int i = 0; i < rows; i ++)
			dp[i][0] = 0;
		
		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < cols; j++) {
				dp[i][j] = Math.max(dp[i-1][j] + this.gapPenalty,
							Math.max(
									dp[i][j-1] + this.gapPenalty, dp[i-1][j-1] + replacementPenalty(str1.charAt(i-1), str2.charAt(j-1)) 
									)
						);
			}
		}
		
		float maxCol = Float.MIN_VALUE;
		int startingCol = -1;
		float maxRow = Float.MIN_VALUE;
		int startingRow = -1;
		
		for (int i = 0; i < rows; i++) {
			if (dp[i][cols - 1] > maxCol) {
				maxCol = dp[i][cols - 1];
				startingCol = i;
			}
		}
		
		for (int j = 0; j < cols; j++) {
			if (dp[rows-1][j] > maxRow) {
				maxRow = dp[rows - 1][j];
				startingRow = j;
			}
		}
		
		if (maxRow > maxCol) {
			findLocalAlignment(str1, str2, dp, rows - 1, startingRow);
		} else {
			findLocalAlignment(str1, str2, dp, startingCol, cols - 1);
		}
		
		return maxRow > maxCol ? maxRow : maxCol;
	}
	
	float replacementPenalty(char c1, char c2) {
		return this.scoringMatrix[alphabets.indexOf(c1)][alphabets.indexOf(c2)];
	}
	
	void findGlobalAlignment(String seq1, String seq2, float[][] memoTable) {
		String seq1Aligned = "";
		String seq2Aligned = "";
		int i = memoTable.length-1;
		int j = memoTable[0].length-1;
		while(i>0 && j>0){
			if(memoTable[i][j] - replacementPenalty(seq1.charAt(i-1), seq2.charAt(j-1)) == memoTable[i-1][j-1]){
				seq1Aligned = seq1.charAt(i-1) + seq1Aligned;
				seq2Aligned = seq2.charAt(j-1) + seq2Aligned;
				i=i-1;
				j=j-1;
			}
			else if(memoTable[i][j] - gapPenalty == memoTable[i-1][j]){
				seq1Aligned = seq1.charAt(i-1) + seq1Aligned;
				seq2Aligned = "." + seq2Aligned;
				i=i-1;
			}
			else if(memoTable[i][j] - gapPenalty == memoTable[i][j-1]){
				seq2Aligned = seq2.charAt(j-1) + seq2Aligned;
				seq1Aligned = "." + seq1Aligned;
				j=j-1;
			}
		}
		while(i>0) {
			seq1Aligned = seq1.charAt(i-1) + seq1Aligned;
			seq2Aligned = "." + seq2Aligned;
			i=i-1;
		}
		while(j>0){
			seq2Aligned = seq2.charAt(j-1) + seq2Aligned;
			seq1Aligned = "." + seq1Aligned;
			j=j-1;
		}
		System.out.println("\nOptimal Alignment:\n" +seq1Aligned+"\n"+seq2Aligned + "\n\n");
	}
	
	void findLocalAlignment(String seq1, String seq2, float[][] memoTable, int startingRow, int startingCol) {
		String seq1Aligned = "";
		String seq2Aligned = "";
		int i = startingRow;
		int j = startingCol;
		while(memoTable[i][j] != 0){
			if(memoTable[i][j] - replacementPenalty(seq1.charAt(i-1), seq2.charAt(j-1)) == memoTable[i-1][j-1]){
				seq1Aligned = seq1.charAt(i-1) + seq1Aligned;
				seq2Aligned = seq2.charAt(j-1) + seq2Aligned;
				i=i-1;
				j=j-1;
			}
			else if(memoTable[i][j] - gapPenalty == memoTable[i-1][j]){
				seq1Aligned = seq1.charAt(i-1) + seq1Aligned;
				seq2Aligned = "." + seq2Aligned;
				i=i-1;
			}
			else if(memoTable[i][j] - gapPenalty == memoTable[i][j-1]){
				seq2Aligned = seq2.charAt(j-1) + seq2Aligned;
				seq1Aligned = "." + seq1Aligned;
				j=j-1;
			}
		}
		System.out.println("\nOptimal Alignment:\n" +seq1Aligned+"\n"+seq2Aligned + "\n\n");
	}
}
