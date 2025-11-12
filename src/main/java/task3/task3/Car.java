package task3.task3;

public class Car implements IProduct {
    private IProductPart chassis;
    private IProductPart engine;
    private IProductPart body;

    @Override
    public void installFirstPart(IProductPart part) {
        this.chassis = part;
        System.out.println("The car chassis is installed");
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.engine = part;
        System.out.println("The car engine is installed");
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.body = part;
        System.out.println("The car body is installed");
    }

    @Override
    public String toString() {
        return "Car [assembled successfully]";
    }
}
