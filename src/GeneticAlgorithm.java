package src;

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
        } while (getDestinationCount(assignments, newDestination) >= newDestination.getMaxStudents());
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
            offspring = mutation(offspring); // uncommented
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
//            parents.add(population.get(selectedIndex));
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
        for (int i = 0; i < offspring.size(); i++) {
            if (isValid(offspring.get(i))) {
                int worstIndex = getWorstSolutionIndex(fitness);
                population.set(worstIndex, offspring.get(i));
                fitness.set(worstIndex, calculateSolutionFitness(offspring.get(i)));
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


//    public static void main(String[] args) {
//                List<Student> studentList = new ArrayList<>();
//        List<Destination> destinations = Destination.getDefaultDestinations();
//
//        Student st1 = new Student("Narmin", "Bambusik");
//        List<Destination> preferredDestinations1 = new ArrayList<>();
//        preferredDestinations1.add(destinations.get(0));
//        preferredDestinations1.add(destinations.get(1));
//        preferredDestinations1.add(destinations.get(2));
//        preferredDestinations1.add(destinations.get(3));
//        preferredDestinations1.add(destinations.get(4));
//        st1.setPreferences(preferredDestinations1);
//
//        studentList.add(st1);
//
//        Student st2 = new Student("Shems", "Bambusik");
//        List<Destination> preferredDestinations2 = new ArrayList<>();
//        preferredDestinations2.add(destinations.get(1));
//        preferredDestinations2.add(destinations.get(8));
//        preferredDestinations2.add(destinations.get(2));
//        preferredDestinations2.add(destinations.get(3));
//        preferredDestinations2.add(destinations.get(4));
//        st2.setPreferences(preferredDestinations2);
//
//        studentList.add(st2);
//
//        Student st3 = new Student("Aydan", "Bambusik");
//        List<Destination> preferredDestinations3 = new ArrayList<>();
//        preferredDestinations3.add(destinations.get(2));
//        preferredDestinations3.add(destinations.get(1));
//        preferredDestinations3.add(destinations.get(8));
//        preferredDestinations3.add(destinations.get(3));
//        preferredDestinations3.add(destinations.get(4));
//        st3.setPreferences(preferredDestinations3);
//
//        studentList.add(st3);
//
//        Student st4 = new Student("Amina", "Bambusik");
//        List<Destination> preferredDestinations4 = new ArrayList<>();
//        preferredDestinations4.add(destinations.get(3));
//        preferredDestinations4.add(destinations.get(1));
//        preferredDestinations4.add(destinations.get(2));
//        preferredDestinations4.add(destinations.get(9));
//        preferredDestinations4.add(destinations.get(4));
//        st4.setPreferences(preferredDestinations4);
//
//        studentList.add(st4);
//
//        Student st5 = new Student("Ktoto", "Bambusik");
//        List<Destination> preferredDestinations5 = new ArrayList<>();
//        preferredDestinations5.add(destinations.get(4));
//        preferredDestinations5.add(destinations.get(1));
//        preferredDestinations5.add(destinations.get(2));
//        preferredDestinations5.add(destinations.get(3));
//        preferredDestinations5.add(destinations.get(5));
//        st5.setPreferences(preferredDestinations5);
//
//        studentList.add(st5);
//
//
//
//        GeneticAlgorithm ga = new GeneticAlgorithm(studentList, destinations, 50, 200, 0.8, 0.05);
//        List<Assignment> assignments = ga.run();
//        for(Assignment a: assignments){
//            System.out.println(a.getStudent().getName()+ " " + a.getStudent().getSurname() + " : " + a.getDestination().getIndex() + ")"+ a.getDestination().getName());
//        }
//
////        List<List<Assignment>> population= ga.initializePopulation();
////        List<Double> fitness = ga.evaluatePopulation(population);
////        List<List<Assignment>> parents = ga.selection(population, fitness);
////        List<List<Assignment>> offspring = ga.crossover(parents);
////        offspring = ga.mutation(offspring);
////        population = ga.replacement(population, offspring, fitness); //elitism
////
////        for(int k =0;k< 12; k++){
////            fitness = ga.evaluatePopulation(population);
////            parents = ga.selection(population, fitness);
////            offspring = ga.crossover(parents);
//////            offspring = ga.mutation(offspring);
////            population = ga.replacement(population, offspring, fitness); //elitism
////        }
////
////
////        int i=1;
////        for(List<Assignment> al: population ){
////            System.out.println(i);
////            for (Assignment a: al){
////                System.out.println(a.getStudent().getName()+ " "+ a.getDestination().getIndex());
////            }
////            i++;
////            System.out.println();
////        }
//
//
//
//
//
//    }
}

