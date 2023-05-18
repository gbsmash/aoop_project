import java.util.*;

public class GeneticAlgorithm {
    private final List<Student> students;
    private final List<Destination> destinations;
    private final int populationSize;
    private final int maxGenerations;
    private final double crossoverRate;
    private final double mutationRate;
    private final Random random;

    public GeneticAlgorithm(List<Student> students, List<Destination> destinations, int populationSize, int maxGenerations, double crossoverRate, double mutationRate) {
        this.students = students;
        this.destinations = destinations;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.random = new Random();
    }
    private int calculateCost(Assignment assignment) {
        // get the student and destination from the assignment
        Student student = assignment.getStudent();
        Destination destination = assignment.getDestination();

        // find the index of the destination in the student's preferences
        int preferenceIndex = student.getPreferences().indexOf(destination);
        if (preferenceIndex >= 0) { // if the destination is found in the preferences
            return preferenceIndex * preferenceIndex;
        } else {
            return 10 * 5 * 5;
        }
    }

    public List<List<Assignment>> initializePopulation() {
        List<List<Assignment>> population = new ArrayList<>();

        // generate the initial population
        for (int i = 0; i < populationSize; i++) {
            List<Assignment> assignments = new ArrayList<>();
            // assign a random destination to each student
            for (Student student : students) {
                Destination destination = destinations.get(random.nextInt(destinations.size()));
                Assignment assignment = new Assignment(student, destination);
                assignment.setCost(calculateCost(assignment));
                assignments.add(assignment);
            }
            while (!isValid(assignments)) { // check if the assignments satisfy the constraints
                mutate(assignments);
            }
            population.add(assignments); // add the assignments to the population
        }
        return population;
    }

    private boolean isValid(List<Assignment> assignments) {
        // create a map to keep track of the counts of each destination
        Map<Destination, Integer> destinationCounts = new HashMap<>();

        // count the occurrences of each destination in the assignments
        for (Assignment assignment : assignments) {
            destinationCounts.put(assignment.getDestination(), destinationCounts.getOrDefault(assignment.getDestination(), 0) + 1);
        }

        // check if any destination has more students assigned than its maximum capacity
        for (Map.Entry<Destination, Integer> entry : destinationCounts.entrySet()) {
            if (entry.getValue() > entry.getKey().getMaxStudents()) {
                return false;
            }
        }
        return true;
    }

    private void mutate(List<Assignment> assignments) {
        // select a random assignment to mutate
        int index = random.nextInt(assignments.size());
        Assignment assignment = assignments.get(index);
        Destination newDestination;

        do { // generate a random new destination until a valid one is found
            newDestination = destinations.get(random.nextInt(destinations.size()));
        } while (getDestinationCount(assignments, newDestination) >= newDestination.getMaxStudents() || newDestination.equals(assignment.getDestination()));
        assignment.setDestination(newDestination); // update the assignment with the new destination
        assignment.setCost(calculateCost(assignment));  // recalculate the cost of the assignment
    }


    private int getDestinationCount(List<Assignment> assignments, Destination destination) {
        // use stream to filter assignments by destination and count the occurrences
        return (int) assignments.stream().filter(a -> a.getDestination().equals(destination)).count();
    }

    public List<Assignment> run() {
        List<List<Assignment>> population = initializePopulation();//initialize the population
        int generation = 0;

        while (generation < maxGenerations) {
            List<Double> fitness = evaluatePopulation(population); //evaluate fitness of population
            List<List<Assignment>> parents = selection(population, fitness); // select parents for reproduction
            List<List<Assignment>> offspring = crossover(parents); // create offspring through crossover
//            offspring = mutation(offspring); // perform mutation on offspring
            population = replacement(population, offspring, fitness); // generate the new population through replacement (including elitism)
            generation++;
        }

        // evaluate the fitness of the final population
        List<Double> fitness = evaluatePopulation(population);
        int bestIndex = getBestSolutionIndex(fitness); // find the index of the best solution
        return population.get(bestIndex); // return best solution
    }

    public List<Double> evaluatePopulation(List<List<Assignment>> population) {
        List<Double> fitness = new ArrayList<>();
        // iterate through each individual (list of assignments) in the population
        for (List<Assignment> assignments : population) {
            int totalCost = 0;
            // calculate the total cost of the assignments for the current individual
            for (Assignment assignment : assignments) {
                totalCost += assignment.getCost();
            }
            // calculate the fitness as the inverse of the total cost and add it to the fitness list
            // the inverse is used so that lower costs have higher fitness values
            fitness.add(1.0 / totalCost);
        }
        return fitness;
    }

    public List<List<Assignment>> selection(List<List<Assignment>> population, List<Double> fitness) {
        List<List<Assignment>> parents = new ArrayList<>();
        double totalFitness = fitness.stream().mapToDouble(Double::doubleValue).sum();

        // perform selection for each parent in the desired population size
        for (int i = 0; i < populationSize; i++) {
            double randomValue = random.nextDouble() * totalFitness; //random number between 0 and totalfitness
            double currentSum = 0;
            int selectedIndex = 0;

            // find the index of the individual whose fitness value represents the random value
            for (int j = 0; j < fitness.size(); j++) {
                currentSum += fitness.get(j);
                if (currentSum >= randomValue) {
                    selectedIndex = j;
                    break;
                }
            }
            // add a new list representing the selected individual to the parents list
            parents.add(new ArrayList<>(population.get(selectedIndex)));

        }
        return parents;
    }

    public List<List<Assignment>> crossover(List<List<Assignment>> parents) {
        List<List<Assignment>> offspring = new ArrayList<>();
        for (int i = 0; i < parents.size(); i += 2) {
            if (random.nextDouble() < crossoverRate) {  // check if crossover should be performed based on crossoverRate
                // get the two parents for crossover
                List<Assignment> parent1 = parents.get(i);
                List<Assignment> parent2 = parents.get(i + 1);

                // determine the crossover point randomly
                int crossoverPoint = random.nextInt(parent1.size());

                // create child 1 by combining parts of parent 1 and parent 2
                List<Assignment> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
                child1.addAll(parent2.subList(crossoverPoint, parent2.size()));

                // create child 2 by combining parts of parent 2 and parent 1
                List<Assignment> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));
                child2.addAll(parent1.subList(crossoverPoint, parent1.size()));

                // check if the generated children are valid solutions
                if (isValid(child1) && isValid(child2)) {
                    offspring.add(child1);
                    offspring.add(child2);
                }
            }
        }
        return offspring;
    }

    public List<List<Assignment>> mutation(List<List<Assignment>> offspring) {
        for (List<Assignment> assignments : offspring) {
            if (random.nextDouble() < mutationRate) {  //check if mutation should be performed based on mutationRate
                // generate two random indices for swapping assignments
                int index1 = random.nextInt(assignments.size());
                int index2 = random.nextInt(assignments.size());
                // swap the assignments at the randomly selected indices
                Collections.swap(assignments, index1, index2);
            }
        }
        return offspring;
    }

    public List<List<Assignment>> replacement(List<List<Assignment>> population, List<List<Assignment>> offspring, List<Double> fitness) {
        // sort the offspring in descending order based on fitness values
        Collections.sort(offspring, Comparator.comparingDouble(this::calculateSolutionFitness).reversed());

        // replace individuals in the population with valid offspring
        for (List<Assignment> assignments : offspring) {
            if (isValid(assignments)) {
                // find the index of the worst solution in the current population
                int worstIndex = getWorstSolutionIndex(fitness);

                // replace the worst solution with the current offspring
                population.set(worstIndex, assignments);

                // update the fitness value of the replaced solution
                fitness.set(worstIndex, calculateSolutionFitness(assignments));
            }
        }
        return population;
    }

    private double calculateSolutionFitness(List<Assignment> assignments) {
        int totalCost = 0;

        // Calculate the total cost of the assignments in the solution
        for (Assignment assignment : assignments) {
            totalCost += assignment.getCost();
        }
        // return the fitness value, which is the inverse of the total cost
        return 1.0 / totalCost;
    }

    public int getBestSolutionIndex(List<Double> fitness) {
        int bestIndex = 0;
        for (int i = 1; i < fitness.size(); i++) {
            if (fitness.get(i) > fitness.get(bestIndex)) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    public int getWorstSolutionIndex(List<Double> fitness) {
        int worstIndex = 0;
        for (int i = 1; i < fitness.size(); i++) {
            if (fitness.get(i) < fitness.get(worstIndex)) {
                worstIndex = i;
            }
        }
        return worstIndex;
    }
}