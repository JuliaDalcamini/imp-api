package com.imp.priority.wiegers;

import com.imp.priority.Priority;

public class WiegersPriority implements Priority {

    private final int a;
    private final int b;
    private final int c;
    private final float d;

    public WiegersPriority(int a, int b, int c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public Float calculate() {
        return a * b + c * d;
    }
}
