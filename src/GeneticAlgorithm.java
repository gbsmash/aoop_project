import java.util.*;

public class GeneticAlgorithm {
    private List<Student> students;
    private List<Destination> destinations;
    private int populationSize;
    private int maxGenerations;
    private double crossoverRate;
    private double mutationRate;
    private Random random;

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
        Student student = assignment.getStudent();
        Destination destination = assignment.getDestination();
        int preferenceIndex = student.getPreferences().indexOf(destination);
        if (preferenceIndex >= 0) {
            return preferenceIndex * preferenceIndex;
        } else {
            return 10 * 5 * 5;
        }
    }

    public List<List<Assignment>> initializePopulation() {
        List<List<Assignment>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Assignment> assignments = new ArrayList<>();
            for (Student student : students) {
                Destination destination = destinations.get(random.nextInt(destinations.size()));
                Assignment assignment = new Assignment(student, destination);
                assignment.setCost(calculateCost(assignment));
                assignments.add(assignment);
            }
            while (!isValid(assignments)) {
                mutate(assignments);
            }
            population.add(assignments);
        }
        return population;
    }

    private boolean isValid(List<Assignment> assignments) {
        Map<Destination, Integer> destinationCounts = new HashMap<>();
        for (Assignment assignment : assignments) {
            destinationCounts.put(assignment.getDestination(), destinationCounts.getOrDefault(assignment.getDestination(), 0) + 1);
        }
        for (Map.Entry<Destination, Integer> entry : destinationCounts.entrySet()) {
            if (entry.getValue() > entry.getKey().getMaxStudents()) {
                return false;
            }
        }
        return true;
    }

    private void mutate(List<Assignment> assignments) {
        int index = random.nextInt(assignments.size());
        Assignment assignment = assignments.get(index);
        Destination newDestination;
        do {
            newDestination = destinations.get(random.nextInt(destinations.size()));
        } while (getDestinationCount(assignments, newDestination) >= newDestination.getMaxStudents() || newDestination.equals(assignment.getDestination()));
        assignment.setDestination(newDestination);
        assignment.setCost(calculateCost(assignment));
    }


    private int getDestinationCount(List<Assignment> assignments, Destination destination) {
        return (int) assignments.stream().filter(a -> a.getDestination().equals(destination)).count();
    }

    public List<Assignment> run() {
        List<List<Assignment>> population = initializePopulation();
        int generation = 0;

        while (generation < maxGenerations) {
            List<Double> fitness = evaluatePopulation(population);
            List<List<Assignment>> parents = selection(population, fitness);
            List<List<Assignment>> offspring = crossover(parents);
//            offspring = mutation(offspring); // uncommented
            population = replacement(population, offspring, fitness); //elitism
            generation++;
        }

        List<Double> fitness = evaluatePopulation(population);

        int bestIndex = getBestSolutionIndex(fitness);
        return population.get(bestIndex);
    }

    public List<Double> evaluatePopulation(List<List<Assignment>> population) {
        List<Double> fitness = new ArrayList<>();
        for (List<Assignment> assignments : population) {
            int totalCost = 0;
            for (Assignment assignment : assignments) {
                totalCost += assignment.getCost();
            }
            fitness.add(1.0 / totalCost);
        }
        return fitness;
    }

    public List<List<Assignment>> selection(List<List<Assignment>> population, List<Double> fitness) {
        List<List<Assignment>> parents = new ArrayList<>();
        double totalFitness = fitness.stream().mapToDouble(Double::doubleValue).sum();

        for (int i = 0; i < populationSize; i++) {
            double randomValue = random.nextDouble() * totalFitness; //random number between 0 and totalfitness
            double currentSum = 0;
            int selectedIndex = 0;
            for (int j = 0; j < fitness.size(); j++) {
                currentSum += fitness.get(j);
                if (currentSum >= randomValue) {
                    selectedIndex = j;
                    break;
                }
            }
            parents.add(new ArrayList<>(population.get(selectedIndex))); // Create a new list for each selected individual

        }
        return parents;
    }

    public List<List<Assignment>> crossover(List<List<Assignment>> parents) {
        List<List<Assignment>> offspring = new ArrayList<>();
        for (int i = 0; i < parents.size(); i += 2) {
            if (random.nextDouble() < crossoverRate) {  // Check against crossoverRate
                List<Assignment> parent1 = parents.get(i);
                List<Assignment> parent2 = parents.get(i + 1);
                int crossoverPoint = random.nextInt(parent1.size());
                List<Assignment> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
                child1.addAll(parent2.subList(crossoverPoint, parent2.size()));
                List<Assignment> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));
                child2.addAll(parent1.subList(crossoverPoint, parent1.size()));

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
            if (random.nextDouble() < mutationRate) {
                int index1 = random.nextInt(assignments.size());
                int index2 = random.nextInt(assignments.size());
                Collections.swap(assignments, index1, index2);
            }
        }
        return offspring;
    }

    public List<List<Assignment>> replacement(List<List<Assignment>> population, List<List<Assignment>> offspring, List<Double> fitness) {
        Collections.sort(offspring, Comparator.comparingDouble(this::calculateSolutionFitness).reversed());
        for (List<Assignment> assignments : offspring) {
            if (isValid(assignments)) {
                int worstIndex = getWorstSolutionIndex(fitness);
                population.set(worstIndex, assignments);
                fitness.set(worstIndex, calculateSolutionFitness(assignments));
            }
        }
        return population;
    }

    private double calculateSolutionFitness(List<Assignment> assignments) {
        int totalCost = 0;
        for (Assignment assignment : assignments) {
            totalCost += assignment.getCost();
        }
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