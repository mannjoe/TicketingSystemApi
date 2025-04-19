package com.catalyst.TicketingSystemApi.util;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {

    public static String getClientOriginUrl(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            return origin;
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            return referer.replace(request.getServletPath(), "");
        }

        return getServerUrl(request);
    }

    public static String getServerUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        return url.toString();
    }
}