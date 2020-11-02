package com.sammurphy.sleepPercentage;

import java.util.ArrayList;
import java.util.Collection;

public class NonNullableArrayList<E> extends ArrayList<E> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3036013234900284011L;

	public NonNullableArrayList() {
        super();
    }

    public NonNullableArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public NonNullableArrayList(Collection<? extends E> e) {
        super();
        addAll(e);
    }

    @Override
    public boolean add(E o) {
        if(o == null)
            return false;
        return super.add(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }
}
