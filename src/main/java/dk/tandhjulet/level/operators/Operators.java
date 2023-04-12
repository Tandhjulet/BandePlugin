package dk.tandhjulet.level.operators;

import net.objecthunter.exp4j.operator.Operator;

public class Operators {

    public static Operator gteq() {
        return new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] >= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };
    }

    public static Operator eq() {
        return new Operator("==", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] == values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };
    }

    public static Operator gt() {
        return new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] > values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };
    }

    public static Operator[] get() {
        return new Operator[] { gt(), eq(), gteq() };
    }
}
