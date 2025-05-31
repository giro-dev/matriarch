package dev.agiro.matriarch.object_samples;

public class SimpleProtectedConstructor {
    private boolean flag;

    protected SimpleProtectedConstructor(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
