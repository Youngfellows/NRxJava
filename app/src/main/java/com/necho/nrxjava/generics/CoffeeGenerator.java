package com.necho.nrxjava.generics;

import com.necho.nrxjava.generics.base.Coffee;
import com.necho.nrxjava.generics.impl.Breve;
import com.necho.nrxjava.generics.impl.Cappuccino;
import com.necho.nrxjava.generics.impl.Latte;
import com.necho.nrxjava.generics.impl.Mocha;
import com.necho.nrxjava.generics.interfac.Generator;

import java.util.Random;


/**
 * 泛型类
 */
public class CoffeeGenerator implements Generator<Coffee> {

    private static Random rand = new Random(47);

    private Class[] types = {Latte.class, Mocha.class, Cappuccino.class, Breve.class};

    @Override
    public Coffee next() {
        try {
            Class aClass = types[rand.nextInt(types.length)];
            Coffee coffee = (Coffee) aClass.newInstance();
            return coffee;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
