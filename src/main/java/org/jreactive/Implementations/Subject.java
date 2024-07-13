package org.jreactive.Implementations;

public interface Subject<CtxT> {
    void update(CtxT ctx);
    void update();
}
