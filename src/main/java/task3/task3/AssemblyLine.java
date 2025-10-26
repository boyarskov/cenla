package task3.task3;

public class AssemblyLine implements IAssemblyLine {
    private ILineStep stepChassis;
    private ILineStep stepEngine;
    private ILineStep stepBody;

    AssemblyLine(ILineStep stepChassis, ILineStep stepEngine, ILineStep stepBody) {
        this.stepChassis = stepChassis;
        this.stepEngine = stepEngine;
        this.stepBody = stepBody;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("The beginning of the car assembly...");

        IProductPart chassisPart = stepChassis.buildProductPart();
        IProductPart enginePart = stepEngine.buildProductPart();
        IProductPart bodyPart = stepBody.buildProductPart();

        product.installFirstPart(chassisPart);
        product.installSecondPart(enginePart);
        product.installThirdPart(bodyPart);

        System.out.println("The car assembly has been completed successfully!");

        return product;
    }
}
