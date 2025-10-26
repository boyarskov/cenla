package task3.task3;

public class StepBody implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Step 3: The car body is assembled");
        return new Body();
    }
}
