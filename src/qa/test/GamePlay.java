package qa.test;

import main.com.Game.GameUtil;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

public class GamePlay
{
	private static Method validatePredictionMethod, getPredictionFromMachineMethod, getWinnerMethod;

	@BeforeClass
	public static void init() throws Exception
	{
		validatePredictionMethod = GameUtil.class.getDeclaredMethod("validateUserPrediction", String.class, boolean.class);
		validatePredictionMethod.setAccessible(true);
		getPredictionFromMachineMethod = GameUtil.class.getDeclaredMethod("getPredictionFromMachine", boolean.class);
		getPredictionFromMachineMethod.setAccessible(true);
		getWinnerMethod = GameUtil.class.getDeclaredMethod("getWinner", String.class, String.class);
		getWinnerMethod.setAccessible(true);
	}

	@Test
	public void testUserPredictionAsMasterWithoutRange()
	{
		try
		{
			validatePredictionMethod.invoke(null, "OC", true);
			fail("Expected failure on prediction without range as Master");
		}
		catch(Exception e)
		{
			String exceptionMessage = ((InvocationTargetException) e).getTargetException().getMessage();
			assertEquals(GameUtil.BAD_MESSAGE_EXCEPTION, exceptionMessage);
		}
	}

	@Test
	public void testUserPredictionAsMasterWithOutOfRange()
	{
		try
		{
			validatePredictionMethod.invoke(null, "CC9", true);
			fail("Expected failure on prediction out of range as Master");
		}
		catch(Exception e)
		{
			String exceptionMessage = ((InvocationTargetException) e).getTargetException().getMessage();
			assertEquals(GameUtil.BAD_RANGE_EXCEPTION, exceptionMessage);
		}
	}

	@Test
	public void testUserBogusPredictionAsMaster()
	{
		try
		{
			validatePredictionMethod.invoke(null, "chicken", true);
			fail("Expected failure on bogus prediction message");
		}
		catch(Exception e)
		{
			String exceptionMessage = ((InvocationTargetException) e).getTargetException().getMessage();
			assertEquals(GameUtil.BAD_MESSAGE_EXCEPTION, exceptionMessage);
		}
	}

	@Test
	public void testUserPredictingAsSlave()
	{
		try
		{
			validatePredictionMethod.invoke(null, "OC2", false);
		}
		catch(Exception e)
		{
			String exceptionMessage = ((InvocationTargetException) e).getTargetException().getMessage();
			assertEquals(GameUtil.NO_PREDICTION_EXCEPTION, exceptionMessage);
		}
	}

	@Test
	public void testAIPredictionsAsMasterAndSlave() throws Exception
	{
		boolean asMaster = true;
		for(int i = 0; i < 30; i++)
		{
			String aiPrediction = (String) getPredictionFromMachineMethod.invoke(null, asMaster);
			validatePredictionMethod.invoke(null, aiPrediction, asMaster);
			if(i == 15)
			{
				asMaster = false;
			}
		}
	}

	@Test
	public void testWinnerDeclarationMethodWithMasterWin() throws Exception
	{
		String winner = (String) getWinnerMethod.invoke(null, "OC3", "CC");
		assertEquals(GameUtil.MASTER, winner);
	}

	@Test
	public void testWinnerDeclarationMethodWithNoWinner() throws Exception
	{
		String winner = (String) getWinnerMethod.invoke(null, "OC3", "OC");
		assertEquals(GameUtil.NONE, winner);
	}
}
