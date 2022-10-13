package aima.gui.demo.search;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFitnessFunction;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Individual;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

/**
 * @author Ravi Mohan
 * 
 */

public class NQueensLocal {

	private static final int _boardSize = 8;
	
	public static void main(String[] args) {
		nQueensHillClimbingSearch_Statistics(10000);
		nQueensRandomRestartHillClimbing();
		nQueensSimulatedAnnealing_Statistics(1000);
		nQueensHillSimulatedAnnealingRestart();
		nQueensGeneticAlgorithmSearch();
	}
	
	 private static void nQueensHillClimbingSearch_Statistics(int numExperiments)  {
		 System.out.format("NQueens HillClimbing con %s estados iniciales diferentes -->\n", numExperiments);
		 Set<NQueensBoard> tabs = NQueensBoard.generateNQueensBoard(_boardSize, numExperiments);
		 
		 int exitos = 0;
		 int fallos = 0;
		 int pasos_exito = 0;	// media de pasos al tener exito
		 int pasos_fallo = 0;	// media de pasos al fallar
		 try {
			 for (Iterator iterator = tabs.iterator(); iterator.hasNext();) {
				NQueensBoard nQueensBoard = (NQueensBoard) iterator.next();
				
				Problem problem = new Problem(nQueensBoard,
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
				SearchAgent agent = new SearchAgent(problem, search);
				
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					exitos++;
					pasos_exito += agent.getActions().size();
				} else {
					fallos++;
					pasos_fallo += agent.getActions().size();
				}
			}
			 
			 System.out.format("Fallos:%s\n", fallos);
			 System.out.format("Coste medio fallos:%.2f\n", (double)pasos_fallo/fallos);
			 System.out.format("Exitos:%s\n", exitos);
			 System.out.format("Coste medio exitos:%.2f\n", (double)pasos_exito/exitos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	private static void nQueensRandomRestartHillClimbing() {
		 System.out.println();
		 int intento = 0;
		 int exitos  = 0;
		 int fallos  = 0;
		 int pasos_exito = 0;	// media de pasos al tener exito
		 int pasos_fallo = 0;	// media de pasos al fallar
		 boolean ok = false;
		 try {
			 while (!ok) {
				Problem problem = new Problem(NQueensBoard.generateRandomNQueensBoard(_boardSize),
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
				SearchAgent agent = new SearchAgent(problem, search);
				
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					ok = true;
					exitos++;
					pasos_exito += agent.getActions().size();
				} else {
					fallos++;
					pasos_fallo += agent.getActions().size();
				}
				intento++;
				pasos_fallo += agent.getActions().size();
			}
			 
			 System.out.format("Numero de intentos:%s\n", intento);
			 System.out.format("Fallos:%s\n", fallos);
			 System.out.format("Coste medio fallos:%.2f\n", (double)pasos_exito/exitos);
			 System.out.format("Exitos:%s\n", exitos);
			 System.out.format("Coste medio exitos:%.2f\n", (double)pasos_fallo/fallos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void nQueensSimulatedAnnealing_Statistics(int numExperiments) {
		System.out.format("\nNQueensDemo Simulated Annealing con %s estados iniciales diferentes -->\n", numExperiments);
		System.out.format("\nParámetros Scheduler: Scheduler (%s,%s,%s)\n", 1, 0.01, 100);
		
		Set<NQueensBoard> tabs = NQueensBoard.generateNQueensBoard(_boardSize, numExperiments);
		Scheduler scheduler = new Scheduler(1, 0.01, 100);
		
		 int exitos = 0;
		 int fallos = 0;
		 int pasos_exito = 0;	// media de pasos al tener exito
		 int pasos_fallo = 0;	// media de pasos al fallar
		 try {
			 for (Iterator iterator = tabs.iterator(); iterator.hasNext();) {
				NQueensBoard nQueensBoard = (NQueensBoard) iterator.next();
				
				Problem problem = new Problem(nQueensBoard,
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic(), scheduler);
				SearchAgent agent = new SearchAgent(problem, search);
				
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					exitos++;
					pasos_exito += agent.getActions().size();
				} else {
					fallos++;
					pasos_fallo += agent.getActions().size();
				}
			}
			 
			 System.out.format("Fallos:%s\n", fallos);
			 System.out.format("Coste medio fallos:%.2f\n", (double)pasos_fallo/fallos);
			 System.out.format("Exitos:%s\n", exitos);
			 System.out.format("Coste medio exitos:%.2f\n", (double)pasos_exito/exitos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void nQueensHillSimulatedAnnealingRestart() {
		 System.out.println();
		 int intento = 0;
		 int exitos  = 0;
		 int fallos  = 0;
		 int pasos_exito = 0;	// media de pasos al tener exito
		 int pasos_fallo = 0;	// media de pasos al fallar
		 boolean ok = false;
		 
		 Scheduler scheduler = new Scheduler(1, 0.01, 100);
		 try {
			 while (!ok) {
				Problem problem = new Problem(NQueensBoard.generateRandomNQueensBoard(_boardSize),
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic(), scheduler);
				SearchAgent agent = new SearchAgent(problem, search);
				
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					ok = true;
					exitos++;
					pasos_exito += agent.getActions().size();
				} else {
					fallos++;
					pasos_fallo += agent.getActions().size();
				}
				intento++;
				pasos_fallo += agent.getActions().size();
			}
			 
			 System.out.format("Numero de intentos:%s\n", intento);
			 System.out.format("Fallos:%s\n", fallos);
			 System.out.format("Coste medio fallos:%.2f\n", (double)pasos_exito/exitos);
			 System.out.format("Exitos:%s\n", exitos);
			 System.out.format("Coste medio exitos:%.2f\n", (double)pasos_fallo/fallos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void nQueensGeneticAlgorithmSearch() {
		System.out.println("\nGeneticAlgorithm");
		try {
			NQueensFitnessFunction fitnessFunction = new NQueensFitnessFunction();
			// Generate an initial population
			Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
			for (int i = 0; i < 50; i++) {
				population.add(fitnessFunction
						.generateRandomIndividual(_boardSize));
			}

			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(
					_boardSize,
					fitnessFunction.getFiniteAlphabetForBoardOfSize(_boardSize),
					0.15);

			
			// Run for a set amount of time
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(
					population, fitnessFunction, fitnessFunction, 1000L);

			// Run till goal is achieved
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction,
					fitnessFunction, 0L);

			System.out.format("Parámetros iniciales:	Poblacion: %s Probabilidad mutación: %.2f \n", 50, 0.15);
			
			System.out.println("");
			System.out.println("Mejor individuo =\n"
					+ fitnessFunction.getBoardForIndividual(bestIndividual));
			System.out.println("Tamaño del tablero = " + _boardSize);
			System.out.println("Fitness            = " + fitnessFunction.getValue(bestIndividual));
			System.out.println("Es objetivo        = " + fitnessFunction.isGoalState(bestIndividual));
			System.out.println("Tamaño población   = " + ga.getPopulationSize());
			System.out.println("Iteraciones        = " + ga.getIterations());
			System.out.println("Tiempo             = " + ga.getTimeInMilliseconds() + "ms.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInstrumentation(Properties properties) {
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}
	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}

}