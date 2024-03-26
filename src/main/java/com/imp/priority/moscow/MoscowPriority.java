package com.imp.priority.moscow;

import com.imp.priority.Priority;

public class MoscowPriority implements Priority {

    private final MoscowPriorityLevel level;

    public MoscowPriority(MoscowPriorityLevel level) {
        this.level = level;
    }

    @Override
    public Float calculate() {
        switch (level) {
            case WONT_HAVE -> {
                return 0f;
            }

            case COULD_HAVE -> {
                return 1f;
            }

            case SHOULD_HAVE -> {
                return 2f;
            }

            case MUST_HAVE -> {
                return 3f;
            }

            default -> {
                throw new IllegalStateException("Invalid priority level");
            }
        }
    }
}
