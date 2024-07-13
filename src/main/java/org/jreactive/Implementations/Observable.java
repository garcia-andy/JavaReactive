package org.jreactive.Implementations;

import java.util.LinkedList;

public class Observable<CtxT> {
    private LinkedList< Subject<CtxT> > _subjects
    = new LinkedList< Subject<CtxT> >();

    public void subscribe(Subject<CtxT> sub)
    { _subjects.add(sub); }

    public void unsubscribe(Subject<CtxT> sub)
    { _subjects.remove(sub); }

    public void notifyEvent(CtxT context)
    { _subjects.stream().forEach( s -> s.update(context) ); }

    public void notifyEvent()
    { _subjects.stream().forEach( s -> s.update() ); }
}
