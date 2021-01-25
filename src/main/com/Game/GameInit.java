package main.com.Game;

import java.util.Map;
import java.util.Scanner;

public class GameInit
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			initiateGamePlayConsole();
			System.out.println("Do you want to play again?");
			String choice = sc.next();
			if(choice.equals("N") || choice.equals("n"))
			{
				System.out.println("Ok, bye!");
				break;
			}
		}
	}

	private static void initiateGamePlayConsole()
	{
		System.out.println("Welcome to the game!");
		boolean isAIPredictor = false;
		String slavePrediction, masterPrediction;
		while(true)
		{
			Map<String, String> inputs = GameUtil.getMasterSlaveInputs(isAIPredictor);
			masterPrediction = inputs.get(GameUtil.MASTER);
			slavePrediction = inputs.get(GameUtil.SLAVE);
			String winner = GameUtil.getWinner(masterPrediction, slavePrediction);
			if(winner.equals(GameUtil.NONE))
			{
				isAIPredictor = isAIPredictor ? false : true;
				System.out.println("No winner.");
				continue;
			}
			if(isAIPredictor)
			{
				System.out.println("AI WIN!!");
			}
			else
			{
				System.out.println("You WIN!!");
			}
			break;
		}
	}

}
