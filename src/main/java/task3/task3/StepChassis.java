package task3.task3;

public class StepChassis implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Step 1: The chassis is assembled");
        return new Chassis();
    }
}
