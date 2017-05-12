package com.nhandler.handlerImpl;

import com.beans.Chater;
import com.beans.Json;
import org.apache.mina.core.session.IoSession;

/**
 * Created by lurunfa on 2017/4/24.
 *
 * @author lurunfa
 * @version 1.0
 */
public interface HandlerInterface {

    String getOrder();
    Json handle(IoSession ioSession, Chater chater);
}
