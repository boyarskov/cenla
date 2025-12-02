package task3.task3;

public class StepEngine implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Step 2: The engine is assembled");
        return new Engine();
    }
}
