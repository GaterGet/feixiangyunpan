package com.fx.pan.config;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @author leaving
 * @date 2022年4月5日
 */
public class GetHttpSessionConfigurator extends Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		if (request.getHttpSession() != null) {
			HttpSession httpSession = (HttpSession) request.getHttpSession();
			sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
		}
	}
}
