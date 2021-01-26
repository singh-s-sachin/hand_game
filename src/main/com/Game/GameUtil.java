package main.com.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class GameUtil
{
	public static final String MASTER = "master", SLAVE = "slave", NONE = "none";
	private static final List<String> POSSIBILITIES = Arrays.asList("O", "C");
	public static final String BAD_MESSAGE_EXCEPTION = "Bad input: correct input should be of the form CC3, where the first two letters indicate [O]pen or [C]losed state for each hand, followed by the prediction (0-4)";
	public static final String BAD_RANGE_EXCEPTION = "Bad input: prediction should be in the range of 0-4.";
	public static final String NO_PREDICTION_EXCEPTION = "Bad input: no prediction expected, you are not the predictor.";

	static Map<String, String> getMasterSlaveInputs(boolean isAIPredictor)
	{
		Map<String, String> data = new HashMap<>();
		try
		{
			if(isAIPredictor)
			{
				data.put(SLAVE, GameUtil.getSlavePrediction(!isAIPredictor));
				data.put(MASTER, GameUtil.getMasterPrediction(isAIPredictor));
			}
			else
			{
				data.put(MASTER, GameUtil.getMasterPrediction(isAIPredictor));
				data.put(SLAVE, GameUtil.getSlavePrediction(!isAIPredictor));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return data;
	}

	private static String getMasterPrediction(boolean fromMachine) throws Exception
	{
		String prediction;
		if(fromMachine)
		{
			prediction = getPredictionFromMachine(true);
			System.out.println("AI: " + prediction);
		}
		else
		{
			System.out.println("You are the predictor, what is your input?");
			prediction = getPredictionFromUser(true);
		}
		return prediction;
	}

	private static String getSlavePrediction(boolean fromMachine) throws Exception
	{
		String prediction;
		if(fromMachine)
		{
			prediction = getPredictionFromMachine(false);
			System.out.println("AI: " + prediction);
		}
		else
		{
			System.out.println("AI is the predictor, what is your input?");
			prediction = getPredictionFromUser(false);
		}
		return prediction;
	}

	private static String getPredictionFromMachine(boolean asMaster)
	{
		Random rand = new Random();
		String leftHand = POSSIBILITIES.get(rand.nextInt(1000) % 2);
		String rightHand = POSSIBILITIES.get(rand.nextInt(1000) % 2);
		int count = rand.nextInt(4);
		if(!asMaster)
		{
			return leftHand + rightHand;
		}
		else
		{
			return leftHand + rightHand + count;
		}

	}

	private static String getPredictionFromUser(boolean asMaster) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		String prediction = sc.next();
		validateUserPrediction(prediction, asMaster);
		return prediction;
	}

	static String getWinner(String masterPrediction, String slavePrediction)
	{
		String predictedHand = "O";
		int predictedCount = Integer.parseInt(String.valueOf(masterPrediction.charAt(2)));
		int actualCount = 0;
		String regex = masterPrediction + slavePrediction;
		int regexLen = regex.length();
		for(int i = 0; i < regexLen; i++)
		{
			if(predictedHand.equals(String.valueOf(regex.charAt(i))))
			{
				actualCount++;
			}
		}
		if(predictedCount == actualCount)
		{
			return GameUtil.MASTER;
		}
		return GameUtil.NONE;
	}

	private static void validateUserPrediction(String prediction, boolean isMasterPrediction) throws Exception
	{
		int expectedLength = isMasterPrediction ? 3 : 2;
		if(prediction.length() != expectedLength)
		{
			if(expectedLength == 2 && Character.isDigit(prediction.charAt(2)))
			{
				throw new Exception(NO_PREDICTION_EXCEPTION);
			}
			throw new Exception(BAD_MESSAGE_EXCEPTION);
		}
		if(!POSSIBILITIES.contains(String.valueOf(prediction.charAt(0))) || !POSSIBILITIES.contains(String.valueOf(prediction.charAt(1))))
		{
			throw new Exception(BAD_MESSAGE_EXCEPTION);
		}
		if(isMasterPrediction)
		{
			int predictionCount;
			try
			{
				predictionCount = Integer.parseInt(String.valueOf(prediction.charAt(2)));
			}
			catch(Exception e)
			{
				throw new Exception(BAD_RANGE_EXCEPTION);
			}

			if(predictionCount > 4)
			{
				throw new Exception(BAD_RANGE_EXCEPTION);
			}
		}
	}
}
