package net.goo.brutality.block.block_entity;

public class FilingCabinetDrawerSlideController {
    private final boolean isUpper;
    private float openness;
    private float prevOpenness;

    public FilingCabinetDrawerSlideController(boolean isUpper) {
        this.isUpper = isUpper;
        this.openness = 0.0F;
        this.prevOpenness = 0.0F;
    }

    public void tickDrawer(boolean shouldBeOpen) {
        this.prevOpenness = this.openness;
        float targetOpenness = shouldBeOpen ? 1.0F : 0.0F;
        if (this.openness < targetOpenness) {
            this.openness = Math.min(this.openness + 0.1F, targetOpenness);
        } else if (this.openness > targetOpenness) {
            this.openness = Math.max(this.openness - 0.1F, targetOpenness);
        }
    }

    public float getOpenness(float partialTicks) {
        return this.prevOpenness + (this.openness - this.prevOpenness) * partialTicks;
    }

    public boolean isUpper() {
        return this.isUpper;
    }
}