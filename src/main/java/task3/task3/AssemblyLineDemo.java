package task3.task3;

public class AssemblyLineDemo {
    public static void main(String[] args) {
        ILineStep stepChassis = new StepChassis();
        ILineStep stepEngine = new StepEngine();
        ILineStep stepBody = new StepBody();

        IAssemblyLine assemblyLine = new AssemblyLine(stepChassis, stepEngine, stepBody);

        IProduct car = new Car();

        IProduct readyCar = assemblyLine.assembleProduct(car);

        System.out.println("The car was successfully assembled: " + readyCar);
    }
}
